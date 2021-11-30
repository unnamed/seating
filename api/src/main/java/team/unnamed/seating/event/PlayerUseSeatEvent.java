package team.unnamed.seating.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import team.unnamed.seating.data.SeatingData;

public class PlayerUseSeatEvent
        extends PlayerEvent
        implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancel;
    private final SeatingData seatingData;

    public PlayerUseSeatEvent(Player who, SeatingData seatingData) {
        super(who);
        this.seatingData = seatingData;
    }

    public SeatingData getSeatingData() {
        return seatingData;
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
