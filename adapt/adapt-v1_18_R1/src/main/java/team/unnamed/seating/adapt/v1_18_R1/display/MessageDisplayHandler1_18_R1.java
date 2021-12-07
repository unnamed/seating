package team.unnamed.seating.adapt.v1_18_R1.display;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.display.MessageDisplayHandler;
import team.unnamed.seating.adapt.v1_18_R1.Packets;

import static team.unnamed.seating.adapt.util.BaseComponentUtil.modernDismountComponent;

public class MessageDisplayHandler1_18_R1
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
                null, ChatMessageType.c, player.getUniqueId());
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
                new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut),
                buildTitlePacket((byte) 0, title),
                buildTitlePacket((byte) 1, subtitle)
        );
    }

    private Packet<?> buildTitlePacket(byte action, BaseComponent component) {
        IChatBaseComponent chatBaseComponent = CraftChatMessage.fromJSON(
                ComponentSerializer.toString(component)
        );
        return action == 0 ?
                new ClientboundSetTitleTextPacket(chatBaseComponent) :
                new ClientboundSetSubtitleTextPacket(chatBaseComponent);
    }

}
