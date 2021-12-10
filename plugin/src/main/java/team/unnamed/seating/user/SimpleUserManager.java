package team.unnamed.seating.user;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleUserManager implements UserManager {

    private final Map<UUID, Byte> enabledSeatingUsers;

    public SimpleUserManager() {
        enabledSeatingUsers = new HashMap<>();
    }

    private boolean hasPropertyEnabled(byte mask, byte property) {
        return (mask & property) != 0x0;
    }

    @Override
    public boolean hasSeatingEnable(Player player, byte property) {
        Byte mask = enabledSeatingUsers.get(player.getUniqueId());
        return mask != null && hasPropertyEnabled(mask, property);
    }

    @Override
    public boolean toggleSeating(Player player, byte property) {
        UUID playerId = player.getUniqueId();
        Byte mask = enabledSeatingUsers.get(playerId);
        byte newMask = mask == null ? 0x0 : mask;

        newMask ^= property;

        enabledSeatingUsers.put(playerId, newMask);
        return hasPropertyEnabled(newMask, property);
    }

    @Override
    public void loadData(Plugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), "users");
        if (file.exists()) {
            try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
                int size = input.readInt();
                for (int i = 0; i < size; i++) {
                    UUID uuid = new UUID(input.readLong(), input.readLong());
                    byte mask = input.readByte();
                    enabledSeatingUsers.put(uuid, mask);
                }
            }
        }
    }

    @Override
    public void saveData(Plugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), "users");
        boolean write = false;
        if (!file.exists()) {
            write = file.createNewFile();
        }

        if (!write) {
            throw new IOException("Could not create file");
        }

        try (DataOutputStream output =
                     new DataOutputStream(new FileOutputStream(file))) {
            output.writeInt(enabledSeatingUsers.size()); // write size for future reading
            for (Map.Entry<UUID, Byte> entry : enabledSeatingUsers.entrySet()) {
                UUID uuid = entry.getKey();
                output.writeLong(uuid.getMostSignificantBits());
                output.writeLong(uuid.getLeastSignificantBits());
                output.writeByte(entry.getValue());
            }
        }
    }

}
