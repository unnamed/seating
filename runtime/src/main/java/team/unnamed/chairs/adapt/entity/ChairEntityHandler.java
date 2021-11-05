package team.unnamed.chairs.adapt.entity;

import org.bukkit.entity.Player;

import team.unnamed.chairs.ChairData;

public interface ChairEntityHandler {

    void showChairMount(ChairData chairData, Player player);

    void updateChairYaw(ChairData chairData);

    void assignArmorStand(ChairData chairData);

    void destroyChair(ChairData chairData);

}
