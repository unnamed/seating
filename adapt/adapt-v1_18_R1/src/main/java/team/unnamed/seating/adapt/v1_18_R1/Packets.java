package team.unnamed.seating.adapt.v1_18_R1;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Packets {

    private Packets() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    public static void send(Player player, Packet<?>... packets) {
        send(((CraftPlayer) player).getHandle(), packets);
    }

    public static void send(EntityPlayer player, Packet<?>... packets) {
        send(player.b, packets);
    }

    public static void send(PlayerConnection connection, Packet<?>... packets) {
        for (Packet<?> packet : packets) {
            connection.a(packet);
        }
    }

}
