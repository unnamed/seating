package team.unnamed.seating.adapt.v1_17_R1;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.adapt.v1_17_R1.intercept.SteerVehiclePacketInterceptor;
import team.unnamed.seating.adapt.v1_17_R1.seat.SeatingEntityHandler1_17_R1;
import team.unnamed.seating.adapt.v1_17_R1.hook.WorldGuardHookManager1_17_R1;
import team.unnamed.seating.adapt.v1_17_R1.intercept.PacketInterceptorAssigner1_17_R1;
import team.unnamed.seating.message.MessageHandler;

public class AdaptionModule1_17_R1 implements AdaptionModule {

    @Override
    public SeatingEntityHandler getEntityHandler(MessageHandler messageHandler) {
        return new SeatingEntityHandler1_17_R1(messageHandler);
    }

    @Override
    public HookManager getWorldGuardHookManager() {
        return new WorldGuardHookManager1_17_R1();
    }

    @Override
    public PacketInterceptorAssigner getPacketInterceptorAssigner() {
        return new PacketInterceptorAssigner1_17_R1();
    }

    @Override
    public void registerPacketInterceptors(Plugin plugin) {
        PacketChannelDuplexHandler.addInterceptor(
                PacketPlayInSteerVehicle.class,
                new SteerVehiclePacketInterceptor(plugin)
        );
    }

}
