package team.unnamed.seating.adapt.v1_17_R1.seat;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingData;
import team.unnamed.seating.adapt.entity.SeatingEntityHandler;
import team.unnamed.seating.adapt.v1_17_R1.track.CustomEntityTracker;
import team.unnamed.seating.adapt.v1_17_R1.track.EmptyEntity;
import team.unnamed.seating.message.MessageHandler;

import java.util.UUID;

import static team.unnamed.seating.adapt.v1_17_R1.track.CustomEntityTracker.ENTITY_TRACKER_FIELD;

public class SeatingEntityHandler1_17_R1 implements SeatingEntityHandler {

    private final MessageHandler messageHandler;

    public SeatingEntityHandler1_17_R1(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public Location calculateBaseLocation(Player owner, Block block) {
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();

        if (blockData instanceof Stairs stairs) {
            if (stairs.getHalf() == Bisected.Half.TOP) {
                return null;
            }

            switch (stairs.getFacing().getOppositeFace()) {
                case EAST -> {
                    yaw = -90;
                    incrementX = 0.8;
                }
                case WEST -> {
                    yaw = 90;
                    incrementX = 0.2;
                }
                case NORTH -> {
                    yaw = -180;
                    incrementZ = 0.2;
                }
                case SOUTH -> {
                    yaw = 0;
                    incrementZ = 0.8;
                }
            }
        } else if (blockData instanceof Slab slab) {
            if (slab.getType() == Slab.Type.TOP) {
                return null;
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

        emptyEntity.setPosition(seatLocation.getX(), seatLocation.getY(), seatLocation.getZ());

        PlayerChunkMap chunkMap = worldServer.getChunkProvider().a;
        PlayerChunkMap.EntityTracker entityTracker = new CustomEntityTracker(chunkMap, emptyEntity);

        ENTITY_TRACKER_FIELD.setAccessible(true);
        try {
            ENTITY_TRACKER_FIELD.set(
                    entityTracker, new SeatingEntityTrackerEntry(
                            worldServer, emptyEntity,
                            entityTracker.f, seatingData
                    )
            );
        } catch (IllegalAccessException ignored) {
        } finally {
            ENTITY_TRACKER_FIELD.setAccessible(false);
        }

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
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
                null, ChatMessageType.c, player.getUniqueId());
        packetPlayOutChat.components = new BaseComponent[]{baseComponent};

        entityTracker.updatePlayer(entityPlayer);
        entityPlayer.b.sendPacket(packetPlayOutChat);
        chunkMap.G.put(entityId, entityTracker);
    }

    @Override
    public void destroy(SeatingData seatingData) {
        PlayerChunkMap.EntityTracker entityTracker
                = ((CraftWorld) seatingData.getLocation().getWorld())
                .getHandle()
                .getChunkProvider()
                .a.G.remove(seatingData.getEntityId());

        if (entityTracker != null) {
            entityTracker.a();
        }
    }

    @Override
    public void testLay(Player player) {
        Location location = player.getLocation();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), entityPlayer.displayName);
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.removeAll("textures");
        propertyMap.putAll("textures", entityPlayer.getProfile().getProperties().get("textures"));

        WorldServer worldServer = entityPlayer.getWorldServer();
        EntityPlayer fakePlayer = new EntityPlayer(
                entityPlayer.c, worldServer, gameProfile
        );

        fakePlayer.setLocation(location.getX(), location.getY(), location.getZ(), 180, 0);
        PacketPlayOutNamedEntitySpawn namedEntitySpawnPacket
                = new PacketPlayOutNamedEntitySpawn(fakePlayer);

        PlayerConnection playerConnection = entityPlayer.b;
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, fakePlayer
        ));
        playerConnection.sendPacket(namedEntitySpawnPacket);

        fakePlayer.setPose(EntityPose.c);
        playerConnection.sendPacket(new PacketPlayOutEntityMetadata(
                fakePlayer.getId(), fakePlayer.getDataWatcher(), false));
    }

}
