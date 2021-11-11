package team.unnamed.chairs.adapt.v1_8_R3.chair;

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
import team.unnamed.chairs.ChairData;

import static team.unnamed.chairs.adapt.entity.ChairHeightConstants.*;

public final class ChairUtils {

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

    private ChairUtils() {
        throw new UnsupportedOperationException();
    }

    public static int generateId(ChairData chairData) {
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) chairData.getLocation().getWorld()).getHandle()
        );

        return armorStand.getId();
    }

    public static double calculateHeight(ChairData chairData, EntityPlayer spectator) {
        Material material = chairData.getBlockType();
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
                incrementY = CARPET_HEIGHT;
            } else {
                incrementY = SLAB_AND_STAIRS_HEIGHT;
            }
        }

        return -incrementY;
    }

    public static void destroy(ChairData chairData, EntityPlayer spectator) {
        spectator.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(chairData.getEntityId()));
    }

    public static void spawn(ChairData chairData, EntityPlayer spectator) {
        Location location = chairData.getLocation();
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) location.getWorld()).getHandle()
        );

        armorStand.setLocation(
                location.getX(),
                location.getY() + calculateHeight(chairData, spectator),
                location.getZ(),
                location.getYaw(), 0
        );

        armorStand.d(chairData.getEntityId());
        armorStand.setGravity(false);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        PlayerConnection playerConnection = spectator.playerConnection;
        playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        playerConnection.sendPacket(new PacketPlayOutAttachEntity(
                0, ((CraftPlayer) chairData.getOwner()).getHandle(), armorStand
        ));
    }

}
