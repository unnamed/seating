package team.unnamed.seating.adapt.v1_17_R1.seat;

import net.minecraft.network.PacketDataSerializer;

public class PacketMountSerializer extends PacketDataSerializer {

    private final int entityId;
    private final int ownerId;

    public PacketMountSerializer(int entityId, int ownerId) {
        super(null);
        this.entityId = entityId;
        this.ownerId = ownerId;
    }

    @Override
    public int j() {
        return entityId;
    }

    @Override
    public int[] c() {
        return new int[] { ownerId };
    }

}
