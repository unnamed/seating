package team.unnamed.chairs;

import org.bukkit.entity.Player;

import team.unnamed.chairs.adapt.entity.ChairEntityHandler;

public class ChairYawUpdaterRunnable implements Runnable {

    private final ChairDataRegistry chairDataRegistry;
    private final ChairEntityHandler chairEntityHandler;

    public ChairYawUpdaterRunnable(ChairDataRegistry chairDataRegistry,
                                   ChairEntityHandler chairEntityHandler) {
        this.chairDataRegistry = chairDataRegistry;
        this.chairEntityHandler = chairEntityHandler;
    }

    @Override
    public void run() {
        for (ChairData chairData : chairDataRegistry.getAllChairs()) {
            Player owner = chairData.getOwner();
            chairData.getLocation().setYaw(owner.getLocation().getYaw());
            chairEntityHandler.updateChairYaw(chairData);
        }
    }

}
