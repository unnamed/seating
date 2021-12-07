package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import team.unnamed.seating.data.SeatingData;

import java.lang.ref.WeakReference;

import static team.unnamed.seating.util.CrawlUtils.*;

public class CrawlRunnable implements Runnable {

    private final WeakReference<Player> ownerReference;
    private final Location location;

    public CrawlRunnable(SeatingData seatingData) {
        this.ownerReference = seatingData.getOwnerReference();
        this.location = seatingData.getLocation();
        generateBarrier(location.getBlock());
    }

    @Override
    public void run() {
        Player owner = ownerReference.get();

        if (owner == null) {
            return;
        }

        Block lastBlock = location.getBlock();

        if (isBlockedToCrawl(owner)) { // this checks avoids any type of visual bugs
            regenerate(lastBlock);
            return;
        }

        Location currentPosition = buildLocation(owner);
        int currentX = currentPosition.getBlockX();
        int currentY = currentPosition.getBlockY();
        int currentZ = currentPosition.getBlockZ();

        if (
                location.getBlockX() == currentX &&
                        location.getBlockY() == currentY &&
                        location.getBlockZ() == currentZ
        ) { // check if player is in same location
            generateBarrier(lastBlock);
        } else {
            generateBarrier(currentPosition.getBlock());
            regenerate(lastBlock);

            // update stored location
            location.setX(currentX);
            location.setY(currentY);
            location.setZ(currentZ);
        }
    }

}
