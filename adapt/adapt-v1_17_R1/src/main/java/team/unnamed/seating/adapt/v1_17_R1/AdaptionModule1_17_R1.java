package team.unnamed.seating.adapt.v1_17_R1;

import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.adapt.intercept.PacketInterceptorRegister;
import team.unnamed.seating.adapt.v1_17_R1.seat.SeatingEntityHandler1_17_R1;
import team.unnamed.seating.adapt.v1_17_R1.hook.WorldGuardHookManager1_17_R1;
import team.unnamed.seating.adapt.v1_17_R1.intercept.PacketInterceptorAssigner1_17_R1;
import team.unnamed.seating.adapt.v1_17_R1.intercept.PacketInterceptorRegister1_17_R1;

public class AdaptionModule1_17_R1 implements AdaptionModule {

    @Override
    public SeatingEntityHandler getEntityHandler() {
        return new SeatingEntityHandler1_17_R1();
    }

    @Override
    public HookManager getWorldGuardHookManager() {
        return new WorldGuardHookManager1_17_R1();
    }

    @Override
    public PacketInterceptorRegister getPacketInterceptorRegister() {
        return new PacketInterceptorRegister1_17_R1();
    }

    @Override
    public PacketInterceptorAssigner getPacketInterceptorAssigner() {
        return new PacketInterceptorAssigner1_17_R1();
    }

}
