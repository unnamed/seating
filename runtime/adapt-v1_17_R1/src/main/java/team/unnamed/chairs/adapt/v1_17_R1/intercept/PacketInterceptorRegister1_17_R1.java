package team.unnamed.chairs.adapt.v1_17_R1.intercept;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import org.bukkit.plugin.Plugin;
import team.unnamed.chairs.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorRegister;

public class PacketInterceptorRegister1_17_R1
        implements PacketInterceptorRegister {

    @Override
    public void registerInterceptors(Plugin plugin) {
        PacketChannelDuplexHandler.addInterceptor(
                PacketPlayInSteerVehicle.class,
                new SteerVehiclePacketInterceptor(plugin)
        );
    }

}
