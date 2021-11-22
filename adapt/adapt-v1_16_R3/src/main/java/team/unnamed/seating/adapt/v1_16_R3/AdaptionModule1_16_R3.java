package team.unnamed.seating.adapt.v1_16_R3;

import net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.adapt.v1_16_R3.intercept.SteerVehiclePacketInterceptor;
import team.unnamed.seating.adapt.v1_16_R3.seat.SeatingEntityHandler1_16_R3;
import team.unnamed.seating.adapt.v1_16_R3.hook.WorldGuardHookManager1_16_R3;
import team.unnamed.seating.adapt.v1_16_R3.intercept.PacketInterceptorAssigner1_16_R3;
import team.unnamed.seating.message.MessageHandler;

public class AdaptionModule1_16_R3 implements AdaptionModule {

    @Override
    public SeatingEntityHandler getEntityHandler(MessageHandler messageHandler) {
        return new SeatingEntityHandler1_16_R3();
    }

    @Override
    public HookManager getWorldGuardHookManager() {
        return new WorldGuardHookManager1_16_R3();
    }

    @Override
    public PacketInterceptorAssigner getPacketInterceptorAssigner() {
        return new PacketInterceptorAssigner1_16_R3();
    }

    public void registerPacketInterceptors(Plugin plugin) {
        PacketChannelDuplexHandler.addInterceptor(
                PacketPlayInSteerVehicle.class,
                new SteerVehiclePacketInterceptor(plugin)
        );
    }

}
