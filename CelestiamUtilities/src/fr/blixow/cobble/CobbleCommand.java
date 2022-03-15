package fr.blixow.cobble;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blixow.utils.ICommand;
import fr.blixow.utils.Utilities;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;

public class CobbleCommand extends ICommand {

	public CobbleCommand(JavaPlugin m) {
		super(m);
		this.addAlias("cobble");
		this.setSyntax("/cooble");
		this.addDescription(Description.COBBLE);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!Utilities.playerList.contains(p.getName())) {
				Utilities.playerList.add(p.getName());
				p.sendMessage("§aLa cobblestone n'est plus récupérable");
			} else {
				Utilities.playerList.remove(p.getName());
				p.sendMessage("§cLa cobblestone est de nouveau récupérable");
			}
		}
		return false;
	}

}