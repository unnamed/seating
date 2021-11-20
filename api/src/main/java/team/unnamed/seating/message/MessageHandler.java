package team.unnamed.seating.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MessageHandler {

    private static final String BASE_MESSAGES_PATH = "messages.%s";

    private final FileConfiguration configuration;

    public MessageHandler(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getMessage(String path) {
        String message = configuration.getString(makePath(path));

        if (message == null) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> getMessages(String path) {
        List<String> messages = configuration.getStringList(makePath(path));

        if (messages.isEmpty()) {
            return Collections.singletonList(path);
        }

        messages.replaceAll(message ->
                ChatColor.translateAlternateColorCodes('&', message));

        return messages;
    }

    public void sendMessage(Player player, String path) {
        player.sendMessage(getMessage(path));
    }

    public void sendMessages(Player player, String path) {
        player.sendMessage(String.join("\n", getMessage(path)));
    }

    private String makePath(String path) {
        return String.format(BASE_MESSAGES_PATH, path);
    }

}
