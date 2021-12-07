package team.unnamed.seating.adapt.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Packets {

    private Packets() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    public static void send(Player player, Packet<?>... packets) {
        send(((CraftPlayer) player).getHandle(), packets);
    }

    public static void send(EntityPlayer player, Packet<?>... packets) {
        send(player.playerConnection, packets);
    }

    public static void send(PlayerConnection connection, Packet<?>... packets) {
        for (Packet<?> packet : packets) {
            connection.sendPacket(packet);
        }
    }

}
