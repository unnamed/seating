package team.unnamed.chairs.adapt.v1_8_R3.chair;

import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;
import team.unnamed.chairs.adapt.v1_8_R3.track.AbstractEntityTrackerEntry;

public class ChairEntityTrackerEntry
        extends AbstractEntityTrackerEntry {

    private final ChairData chairData;

    public ChairEntityTrackerEntry(ChairData chairData) {
        this.chairData = chairData;
    }

    @Override
    protected Location getLocation() {
        return chairData.getLocation();
    }

    @Override
    protected void show(EntityPlayer player) {
        Player spectator = player.getBukkitEntity();
        ChairUtils.spawn(chairData, spectator);
    }

    @Override
    protected void hide(EntityPlayer player) {
        ChairUtils.destroy(chairData, player.getBukkitEntity());
    }

}
