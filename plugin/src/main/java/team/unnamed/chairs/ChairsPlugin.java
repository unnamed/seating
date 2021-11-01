package team.unnamed.chairs;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import team.unnamed.chairs.adapt.AdaptionModule;
import team.unnamed.chairs.adapt.AdaptionModuleFactory;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorAssigner;
import team.unnamed.chairs.listener.PlayerInteractListener;
import team.unnamed.chairs.listener.PlayerJoinListener;

public class ChairsPlugin extends JavaPlugin {

    private PacketInterceptorAssigner packetInterceptorAssigner;
    private ChairHandler chairHandler;
    private ChairEntityHandler chairEntityHandler;
    private ChairDataRegistry chairDataRegistry;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        AdaptionModule adaptionModule = AdaptionModuleFactory.create();
        packetInterceptorAssigner = adaptionModule.getPacketInterceptorAssigner();
        chairEntityHandler = adaptionModule.getChairEntityHandler();

        chairHandler = new ChairHandler(getConfig());
        chairDataRegistry = new ChairDataRegistry();

        adaptionModule.getPacketInterceptorRegister(chairDataRegistry, chairEntityHandler)
                .registerInterceptors();
    }

    @Override
    public void onEnable() {
        registerListeners(
                new PlayerInteractListener(chairHandler, chairEntityHandler, chairDataRegistry),
                new PlayerJoinListener(packetInterceptorAssigner)
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
