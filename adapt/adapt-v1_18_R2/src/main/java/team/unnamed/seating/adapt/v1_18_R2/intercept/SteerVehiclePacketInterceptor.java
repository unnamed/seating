package team.unnamed.seating.adapt.v1_18_R2.intercept;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.intercept.PacketInterceptor;
import team.unnamed.seating.event.PlayerDismountFakeEntityEvent;

public record SteerVehiclePacketInterceptor(Plugin plugin)
        implements PacketInterceptor<PacketPlayInSteerVehicle> {

    @Override
    public PacketPlayInSteerVehicle in(Player player, PacketPlayInSteerVehicle packet) {
        if (packet.e()) {
            Bukkit.getScheduler().runTask(
                    plugin,
                    () -> Bukkit.getPluginManager().callEvent(
                            new PlayerDismountFakeEntityEvent(player)
                    )
            );
        }

        return packet;
    }

}
