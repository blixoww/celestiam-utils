package fr.blixow.utils;

import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.cobble.CobbleCommand;
import fr.blixow.commands.DiscordCommand;
import fr.blixow.commands.FurnaceCommand;
import fr.blixow.commands.HelpCommand;
import fr.blixow.commands.LotCommand;
import fr.blixow.commands.MsgCommand;
import fr.blixow.commands.SiteCommand;
import fr.blixow.dons.Dons;
import fr.blixow.ks.KillStreaks;
import fr.blixow.plugin.GiveAll;
import fr.blixow.repair.RepairAllCommand;
import fr.blixow.reward.RewardCommand;
import fr.blixow.rtp.RtpCommand;
import fr.blixow.trade.AcceptCommand;
import fr.blixow.trade.TradeCommand;
import fr.blixow.xp.XpCommand;
import fr.blixow.commands.PoubelleCommand;
import fr.blixow.commands.RespondCommand;

public class CommandManager implements CommandExecutor {
	public ArrayList<ICommand> commands = new ArrayList<ICommand>();

	private JavaPlugin pl;

	// enregistrement des commandes
	public CommandManager(JavaPlugin m) {
		pl = m;
		addCommand(new SiteCommand(pl));
		addCommand(new DiscordCommand(pl));
		addCommand(new MsgCommand(pl));
		addCommand(new RespondCommand(pl));
		addCommand(new PoubelleCommand(pl));
		addCommand(new LotCommand(pl));
		addCommand(new RtpCommand(pl));
		addCommand(new RepairAllCommand(pl));
		addCommand(new CobbleCommand(pl));
		addCommand(new Dons(pl));
		addCommand(new GiveAll(pl));
		addCommand(new TradeCommand(pl));
		addCommand(new AcceptCommand(pl));
		addCommand(new XpCommand(pl));
		addCommand(new RewardCommand(pl));
		addCommand(new FurnaceCommand(pl));
		addCommand(new KillStreaks(pl));
		addCommand(new HelpCommand(pl));
		registerCommands();
	}

	public void addCommand(ICommand icommand) {
		this.commands.add(icommand);
	}

	public ArrayList<ICommand> getCommands() {
		return commands;
	}

	public void registerCommands() {
		for (ICommand iCommand : this.getCommands()) {
			for (String alias : iCommand.getAliases()) {
				pl.getCommand(alias).setExecutor(this);
			}
		}
	}
	
	// execution de la commande
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (ICommand iCommand : getCommands()) {
			if (iCommand.getAliases().contains(label.toLowerCase())) {
				if (!checkPermission(iCommand, sender)) {
					Utilities.noPermissions(sender);
					return false;
				}
				try {
					iCommand.execute(sender, args);
				} catch (Exception e) {
					sender.sendMessage("§cErreur avec l'exécution de la commande : " + iCommand.getSyntax());
				}
				return true;
			}
		}
		return false;
	}

	// Verification des permissions
	public boolean checkPermission(ICommand icommand, CommandSender sender) {
		for (Permission permission : icommand.getPermission()) {
			if (!icommand.hasPermission(sender, permission)) {
				return false;
			}
		}
		return true;
	}

	public enum Permission {
		PLAYER, ADMIN, MODO;
	}

}
