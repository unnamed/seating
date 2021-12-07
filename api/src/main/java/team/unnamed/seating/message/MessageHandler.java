package team.unnamed.seating.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface MessageHandler {

    String BASE_MESSAGES_PATH = "messages.%s";

    String getMessage(String path);

    List<String> getMessages(String path);

    void sendMessage(CommandSender player, String path);

    void sendMessages(CommandSender player, String path);

    void sendDismountMessage(Player player);

    boolean sendOnlyPlayersMessage(CommandSender player);

    boolean hasPermission(Player player, String permission);

    String makePath(String path);

}
