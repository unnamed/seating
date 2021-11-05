package team.unnamed.chairs.adapt.v1_8_R3.chair;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.EntitySpectators;
import team.unnamed.chairs.adapt.v1_8_R3.Packets;

public final class ChairUtils {

    private ChairUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(ChairData chairData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) chairData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static void destroy(ChairData chairData, Player spectator) {
        chairData.getSpectators().removeSpectator(spectator);
        Packets.send(spectator, new PacketPlayOutEntityDestroy(chairData.getEntityId()));
    }

    public static void spawn(ChairData chairData, Player spectator) {
        EntitySpectators spectators = chairData.getSpectators();

        if (spectators.isSpectator(spectator)) {
            return;
        }

        Location location = chairData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), 0
        );

        armorStand.d(chairData.getEntityId());
        armorStand.setGravity(false);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        spectators.addSpectator(spectator);

        Packets.send(
                spectator,
                new PacketPlayOutSpawnEntityLiving(armorStand),
                new PacketPlayOutAttachEntity(
                        0, ((CraftPlayer) chairData.getOwner()).getHandle(), armorStand
                )
        );
    }

}
