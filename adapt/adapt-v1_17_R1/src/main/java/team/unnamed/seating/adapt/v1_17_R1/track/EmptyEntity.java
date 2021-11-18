package team.unnamed.seating.adapt.v1_17_R1.track;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;

public class EmptyEntity extends Entity {

    public EmptyEntity(World world) {
        super(EntityTypes.c, world);
    }

    @Override
    protected void initDatawatcher() {

    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {

    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {

    }

    @Override
    public Packet<?> getPacket() {
        return null;
    }

}
