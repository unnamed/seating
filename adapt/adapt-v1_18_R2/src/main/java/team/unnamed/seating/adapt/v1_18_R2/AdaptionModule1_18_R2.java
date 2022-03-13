package team.unnamed.seating.adapt.v1_18_R2;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.material.MaterialChecker;
import team.unnamed.seating.adapt.seat.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_18_R2.display.MessageDisplayHandler1_18_R2;
import team.unnamed.seating.adapt.v1_18_R2.hook.WorldGuardHookManager1_18_R2;
import team.unnamed.seating.adapt.v1_18_R2.intercept.SteerVehiclePacketInterceptor;
import team.unnamed.seating.adapt.v1_18_R2.material.MaterialChecker1_18_R2;
import team.unnamed.seating.adapt.v1_18_R2.seat.SeatingEntityHandler1_18_R2;
import team.unnamed.seating.message.MessageHandler;

public class AdaptionModule1_18_R2 implements AdaptionModule {

    @Override
    public SeatingEntityHandler getEntityHandler(MessageHandler messageHandler) {
        return new SeatingEntityHandler1_18_R2(messageHandler);
    }

    @Override
    public MessageDisplayHandler getMessageDisplayHandler() {
        return new MessageDisplayHandler1_18_R2();
    }

    @Override
    public MaterialChecker getMaterialChecker() {
        return new MaterialChecker1_18_R2();
    }

    @Override
    public HookManager getWorldGuardHookManager() {
        return new WorldGuardHookManager1_18_R2();
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
                .b.a.m.pipeline()
                .addBefore(
                        "packet_handler", channelName,
                        new PacketChannelDuplexHandler(player)
                );
    }

}
