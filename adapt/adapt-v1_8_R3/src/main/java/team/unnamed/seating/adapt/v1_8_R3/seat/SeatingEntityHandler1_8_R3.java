package team.unnamed.seating.adapt.v1_8_R3.seat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_8_R3.track.EntityTrackerRegistry;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.message.MessageHandler;

public class SeatingEntityHandler1_8_R3 implements SeatingEntityHandler {

    private final EntityTrackerRegistry trackerRegistry;
    private final MessageHandler messageHandler;

    public SeatingEntityHandler1_8_R3(MessageHandler messageHandler) {
        this.trackerRegistry = new EntityTrackerRegistry();
        this.messageHandler = messageHandler;
    }

    @Override
    public void calculateBaseLocation(Player owner, Block block, ChairSeatingData.Builder builder) {
        Location location = block.getLocation();
        Material material = block.getType();

        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        MaterialData materialData = block.getState().getData();
        ChairSeatingData.ChairType chairType;

        if (materialData instanceof Stairs) {
            Stairs stairs = (Stairs) materialData;
            if (stairs.isInverted()) {
                return;
            }

            chairType = ChairSeatingData.ChairType.STAIR;
            switch (stairs.getFacing()) {
                case EAST: {
                    yaw = -90;
                    incrementX = 0.8;
                    break;
                }
                case WEST: {
                    yaw = 90;
                    incrementX = 0.2;
                    break;
                }
                case NORTH: {
                    yaw = -180;
                    incrementZ = 0.2;
                    break;
                }
                case SOUTH: {
                    yaw = 0;
                    incrementZ = 0.8;
                    break;
                }
            }
        } else if (materialData instanceof Step) {
            Step step = (Step) materialData;
            if (step.isInverted()) {
                return;
            }
            chairType = ChairSeatingData.ChairType.SLAB;
        } else if (material.name().contains("CARPET")) {
            chairType = ChairSeatingData.ChairType.CARPET;
        } else {
            chairType = ChairSeatingData.ChairType.BLOCK;
        }

        Location ownerLocation = owner.getLocation();
        ownerLocation.setYaw(yaw);
        owner.teleport(ownerLocation);
        location.add(incrementX, 0, incrementZ);
        location.setYaw(yaw);
        builder.setLocation(location)
                .setBlockType(material)
                .setChairType(chairType);
    }

    @Override
    public void sit(Player player, ChairSeatingData seatingData) {
        EntityTrackerEntry trackerEntry = new ChairEntityTrackerEntry(seatingData);
        seatingData.setSpigotId(SeatUtils.generateId(seatingData));
        trackerRegistry.bindEntry(seatingData, trackerEntry);
        sendDismountActionbar(player);
    }

    @Override
    public void destroySit(ChairSeatingData seatingData) {
        trackerRegistry.unbindEntry(seatingData);
    }

    @Override
    public void sendDismountActionbar(Player player) {
        BaseComponent baseComponent = new TextComponent(messageHandler.getMessage("dismount"));

        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(null, (byte) 2);
        packetPlayOutChat.components = new BaseComponent[]{baseComponent};
        ((CraftPlayer) player).getHandle()
                .playerConnection
                .sendPacket(packetPlayOutChat);
    }

    @Override
    public void lay(Player player) {

    }

}
