package fr.blixow.trade;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.plugin.Core;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TradeCommand extends ICommand {

	public TradeCommand(JavaPlugin m) {
		super(m);
		this.addAlias("trade");
		this.setSyntax("/trade");
		this.addDescription(Description.TRADE);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			if (args.length == 1) {
				Player player = (Player) sender;
				Player receiver = null;
				for (Player p : Core.getInstance().getServer().getOnlinePlayers()) {
					if (p.getName().equalsIgnoreCase(args[0])) {
						receiver = p;
						break;
					}
				}
				if ((!args[0].equalsIgnoreCase(player.getName())) && (receiver != null)) {
					new TradeInventory(player, receiver, Core.getInstance());
					sender.sendMessage("§eVous avez envoyé une demande d'échange à " + receiver.getName());
					TextComponent text = new TextComponent("§e" + sender.getName() + " vous a envoyé une demande d'échange §7(/accept)");
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept"));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Clique pour accepter").italic(true).create()));
					receiver.spigot().sendMessage(text);

					return true;
				}
				if (receiver == null) {
					sender.sendMessage("§cLe joueur n'est pas trouvable");
				}
			} else {
				sender.sendMessage("§c/trade §8[§cpseudo§8]");
			}
		}
		return false;
	}

}
