package team.unnamed.chairs.adapt.v1_17_R1;

import team.unnamed.chairs.adapt.AdaptionModule;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.hook.HookManager;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorRegister;
import team.unnamed.chairs.adapt.v1_17_R1.chair.ChairEntityHandler1_17_R1;
import team.unnamed.chairs.adapt.v1_17_R1.hook.WorldGuardHookManager1_17_R1;
import team.unnamed.chairs.adapt.v1_17_R1.intercept.PacketInterceptorAssigner1_17_R1;
import team.unnamed.chairs.adapt.v1_17_R1.intercept.PacketInterceptorRegister1_17_R1;

public class AdaptionModule1_17_R1 implements AdaptionModule {

    @Override
    public ChairEntityHandler getChairEntityHandler() {
        return new ChairEntityHandler1_17_R1();
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
