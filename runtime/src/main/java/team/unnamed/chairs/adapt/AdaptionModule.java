package team.unnamed.chairs.adapt;

import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.hook.HookManager;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorRegister;

public interface AdaptionModule {

    ChairEntityHandler getChairEntityHandler();

    HookManager getWorldGuardHookManager();

    PacketInterceptorRegister getPacketInterceptorRegister();

    PacketInterceptorAssigner getPacketInterceptorAssigner();

}
