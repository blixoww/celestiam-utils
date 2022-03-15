package fr.blixow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.Utilities;

public class MsgCommand extends ICommand {

	private Player target;
	
	public MsgCommand(JavaPlugin m) {
		super(m);
		this.addAlias("msg", "emsg", "tell", "etell", "t", "m", "w");
		this.setSyntax("/msg");
		this.addDescription(Description.MSG);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		String message = "";
		if ((sender instanceof Player)) {
			Player p = (Player) sender;
			if (args.length <= 0) {
				p.sendMessage(ChatColor.GREEN + "Utilisation: §a" + this.getSyntax()
						+ " §8<§ajoueur§8> §8[§amessage§8]");
				return true;
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.RED + "§l[!]" + ChatColor.GRAY + " Ecris un message");
				return true;
			}
			if (args.length >= 2) {
				this.target = Bukkit.getPlayerExact(args[0]);
				if (this.target == null) {
					p.sendMessage(ChatColor.RED + "§l[!]" + ChatColor.GRAY + " Le joueur n'est pas trouvable");
					return true;
				}
				for (int i = 1; i < args.length; i++) {
					message = message + args[i] + " ";
				}
				p.sendMessage(
						"§e ■ §7" + "moi -> §e" + this.target.getName() + " §8» " + ChatColor.WHITE + message);
				this.target
						.sendMessage("§e ■ §e" + p.getName() + "§7 -> moi" + " §8» " + ChatColor.WHITE + message);
				Utilities.msg.put(p.getUniqueId(), this.target.getUniqueId());
				Utilities.msg.put(this.target.getUniqueId(), p.getUniqueId());
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.GREEN + "§lSeul un joueur peut faire cette commande");
			return true;
		}
		return false;
	}
}


