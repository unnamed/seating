package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.message.MessageHandler;

public class CrawlCommand implements CommandExecutor {

    private final MessageHandler messageHandler;

    public CrawlCommand(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to do this");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("seating.crawl")) {
            messageHandler.sendMessage(player, "no-permission");
            return true;
        }

        return true;
    }

}
