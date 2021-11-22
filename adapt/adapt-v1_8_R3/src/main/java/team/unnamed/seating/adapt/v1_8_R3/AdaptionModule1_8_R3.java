package team.unnamed.seating.adapt.v1_8_R3;

import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.v1_8_R3.intercept.SteerVehiclePacketInterceptor;
import team.unnamed.seating.adapt.v1_8_R3.seat.SeatingEntityHandler1_8_R3;
import team.unnamed.seating.adapt.v1_8_R3.hook.WorldGuardHookManager1_8_R3;
import team.unnamed.seating.message.MessageHandler;

public class AdaptionModule1_8_R3 implements AdaptionModule {

    @Override
    public SeatingEntityHandler getEntityHandler(MessageHandler messageHandler) {
        return new SeatingEntityHandler1_8_R3();
    }

    @Override
    public HookManager getWorldGuardHookManager() {
        return new WorldGuardHookManager1_8_R3();
    }

    @Override
    public void registerPacketInterceptors(Plugin plugin) {
        PacketChannelDuplexHandler.addInterceptor(
                PacketPlayInSteerVehicle.class,
                new SteerVehiclePacketInterceptor(plugin)
        );
    }

    @Override
    public void injectPacketHandler(Plugin plugin, String channelName, Player player) {
        ((CraftPlayer) player).getHandle()
                .playerConnection.networkManager
                .channel
                .pipeline()
                .addBefore("packet_handler", channelName, new PacketChannelDuplexHandler(player));
    }

}
