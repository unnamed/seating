package team.unnamed.seating.user;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class SimpleUserManager implements UserToggleSeatingManager {

    private final Set<UUID> disabledSeatingUsers;

    public SimpleUserManager() {
        disabledSeatingUsers = new HashSet<>();
    }

    @Override
    public boolean hasSeatingEnabled(Player player) {
        return !disabledSeatingUsers.contains(player.getUniqueId());
    }

    @Override
    public boolean toggleSeating(Player player) {
        UUID playerId = player.getUniqueId();
        if (disabledSeatingUsers.remove(playerId)) {
            return true;
        } else {
            disabledSeatingUsers.add(playerId);
            return false;
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
                disabledSeatingUsers.add(UUID.fromString(line));
            }
        }
    }

    @Override
    public void saveData(Plugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), "users.txt");
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                Iterator<UUID> iterator = disabledSeatingUsers.iterator();
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
