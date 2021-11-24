package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.message.MessageHandler;
import team.unnamed.seating.user.UserToggleSeatingManager;

public class SitCommand implements CommandExecutor {

    private final SeatingDataRegistry dataRegistry;
    private final MessageHandler messageHandler;
    private final UserToggleSeatingManager userToggleSeatingManager;

    public SitCommand(SeatingDataRegistry dataRegistry,
                      MessageHandler messageHandler,
                      UserToggleSeatingManager userToggleSeatingManager) {
        this.dataRegistry = dataRegistry;
        this.messageHandler = messageHandler;
        this.userToggleSeatingManager = userToggleSeatingManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to do this");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("seating.sit")
                || !player.hasPermission("seating.sit-toggle")) {
            messageHandler.sendMessage(player, "no-permission");
            return true;
        }

        if (args.length == 0) {
            dataRegistry.createAndAddRegistry(player, player.getLocation().getBlock());
        } else {
            String path = userToggleSeatingManager.toggleSeating(player) ? "enable" : "disable";
            messageHandler.sendMessage(player, path + "-seatings");
        }

        return false;
    }

}
