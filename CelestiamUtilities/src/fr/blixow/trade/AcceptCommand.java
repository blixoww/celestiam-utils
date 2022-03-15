package fr.blixow.trade;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;

public class AcceptCommand extends ICommand {

	public AcceptCommand(JavaPlugin m) {
		super(m);
		this.addAlias("accept");
		this.setSyntax("/accept");
		this.addDescription(Description.ACCEPT);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			if (args.length == 0) {
				Player player = (Player) sender;
				TradeInventory trade = TradeInventory.acceptTradeRequest(player);
				if (trade != null) {
					trade.initiateTrade();
					return true;
				} else {
					sender.sendMessage("§cIl n'y a aucune demande d'échange");
				}
			}
		}
		return false;
	}

}
