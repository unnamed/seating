package team.unnamed.seating;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.AdaptionModuleFactory;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookRegistry;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.command.LayCommand;
import team.unnamed.seating.command.SitCommand;
import team.unnamed.seating.listener.BlockListeners;
import team.unnamed.seating.listener.PlayerDismountFakeEntityListener;
import team.unnamed.seating.listener.PlayerInteractListener;
import team.unnamed.seating.listener.PlayerJoinListener;
import team.unnamed.seating.listener.PlayerLeaveListener;
import team.unnamed.seating.message.MessageHandler;

public class SeatingPlugin extends JavaPlugin {

    private MessageHandler messageHandler;
    private PacketInterceptorAssigner packetInterceptorAssigner;
    private HookRegistry hookRegistry;
    private SeatingHandler seatingHandler;
    private SeatingEntityHandler seatingEntityHandler;
    private SeatingDataRegistry seatingDataRegistry;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        messageHandler = new MessageHandler(getConfig());

        AdaptionModule adaptionModule = AdaptionModuleFactory.create();
        packetInterceptorAssigner = adaptionModule.getPacketInterceptorAssigner();
        seatingEntityHandler = adaptionModule.getEntityHandler(messageHandler);

        seatingHandler = new SeatingHandler(getConfig());
        seatingDataRegistry = new SimpleSeatingDataRegistry(seatingEntityHandler);

        //hooks
        hookRegistry = new HookRegistry();
        hookRegistry.setupHookManagers(this, adaptionModule);

        adaptionModule.getPacketInterceptorRegister().registerInterceptors(this);
    }

    @Override
    public void onEnable() {
        registerListeners(
                new PlayerInteractListener(seatingHandler, hookRegistry, seatingDataRegistry),
                new PlayerJoinListener(packetInterceptorAssigner),
                new PlayerDismountFakeEntityListener(seatingDataRegistry),
                new PlayerLeaveListener(seatingDataRegistry),
                new BlockListeners(seatingDataRegistry)
        );

        registerCommand("sit", new SitCommand(seatingDataRegistry, messageHandler));
        registerCommand("lay", new LayCommand(seatingEntityHandler));
    }

    @Override
    public void onDisable() {

    }

    private void registerCommand(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

}
