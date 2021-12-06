package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import team.unnamed.seating.data.SeatingData;

import java.lang.ref.WeakReference;

import static team.unnamed.seating.util.CrawlUtils.*;

public class CrawlRunnable implements Runnable {

    private final WeakReference<Player> ownerReference;
    private final Location storedLocation;
    private Location lastOwnerPosition;

    public CrawlRunnable(SeatingData seatingData) {
        this.ownerReference = seatingData.getOwnerReference();
        this.storedLocation = seatingData.getLocation();
        this.lastOwnerPosition = buildLocation(seatingData.getOwner());
        lastOwnerPosition.getBlock().setType(Material.BARRIER);
    }

    @Override
    public void run() {
        Player owner = ownerReference.get();

        if (owner == null) {
            return;
        }

        Block lastBlock = lastOwnerPosition.getBlock();

        if (isBlockedToCrawl(owner)) { // this checks avoids any type of visual bugs
            regenerate(lastBlock);
            return;
        }

        Location currentPosition = buildLocation(owner);

        if (lastOwnerPosition.equals(currentPosition)) { // check if players is in same location
            generateBarrier(lastBlock);
            return;
        }

        Block currentBlock = currentPosition.getBlock();

        generateBarrier(currentBlock);
        regenerate(lastBlock);

        lastOwnerPosition = currentPosition;

        // update stored location
        storedLocation.setX(currentPosition.getX());
        storedLocation.setY(currentPosition.getY());
        storedLocation.setZ(currentPosition.getZ());
    }

}
