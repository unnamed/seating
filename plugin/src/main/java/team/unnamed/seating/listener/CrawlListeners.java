package team.unnamed.seating.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.data.CrawlSeatingData;

import static team.unnamed.seating.SeatingHandler.CRAWL_PERMISSION;
import static team.unnamed.seating.util.CrawlUtils.isBlockedToCrawl;

public class CrawlListeners implements Listener {

    private final SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry;
    private final SeatingHandler seatingHandler;

    public CrawlListeners(SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry,
                          SeatingHandler seatingHandler) {
        this.crawlDataRegistry = crawlDataRegistry;
        this.seatingHandler = seatingHandler;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                // avoid player gets damage by the block above
                if (crawlDataRegistry.isRegistered(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (event.isSneaking()) {
            if (crawlDataRegistry.removeRegistry(player) == null) {
                if (!player.hasPermission(CRAWL_PERMISSION)) {
                    return;
                }

                if (player.getLocation().getPitch() > 45) { // check if player is looking at floor
                    seatingHandler.crawl(player);
                }
            }
        }
    }

}
