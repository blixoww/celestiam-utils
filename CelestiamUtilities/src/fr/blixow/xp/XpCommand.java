package fr.blixow.xp;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;

public class XpCommand extends ICommand {

	public XpCommand(JavaPlugin m) {
		super(m);
		this.addAlias("bottlexp", "bottle");
		this.setSyntax("/bottlexp");
		this.addDescription(Description.XP);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		Player p = (Player) sender;
		if (p.getInventory().firstEmpty() == -1) {
			p.sendMessage("§8[§cXp§8] §cVous n'avez plus de place dans votre inventaire !");
		} else {
			int x = new Random().nextInt(2000000);
			int count = p.getLevel();
			if (count != 0 && count >= 10) {
				p.setExp(0.0F);
				p.setLevel(0);
				ItemStack stack = new ItemStack(Material.EXP_BOTTLE);
				ItemMeta meta = stack.getItemMeta();
				meta.setDisplayName("§a§lLevel §8: §6" + count);
				meta.setLore(Arrays.asList(new String[] { "Clique droit pour utiliser" }));
				meta.setLore(Arrays.asList(new String[] { "§dID:" + x }));
				stack.setItemMeta(meta);

				p.getInventory().addItem(new ItemStack[] { stack });
				p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ORB_PICKUP, 30.0F, 1.0F);
				p.sendMessage("§8[§aXp§8] §aTu as mis en bouteille §6" + count + " §alevels");
			} else {
				p.sendMessage("§8[§cXp§8] §cVous devez avoir au moins 10 niveaux !");
			}
		}
		return false;
	}

}
