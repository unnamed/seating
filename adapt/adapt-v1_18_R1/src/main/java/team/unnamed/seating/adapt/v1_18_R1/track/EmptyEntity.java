package team.unnamed.seating.adapt.v1_18_R1.track;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;

public class EmptyEntity extends Entity {

    public EmptyEntity(WorldServer world) {
        super(EntityTypes.c, world);
    }

    // override this method for cheat bukkit
    // otherwise we should register the entity
    @Override
    public CraftEntity getBukkitEntity() {
        return new CraftEmptyEntity(
                (CraftServer) Bukkit.getServer(), this
        );
    }

    @Override
    protected void a_() {

    }

    @Override
    protected void a(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void b(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public Packet<?> S() {
        return null;
    }

}
