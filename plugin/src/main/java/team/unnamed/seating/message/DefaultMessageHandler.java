package team.unnamed.seating.message;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;

import java.util.Collections;
import java.util.List;

public class DefaultMessageHandler
        implements MessageHandler {

    private final FileConfiguration configuration;
    private final MessageDisplayHandler displayHandler;

    public DefaultMessageHandler(FileConfiguration configuration,
                                 MessageDisplayHandler displayHandler) {
        this.configuration = configuration;
        this.displayHandler = displayHandler;
    }

    @Override
    public String getMessage(String path) {
        String message = configuration.getString(makePath(path));

        if (message == null) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public List<String> getMessages(String path) {
        List<String> messages = configuration.getStringList(makePath(path));

        if (messages.isEmpty()) {
            return Collections.singletonList(path);
        }

        messages.replaceAll(message ->
                ChatColor.translateAlternateColorCodes('&', message));

        return messages;
    }

    @Override
    public void sendMessage(CommandSender player, String path) {
        player.sendMessage(getMessage(path));
    }

    @Override
    public void sendMessages(CommandSender player, String path) {
        player.sendMessage(String.join("\n", getMessage(path)));
    }

    @Override
    public void sendDismountMessage(Player player) {
        String dismountMessage = getMessage("dismount.message");
        BaseComponent dismountComponent
                = displayHandler.buildDismountComponent(dismountMessage);

        String[] displayType = getMessage("dismount.type")
                .split(";");

        boolean sendEmptyActionBar = false;
        switch (displayType[0]) {
            case "actionbar": {
                displayHandler.displayActionBar(player, dismountComponent);
                break;
            }
            case "title": {
                displayHandler.displayTitle(
                        player, dismountComponent,
                        Integer.parseInt(displayType[1]),
                        Integer.parseInt(displayType[2]),
                        Integer.parseInt(displayType[3])
                );
                sendEmptyActionBar = true;
                break;
            }
            case "subtitle": {
                displayHandler.displaySubtitle(
                        player, dismountComponent,
                        Integer.parseInt(displayType[1]),
                        Integer.parseInt(displayType[2]),
                        Integer.parseInt(displayType[3])
                );
                sendEmptyActionBar = true;
                break;
            }
            default: { //includes "message"
                player.spigot().sendMessage(dismountComponent);
                sendEmptyActionBar = true;
                break;
            }
        }

        if (sendEmptyActionBar) {
            displayHandler.displayActionBar(player, new TextComponent());
        }
    }

    @Override
    public boolean sendOnlyPlayersMessage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, "only-players");
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            sendMessage(player, "no-permission");
            return false;
        }
        return true;
    }

    @Override
    public String makePath(String path) {
        return String.format(BASE_MESSAGES_PATH, path);
    }

}
