package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.message.MessageHandler;

import java.util.Collections;
import java.util.List;

abstract class ToggleSeatsCommand
        implements CommandExecutor, TabCompleter {

    private final String togglePermission;
    private final String permission;
    private final byte property;

    private final MessageHandler messageHandler;
    protected final SeatingHandler seatingHandler;

    public ToggleSeatsCommand(String togglePermission, String permission,
                              byte property, MessageHandler messageHandler,
                              SeatingHandler seatingHandler) {
        this.togglePermission = togglePermission;
        this.permission = permission;
        this.property = property;
        this.messageHandler = messageHandler;
        this.seatingHandler = seatingHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (messageHandler.sendOnlyPlayersMessage(sender)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (messageHandler.hasPermission(player, permission)) {
                execute(player);
            }
        } else {
            if (messageHandler.hasPermission(player, togglePermission)) {
                seatingHandler.toggleSeating(player, property);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String s, String[] strings) {
        if (messageHandler.sendOnlyPlayersMessage(sender)) {
            return Collections.emptyList();
        }

        Player player = (Player) sender;

        if (messageHandler.hasPermission(player, togglePermission)) {
            return Collections.singletonList("toggle");
        }

        return Collections.emptyList();
    }

    protected abstract void execute(Player player);

}
