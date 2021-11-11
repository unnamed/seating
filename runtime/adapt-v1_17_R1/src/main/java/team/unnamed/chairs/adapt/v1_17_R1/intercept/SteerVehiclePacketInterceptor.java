package team.unnamed.chairs.adapt.v1_17_R1.intercept;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.chairs.adapt.intercept.PacketInterceptor;
import team.unnamed.chairs.event.PlayerDismountFakeEntityEvent;

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
