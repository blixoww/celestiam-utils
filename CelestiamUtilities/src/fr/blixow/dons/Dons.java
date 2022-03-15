package fr.blixow.dons;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.plugin.Core;
import fr.blixow.utils.Description;
import fr.blixow.utils.ICommand;
import fr.blixow.utils.CommandManager.Permission;

public class Dons extends ICommand implements Listener {
	public Dons(JavaPlugin m) {
		super(m);
		this.addAlias("dons");
		this.setSyntax("/dons");
		this.addDescription(Description.DONS);
		this.addPermission(Permission.PLAYER);
	}

	public HashMap<Player, Long> donationMap = new HashMap<Player, Long>();

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("send")) {
					if (!args[1].equalsIgnoreCase(p.getName())) {
						if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
							OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
							if (this.donationMap.containsKey(p)) {
								long remaining = this.donationMap.get(p).longValue() / 1000L + 600L
										- System.currentTimeMillis() / 1000L;
								if (remaining > 0L) {
									p.sendMessage(ChatColor.RED + "Veuillez patienter encore " + remaining
											+ " secondes avant d'envoyer un autre don");
									return false;
								}
							}
							final File don_f = new File(Core.getInstance().donationFolder, op.getUniqueId().toString());
							if (!don_f.exists()) {
								don_f.mkdirs();
							}
							boolean already_sent = false;
							File[] p_donations = don_f.listFiles();
							if (p_donations.length > 0) {
								for (int i = 0; i < p_donations.length; i++) {
									if ((!p_donations[i].isDirectory())
											&& (p_donations[i].getName().equalsIgnoreCase(p.getName() + ".yml"))) {
										already_sent = true;
									}
								}
							}
							if (!already_sent) {
								Inventory don_inv = Bukkit.createInventory(null, 54, "Don pour: " + args[1]);
								p.openInventory(don_inv);
							} else {
								p.sendMessage(ChatColor.RED + "Vous avez déjà envoyé un don à ce joueur!");
							}
						} else {
							p.sendMessage(ChatColor.RED + "Ce joueur n'a jamais joué sur le serveur!");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous envoyer de don à vous même!");
					}
				} else {
					p.sendMessage(ChatColor.RED + "Utilisation: /dons send §8<§cpseudo§8>");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("open")) {
					File[] donations = Core.getInstance().donationFolder.listFiles();
					if (donations.length > 0) {
						boolean exists = false;
						for (int i = 0; i < donations.length; i++) {
							if ((donations[i].isDirectory())
									&& (donations[i].getName().equalsIgnoreCase(p.getUniqueId().toString()))) {
								exists = true;
							}
						}
						if (exists) {
							Inventory inv = Bukkit.createInventory(null, 54, "§5Donations");
							File[] p_donations = new File(Core.getInstance().donationFolder, p.getUniqueId().toString())
									.listFiles();
							for (int i = 0; i < p_donations.length; i++) {
								if (!p_donations[i].isDirectory()) {
									FileConfiguration donation = YamlConfiguration.loadConfiguration(p_donations[i]);
									String donator = donation.getString("donator");
									ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
									SkullMeta skull_sm = (SkullMeta) skull.getItemMeta();
									skull_sm.setDisplayName(ChatColor.YELLOW + "Donation de " + donator);
									skull_sm.setOwner(donator);
									skull.setItemMeta(skull_sm);

									inv.addItem(new ItemStack[] { skull });
								}
							}
							p.openInventory(inv);
						} else {
							p.sendMessage(ChatColor.RED + "Vous n'avez pas de donations en attente");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Vous n'avez pas de donations en attente");
					}
				} else {
					p.sendMessage(ChatColor.RED + "Utilisation: /dons §8<§copen§8/§csend§8> [§cpseudo§8]");
				}
			} else {
				p.sendMessage(ChatColor.RED + "Utilisation: /dons §8<§copen§8/§csend§8> [§cpseudo§8]");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Cette commande ne peut être executée que par un joueur!");
		}
		return false;
	}

	@EventHandler
	public void onDonationSelect(InventoryClickEvent e) {
		if (e.getClickedInventory() != null) {
			final Inventory inv = e.getInventory();
			if (inv.getName().equalsIgnoreCase("§5Donations")) {
				if (e.getCursor() == null) {
					return;
				}
				e.setCancelled(true);
				if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
					final ItemMeta meta = e.getCurrentItem().getItemMeta();
					final String filename = ChatColor.stripColor(meta.getDisplayName().split(" ")[2]);
					final Player p = (Player) e.getWhoClicked();
					final File don = new File(Core.getInstance().donationFolder + "/" + p.getUniqueId().toString(),
							String.valueOf(filename) + ".yml");
					if (don.exists()) {
						final FileConfiguration config = (FileConfiguration) YamlConfiguration.loadConfiguration(don);
						ItemStack[] content = (ItemStack[]) ((List<?>) config.get("content")).toArray(new ItemStack[0]);
						final Inventory don_inv = Bukkit.createInventory((InventoryHolder) null, 54,
								"§5Donation de " + filename);
						don_inv.setContents(content);
						p.openInventory(don_inv);
						don.delete();
					} else {
						p.closeInventory();
						p.sendMessage(ChatColor.RED + "Il y a un problème avec ce don");
					}
				}
			} else if (ChatColor.stripColor(e.getClickedInventory().getTitle()).contains("Donation de ")) {
				e.setCancelled(true);
				if (e.getCursor() == null) {
					return;
				}
				if (e.getCurrentItem() != null) {
					boolean enough_space = false;
					for (final ItemStack is : ((Player) e.getWhoClicked()).getInventory()) {
						if (is == null) {
							((Player) e.getWhoClicked()).getInventory().addItem(new ItemStack[] { e.getCurrentItem() });
							enough_space = true;
							break;
						}
					}
					if (!enough_space) {
						((Player) e.getWhoClicked()).getLocation().getWorld()
								.dropItemNaturally(((Player) e.getWhoClicked()).getLocation(), e.getCurrentItem());
					}
					e.getClickedInventory().setItem(e.getSlot(), (ItemStack) null);
				}
			}
		}
	}

	@SuppressWarnings("all")
	@EventHandler
	public void onDonationSend(InventoryCloseEvent e) throws Exception {
		File fileDonation;
		if (ChatColor.stripColor(e.getInventory().getTitle()).contains("Don pour: ")) {
			boolean empty = true;
			for (ItemStack is : e.getInventory()) {
				if (is != null) {
					empty = false;
					break;
				}
			}
			if (empty) {
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez annulé votre don");
				return;
			}
			OfflinePlayer offlinePlayer = Bukkit
					.getOfflinePlayer(ChatColor.stripColor(e.getInventory().getTitle()).split(" ")[2]);
			fileDonation = new File(Core.getInstance().donationFolder + "/" + offlinePlayer.getUniqueId().toString(),
					e.getPlayer().getName() + ".yml");
			if (!fileDonation.exists()) {
				fileDonation.createNewFile();
			}
			Object config = YamlConfiguration.loadConfiguration(fileDonation);
			((FileConfiguration) config).set("donator", e.getPlayer().getName());
			((FileConfiguration) config).set("content", e.getInventory().getContents());
			((FileConfiguration) config).save(fileDonation);
			if (Bukkit.getPlayer(offlinePlayer.getUniqueId()) != null) {
				Bukkit.getPlayer(offlinePlayer.getUniqueId()).sendMessage(
						ChatColor.YELLOW + "Vous avez reçu un don de la part de " + e.getPlayer().getName());
			}
			e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez envoyé un don à " + offlinePlayer.getName());
			this.donationMap.put((Player) e.getPlayer(), Long.valueOf(System.currentTimeMillis()));
		} else if (ChatColor.stripColor(e.getInventory().getTitle()).contains("Donation de ")) {
			for (ItemStack is : e.getInventory().getContents()) {
				if (is != null) {
					e.getPlayer().getLocation().getWorld().dropItemNaturally(e.getPlayer().getLocation(), is);
				}
			}
			File[] p_donations = new File(Core.getInstance().donationFolder, e.getPlayer().getUniqueId().toString())
					.listFiles();
			if (p_donations.length == 0) {
				new File(Core.getInstance().donationFolder, e.getPlayer().getUniqueId().toString()).delete();
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Le don a été récupéré");
			} else {
				e.getPlayer()
						.sendMessage(ChatColor.YELLOW + "Vous avez encore " + p_donations.length + " dons en attente");
			}
		}
	}

}
