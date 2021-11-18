package team.unnamed.seating.adapt;

import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.adapt.intercept.PacketInterceptorRegister;

public interface AdaptionModule {

    SeatingEntityHandler getEntityHandler();

    HookManager getWorldGuardHookManager();

    PacketInterceptorRegister getPacketInterceptorRegister();

    PacketInterceptorAssigner getPacketInterceptorAssigner();

}
