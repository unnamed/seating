package team.unnamed.seating.user;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class SimpleUserManager implements UserManager {

    private final Set<UUID> enabledSeatingUsers;

    public SimpleUserManager() {
        enabledSeatingUsers = new HashSet<>();
    }

    @Override
    public boolean hasSeatingEnabled(Player player) {
        return enabledSeatingUsers.contains(player.getUniqueId());
    }

    @Override
    public boolean toggleSeating(Player player) {
        UUID playerId = player.getUniqueId();
        if (enabledSeatingUsers.remove(playerId)) {
            return false;
        } else {
            return enabledSeatingUsers.add(playerId);
        }
    }

    @Override
    public void loadData(Plugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), "users.txt");
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Cannot create users file.");
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                enabledSeatingUsers.add(UUID.fromString(line));
            }
        }
    }

    @Override
    public void saveData(Plugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), "users.txt");
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                Iterator<UUID> iterator = enabledSeatingUsers.iterator();
                while (iterator.hasNext()) {
                    writer.write(iterator.next().toString());

                    if (iterator.hasNext()) {
                        writer.write("\n");
                    }
                }
            }
        }
    }

}
