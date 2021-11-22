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
import team.unnamed.seating.command.LayCommand;
import team.unnamed.seating.command.SitCommand;
import team.unnamed.seating.listener.*;
import team.unnamed.seating.message.MessageHandler;
import team.unnamed.seating.user.SimpleUserManager;
import team.unnamed.seating.user.UserManager;

import java.io.IOException;

public class SeatingPlugin extends JavaPlugin {

    private MessageHandler messageHandler;
    private AdaptionModule adaptionModule;
    private UserManager userManager;
    private HookRegistry hookRegistry;
    private SeatingHandler seatingHandler;
    private SeatingEntityHandler seatingEntityHandler;
    private SeatingDataRegistry seatingDataRegistry;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        messageHandler = new MessageHandler(getConfig());

        userManager = new SimpleUserManager();

        try {
            adaptionModule = AdaptionModuleFactory.create();
            userManager.loadData(this);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

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
                new PlayerJoinListener(this, adaptionModule),
                new PlayerDismountFakeEntityListener(seatingDataRegistry),
                new PlayerLeaveListener(seatingDataRegistry),
                new BlockListeners(seatingDataRegistry)
        );

        registerCommand("sit", new SitCommand(seatingDataRegistry, messageHandler));
        registerCommand("lay", new LayCommand(seatingEntityHandler));
        registerCommand("crawl", new CrawlCommand(messageHandler, seatingEntityHandler));
    }

    @Override
    public void onDisable() {
        try {
            userManager.saveData(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
