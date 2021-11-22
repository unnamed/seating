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
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import team.unnamed.seating.SeatingData;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_16_R3.track.CustomEntityTracker;
import team.unnamed.seating.adapt.v1_16_R3.track.EmptyEntity;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.adapt.v1_16_R3.track.CustomEntityTracker.ENTITY_TRACKER_FIELD;

public class SeatingEntityHandler1_16_R3 implements SeatingEntityHandler {

    private final MessageHandler messageHandler;

    public SeatingEntityHandler1_16_R3(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public Location calculateBaseLocation(Player owner, Block block) {
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();

        if (blockData instanceof Stairs) {
            Stairs stairs = (Stairs) blockData;
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
        }

        Location ownerLocation = owner.getLocation();
        ownerLocation.setYaw(yaw);
        owner.teleport(ownerLocation);
        location.add(incrementX, 0, incrementZ);
        location.setYaw(yaw);
        return location;
    }

    @Override
    public void create(Player player, SeatingData seatingData) {
        int entityId = SeatUtils.generateId(seatingData);
        seatingData.setEntityId(entityId);

        Location seatLocation = seatingData.getLocation();
        WorldServer worldServer = ((CraftWorld) seatLocation.getWorld()).getHandle();
        EmptyEntity emptyEntity = new EmptyEntity(worldServer);
        emptyEntity.e(entityId);

        //paper and paper forks :(
        Chunk chunk = seatLocation.getChunk();
        emptyEntity.chunkX = chunk.getX();
        emptyEntity.chunkZ = chunk.getZ();

        emptyEntity.setPosition(seatLocation.getX(), seatLocation.getY(), seatLocation.getZ());

        PlayerChunkMap chunkMap = worldServer.getChunkProvider().playerChunkMap;
        PlayerChunkMap.EntityTracker entityTracker = new CustomEntityTracker(chunkMap, emptyEntity);

        ENTITY_TRACKER_FIELD.setAccessible(true);
        try {
            ENTITY_TRACKER_FIELD.set(
                    entityTracker, new SeatingEntityTrackerEntry(
                            worldServer, emptyEntity,
                            entityTracker.trackedPlayers, seatingData
                    )
            );
        } catch (IllegalAccessException ignored) {
        } finally {
            ENTITY_TRACKER_FIELD.setAccessible(false);
        }

        chunkMap.trackedEntities.put(entityId, entityTracker);
    }

    @Override
    public void destroy(SeatingData seatingData) {
        PlayerChunkMap.EntityTracker entityTracker
                = ((CraftWorld) seatingData.getLocation().getWorld())
                .getHandle()
                .getChunkProvider()
                .playerChunkMap.trackedEntities
                .remove(seatingData.getEntityId());

        if (entityTracker != null) {
            entityTracker.a();
        }
    }

    @Override
    public void testLay(Player player) {

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

    @Override
    public void crawl(Player player) {

    }

}
