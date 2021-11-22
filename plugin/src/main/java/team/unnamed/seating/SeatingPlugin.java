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
import team.unnamed.seating.listener.*;
import team.unnamed.seating.message.MessageHandler;
import team.unnamed.seating.user.SimpleUserManager;
import team.unnamed.seating.user.UserManager;

import java.io.IOException;

public class SeatingPlugin extends JavaPlugin {

    private MessageHandler messageHandler;
    private PacketInterceptorAssigner packetInterceptorAssigner;
    private HookRegistry hookRegistry;
    private SeatingHandler seatingHandler;
    private SeatingEntityHandler seatingEntityHandler;
    private SeatingDataRegistry seatingDataRegistry;
    private UserManager userManager;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        AdaptionModule adaptionModule;
        userManager = new SimpleUserManager();

        try {
            adaptionModule = AdaptionModuleFactory.create();
            userManager.loadData(this);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        messageHandler = new MessageHandler(getConfig());

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

        registerCommands(
                "sit", new SitCommand(seatingDataRegistry, messageHandler),
                "lay", new LayCommand(seatingEntityHandler)
        );
    }

    @Override
    public void onDisable() {
        try {
            userManager.saveData(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands(Object... commands) {
        for (int i = 0; i < commands.length; i += 2) {
            String name = (String) commands[i];
            CommandExecutor commandExecutor = (CommandExecutor) commands[i + 1];

            getCommand(name).setExecutor(commandExecutor);
        }
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

}
