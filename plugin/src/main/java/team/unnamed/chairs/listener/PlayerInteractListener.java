package team.unnamed.chairs.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.ChairDataRegistry;
import team.unnamed.chairs.ChairMaterialChecker;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;

public class PlayerInteractListener implements Listener {

    private final ChairMaterialChecker chairMaterialChecker;
    private final ChairEntityHandler chairEntityHandler;
    private final ChairDataRegistry chairDataRegistry;

    public PlayerInteractListener(ChairMaterialChecker chairMaterialChecker,
                                  ChairEntityHandler chairEntityHandler,
                                  ChairDataRegistry chairDataRegistry) {
        this.chairMaterialChecker = chairMaterialChecker;
        this.chairEntityHandler = chairEntityHandler;
        this.chairDataRegistry = chairDataRegistry;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Material type = block.getType();

        if (chairMaterialChecker.test(type)) {
            Player player = event.getPlayer();
            ChairData chairData = ChairData.create(player, block);

            chairEntityHandler.assignArmorStand(chairData);
            chairDataRegistry.addChairRegistry(player, chairData);
        }
    }

}
