package team.unnamed.chairs.adapt.intercept;

import org.bukkit.entity.Player;

public interface PacketInterceptor<T> {

    default T in(Player player, T packet) {
        return packet;
    }

    default T out(Player player, T packet) {
        return packet;
    }

}
