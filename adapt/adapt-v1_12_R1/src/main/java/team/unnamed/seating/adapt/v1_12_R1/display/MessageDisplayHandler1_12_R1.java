package team.unnamed.seating.adapt.v1_12_R1.display;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;
import team.unnamed.seating.adapt.v1_12_R1.Packets;

import static team.unnamed.seating.adapt.util.BaseComponentUtil.modernDismountComponent;

public class MessageDisplayHandler1_12_R1
        implements MessageDisplayHandler {

    @Override
    public void displayTitle(Player player, BaseComponent component,
                             int fadeIn, int stay, int fadeOut) {
        displayTitle(
                player, component, EMPTY_COMPONENT,
                fadeIn, stay, fadeOut);
    }

    @Override
    public void displaySubtitle(Player player, BaseComponent component,
                                int fadeIn, int stay, int fadeOut) {
        displayTitle(
                player, EMPTY_COMPONENT, component,
                fadeIn, stay, fadeOut);
    }

    @Override
    public void displayActionBar(Player player, BaseComponent component) {
        Packets.send(
                player,
                buildTitlePacket(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, component)
        );
    }

    @Override
    public BaseComponent buildDismountComponent(String dismountMessage) {
        return modernDismountComponent(dismountMessage);
    }

    private void displayTitle(Player player,
                              BaseComponent title, BaseComponent subtitle,
                              int fadeIn, int stay, int fadeOut) {
        Packets.send(player,
                new PacketPlayOutTitle(fadeIn, stay, fadeOut), // first send timers
                buildTitlePacket(PacketPlayOutTitle.EnumTitleAction.TITLE, title),
                buildTitlePacket(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitle)
        );
    }

    private Packet<?> buildTitlePacket(PacketPlayOutTitle.EnumTitleAction action,
                                       BaseComponent component) {
        return new PacketPlayOutTitle(
                action, toNmsComponent(component)
        );
    }

    private IChatBaseComponent toNmsComponent(BaseComponent component) {
        return IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(component));
    }

}
