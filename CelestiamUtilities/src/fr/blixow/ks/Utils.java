package fr.blixow.ks;

import org.bukkit.entity.Player;
import fr.blixow.plugin.Core;

public class Utils {
	static Utils utils;

	public static Integer getKills(Player player) {
		return Integer.valueOf(Core.instance.getConfig().getInt(player.getUniqueId().toString() + ".kills"));
	}

	public static Integer getDeaths(Player player) {
		return Integer.valueOf(Core.instance.getConfig().getInt(player.getUniqueId().toString() + ".deaths"));
	}

	public static double getKs(Player player) {
		double kills = getKills(player);
		double deaths = getDeaths(player);
		double ratio = kills / deaths;

		if (deaths == 0) {
			return kills;
		}
		if (deaths != 0) {
			return ratio;
		}
		return ratio;
	}

	public static Utils getUtils() {
		return utils;
	}
}
