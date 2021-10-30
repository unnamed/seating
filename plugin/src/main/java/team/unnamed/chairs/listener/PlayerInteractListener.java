package team.unnamed.chairs.listener;

import net.minecraft.server.v1_8_R3.EntityArmorStand;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Material type = block.getType();

        if (!type.name().contains("STAIRS")) {
            return;
        }

        Player player = event.getPlayer();
        EntityArmorStand armorStand = new EntityArmorStand(
                ((CraftWorld) player.getWorld()).getHandle()
        );

        Location location = block.getLocation();
        armorStand.setLocation(
                location.getX() + 0.5,
                location.getY() - 0.75,
                location.getZ() + 0.5, location.getYaw(), location.getPitch()
        );
        armorStand.setInvisible(true);
        armorStand.setSmall(true);

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PlayerConnection playerConnection = entityPlayer.playerConnection;

        playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, entityPlayer, armorStand));
    }

}
