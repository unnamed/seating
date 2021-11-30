package team.unnamed.seating.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.UUID;

public interface SeatingData {

    Type getType();

    WeakReference<Player> getOwnerReference();

    Player getOwner();

    UUID getOwnerId();

    int getSpigotId();

    void setSpigotId(int spigotId);

    Location getLocation();

    static CrawlSeatingData createCrawlData(Player player) {
        return new CrawlSeatingData(player, player.getLocation());
    }

    enum Type {
        CRAWL, CHAIR, LAY
    }

}
