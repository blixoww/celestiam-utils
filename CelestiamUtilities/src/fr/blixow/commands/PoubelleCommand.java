package fr.blixow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;

public class PoubelleCommand extends ICommand {

	public PoubelleCommand(JavaPlugin m) {
		super(m);
		this.addAlias("poubelle");
		this.setSyntax("/poubelle");
		this.addDescription(Description.POUBELLE);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (args.length == 0) {
				PoubelleInv(p);
			} else {
				p.sendMessage("§cUtilisation: " + this.getSyntax());
			}
		}
		return true;
	}

	public void PoubelleInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&8Poubelle"));
		p.openInventory(inv);
	}
}
