package team.unnamed.seating.adapt.intercept;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PacketChannelDuplexHandler extends ChannelDuplexHandler {

    private static final Map<Class<?>, PacketInterceptor<?>> INTERCEPTORS =
            new HashMap<>();

    private final WeakReference<Player> playerReference;

    public PacketChannelDuplexHandler(Player player) {
        this.playerReference = new WeakReference<>(player);
    }

    public static <T> void addInterceptor(Class<T> packetType, PacketInterceptor<T> interceptor) {
        INTERCEPTORS.putIfAbsent(packetType, interceptor);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        handleRead(ctx, packet);
    }

    private <T> void handleRead(ChannelHandlerContext ctx, T packet) throws Exception {
        @SuppressWarnings("unchecked")
        PacketInterceptor<T> interceptor =
                (PacketInterceptor<T>) INTERCEPTORS.get(packet.getClass());
        if (interceptor == null) {
            super.channelRead(ctx, packet);
        } else {
            try {
                Player player = playerReference.get();

                if (player == null) {
                    return;
                }

                packet = interceptor.in(player, packet);
            } catch (Throwable error) {
                Bukkit.getLogger().log(Level.SEVERE, "[Packet Interceptor] Error while" +
                        " intercepting an ingoing packet.", error);
                return;
            }
            if (packet != null) {
                super.channelRead(ctx, packet);
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        handleWrite(ctx, packet, promise);
    }

    private <T> void handleWrite(ChannelHandlerContext ctx, T packet, ChannelPromise promise) throws Exception {
        @SuppressWarnings("unchecked")
        PacketInterceptor<T> interceptor =
                (PacketInterceptor<T>) INTERCEPTORS.get(packet.getClass());
        if (interceptor == null) {
            super.write(ctx, packet, promise);
        } else {
            try {
                Player player = playerReference.get();

                if (player == null) {
                    return;
                }

                packet = interceptor.out(player, packet);
            } catch (Throwable error) {
                Bukkit.getLogger().log(Level.SEVERE, "[Packet Interceptor] Error while" +
                        " intercepting an outgoing packet.", error);
                return;
            }
            if (packet != null) {
                super.write(ctx, packet, promise);
            }
        }
    }

}
