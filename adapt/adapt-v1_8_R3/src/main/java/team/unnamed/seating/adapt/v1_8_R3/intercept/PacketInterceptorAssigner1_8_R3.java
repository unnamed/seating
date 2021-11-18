package team.unnamed.seating.adapt.v1_8_R3.intercept;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import team.unnamed.seating.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.seating.adapt.intercept.PacketInterceptorAssigner;

public class PacketInterceptorAssigner1_8_R3
        implements PacketInterceptorAssigner {

    @Override
    public void assignToPlayer(Player player, String channelName) {
        ((CraftPlayer) player).getHandle()
                .playerConnection.networkManager
                .channel.pipeline()
                .addBefore("packet_handler", channelName, new PacketChannelDuplexHandler(player));
    }

}
