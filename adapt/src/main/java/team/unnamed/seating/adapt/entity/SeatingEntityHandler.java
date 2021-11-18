package team.unnamed.seating.adapt.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import team.unnamed.seating.SeatingData;

public interface SeatingEntityHandler {

    Location calculateBaseLocation(Player owner, Block block);

    void assignArmorStand(SeatingData seatingData);

    void destroy(SeatingData seatingData);

}
