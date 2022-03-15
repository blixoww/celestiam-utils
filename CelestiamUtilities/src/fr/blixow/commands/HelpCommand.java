package fr.blixow.commands;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.plugin.Core;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.Utilities;

public class HelpCommand extends ICommand {

	public HelpCommand(JavaPlugin m) {
		super(m);
		this.addAlias("chelp");
		this.setSyntax("/chelp");
		this.addDescription(Description.HELP);
		this.addPermission(Permission.MODO);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (args.length == 0) {
			Bukkit.dispatchCommand(sender, "chelp 1");
		} else if (args.length == 1) {
			ArrayList<ICommand> listCommand = Core.getInstance().getCommandManager().getCommands();
			int commandCount = listCommand.size();
			String str = args[0];
			if (Utilities.isNumeric(str)) {
				int page = Integer.parseInt(str);
				int max = (page * 6) > commandCount ? commandCount : page * 6;
				int maxPage = (int) Math.ceil(commandCount / 6);
				String up = "§8§m------------------"+ "§e (§7" + page + "§8/§7" + maxPage + "§e)" +" §8§m------------------";
				String down = "§8§m------------------------------------------";
				setMessage(sender, up);
				for (int i = (page - 1) * 6; i < max; i++) {
					ICommand icommand = listCommand.get(i);
					for (Description desc : Description.values()) {
						if (desc.toString().equalsIgnoreCase(icommand.getDescription().toString().substring(1,
								icommand.getDescription().toString().length() - 1))) {
							sender.sendMessage("§a" + icommand.getSyntax() + " §8-§a " + desc.getDescriptionCommand());
						}
					}
				}
				setMessage(sender, down);
			}
		} else {
			setMessage(sender, "§cErreur avec l'exécution de la commande");
		}
		return true;
	}

}
