package team.unnamed.seating;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.hook.HookRegistry;
import team.unnamed.seating.adapt.material.MaterialChecker;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.data.CrawlSeatingData;
import team.unnamed.seating.message.MessageHandler;
import team.unnamed.seating.user.UserManager;

import java.util.List;

import static team.unnamed.seating.user.UserManager.CRAWL;
import static team.unnamed.seating.user.UserManager.SIT;
import static team.unnamed.seating.util.CrawlUtils.isBlockedToCrawl;

public class SimpleSeatingHandler implements SeatingHandler {

    private final FileConfiguration configuration;
    private final MessageHandler messageHandler;
    private final SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry;
    private final SeatingDataRegistry<ChairSeatingData> chairDataRegistry;
    private final HookRegistry hookRegistry;
    private final MaterialChecker materialChecker;
    private final UserManager userManager;

    public SimpleSeatingHandler(FileConfiguration configuration,
                                MessageHandler messageHandler,
                                SeatingDataRegistry<CrawlSeatingData> crawlDataRegistry,
                                SeatingDataRegistry<ChairSeatingData> chairDataRegistry,
                                HookRegistry hookRegistry,
                                MaterialChecker materialChecker,
                                UserManager userManager) {
        this.configuration = configuration;
        this.messageHandler = messageHandler;
        this.crawlDataRegistry = crawlDataRegistry;
        this.chairDataRegistry = chairDataRegistry;
        this.hookRegistry = hookRegistry;
        this.materialChecker = materialChecker;
        this.userManager = userManager;
    }

    @Override
    public boolean isInChairUseRange(Player player, Block block) {
        int range = configuration.getInt("seating.chairs-use-range");

        if (range == 0) { // ignore range
            return true;
        }

        return Math.floor(
                player.getLocation().distance(block.getLocation())
        ) <= range;
    }

    @Override
    public boolean isWorldDenied(World world) {
        List<String> deniedWorldNames = configuration.getStringList("seating.denied-worlds");
        return deniedWorldNames.contains(world.getName());
    }

    @Override
    public boolean isChairMaterial(Block block) {
        List<String> configuredChairsMaterials
                = configuration.getStringList("seating.chairs-materials");

        for (String key : configuredChairsMaterials) {
            String[] tags = key.split("#");

            if (tags.length == 1) {
                Material material = Material.getMaterial(tags[0]);
                if (material == block.getType()) {
                    return true;
                }
            } else {
                switch (tags[1]) {
                    case "SLABS": {
                        if (materialChecker.isSlab(block)) {
                            return true;
                        }
                    }
                    case "STAIRS": {
                        if (materialChecker.isStair(block)) {
                            return true;
                        }
                    }
                    case "CARPETS": {
                        if (materialChecker.isCarpet(block)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void toggleSeating(Player player, byte property) {
        String toggleType = "";

        switch (property) {
            case SIT: {
                toggleType = "sit";
                break;
            }
            case CRAWL: {
                toggleType = "crawl";
                break;
            }
        }

        String path = userManager
                .toggleSeating(player, property) ?
                "enable" : "disable";

        messageHandler.sendMessage(player, path + "-seatings." + toggleType);
    }

    @Override
    public void sit(Player player, Block block, boolean ignoreType) {
        if (!ignoreType && !isChairMaterial(block)) {
            return;
        }

        if (userManager.hasSeatingEnable(player, SIT)) {
            Location location = block.getLocation();

            if (hookRegistry.isAvailableToSit(location, player)) {
                if (isWorldDenied(location.getWorld())) {
                    return;
                }

                if (!materialChecker.isAir(block.getRelative(BlockFace.UP))) {
                    return;
                }

                if (chairDataRegistry.isLocationUsed(location)) {
                    return;
                }

                chairDataRegistry.removeRegistry(player);
                chairDataRegistry.createAndAddRegistry(player, block);
            }
        }
    }

    @Override
    public void crawl(Player player) {
        Location location = player.getLocation();
        if (isWorldDenied(location.getWorld())) {
            return;
        }

        if (isBlockedToCrawl(player)) {
            return;
        }

        if (!hookRegistry.isAvailableToCrawl(player)) {
            return;
        }

        Block block = location.getBlock();

        if (materialChecker.isSlab(block) ||
                materialChecker.isStair(block) ||
                materialChecker.isStair(
                        location.subtract(0, 1, 0).getBlock()
                )) {
            return;
        }

        if (userManager.hasSeatingEnable(player, CRAWL)) {
            if (crawlDataRegistry.removeRegistry(player) == null) {
                crawlDataRegistry.createAndAddRegistry(player, null);
            }
        }
    }

}
