package fr.blixow.reward;

import java.util.Random;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import fr.blixow.plugin.Core;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.Registers;
import fr.blixow.utils.Utilities;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;

public class RewardCommand extends ICommand {

	public RewardCommand(JavaPlugin m) {
		super(m);
		this.addAlias("reward");
		this.setSyntax("/reward");
		this.addDescription(Description.REWARD);
		this.addPermission(Permission.PLAYER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if ((sender instanceof Player)) {
			Player p = (Player) sender;
			if (!Registers.getRewardManager().isCooldown(p)) {
				new BukkitRunnable() {
					public void run() {
						if (!p.isOp()) {
							Registers.getRewardManager().addPlayer(p);
						}
						this.cancel();
					}
				}.runTaskTimer(Core.getInstance(), 0L, 20L);

				int minimum = 1000;
				int maximum = 5000;
				Random rand = new Random();
				int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
				Utilities.getEconomy().depositPlayer(p.getName(), randomNum);
				p.sendMessage("§e" + randomNum + "$ ont été ajouté au compte");
			}
			if (Registers.getRewardManager().isCooldown(p)) {
				p.sendMessage("§cVous devez encore attendre : " + Registers.getRewardManager().getTimeLeft(p));
				return true;
			}
		}
		return false;
	}

}
