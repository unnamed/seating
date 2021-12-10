package team.unnamed.seating.command;

import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.SeatingHandler.SIT_PERMISSION;
import static team.unnamed.seating.SeatingHandler.SIT_TOGGLE_PERMISSION;
import static team.unnamed.seating.user.UserManager.SIT;

public class SitCommand extends ToggleSeatsCommand {

    public SitCommand(MessageHandler messageHandler,
                      SeatingHandler seatingHandler) {
        super(
                SIT_TOGGLE_PERMISSION, SIT_PERMISSION, SIT,
                messageHandler, seatingHandler
        );
    }

    @Override
    protected void execute(Player player) {
        seatingHandler.sit(player, player.getLocation().getBlock(), true);
    }

}
