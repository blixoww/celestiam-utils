package fr.blixow.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.CommandManager.Permission;

public abstract class ICommand {

	protected JavaPlugin pl;
	private ArrayList<String> aliases = new ArrayList<String>();
	private ArrayList<Permission> permission = new ArrayList<Permission>();
	private ArrayList<Description> description = new ArrayList<Description>();
	private ArrayList<ICommand> icommand = new ArrayList<ICommand>();
	private String syntax;
	private String message;
	private CommandSender sender;

	public ICommand(JavaPlugin m) {
		pl = m;
	}
	
	public ArrayList<ICommand> getCommands() {
		return icommand;
	}

	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
	}

	public ArrayList<String> getAliases() {
		return aliases;
	}

	public void addAlias(String... args) {
		for (String str : args) {
			this.aliases.add(str.toLowerCase());
		}
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setDescription(ArrayList<Description> description) {
		this.description = description;
	}

	public ArrayList<Description> getDescription() {
		return description;
	}

	public void addDescription(Description description) {
		this.description.add(description);
	}

	public void setPermission(ArrayList<Permission> permission) {
		this.permission = permission;
	}

	public ArrayList<Permission> getPermission() {
		return permission;
	}

	public void addPermission(Permission permission) {
		this.permission.add(permission);
	}

	public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

	public boolean hasPermission(CommandSender sender, Permission permission) {
		switch (permission) {
		case PLAYER:
			if (!(sender instanceof Player)) {
				return false;
			}
			break;
		case ADMIN:
			if (sender instanceof Player) {
				if (!sender.hasPermission("celestiam.admin")) {
					return false;
				}
			}
			break;
		case MODO:
			if (sender instanceof Player) {
				if (!sender.hasPermission("celestiam.modo")) {
					return false;
				}
			}
			break;
		}
		return true;
	}

	public ICommand setMessage(CommandSender sender, String message) {
		this.setSender(sender);
		this.message = message;
		sender.sendMessage(message);
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(List<String> msgs) {
		for (String msg : msgs)
			setMessage(msg);
	}

	public CommandSender getSender() {
		return sender;
	}

	public void setSender(CommandSender sender) {
		this.sender = sender;
	}
	
}
