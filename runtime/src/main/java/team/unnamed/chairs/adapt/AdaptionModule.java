package team.unnamed.chairs.adapt;

import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorRegister;

public interface AdaptionModule {

    ChairEntityHandler getChairEntityHandler();

    PacketInterceptorRegister getPacketInterceptorRegister(ChairDataRegistry chairDataRegistry,
                                                           ChairEntityHandler chairEntityHandler);

    PacketInterceptorAssigner getPacketInterceptorAssigner();

}
