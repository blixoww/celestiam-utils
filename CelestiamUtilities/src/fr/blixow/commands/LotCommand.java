package fr.blixow.commands;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;

public class LotCommand extends ICommand {

	public LotCommand(JavaPlugin m) {
		super(m);
		this.addAlias("lot");
		this.setSyntax("/lot");
		this.addDescription(Description.LOT);
		this.addPermission(Permission.ADMIN);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		final Player p = (Player) sender;
		if (args.length < 1) {
			p.sendMessage("§c/lot §8[§crécompense§8]");
		} else {
			int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
			Player players = (Player) Bukkit.getOnlinePlayers().toArray()[random];
			StringBuilder lotBuilder = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				lotBuilder.append(args[i]).append(" ");
			}
			Bukkit.broadcastMessage("§8§m-------------------------\n" + "\n" + "§8» §e" + players.getName()
					+ " §ea gagné §8!§b" + "\n§8» §eVoici sa récompense §8:§e " + lotBuilder + " \n" + "\n"
					+ "§8§m-------------------------\n");
		}
		return false;

	}
}
