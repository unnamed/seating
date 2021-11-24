package team.unnamed.seating.user;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

/**
 * Class responsible for managing users who
 * have disabled or enabled the use of
 * seats.
 * Designed for storing only users who
 * have disabled the use of seats.
 */
public interface UserToggleSeatingManager {

    /**
     * @param player player to check
     * @return if player has seats enabled.
     */
    boolean hasSeatingEnabled(Player player);

    /**
     * @param player player to add or remove from storage
     * @return if player has toggled seats.
     */
    boolean toggleSeating(Player player);

    void loadData(Plugin plugin) throws IOException;

    void saveData(Plugin plugin) throws IOException;

}
