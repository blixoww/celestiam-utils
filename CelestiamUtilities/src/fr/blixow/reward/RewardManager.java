package fr.blixow.reward;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class RewardManager {
	private Map<String, Long> rewardCooldown = new HashMap<String, Long>();
	// 86400 = 24h
	public void addPlayer(Player player) {
		this.rewardCooldown.put(player.getName(), Long.valueOf(System.currentTimeMillis() / 1000L + 86400L));
	}

	public boolean isCooldown(Player player) {
		if (this.rewardCooldown.containsKey(player.getName())) {
			if (((Long) this.rewardCooldown.get(player.getName())).longValue() <= System.currentTimeMillis() / 1000L) {
				this.rewardCooldown.remove(player.getName());
				return false;
			}
			return true;
		}
		return false;
	}

	public String getTimeLeft(Player player) {
		long provisoire = ((Long) this.rewardCooldown.get(player.getName())).longValue()
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
