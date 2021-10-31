package team.unnamed.chairs.adapt.v1_8_R3.intercept;

import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;

import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptor;

public class SteerVehiclePacketInterceptor
        implements PacketInterceptor<PacketPlayInSteerVehicle> {

    private final ChairDataRegistry chairDataRegistry;
    private final ChairEntityHandler chairEntityHandler;

    public SteerVehiclePacketInterceptor(ChairDataRegistry chairDataRegistry,
                                         ChairEntityHandler chairEntityHandler) {
        this.chairDataRegistry = chairDataRegistry;
        this.chairEntityHandler = chairEntityHandler;
    }

    @Override
    public PacketPlayInSteerVehicle in(Player player, PacketPlayInSteerVehicle packet) {
        if (packet.d()) {
            ChairData chairData = chairDataRegistry.getRegistry(player);

            if (chairData != null) {
                chairEntityHandler.destroyChair(chairData);
                chairDataRegistry.removeChairRegistry(player);
            }
        }

        return packet;
    }

}
