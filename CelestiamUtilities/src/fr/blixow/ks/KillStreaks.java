package fr.blixow.ks;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.plugin.Core;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.CommandManager.Permission;

public class KillStreaks extends ICommand implements Listener {

	public KillStreaks(JavaPlugin m) {
		super(m);
		this.addAlias("ks");
		this.setSyntax("/ks");
		this.addDescription(Description.KILL);
		this.addPermission(Permission.PLAYER);
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				Long playTimeForMe = (long) p.getStatistic(Statistic.PLAY_ONE_TICK);
				p.sendMessage("§8§m----------------------------§e");
				p.sendMessage("§b§nTes statistiques");
				p.sendMessage("");
				p.sendMessage("§7Nombre de joueur tué §8: §a" + Integer.toString(Utils.getKills(p).intValue()));
				p.sendMessage("§7Nombre de mort §8: §c" + Integer.toString(Utils.getDeaths(p).intValue()));
				p.sendMessage("§7Ks §8: §b" + Utils.getKs(p));
				p.sendMessage("");
				p.sendMessage("§7Temps de jeu §8: §e"
						+ DurationFormatUtils.formatDurationWords(playTimeForMe * 50L, true, true));
				p.sendMessage("§8§m----------------------------§e");

			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					p.sendMessage("§cLe joueur n'est pas trouvable.");
					return true;
				}

				if (target != null) {
					Long playTimeForOther = (long) target.getStatistic(Statistic.PLAY_ONE_TICK);
					p.sendMessage("§8§m----------------------------§e");
					p.sendMessage("§b§nStatistiques de " + target.getName());
					p.sendMessage("");
					p.sendMessage(
							"§7Nombre de joueur tué §8: §a" + Integer.toString(Utils.getKills(target).intValue()));
					p.sendMessage("§7Nombre de mort §8: §c" + Integer.toString(Utils.getDeaths(target).intValue()));
					p.sendMessage("§7Ks §8: §b" + Utils.getKs(target));
					p.sendMessage("");
					p.sendMessage("§7Temps de jeu §8: §e"
							+ DurationFormatUtils.formatDurationWords(playTimeForOther * 50L, true, true));
					p.sendMessage("§8§m----------------------------§e");
				}
			}
			if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if (target == null) {
					p.sendMessage("§cLe joueur n'est pas trouvable.");
					return true;
				}

				if (target != null) {
					p.sendMessage("§aLes statistiques de " + target.getName() + " ont été reset");
					Core.getInstance().getConfig().set(target.getUniqueId().toString() + ".deaths", Integer.valueOf(0));
					Core.getInstance().getConfig().set(target.getUniqueId().toString() + ".kills", Integer.valueOf(0));
					Core.getInstance().saveConfig();
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Player killer = e.getEntity().getKiller();
		if (((p instanceof Player)) && ((killer instanceof Player))) {
			int currentDeaths = Core.getInstance().getConfig().getInt(p.getUniqueId().toString() + ".deaths");
			Core.getInstance().getConfig().set(p.getUniqueId().toString() + ".deaths",
					Integer.valueOf(currentDeaths + 1));

			int currentKills = Core.getInstance().getConfig().getInt(killer.getUniqueId().toString() + ".kills");
			Core.getInstance().getConfig().set(killer.getUniqueId().toString() + ".kills",
					Integer.valueOf(currentKills + 1));

			Core.getInstance().saveConfig();
		}
	}
	
}
