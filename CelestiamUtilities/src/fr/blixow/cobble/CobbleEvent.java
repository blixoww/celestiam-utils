package fr.blixow.cobble;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import fr.blixow.utils.Utilities;

public class CobbleEvent implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (!Utilities.playerList.contains(e.getPlayer().getName())) {
			return;
		}
		if ((e.getBlock().getType().equals(Material.COBBLESTONE)) || (e.getBlock().getType().equals(Material.STONE))) {
			Utilities.Location.add(e.getBlock().getLocation());
		}
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e) {
		if (Utilities.Location.contains(e.getLocation().getBlock().getLocation())) {
			e.setCancelled(true);
			Utilities.Location.remove(e.getLocation().getBlock().getLocation());
		}
	}
}
