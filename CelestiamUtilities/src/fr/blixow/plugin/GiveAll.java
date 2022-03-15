package fr.blixow.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;

public class GiveAll extends ICommand implements Listener {

	public GiveAll(JavaPlugin m) {
		super(m);
		this.addAlias("giveall");
		this.setSyntax("/giveall");
		this.addDescription(Description.GIVE);
		this.addPermission(Permission.ADMIN);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			Inventory inventory = Bukkit.createInventory(null, 27, "§5Give All");
			player.openInventory(inventory);
		}
		return false;
	}

	@EventHandler
	public void invClose(InventoryCloseEvent e) {
		if (e.getInventory().getName().equalsIgnoreCase("§5Give All")) {
			for (ItemStack item : e.getInventory().getContents()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!(player.getInventory().firstEmpty() == -1)) {
						player.getInventory().addItem(new ItemStack[] { item });
					} else {
						player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
					}
				}
			}
		}
	}

}