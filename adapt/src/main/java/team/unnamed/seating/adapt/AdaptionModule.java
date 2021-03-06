package team.unnamed.seating.adapt;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;
import team.unnamed.seating.adapt.seat.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookManager;
import team.unnamed.seating.adapt.material.MaterialChecker;
import team.unnamed.seating.message.MessageHandler;

public interface AdaptionModule {

    SeatingEntityHandler getEntityHandler(MessageHandler messageHandler);

    MessageDisplayHandler getMessageDisplayHandler();

    MaterialChecker getMaterialChecker();

    HookManager getWorldGuardHookManager();

    void injectPacketHandler(Plugin plugin, String channelName, Player player);

    void registerPacketInterceptors(Plugin plugin);

}
