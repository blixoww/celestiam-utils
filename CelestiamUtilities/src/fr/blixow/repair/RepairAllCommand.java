package fr.blixow.repair;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import fr.blixow.plugin.Core;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.Registers;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;

public class RepairAllCommand extends ICommand {
	public ItemStack item;

	public RepairAllCommand(JavaPlugin m) {
		super(m);
		this.addAlias("repairall");
		this.setSyntax("/repairall");
		this.addDescription(Description.REPAIR);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if ((sender instanceof Player)) {
			final Player p = (Player) sender;
			if (!Registers.getRepairManager().isCooldown(p)) {
				new BukkitRunnable() {
					public void run() {
						if (!p.isOp()) {
							Registers.getRepairManager().addPlayer(p);
						}
						this.cancel();
					}
				}.runTaskTimer(Core.getInstance(), 0L, 20L);

				repairAll(p);
				p.sendMessage("§eTous les items ont été réparés");
			}
			if (Registers.getRepairManager().isCooldown(p)) {
				p.sendMessage("§cVous devez encore attendre : " + Registers.getRepairManager().getTimeLeft(p));
				return true;
			}
		}
		return false;
	}

	public void repairAll(Player player) {
		ItemStack[] inv = player.getInventory().getContents();
		for (ItemStack i : inv) {
			if (i != null) {
				repair(i, player);
			}
		}

		ItemStack[] armor = player.getInventory().getArmorContents();
		for (ItemStack i : armor) {
			if (i != null) {
				repair(i, player);
			}
		}
	}

	public void repair(ItemStack item, Player p) {
		item.setDurability((short) 0);
	}

}
