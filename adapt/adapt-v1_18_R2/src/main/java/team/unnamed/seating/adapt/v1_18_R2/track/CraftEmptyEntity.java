package team.unnamed.seating.adapt.v1_18_R2.track;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CraftEmptyEntity extends CraftEntity {
    public CraftEmptyEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }
}
