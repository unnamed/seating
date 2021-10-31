package team.unnamed.chairs.adapt.v1_8_R3;

import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.AdaptionModule;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorRegister;
import team.unnamed.chairs.adapt.v1_8_R3.chair.ChairEntityHandler1_8_R3;
import team.unnamed.chairs.adapt.v1_8_R3.intercept.PacketInterceptorAssigner1_8_R3;
import team.unnamed.chairs.adapt.v1_8_R3.intercept.PacketInterceptorRegister1_8_R3;

public class AdaptionModule1_8_R3 implements AdaptionModule {

    @Override
    public ChairEntityHandler getChairEntityHandler() {
        return new ChairEntityHandler1_8_R3();
    }

    @Override
    public PacketInterceptorRegister getPacketInterceptorRegister(ChairDataRegistry chairDataRegistry,
                                                                  ChairEntityHandler chairEntityHandler) {
        return new PacketInterceptorRegister1_8_R3(chairDataRegistry, chairEntityHandler);
    }

    @Override
    public PacketInterceptorAssigner getPacketInterceptorAssigner() {
        return new PacketInterceptorAssigner1_8_R3();
    }

}
