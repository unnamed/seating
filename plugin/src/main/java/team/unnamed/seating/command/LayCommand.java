package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;

public class LayCommand implements CommandExecutor {

    private final SeatingEntityHandler entityHandler;

    public LayCommand(SeatingEntityHandler entityHandler) {
        this.entityHandler = entityHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        Player player = (Player) sender;
        entityHandler.testLay(player);

        return true;
    }

}
