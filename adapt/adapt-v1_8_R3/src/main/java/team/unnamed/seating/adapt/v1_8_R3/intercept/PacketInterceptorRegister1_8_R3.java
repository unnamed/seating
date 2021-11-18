package team.unnamed.seating.adapt.v1_8_R3.intercept;

import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;

import org.bukkit.plugin.Plugin;

import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.intercept.PacketInterceptorRegister;

public class PacketInterceptorRegister1_8_R3
        implements PacketInterceptorRegister {

    @Override
    public void registerInterceptors(Plugin plugin) {
        PacketChannelDuplexHandler.addInterceptor(
                PacketPlayInSteerVehicle.class,
                new SteerVehiclePacketInterceptor(plugin)
        );
    }

}
