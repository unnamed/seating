package team.unnamed.seating.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CrawlSeatingData extends AbstractSeatingData {

    protected CrawlSeatingData(Player owner, Location location) {
        super(owner, location);
    }

    @Override
    public Type getType() {
        return Type.CRAWL;
    }
}
