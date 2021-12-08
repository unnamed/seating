package team.unnamed.seating.adapt.v1_12_R1;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.material.MaterialChecker;
import team.unnamed.seating.adapt.seat.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_12_R1.display.MessageDisplayHandler1_12_R1;
import team.unnamed.seating.adapt.v1_12_R1.hook.WorldGuardHookManager1_12_R1;
import team.unnamed.seating.adapt.v1_12_R1.intercept.SteerVehiclePacketInterceptor;
import team.unnamed.seating.adapt.v1_12_R1.material.MaterialChecker1_12_R1;
import team.unnamed.seating.adapt.v1_12_R1.seat.SeatingEntityHandler1_12_R1;
import team.unnamed.seating.message.MessageHandler;

public class AdaptionModule1_12_R1 implements AdaptionModule {

    @Override
    public SeatingEntityHandler getEntityHandler(MessageHandler messageHandler) {
        return new SeatingEntityHandler1_12_R1(messageHandler);
    }

    @Override
    public MessageDisplayHandler getMessageDisplayHandler() {
        return new MessageDisplayHandler1_12_R1();
    }

    @Override
    public MaterialChecker getMaterialChecker() {
        return new MaterialChecker1_12_R1();
    }

    @Override
    public HookManager getWorldGuardHookManager() {
        return new WorldGuardHookManager1_12_R1();
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
