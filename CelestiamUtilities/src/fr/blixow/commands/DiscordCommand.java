package fr.blixow.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;

public class DiscordCommand extends ICommand {

	public DiscordCommand(JavaPlugin m) {
		super(m);
		this.addAlias("discord");
		this.setSyntax("/discord");
		this.addDescription(Description.DISCORD);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		setMessage(sender, "§8§m-----------------------" + "\n" + "§ahttps://discord.io/celestiam" + "\n"
				+ "§8§m-----------------------");
		return true;
	}

}
