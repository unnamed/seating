package team.unnamed.chairs.adapt.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;

public interface ChairEntityHandler {

    Location calculateBaseLocation(Player owner, Block block);

    void assignArmorStand(ChairData chairData);

    void destroyChair(ChairData chairData);

}
