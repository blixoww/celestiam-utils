package fr.blixow.trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import fr.blixow.plugin.Core;

public class TradeInventory implements Listener {

	public static HashMap<Player, TradeInventory> pendingRequests;
	ItemStack[][] items;
	private Inventory[] inventories;
	private Player[] players;
	Timer requestTimeout;
	Timer finishTrade;
	Timer sync;
	private boolean tradeClosed;
	private boolean[] accepted;
	CopyOnWriteArrayList<Integer> events;

	public TradeInventory(final Player sender, final Player receiver, final Core plugin) {
		if (TradeInventory.pendingRequests.containsKey(receiver)) {
			TradeInventory.pendingRequests.get(receiver).requestTimeout.cancel();
		}
		TradeInventory.pendingRequests.put(receiver, this);
		plugin.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) plugin);
		this.accepted = new boolean[2];
		this.players = new Player[2];
		this.inventories = new Inventory[2];
		this.players[0] = sender;
		this.players[1] = receiver;
		(this.requestTimeout = new Timer()).schedule(new TimeOutTask(this), 10000L);
		this.events = new CopyOnWriteArrayList<Integer>();
		(this.sync = new Timer()).schedule(new UpdateSync(this.events), 0L, 400L);
	}

	@EventHandler
	public void InventoryDrag(final InventoryDragEvent event) {
		if (!this.tradeClosed) {
			int index = -1;
			if (this.inventories[0] != null && event.getInventory().equals(this.inventories[0])) {
				index = 0;
			}
			if (this.inventories[1] != null && event.getInventory().equals(this.inventories[1])) {
				index = 1;
			}
			for (final int slot : event.getRawSlots()) {
				final int sRow = slot / 9;
				final int sColumn = slot % 9;
				if ((sColumn == 0 && (sRow == 0 || sRow == 8)) || sColumn > 3) {
					event.setCancelled(true);
					return;
				}
			}
			if (index != -1) {
				this.events.add(index);
			}
		}
	}

	@EventHandler
	public void InventoryEdit(final InventoryClickEvent event) {
		if (!this.tradeClosed && Arrays.asList(this.players).contains(event.getWhoClicked())) {
			if (event.getClickedInventory() == null) {
				return;
			}
			int index = -1;
			if (this.inventories[0] != null && event.getClickedInventory().equals(this.inventories[0])) {
				index = 0;
			}
			if (this.inventories[1] != null && event.getClickedInventory().equals(this.inventories[1])) {
				index = 1;
			}
			final int sRow = event.getRawSlot() / 9;
			final int sColumn = event.getRawSlot() % 9;
			if ((index != -1 || this.players[0].getOpenInventory().getTopInventory().equals(this.inventories[0])
					|| this.players[0].getOpenInventory().getTopInventory().equals(this.inventories[0]))
					&& event.getClick() != ClickType.RIGHT && event.getClick() != ClickType.LEFT) {
				event.setCancelled(true);
				return;
			}
			if (index != -1) {
				if ((sColumn == 0 && (sRow == 0 || sRow == 8)) || sColumn > 3) {
					event.setCancelled(true);
					if (sColumn == 0) {
						if (!this.accepted[index] && this.canFit((Inventory) this.players[index].getInventory(),
								this.getOffer(1 - index))) {
							this.acceptTrade(index);
						} else {
							this.declineTrade();
						}
					}
					return;
				}
				this.events.add(index);
			}
		}
	}

	public boolean canFit(Inventory inv, final ItemStack[] offer) {
		inv = this.cloneInventory(inv);
		for (final ItemStack item : offer) {
			if (inv.addItem(new ItemStack[] { item }).size() > 0) {
				inv.clear();
				return false;
			}
		}
		inv.clear();
		return true;
	}

	public Inventory cloneInventory(final Inventory inv) {
		final Inventory cloned = Bukkit.createInventory((InventoryHolder) null, 36);
		final ItemStack[] items = inv.getContents();
		for (int i = 0; i < items.length; ++i) {
			if (items[i] != null) {
				cloned.setItem(i, items[i].clone());
			} else {
				cloned.addItem(new ItemStack[] { new ItemStack(Material.AIR) });
			}
		}
		return cloned;
	}

	public void declineTrade() {
		this.tradeClosed = false;
		this.accepted[0] = false;
		this.accepted[1] = false;
		this.addTradingDesign(0);
		this.addTradingDesign(1);
		if (this.finishTrade != null) {
			this.finishTrade.cancel();
		}
	}

	public void acceptTrade(final int index) {
		ItemStack decline = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta meta = decline.getItemMeta();
		meta.setDisplayName("§aAccepté.");
		meta.setLore((List<String>) Collections.singletonList("§cClique pour refuser."));
		decline.setItemMeta(meta);
		this.inventories[index].setItem(0, decline);
		meta = meta.clone();
		meta.setLore((List<String>) Collections.singletonList("§cEn Attente..."));
		decline = decline.clone();
		decline.setItemMeta(meta);
		this.inventories[1 - index].setItem(8, decline);
		this.accepted[index] = true;
		if (this.accepted[0] && this.accepted[1]) {
			(this.finishTrade = new Timer()).schedule(new FinishTrade(this), 0L, 1000L);
		}
	}

	@EventHandler
	public void closeInventory(final InventoryCloseEvent event) {
		if (!this.tradeClosed) {
			int index = -1;
			if (this.inventories[0] != null && event.getInventory().equals(this.inventories[0])) {
				index = 0;
			}
			if (this.inventories[1] != null && event.getInventory().equals(this.inventories[1])) {
				index = 1;
			}
			if (index != -1 && this.closeTrade(0, 1)) {
				if (this.finishTrade != null) {
					this.finishTrade.cancel();
				}
				this.players[1 - index].closeInventory();
				TradeInventory.pendingRequests.remove(this.players[1]);
				this.players[0].sendMessage("§cEchange annulé.");
				this.players[1].sendMessage("§cEchange annulé.");
			}
		}
	}

	public boolean closeTrade(final int loots1, final int loots2) {
		if (!this.tradeClosed) {
			for (final ItemStack item : this.getOffer(0)) {
				this.players[loots1].getInventory().addItem(new ItemStack[] { item });
			}
			this.players[loots1].updateInventory();
			for (final ItemStack item : this.getOffer(1)) {
				this.players[loots2].getInventory().addItem(new ItemStack[] { item });
			}
			this.players[loots2].updateInventory();
			this.tradeClosed = true;
			this.sync.cancel();
			this.inventories[0].clear();
			this.inventories[1].clear();
			return true;
		}
		return false;
	}

	public static TradeInventory acceptTradeRequest(final Player player) {
		if (TradeInventory.pendingRequests.containsKey(player)) {
			final TradeInventory tempInventory = TradeInventory.pendingRequests.get(player);
			TradeInventory.pendingRequests.remove(player);
			tempInventory.requestTimeout.cancel();
			tempInventory.players[0]
					.sendMessage(("§e" + tempInventory.players[1].getName() + " a accepté la demande d'échange"));
			return tempInventory;
		}
		return null;
	}
	
	public void initiateTrade() {
		final String fNameOne = String.format("%-16s", this.players[0].getName());
		final String fNameTwo = String.format("%-16s", this.players[1].getName());
		this.inventories[0] = Bukkit.getServer().createInventory((InventoryHolder) null, 54, fNameOne + fNameTwo);
		this.addTradingDesign(0);
		this.players[0].closeInventory();
		this.players[0].openInventory(this.inventories[0]);
		this.players[0].updateInventory();
		this.inventories[1] = Bukkit.getServer().createInventory((InventoryHolder) null, 54, fNameTwo + fNameOne);
		this.addTradingDesign(1);
		this.players[1].closeInventory();
		this.players[1].openInventory(this.inventories[1]);
		this.players[1].updateInventory();
	}

	public void addTradingDesign(final int index) {
		ItemStack accept = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta meta = accept.getItemMeta();
		meta.setDisplayName("§cRefusé");
		meta.setLore(
				(List<String>) Collections.singletonList("§aClique pour accepter"));
		accept.setItemMeta(meta);
		this.inventories[index].setItem(0, accept);
		meta = meta.clone();
		meta.setLore((List<String>) Collections.singletonList("§cEn attente"));
		accept = accept.clone();
		accept.setItemMeta(meta);
		this.inventories[index].setItem(8, accept);
		final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = glass.getItemMeta();
		meta.setDisplayName("§7");
		meta.setLore((List<String>) Arrays.asList("§d<- Ton côté"));
		glass.setItemMeta(meta);
		for (int i = 0; i < 6; ++i) {
			this.inventories[index].setItem(4 + 9 * i, glass);
		}
	}

	public ItemStack[] getOffer(final int p) {
		final ItemStack[] contents = this.inventories[p].getContents();
		final ArrayList<ItemStack> offer = new ArrayList<ItemStack>();
		for (int i = 1; i < 54; ++i) {
			final int column = i % 9;
			if (column < 4 && contents[i] != null) {
				offer.add(contents[i]);
			}
		}
		return offer.toArray(new ItemStack[0]);
	}

	static {
		TradeInventory.pendingRequests = new HashMap<Player, TradeInventory>();
	}

	private class FinishTrade extends TimerTask {
		TradeInventory inventory;
		int count;

		public FinishTrade(final TradeInventory tradeInventory) {
			this.inventory = tradeInventory;
		}

		@Override
		public void run() {
			if (this.count == 6) {
				TradeInventory.this.closeTrade(1, 0);
				TradeInventory.this.players[0].closeInventory();
				TradeInventory.this.players[1].closeInventory();
				TradeInventory.pendingRequests.remove(TradeInventory.this.players[1]);
				this.cancel();
				return;
			}
			final int slot = 9 * (5 - this.count) + 4;
			final ItemMeta glassMeta = this.inventory.inventories[0].getItem(slot).getItemMeta();
			final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			glass.setItemMeta(glassMeta);
			this.inventory.inventories[0].setItem(slot, glass);
			this.inventory.inventories[1].setItem(slot, glass);
			++this.count;
		}
	}

	private class UpdateSync extends TimerTask {
		CopyOnWriteArrayList<Integer> events;

		public UpdateSync(final CopyOnWriteArrayList<Integer> events) {
			this.events = events;
		}

		@Override
		public void run() {
			while (!this.events.isEmpty()) {
				final int index = this.events.remove(0);
				for (int i = 1; i < 54; ++i) {
					final int row = i / 9;
					final int column = i % 9;
					if (column < 4) {
						ItemStack item = new ItemStack(Material.AIR);
						if (TradeInventory.this.inventories[index].getItem(i) != null) {
							item = TradeInventory.this.inventories[index].getItem(i).clone();
						}
						TradeInventory.this.inventories[1 - index].setItem(row * 9 - column + 8, item);
						TradeInventory.this.declineTrade();
					}
				}
			}
			if (TradeInventory.this.accepted[0] && TradeInventory.this.accepted[1]) {
				if (!TradeInventory.this.canFit((Inventory) TradeInventory.this.players[1].getInventory(),
						TradeInventory.this.getOffer(0))) {
					TradeInventory.this.declineTrade();
				}
				if (!TradeInventory.this.canFit((Inventory) TradeInventory.this.players[0].getInventory(),
						TradeInventory.this.getOffer(1))) {
					TradeInventory.this.declineTrade();
				}
			}
		}
	}

	private class TimeOutTask extends TimerTask {
		TradeInventory inventory;

		public TimeOutTask(final TradeInventory inventory) {
			this.inventory = inventory;
		}

		@Override
		public void run() {
			TradeInventory.pendingRequests.remove(this.inventory.players[1]);
			this.inventory.players[0]
					.sendMessage(("§cLa demande d'échange envoyé à " + TradeInventory.this.players[1].getName() + " a expiré"));
			this.inventory.players[1]
					.sendMessage("§cLa demande d'échange de " + TradeInventory.this.players[0].getName() + " a expiré");
		}
	}
}
