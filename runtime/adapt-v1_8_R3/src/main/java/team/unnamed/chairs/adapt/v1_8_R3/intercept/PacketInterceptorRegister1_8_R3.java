package team.unnamed.chairs.adapt.v1_8_R3.intercept;

import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;

import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorRegister;

public class PacketInterceptorRegister1_8_R3
        implements PacketInterceptorRegister {

    private final ChairDataRegistry chairDataRegistry;
    private final ChairEntityHandler chairEntityHandler;

    public PacketInterceptorRegister1_8_R3(ChairDataRegistry chairDataRegistry,
                                           ChairEntityHandler chairEntityHandler) {
        this.chairDataRegistry = chairDataRegistry;
        this.chairEntityHandler = chairEntityHandler;
    }

    @Override
    public void registerInterceptors() {
        PacketChannelDuplexHandler.addInterceptor(
                PacketPlayInSteerVehicle.class,
                new SteerVehiclePacketInterceptor(chairDataRegistry, chairEntityHandler)
        );
    }

}
