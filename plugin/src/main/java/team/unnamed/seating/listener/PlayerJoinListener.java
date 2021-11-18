package team.unnamed.seating.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;

public class PlayerJoinListener implements Listener {

    private final PacketInterceptorAssigner packetInterceptorAssigner;

    public PlayerJoinListener(PacketInterceptorAssigner packetInterceptorAssigner) {
        this.packetInterceptorAssigner = packetInterceptorAssigner;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        packetInterceptorAssigner.assignToPlayer(event.getPlayer(), "un-seats");
    }

}
