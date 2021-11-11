package team.unnamed.chairs.adapt.v1_17_R1.chair;

import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import team.unnamed.chairs.ChairData;

import static team.unnamed.chairs.adapt.entity.ChairHeightConstants.CARPET_HEIGHT;
import static team.unnamed.chairs.adapt.entity.ChairHeightConstants.SLAB_AND_STAIRS_HEIGHT;

;

public final class ChairUtils {

    private ChairUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(ChairData chairData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.c,
                ((CraftWorld) chairData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static double calculateHeight(ChairData chairData) {
        Material material = chairData.getBlockType();
        String materialName = material.name();
        double incrementY;

        if (materialName.contains("CARPET")) {
            incrementY = CARPET_HEIGHT;
        } else {
            incrementY = SLAB_AND_STAIRS_HEIGHT;
        }

        return -incrementY;
    }

    public static void destroy(ChairData chairData, EntityPlayer spectator) {
        spectator.b.sendPacket(new PacketPlayOutEntityDestroy(chairData.getEntityId()));
    }

    public static void spawn(ChairData chairData, EntityPlayer spectator) {
        Location location = chairData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                EntityTypes.c,
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(),
                location.getY() + calculateHeight(chairData),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.e(chairData.getEntityId());
        armorStand.setNoGravity(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        PlayerConnection playerConnection = spectator.b;
        playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        playerConnection.sendPacket(new PacketPlayOutEntityMetadata(
                armorStand.getId(), armorStand.getDataWatcher(), false
        ));

        playerConnection.sendPacket(new PacketPlayOutMount(new PacketMountSerializer(
                armorStand.getId(), spectator.getId()
        )));
    }

}
