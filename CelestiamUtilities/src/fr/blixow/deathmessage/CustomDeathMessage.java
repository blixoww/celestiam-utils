package fr.blixow.deathmessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CustomDeathMessage implements Listener {
	private List<String> deathMessages;

	public CustomDeathMessage() {
		(this.deathMessages = new ArrayList<String>()).add("§c%player% §7 s'est fait péter le cul.");
		this.deathMessages.add("§c%player% §7s'est fait détruire par le respect.");
		this.deathMessages.add("§c%player% §7s'est fait entasser dans une boite de conserve.");
		this.deathMessages.add("§c%player% §7s'est fait détruire par blixow le grande bg");
		this.deathMessages.add("§c%player% §7s'est pris pour Superman.");
		this.deathMessages.add("§c%player% §7a disparu de la surface de ce monde.");
		this.deathMessages.add("§c%player% §7s'est fait éclater le fiak.");
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player death = event.getEntity();
		Player killer = death.getKiller();
		if (killer == null) {
			final Random random = new Random();
			final String msg = this.deathMessages.get(random.nextInt(this.deathMessages.size()));
			event.setDeathMessage(msg.replace("%player%", death.getName()));
			return;
		}
		ItemStack killItem = killer.getItemInHand();
		if (killItem != null && killItem.hasItemMeta() && killItem.getItemMeta().hasDisplayName()
				&& (killItem.getType().name().toLowerCase().contains("sword")
						|| killItem.getType().name().toLowerCase().contains("axe") || this.validModSword(killItem))) {
			String displayName = killItem.getItemMeta().getDisplayName();
			TextComponent component = new TextComponent(
					"§c" + death.getName() + "§7 s'est fait tuer par §3" + killer.getName() + " §7avec ");
			TextComponent item = new TextComponent(displayName);
			item.setColor(ChatColor.AQUA);
			item.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					TextComponent.fromLegacyText(this.enchantToString(killItem))));
			for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				onlinePlayer.spigot().sendMessage(new BaseComponent[] { component, item });
			}
			event.setDeathMessage((String) null);
			return;
		}
		event.setDeathMessage("§c" + death.getName() + "§7 s'est fait tuer par §3" + killer.getName());
	}

	@SuppressWarnings("deprecation")
	private boolean validModSword(ItemStack it) {
		return it.getTypeId() == 267 || it.getTypeId() == 268 || it.getTypeId() == 272 || it.getTypeId() == 276
				|| it.getTypeId() == 283;
	}

	private String enchantToString(ItemStack it) {
		if (it.getEnchantments().isEmpty()) {
			return "§7Aucun enchantements";
		}
		StringBuilder builder = new StringBuilder("§7Enchantements:\n");
		it.getEnchantments().forEach((a, b) -> {
			String level = String.valueOf(b).replace("1", "I").replace("2", "II").replace("3", "III").replace("4", "IV")
					.replace("5", "V");
			String name = EnchantName.valueOf(a.getName()).getName();
			builder.append("§b").append(name).append(" §l").append(level).append("\n");
			return;
		});
		return builder.toString();
	}
}
