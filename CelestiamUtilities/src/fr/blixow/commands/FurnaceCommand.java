package fr.blixow.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.CommandManager.Permission;
import fr.blixow.utils.Description;

public class FurnaceCommand extends ICommand {

	public FurnaceCommand(JavaPlugin m) {
		super(m);
		this.addAlias("furnace");
		this.setSyntax("/furnace");
		this.addDescription(Description.FURNACE);
		this.addPermission(Permission.PLAYER);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		Player p = (Player) sender;
		ItemStack is = p.getInventory().getItemInHand();
		Material item = is.getType();
		int number = is.getAmount();
		boolean worked = false;
		Material newItem = null;
		if (item == Material.PORK) {
			worked = true;
			newItem = Material.GRILLED_PORK;
		} else if (item == Material.RAW_BEEF) {
			worked = true;
			newItem = Material.COOKED_BEEF;
		} else if (item == Material.RAW_CHICKEN) {
			worked = true;
			newItem = Material.COOKED_CHICKEN;
		} else if (item == Material.POTATO) {
			worked = true;
			newItem = Material.BAKED_POTATO;
		} else if (item == Material.IRON_ORE) {
			worked = true;
			newItem = Material.IRON_INGOT;
		} else if (item == Material.GOLD_ORE) {
			worked = true;
			newItem = Material.GOLD_INGOT;
		} else if (item == Material.SAND) {
			worked = true;
			newItem = Material.GLASS;
		} else if (item == Material.COBBLESTONE) {
			worked = true;
			newItem = Material.STONE;
		} else if (item == Material.CLAY_BALL) {
			worked = true;
			newItem = Material.BRICK;
		} else if (item == Material.NETHERRACK) {
			worked = true;
			newItem = Material.NETHER_BRICK;
		} else if (item == Material.DIAMOND_ORE) {
			worked = true;
			newItem = Material.DIAMOND;
		} else if (item == Material.REDSTONE_ORE) {
			worked = true;
			newItem = Material.REDSTONE;
		} else if (item == Material.COAL_ORE) {
			worked = true;
			newItem = Material.COAL;
		} else if (item == Material.EMERALD_ORE) {
			worked = true;
			newItem = Material.EMERALD;
		}
		if (worked) {
			ItemStack newStack = new ItemStack(newItem, number);
			p.getInventory().setItemInHand(newStack);
			p.sendMessage("§8[§aFurnace§8]§7 L'item a été correctement cuit §a!");
		} else {
			p.sendMessage("§8[§aFurnace§8]§7 Tu dois avoir un item cuisable dans la main §a!");
		}
		return true;
	}

}
