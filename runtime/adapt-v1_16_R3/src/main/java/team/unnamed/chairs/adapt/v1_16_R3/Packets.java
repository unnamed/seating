package team.unnamed.chairs.adapt.v1_16_R3;

import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Packets {

    private Packets() {
        throw new UnsupportedOperationException();
    }

    public static void send(Player player, Iterable<Packet<?>> packets) {
        PlayerConnection connection = ((CraftPlayer) player)
                .getHandle().playerConnection;
        for (Packet<?> packet : packets) {
            connection.sendPacket(packet);
        }
    }

    public static void send(Player player, Packet<?>... packets) {
        PlayerConnection connection = ((CraftPlayer) player)
                .getHandle().playerConnection;
        for (Packet<?> packet : packets) {
            connection.sendPacket(packet);
        }
    }

}
