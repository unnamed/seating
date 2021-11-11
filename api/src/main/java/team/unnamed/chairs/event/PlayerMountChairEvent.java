package team.unnamed.chairs.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import team.unnamed.chairs.ChairData;

public class PlayerMountChairEvent
        extends PlayerEvent
        implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancel;
    private final ChairData chairData;

    public PlayerMountChairEvent(Player who, ChairData chairData) {
        super(who);
        this.chairData = chairData;
    }

    public ChairData getChairData() {
        return chairData;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
