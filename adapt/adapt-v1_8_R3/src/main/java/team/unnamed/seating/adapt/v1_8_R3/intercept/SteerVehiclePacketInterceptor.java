package team.unnamed.seating.adapt.v1_8_R3.intercept;

import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import team.unnamed.seating.event.PlayerDismountFakeEntityEvent;
import team.unnamed.seating.adapt.intercept.PacketInterceptor;

public class SteerVehiclePacketInterceptor
        implements PacketInterceptor<PacketPlayInSteerVehicle> {

    private final Plugin plugin;

    public SteerVehiclePacketInterceptor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PacketPlayInSteerVehicle in(Player player, PacketPlayInSteerVehicle packet) {
        if (packet.d()) {
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
