package team.unnamed.seating.adapt.v1_16_R3.display;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;
import team.unnamed.seating.adapt.v1_16_R3.Packets;

import static team.unnamed.seating.adapt.util.BaseComponentUtil.modernDismountComponent;

public class MessageDisplayHandler1_16_R3
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
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(
                null, ChatMessageType.GAME_INFO, player.getUniqueId());
        packetPlayOutChat.components = new BaseComponent[]{component};
        Packets.send(player, packetPlayOutChat);
    }

    @Override
    public BaseComponent buildDismountComponent(String dismountMessage) {
        return modernDismountComponent(dismountMessage);
    }

    private void displayTitle(Player player,
                              BaseComponent title, BaseComponent subtitle,
                              int fadeIn, int stay, int fadeOut) {
        Packets.send(player,
                new PacketPlayOutTitle(fadeIn, stay, fadeOut),
                buildTitlePacket(PacketPlayOutTitle.EnumTitleAction.TITLE, title),
                buildTitlePacket(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitle)
        );
    }

    private Packet<?> buildTitlePacket(PacketPlayOutTitle.EnumTitleAction action,
                                       BaseComponent component) {
        PacketPlayOutTitle packet = new PacketPlayOutTitle(
                action,
                CraftChatMessage.fromJSON(ComponentSerializer.toString(component))
        );
        return packet;
    }

}
