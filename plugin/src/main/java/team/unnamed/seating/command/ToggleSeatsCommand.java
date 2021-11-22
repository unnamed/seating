package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.message.MessageHandler;
import team.unnamed.seating.user.UserManager;

public class ToggleSeatsCommand implements CommandExecutor {

    private final UserManager userManager;
    private final MessageHandler messageHandler;

    public ToggleSeatsCommand(UserManager userManager,
                              MessageHandler messageHandler) {
        this.userManager = userManager;
        this.messageHandler = messageHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to do this");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("seating.toggle-sit")) {
            messageHandler.sendMessage(player, "no-permission");
            return true;
        }

        userManager.toggleSeating(player); //TODO: send toggle message
        return false;
    }

}
