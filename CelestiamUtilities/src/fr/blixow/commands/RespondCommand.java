package fr.blixow.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.Utilities;
import fr.blixow.utils.CommandManager.Permission;

public class RespondCommand extends ICommand {

	public RespondCommand(JavaPlugin m) {
		super(m);
		this.addAlias("r", "er", "reply");
		this.setSyntax("/r");
		this.addDescription(Description.RESPOND);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		String message = "";
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			if (args.length <= 0) {
				player.sendMessage(ChatColor.GREEN + "Utilisation: §a" + this.getSyntax() + " §8[§amessage§8]");
				return true;
			}
			if (args.length >= 1) {
				UUID targetUUID = (UUID) Utilities.msg.get(player.getUniqueId());
				if (targetUUID != null) {
					if (Bukkit.getPlayer(targetUUID) == null) {
						Utilities.msg.remove(player.getUniqueId());
						return true;
					}
					Player target = Bukkit.getPlayer(targetUUID);
					for (int i = 0; i < args.length; i++) {
						message = message + args[i] + " ";
					}
					player.sendMessage(
							"§e ■ §7" + "moi -> §e" + target.getName() + " §8» " + ChatColor.WHITE + message);
					Bukkit.getPlayer(targetUUID).sendMessage(
							"§e ■ §e" + player.getName() + "§7 -> moi" + " §8» " + ChatColor.WHITE + message);
					return true;
				}
				player.sendMessage(ChatColor.RED + "§l[!]" + ChatColor.GRAY + " Le joueur n'est pas trouvable");
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "§lSeul un joueur peut faire cette commande");
			return true;
		}
		return false;
	}
}

