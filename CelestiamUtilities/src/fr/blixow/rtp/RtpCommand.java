package fr.blixow.rtp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.blixow.plugin.Core;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.Registers;

public class RtpCommand extends ICommand {

	public RtpCommand(JavaPlugin m) {
		super(m);
		this.addAlias("rtp");
		this.setSyntax("/rtp");
		this.addDescription(Description.RTP);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if ((sender instanceof Player)) {
			final Player p = (Player) sender;
			if (Registers.getRtpManager().isCooldown(p)) {
				p.sendMessage("§cVous devez encore attendre " + Registers.getRtpManager().getTimeLeft(p)
						+ " avant de pouvoir refaire la commande.");
				return true;
			}
			if (p.isOp()) {
				p.teleport(Registers.getRtpManager().getRandomLocation());
				return true;
			}
			Registers.getRtpManager().getIsTeleporting().add(p.getName());
			new BukkitRunnable() {
				int timer = 5;

				public void run() {
					if (Registers.getRtpManager().getIsTeleporting().contains(p.getName())) {
						p.sendMessage("§7Vous allez être téléporté dans " + this.timer + " §7secondes");
						this.timer -= 1;
						if (this.timer == 0) {
							Registers.getRtpManager().getIsTeleporting().remove(p.getName());
							p.teleport(Registers.getRtpManager().getRandomLocation());
							if (!p.isOp()) {
								Registers.getRtpManager().addPlayer(p);
							}
							cancel();
						}
					} else {
						cancel();
					}
				}
			}.runTaskTimer(Core.getInstance(), 0L, 20L);
		}
		return false;
	}

}
