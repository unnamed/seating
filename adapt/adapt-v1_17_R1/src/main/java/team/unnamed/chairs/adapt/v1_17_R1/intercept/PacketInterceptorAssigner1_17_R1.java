package team.unnamed.chairs.adapt.v1_17_R1.intercept;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import team.unnamed.chairs.adapt.intercept.PacketChannelDuplexHandler;
import team.unnamed.chairs.adapt.intercept.PacketInterceptorAssigner;

public class PacketInterceptorAssigner1_17_R1
        implements PacketInterceptorAssigner {

    @Override
    public void assignToPlayer(Player player, String channelName) {
        ((CraftPlayer) player).getHandle()
                .b.a.k.pipeline()
                .addBefore("packet_handler", channelName, new PacketChannelDuplexHandler(player));
    }

}
