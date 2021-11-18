package team.unnamed.chairs.adapt.v1_16_R3.track;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.World;

public class EmptyEntity extends Entity {

    public EmptyEntity(World world) {
        super(EntityTypes.ARMOR_STAND, world);
    }

    @Override
    protected void initDatawatcher() {

    }

    @Override
    protected void loadData(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void saveData(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public Packet<?> P() {
        return null;
    }

}
