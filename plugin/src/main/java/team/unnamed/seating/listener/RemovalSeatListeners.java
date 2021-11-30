package team.unnamed.seating.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.data.CrawlSeatingData;

public class RemovalSeatListeners implements Listener {

    private final SeatingDataRegistry<ChairSeatingData> chairDataRegistry;
    private final SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry;

    public RemovalSeatListeners(SeatingDataRegistry<ChairSeatingData> chairDataRegistry,
                                SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry) {
        this.chairDataRegistry = chairDataRegistry;
        this.crawlDataRegistry = crawlDataRegistry;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        remove(event);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        remove(event);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        remove(event);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        remove(event);
    }

    @EventHandler(ignoreCancelled = true) // for worldguard or some region manager
    public void onBlockBreak(BlockBreakEvent event) {
        // just remove without checking if we check the location is used
        // will result in a double get of the node
        chairDataRegistry.removeRegistry(event.getBlock().getLocation());
    }

    private void remove(PlayerEvent event) {
        remove(event.getPlayer());
    }

    private void remove(EntityEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        remove((Player) entity);
    }

    private void remove(Player player) {
        chairDataRegistry.removeRegistry(player);
        if (crawlDataRegistry != null) {
            crawlDataRegistry.removeRegistry(player);
        }
    }

}
