package team.unnamed.seating.adapt.v1_18_R1.seat;

public record SeatingEntityHandler1_18_R1(
        MessageHandler messageHandler) implements SeatingEntityHandler {

    @Override
    public void calculateBaseLocation(Player owner, Block block, ChairSeatingData.Builder builder) {
        Material material = block.getType();
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();
        ChairSeatingData.ChairType chairType;

        if (blockData instanceof Stairs stairs) {
            if (stairs.getHalf() == Bisected.Half.TOP) {
                return;
            }

            chairType = ChairSeatingData.ChairType.STAIR;
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
        PlayerChunkMap.EntityTracker entityTracker = addEntry(
                seatLocation, entityId,
                (worldServer, entity, players) -> new ChairEntityTrackerEntry(
                        worldServer, entity, players, seatingData
                )
        );

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityTracker.b(entityPlayer);
        sendDismountActionbar(entityPlayer);
    }

    @Override
    public void destroySit(ChairSeatingData seatingData) {
        removeEntry(seatingData.getLocation(), seatingData.getSpigotId());
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
                null, ChatMessageType.c, entityPlayer.fp().getId());
        packetPlayOutChat.components = new BaseComponent[]{baseComponent};
        entityPlayer.b.a(packetPlayOutChat);
    }

}
