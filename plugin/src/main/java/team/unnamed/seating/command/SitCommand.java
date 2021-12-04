package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.SeatingHandler.SIT_PERMISSION;
import static team.unnamed.seating.SeatingHandler.SIT_TOGGLE_PERMISSION;

public class SitCommand implements CommandExecutor {

    private final MessageHandler messageHandler;
    private final SeatingHandler seatingHandler;

    public SitCommand(MessageHandler messageHandler,
                      SeatingHandler seatingHandler) {
        this.messageHandler = messageHandler;
        this.seatingHandler = seatingHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to do this");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(SIT_PERMISSION)
                || !player.hasPermission(SIT_TOGGLE_PERMISSION)) {
            messageHandler.sendMessage(player, "no-permission");
            return true;
        }

        if (args.length == 0) {
            seatingHandler.sit(player, player.getLocation().getBlock(), true);
        } else {
            seatingHandler.toggleSeating(player);
        }

        return false;
    }

}
