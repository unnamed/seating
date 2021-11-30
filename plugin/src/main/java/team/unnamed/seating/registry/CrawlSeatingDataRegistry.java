package team.unnamed.seating.registry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import team.unnamed.seating.CrawlRunnable;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.data.CrawlSeatingData;
import team.unnamed.seating.data.SeatingData;

import java.util.UUID;

import static team.unnamed.seating.util.CrawlUtils.regenerate;

public class CrawlSeatingDataRegistry
        extends AbstractSeatingDataRegistry<CrawlSeatingData> {

    private final Plugin plugin;

    public CrawlSeatingDataRegistry(Plugin plugin, SeatingEntityHandler entityHandler) {
        super(entityHandler);
        this.plugin = plugin;
    }

    @Override
    public void addRegistry(Player player, CrawlSeatingData seatingData) {
        super.addRegistry(player, seatingData);
        entityHandler.sendDismountActionbar(player);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(
                plugin, new CrawlRunnable(seatingData), 0, 1
        );
        seatingData.setSpigotId(bukkitTask.getTaskId());
    }

    @Override
    public @Nullable CrawlSeatingData removeRegistry(UUID uuid) {
        CrawlSeatingData seatingData = super.removeRegistry(uuid);

        if (seatingData != null) {
            Block block = seatingData.getLocation().getBlock();
            regenerate(block);
            Bukkit.getScheduler().cancelTask(seatingData.getSpigotId());
        }

        return seatingData;
    }

    @Override
    public @Nullable CrawlSeatingData getRegistry(Location location) {
        return null;
    }

    @Override
    public @Nullable CrawlSeatingData removeRegistry(Location location) {
        return null;
    }

    @Override
    public boolean isLocationUsed(Location location) {
        return false;
    }

    @Override
    protected CrawlSeatingData internalCreateAndAdd(Player player, Block block) {
        return SeatingData.createCrawlData(player);
    }

}
