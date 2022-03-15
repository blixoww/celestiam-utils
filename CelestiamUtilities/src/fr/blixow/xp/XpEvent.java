package fr.blixow.xp;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class XpEvent implements Listener {
	@EventHandler
	public void onClickEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			ItemStack is = e.getItem();
			if (is == null) {
				return;
			}
			if ((e.getItem().getType() == Material.EXP_BOTTLE) && (e.getItem().getItemMeta().getDisplayName() != null)
					&& (e.getItem().getItemMeta().getDisplayName().startsWith("§a§lLevel §8: §6"))) {
				e.setCancelled(true);
				String str = e.getItem().getItemMeta().getDisplayName().replace("§a§lLevel §8: §6", "");
				int value = Integer.parseInt(str);
				int newValue = (int) (Integer.parseInt(str) * 0.75);
				if (p.getLevel() < 10) {
					p.setLevel(p.getLevel() + value);
					p.getInventory().remove(p.getItemInHand());
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 30.0F, 1.0F);
				} else {
					p.setLevel(p.getLevel() + newValue);
					p.getInventory().remove(p.getItemInHand());
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 30.0F, 1.0F);
				}
			}
		}
	}
}
