package fr.blixow.plugin;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class EnderpearlCooldown implements Listener {

	public HashMap<Player, Long> ender = new HashMap<>();

	@EventHandler
	public void onEnderPearlThrow(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			if (((e.getEntity() instanceof EnderPearl)) && (!p.hasPermission("celestiam.bypass"))) {
				int cool_time = 5;
				if (this.ender.containsKey(p)) {
					long remaining = ((Long) this.ender.get(p)).longValue() / 1000L + cool_time
							- System.currentTimeMillis() / 1000L;
					if (remaining > 0L) {
						e.setCancelled(true);
						p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL, 1) });
						p.sendMessage("§7vous devez encore attendre §c" + String.valueOf(remaining) + " §7secondes");
					} else {
						this.ender.put(p, Long.valueOf(System.currentTimeMillis()));
					}
				} else {
					this.ender.put(p, Long.valueOf(System.currentTimeMillis()));
				}
			}
		}
	}
	
}
