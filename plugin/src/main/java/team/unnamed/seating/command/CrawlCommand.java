package team.unnamed.seating.command;

import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.SeatingHandler.*;
import static team.unnamed.seating.user.UserManager.CRAWL;

public class CrawlCommand extends ToggleSeatsCommand {

    public CrawlCommand(MessageHandler messageHandler,
                      SeatingHandler seatingHandler) {
        super(
                CRAWL_TOGGLE_PERMISSION, CRAWL_PERMISSION, CRAWL,
                messageHandler, seatingHandler
        );
    }

    @Override
    protected void execute(Player player) {
        seatingHandler.crawl(player);
    }

}
