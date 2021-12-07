package team.unnamed.seating.adapt.display;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public interface MessageDisplayHandler {

    TextComponent EMPTY_COMPONENT = new TextComponent();

    void displayTitle(Player player, BaseComponent component,
                      int fadeIn, int stay, int fadeOut);

    void displaySubtitle(Player player, BaseComponent component,
                         int fadeIn, int stay, int fadeOut);

    void displayActionBar(Player player, BaseComponent component);

    BaseComponent buildDismountComponent(String dismountMessage);

}
