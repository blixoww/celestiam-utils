package fr.blixow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import net.milkbowl.vault.economy.Economy;

public class Utilities {

	public static List<String> playerList = new ArrayList<String>();
	public static List<Location> Location = new ArrayList<Location>();
	public static HashMap<UUID, UUID> msg = new HashMap<>();
	public static Economy economy = null;

	public static Economy getEconomy() {
		return economy;
	}

	public static void noPermissions(CommandSender sender) {
		sender.sendMessage("§cVous n'avez pas accès à cette commande.");
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
