package fr.blixow.repair;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class RepairManager {

	private Map<String, Long> repairCooldown = new HashMap<String, Long>();

	public void addPlayer(Player player) {
		this.repairCooldown.put(player.getName(), Long.valueOf(System.currentTimeMillis() / 1000L + 86400L));
	}

	public boolean isCooldown(Player player) {
		if (this.repairCooldown.containsKey(player.getName())) {
			if (((Long) this.repairCooldown.get(player.getName())).longValue() <= System.currentTimeMillis() / 1000L) {
				this.repairCooldown.remove(player.getName());
				return false;
			}
			return true;
		}
		return false;
	}

	public String getTimeLeft(Player player) {
		long provisoire = ((Long) this.repairCooldown.get(player.getName())).longValue()
				- System.currentTimeMillis() / 1000L;
		long second = provisoire % 60L;
		provisoire /= 60L;
		long mins = provisoire % 60L;
		provisoire /= 60L;
		long hours = provisoire % 60L;
		String hoursF = String.format("%02d", new Object[] { Long.valueOf(hours) });
		String minsF = String.format("%02d", new Object[] { Long.valueOf(mins) });
		String secondF = String.format("%02d", new Object[] { Long.valueOf(second) });
		if (hours == 0L) {
			if (mins == 0L) {
				return secondF;
			}
			return minsF + "m:" + secondF + "s";
		}
		return hoursF + "h:" + minsF + "m:" + secondF + "s";
	}

}
