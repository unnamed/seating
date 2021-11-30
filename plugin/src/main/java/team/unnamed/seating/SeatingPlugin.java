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
import team.unnamed.seating.command.CrawlCommand;
import team.unnamed.seating.command.LayCommand;
import team.unnamed.seating.command.SitCommand;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.data.CrawlSeatingData;
import team.unnamed.seating.data.SeatingData;
import team.unnamed.seating.listener.CrawlListeners;
import team.unnamed.seating.listener.PlayerJoinListener;
import team.unnamed.seating.listener.RemovalSeatListeners;
import team.unnamed.seating.listener.SitListeners;
import team.unnamed.seating.message.MessageHandler;
import team.unnamed.seating.registry.ChairSeatingDataRegistry;
import team.unnamed.seating.registry.CrawlSeatingDataRegistry;
import team.unnamed.seating.user.SimpleUserManager;
import team.unnamed.seating.user.UserToggleSeatingManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static team.unnamed.bukkit.ServerVersionUtils.SERVER_VERSION_INT;
import static team.unnamed.seating.util.CrawlUtils.regenerate;

public class SeatingPlugin extends JavaPlugin {

    private MessageHandler messageHandler;
    private AdaptionModule adaptionModule;
    private UserToggleSeatingManager userToggleSeatingManager;
    private HookRegistry hookRegistry;
    private SeatingHandler seatingHandler;
    private SeatingEntityHandler seatingEntityHandler;
    private SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry;
    private SeatingDataRegistry<ChairSeatingData> chairDataRegistry;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        userToggleSeatingManager = new SimpleUserManager();

        try {
            adaptionModule = AdaptionModuleFactory.create();
            userToggleSeatingManager.loadData(this);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        messageHandler = new MessageHandler(getConfig());
        seatingEntityHandler = adaptionModule.getEntityHandler(messageHandler);

        if (SERVER_VERSION_INT >= 13) {
            crawlDataRegistry = new CrawlSeatingDataRegistry(this, seatingEntityHandler);
        }

        chairDataRegistry = new ChairSeatingDataRegistry(seatingEntityHandler);

        hookRegistry = new HookRegistry();
        hookRegistry.setupHookManagers(this, adaptionModule);

        seatingHandler = new SimpleSeatingHandler(
                getConfig(), messageHandler,
                crawlDataRegistry, chairDataRegistry,
                hookRegistry, adaptionModule.getMaterialChecker(),
                userToggleSeatingManager
        );

        adaptionModule.registerPacketInterceptors(this);
    }

    @Override
    public void onEnable() {
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerJoinListener(this, adaptionModule));
        listeners.add(new RemovalSeatListeners(chairDataRegistry, crawlDataRegistry));
        listeners.add(new SitListeners(chairDataRegistry, seatingHandler));

        if (crawlDataRegistry != null) {
            listeners.add(new CrawlListeners(crawlDataRegistry, seatingHandler));
            registerCommand("crawl", new CrawlCommand(messageHandler, seatingHandler));
        }

        listen(listeners);
        registerCommand("sit", new SitCommand(messageHandler, seatingHandler));
        registerCommand("lay", new LayCommand(seatingEntityHandler));
    }

    @Override
    public void onDisable() {
        // remove all blocks of players in crawl mode
        if (crawlDataRegistry != null) {
            for (SeatingData seatingData : crawlDataRegistry.getAllData()) {
                regenerate(seatingData.getLocation().getBlock());
            }
        }

        try {
            userToggleSeatingManager.saveData(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerCommand(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
    }

    private void listen(List<Listener> listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

}
