package team.unnamed.seating.user;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public interface UserManager {

    boolean hasSeatingEnabled(Player player);

    boolean toggleSeating(Player player);

    void loadData(Plugin plugin) throws IOException;

    void saveData(Plugin plugin) throws IOException;

}
