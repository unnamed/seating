package team.unnamed.seating.adapt;

import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.message.MessageHandler;

public interface AdaptionModule {

    SeatingEntityHandler getEntityHandler(MessageHandler messageHandler);

    HookManager getWorldGuardHookManager();

    PacketInterceptorAssigner getPacketInterceptorAssigner();

    void registerPacketInterceptors(Plugin plugin);

}
