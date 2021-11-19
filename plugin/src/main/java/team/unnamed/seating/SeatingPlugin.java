package team.unnamed.seating;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import team.unnamed.seating.adapt.AdaptionModule;
import team.unnamed.seating.adapt.AdaptionModuleFactory;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.hook.HookRegistry;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.seating.listener.PlayerDismountFakeEntityListener;
import team.unnamed.seating.listener.PlayerInteractListener;
import team.unnamed.seating.listener.PlayerJoinListener;
import team.unnamed.seating.listener.PlayerLeaveListener;
import team.unnamed.seating.metrics.Metrics;

public class SeatingPlugin extends JavaPlugin {

    private PacketInterceptorAssigner packetInterceptorAssigner;
    private HookRegistry hookRegistry;
    private SeatingHandler seatingHandler;
    private SeatingEntityHandler seatingEntityHandler;
    private SeatingDataRegistry seatingDataRegistry;

    @Override
    public void onLoad() {

        Metrics metrics = new Metrics(this, 13356);
        metrics.startSubmittingIfEnabled();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        AdaptionModule adaptionModule = AdaptionModuleFactory.create();
        packetInterceptorAssigner = adaptionModule.getPacketInterceptorAssigner();
        seatingEntityHandler = adaptionModule.getEntityHandler();

        seatingHandler = new SeatingHandler(getConfig());
        seatingDataRegistry = new SeatingDataRegistry();

        //hooks
        hookRegistry = new HookRegistry();
        hookRegistry.setupHookManagers(this, adaptionModule);

        adaptionModule.getPacketInterceptorRegister().registerInterceptors(this);
    }

    @Override
    public void onEnable() {
        registerListeners(
                new PlayerInteractListener(seatingHandler, hookRegistry, seatingEntityHandler, seatingDataRegistry),
                new PlayerJoinListener(packetInterceptorAssigner),
                new PlayerDismountFakeEntityListener(seatingEntityHandler, seatingDataRegistry),
                new PlayerLeaveListener(seatingDataRegistry, seatingEntityHandler)
        );
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

}
