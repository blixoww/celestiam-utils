package fr.blixow.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class VariousListener implements Listener {

	@EventHandler
	public void onVoidDamge(EntityDamageEvent event) {
		if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
			Entity entity = event.getEntity();
			if (entity == null) {
				return;
			}
			if (!(entity instanceof Player)) {
				return;
			}
			if ((entity instanceof Player)) {
				((Player) entity).teleport(entity.getWorld().getSpawnLocation());
				((Player) entity).setFallDistance(0.0F);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		boolean rain = event.toWeatherState();
		if (rain) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage().replaceAll(" ", "").toLowerCase();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (message.contains(p.getName().toLowerCase())) {
				String replacement = "";
				replacement = replacement + "§e@";
				replacement = replacement + p.getName() + "§7";
				event.setMessage(event.getMessage().replaceAll(p.getName(), (replacement)));
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onBlockFromToEvent(BlockFromToEvent event) {
		Material material = event.getBlock().getType();
		if ((material == Material.LAVA) || (material == Material.STATIONARY_LAVA)) { 
		event.setCancelled(true);
		  }
		}

}
