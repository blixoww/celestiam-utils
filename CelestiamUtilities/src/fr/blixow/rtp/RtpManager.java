package fr.blixow.rtp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RtpManager {
	private Map<String, Long> rtpCooldown = new HashMap<String, Long>();
	private List<String> isTeleporting = new ArrayList<String>();

	public void addPlayer(Player player) {
		// 10800 = 3h
		this.rtpCooldown.put(player.getName(), Long.valueOf(System.currentTimeMillis() / 1000L + 10800L));
	}

	public boolean isCooldown(Player player) {
		if (this.rtpCooldown.containsKey(player.getName())) {
			if (((Long) this.rtpCooldown.get(player.getName())).longValue() <= System.currentTimeMillis() / 1000L) {
				this.rtpCooldown.remove(player.getName());
				return false;
			}
			return true;
		}
		return false;
	}

	public String getTimeLeft(Player player) {
		long provisoire = ((Long) this.rtpCooldown.get(player.getName())).longValue()
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

	public Location getRandomLocation() {
		int max = 3500;
		int min = 1500;
		Random random = new Random();
		boolean isNegativeX = random.nextBoolean();
		boolean isNegativeZ = random.nextBoolean();
		int x = random.nextInt(max) + min;
		int z = random.nextInt(max) + min;
		if (isNegativeX) {
			x = -x;
		}
		if (isNegativeZ) {
			z = -z;
		}
		return new Location(Bukkit.getWorld("world"), x, Bukkit.getWorld("world").getHighestBlockYAt(x, z), z);
	}

	public List<String> getIsTeleporting() {
		return this.isTeleporting;
	}
}
