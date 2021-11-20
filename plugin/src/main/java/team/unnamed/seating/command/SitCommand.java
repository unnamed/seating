package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingDataRegistry;
import team.unnamed.seating.message.MessageHandler;

public class SitCommand implements CommandExecutor {

    private final SeatingDataRegistry dataRegistry;
    private final MessageHandler messageHandler;

    public SitCommand(SeatingDataRegistry dataRegistry,
                      MessageHandler messageHandler) {
        this.dataRegistry = dataRegistry;
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

        if (!player.hasPermission("seating.sit")) {
            messageHandler.sendMessage(player, "no-permission");
            return true;
        }

        dataRegistry.createAndAddRegistry(player, player.getLocation().getBlock());
        return false;
    }

}
