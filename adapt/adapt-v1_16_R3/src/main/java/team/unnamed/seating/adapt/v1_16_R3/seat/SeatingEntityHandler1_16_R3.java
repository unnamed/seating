package team.unnamed.seating.adapt.v1_16_R3.seat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PlayerChunkMap;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.seat.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_16_R3.track.CustomEntityTracker;
import team.unnamed.seating.adapt.v1_16_R3.track.EmptyEntity;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.adapt.v1_16_R3.track.CustomEntityTracker.ENTITY_TRACKER_FIELD;

public class SeatingEntityHandler1_16_R3 implements SeatingEntityHandler {

    private final MessageHandler messageHandler;

    public SeatingEntityHandler1_16_R3(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void calculateBaseLocation(Player owner, Block block, ChairSeatingData.Builder builder) {
        Material material = block.getType();
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();
        ChairSeatingData.ChairType chairType;

        if (blockData instanceof Stairs) {
            Stairs stairs = (Stairs) blockData;
            if (stairs.getHalf() == Bisected.Half.TOP) {
                return;
            }

            chairType = ChairSeatingData.ChairType.STAIR;
            switch (stairs.getFacing().getOppositeFace()) {
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
        } else if (blockData instanceof Slab) {
            Slab slab = (Slab) blockData;
            if (slab.getType() == Slab.Type.TOP) {
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
        int entityId = SeatUtils.generateId(seatingData);
        seatingData.setSpigotId(entityId);

        Location seatLocation = seatingData.getLocation();
        WorldServer worldServer = ((CraftWorld) seatLocation.getWorld()).getHandle();
        EmptyEntity emptyEntity = new EmptyEntity(worldServer);
        emptyEntity.e(entityId);

        // paper and paper forks :(
        Chunk chunk = seatLocation.getChunk();
        emptyEntity.chunkX = chunk.getX();
        emptyEntity.chunkZ = chunk.getZ();

        emptyEntity.setPosition(seatLocation.getX(), seatLocation.getY(), seatLocation.getZ());

        PlayerChunkMap chunkMap = worldServer.getChunkProvider().playerChunkMap;
        PlayerChunkMap.EntityTracker entityTracker = new CustomEntityTracker(chunkMap, emptyEntity);

        ENTITY_TRACKER_FIELD.setAccessible(true);
        try {
            ENTITY_TRACKER_FIELD.set(
                    entityTracker, new ChairEntityTrackerEntry(
                            worldServer, emptyEntity,
                            entityTracker.trackedPlayers, seatingData
                    )
            );
        } catch (IllegalAccessException ignored) {
        } finally {
            ENTITY_TRACKER_FIELD.setAccessible(false);
        }

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityTracker.updatePlayer(entityPlayer);
        sendDismountActionbar(entityPlayer);
        chunkMap.trackedEntities.put(entityId, entityTracker);
    }

    @Override
    public void destroySit(ChairSeatingData seatingData) {
        PlayerChunkMap.EntityTracker entityTracker
                = ((CraftWorld) seatingData.getLocation().getWorld())
                .getHandle()
                .getChunkProvider()
                .playerChunkMap.trackedEntities
                .remove(seatingData.getSpigotId());

        if (entityTracker != null) {
            entityTracker.a();
        }
    }

    @Override
    public void lay(Player player) {

    }

    @Override
    public void sendDismountActionbar(Player player) {
        sendDismountActionbar(((CraftPlayer) player).getHandle());
    }

    private void sendDismountActionbar(EntityPlayer entityPlayer) {
        String dismountMessage = messageHandler.getMessage("dismount");
        String[] componentKeys = dismountMessage.split("%keybind%");
        BaseComponent baseComponent = new TextComponent();
        BaseComponent lastComponent = null;

        for (int i = 0; i < componentKeys.length; i++) {
            if (i != 0) {
                BaseComponent component = new KeybindComponent("key.sneak");
                component.copyFormatting(lastComponent);
                baseComponent.addExtra(component);
            }

            BaseComponent[] components = TextComponent.fromLegacyText(componentKeys[i]);
            lastComponent = components[components.length - 1];
            for (BaseComponent component : components) {
                baseComponent.addExtra(component);
            }
        }

        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(
                null, ChatMessageType.GAME_INFO, entityPlayer.getUniqueID());
        packetPlayOutChat.components = new BaseComponent[]{baseComponent};
        entityPlayer.playerConnection.sendPacket(packetPlayOutChat);
    }

}
