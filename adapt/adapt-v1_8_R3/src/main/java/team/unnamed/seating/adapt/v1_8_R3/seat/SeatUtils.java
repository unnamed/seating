package team.unnamed.seating.adapt.v1_8_R3.seat;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingData;

import static team.unnamed.seating.adapt.entity.SeatingHeightConstants.*;

public final class SeatUtils {

    private static final boolean VIA_VERSION;

    static {
        boolean viaVersion;

        try {
            Class.forName("com.viaversion.viaversion.api.ViaAPI");
            viaVersion = true;
        } catch (ClassNotFoundException ignored) {
            viaVersion = false;
        }

        VIA_VERSION = viaVersion;
    }

    private SeatUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(SeatingData seatingData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) seatingData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static double calculateHeight(SeatingData seatingData, EntityPlayer spectator) {
        Material material = seatingData.getBlockType();
        double incrementY;

        if (VIA_VERSION) {
            @SuppressWarnings("unchecked") ViaAPI<Player> viaAPI =
                    (ViaAPI<Player>) Via.getAPI();
            if (viaAPI.getPlayerVersion(spectator.getUniqueID()) < 393) { //<1.13
                if (material == Material.CARPET) {
                    incrementY = LEGACY_CARPET_HEIGHT;
                } else {
                    incrementY = LEGACY_SLAB_AND_STAIRS_HEIGHT;
                }
            } else {
                if (material == Material.CARPET) {
                    incrementY = CARPET_HEIGHT;
                } else {
                    incrementY = SLAB_AND_STAIRS_HEIGHT;
                }
            }
        } else {
            if (material == Material.CARPET) {
                incrementY = LEGACY_CARPET_HEIGHT;
            } else {
                incrementY = LEGACY_SLAB_AND_STAIRS_HEIGHT;
            }
        }

        return -incrementY;
    }

    public static void destroy(SeatingData seatingData, EntityPlayer spectator) {
        spectator.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(seatingData.getEntityId()));
    }

    public static void spawn(SeatingData seatingData, EntityPlayer spectator) {
        Location location = seatingData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(),
                location.getY() + calculateHeight(seatingData, spectator),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.d(seatingData.getEntityId());
        armorStand.setGravity(false);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        PlayerConnection playerConnection = spectator.playerConnection;
        playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        playerConnection.sendPacket(new PacketPlayOutAttachEntity(
                0, ((CraftPlayer) seatingData.getOwner()).getHandle(), armorStand
        ));
    }

}
