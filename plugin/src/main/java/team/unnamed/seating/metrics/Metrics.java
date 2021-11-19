package team.unnamed.seating.metrics;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Slightly modified version of bStats' Bukkit Metrics
 * class from github.com/Bastian/bStats-Metrics, this
 * class was modified to keep only necessary stuff and
 * maintain a consistent code-style in all the project
 */
public class Metrics {

    private static final URL REPORT_URL;
    public static final String METRICS_VERSION = "2.2.1";

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(
            1,
            task -> new Thread(task, "bStats-Metrics")
    );

    static {
        try {
            REPORT_URL = new URL("https://bStats.org/api/v2/data/bukkit");
        } catch (MalformedURLException e) {
            // should never happen, we know the URL is valid
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Plugin plugin;
    private final int serviceId;
    private final String serverUUID;

    private final boolean enabled;
    private final boolean logErrors;
    private final boolean logSentData;
    private final boolean logResponseStatusText;

    /**
     * Creates a new Metrics instance.
     *
     * @param plugin Your plugin instance.
     */
    public Metrics(Plugin plugin, int serviceId) {
        this.plugin = plugin;
        this.serviceId = serviceId;

        YamlConfiguration config = loadBStatsConfiguration();
        // Load the data
        this.enabled = config.getBoolean("enabled", true);
        this.serverUUID = config.getString("serverUuid");
        this.logErrors = config.getBoolean("logFailedRequests", false);
        this.logSentData = config.getBoolean("logSentData", false);
        this.logResponseStatusText = config.getBoolean("logResponseStatusText", false);
    }

    private YamlConfiguration loadBStatsConfiguration() {
        // Get the config file
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);
            // Inform the server owners about bStats
            config.options().header(
                    "bStats (https://bStats.org) collects some basic information for plugin authors, like how\n"
                    + "many people use their plugin and their total player count. It's recommended to keep bStats\n"
                    + "enabled, but if you're not comfortable with this, you can turn this setting off. There is no\n"
                    + "performance penalty associated with having metrics enabled, and data sent to bStats is fully\n"
                    + "anonymous."
            ).copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
        }
        return config;
    }

    public void startSubmittingIfEnabled() {
        if (!enabled) {
            return;
        }

        Runnable submitTask = () -> {
            if (plugin.isEnabled()) {
                Bukkit.getScheduler().runTask(plugin, this::submitData);
            } else {
                SCHEDULER.shutdown();
            }
        };
        // Many servers tend to restart at a fixed time at xx:00 which causes an uneven distribution
        // of requests on the
        // bStats backend. To circumvent this problem, we introduce some randomness into the initial
        // and second delay.
        // WARNING: You must not modify and part of this Metrics class, including the submit delay or
        // frequency!
        // WARNING: Modifying this code will get your plugin banned on bStats. Just don't do it!
        long initialDelay = (long) (1000 * 60 * (3 + Math.random() * 3));
        long secondDelay = (long) (1000 * 60 * (Math.random() * 30));
        SCHEDULER.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        SCHEDULER.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30, TimeUnit.MILLISECONDS);
    }

    private void submitData() {
        JsonObjectBuilder builder = new JsonObjectBuilder();
        // platform data
        {
            builder.appendField("serverUUID", serverUUID);
            builder.appendField("metricsVersion", METRICS_VERSION);
            builder.appendField("playerAmount", Bukkit.getOnlinePlayers().size());
            builder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
            builder.appendField("bukkitVersion", Bukkit.getVersion());
            builder.appendField("bukkitName", Bukkit.getName());
            builder.appendField("javaVersion", System.getProperty("java.version"));
            builder.appendField("osName", System.getProperty("os.name"));
            builder.appendField("osArch", System.getProperty("os.arch"));
            builder.appendField("osVersion", System.getProperty("os.version"));
            builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
        }

        // service data
        {
            JsonObjectBuilder serviceJsonBuilder = new JsonObjectBuilder();
            serviceJsonBuilder.appendField("id", serviceId);
            serviceJsonBuilder.appendField("pluginVersion", plugin.getDescription().getVersion());
            serviceJsonBuilder.appendEmptyArray("customCharts");
            builder.appendField("service", serviceJsonBuilder.build());
        }
        JsonObjectBuilder.JsonObject data = builder.build();
        SCHEDULER.execute(() -> {
            try {
                // Send the data
                sendData(data);
            } catch (Exception e) {
                // Something went wrong! :(
                if (logErrors) {
                    plugin.getLogger().log(Level.WARNING, "Could not submit bStats metrics data", e);
                }
            }
        });
    }

    private void sendData(JsonObjectBuilder.JsonObject data) throws Exception {
        if (logSentData) {
            plugin.getLogger().info("Sent bStats metrics data: " + data.toString());
        }
        HttpsURLConnection connection = (HttpsURLConnection) REPORT_URL.openConnection();

        // Compress the data to save bandwidth
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(data.toString().getBytes(StandardCharsets.UTF_8));
        }
        byte[] compressedData = outputStream.toByteArray();

        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Metrics-Service/1");
        connection.setDoOutput(true);

        // write request body
        try (OutputStream output = connection.getOutputStream()) {
            output.write(compressedData);
        }

        // execute request
        if (logResponseStatusText) {
            // ...and read response
            StringBuilder builder = new StringBuilder();
            try (BufferedReader bufferedReader =
                         new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
            }
            plugin.getLogger().info("Sent data to bStats and received response: " + builder);
        } else {
            // ...and ignore response
            connection.getInputStream().close();
        }
    }

    private static String escape(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '"') {
                builder.append("\\\"");
            } else if (c == '\\') {
                builder.append("\\\\");
            } else if (c <= '\u000F') {
                builder.append("\\u000").append(Integer.toHexString(c));
            } else if (c <= '\u001F') {
                builder.append("\\u00").append(Integer.toHexString(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * An extremely simple JSON builder.
     */
    public static class JsonObjectBuilder {

        private StringBuilder builder = new StringBuilder();

        private boolean hasAtLeastOneField = false;

        public JsonObjectBuilder() {
            builder.append("{");
        }

        public void appendField(String key, String value) {
            appendFieldUnescaped(key, "\"" + escape(value) + "\"");
        }

        public void appendField(String key, int value) {
            appendFieldUnescaped(key, String.valueOf(value));
        }

        public void appendField(String key, JsonObject object) {
            appendFieldUnescaped(key, object.toString());
        }

        public void appendEmptyArray(String key) {
            appendFieldUnescaped(key, "[]");
        }

        private void appendFieldUnescaped(String key, String escapedValue) {
            if (builder == null) {
                throw new IllegalStateException("JSON has already been built");
            }
            if (key == null) {
                throw new IllegalArgumentException("JSON key must not be null");
            }
            if (hasAtLeastOneField) {
                builder.append(",");
            }
            builder.append("\"").append(escape(key)).append("\":").append(escapedValue);
            hasAtLeastOneField = true;
        }

        public JsonObject build() {
            if (builder == null) {
                throw new IllegalStateException("JSON has already been built");
            }
            JsonObject object = new JsonObject(builder.append("}").toString());
            builder = null;
            return object;
        }

        public static class JsonObject {

            private final String value;

            private JsonObject(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }
}