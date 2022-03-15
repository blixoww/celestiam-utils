package fr.blixow.plugin;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import fr.blixow.auto.AutoMessage;
import fr.blixow.cobble.CobbleEvent;
import fr.blixow.deathmessage.CustomDeathMessage;
import fr.blixow.dons.Dons;
import fr.blixow.ks.KillStreaks;
import fr.blixow.repair.RepairManager;
import fr.blixow.reward.RewardManager;
import fr.blixow.rtp.RtpListener;
import fr.blixow.rtp.RtpManager;
import fr.blixow.utils.CommandManager;
import fr.blixow.utils.Registers;
import fr.blixow.utils.Utilities;
import fr.blixow.xp.XpEvent;
import net.milkbowl.vault.economy.Economy;

public class Core extends JavaPlugin {

	public static Core instance;
	public File donationFolder;
	private CommandManager cm;
	
	public static Core getInstance() {
		return instance;
	}
	
	public CommandManager getCommandManager() {
		return cm;
	}

	public void onEnable() {
        getServer().getConsoleSender().sendMessage("§7§m--------------------------");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage("§aLancement de CelestiamUtilities");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage("§7Createur: §ablixow14 ");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage("§7Version: §a1.5");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage("§7§m--------------------------");
		if (!setupEconomy()) {
			getLogger().severe(
					String.format("A problem is occurred with VAULT", new Object[] { getDescription().getName() }));
			return;
		}
		instance = this;
		this.cm = new CommandManager(this);
		this.registerEvents();
		this.registerOther();
		this.getDonationFolder();
		this.saveConfig();
	}


	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new RtpListener(), this);
		pm.registerEvents(new KillStreaks(this), this);
		pm.registerEvents(new Dons(this), this);
		pm.registerEvents(new EnderpearlCooldown(), this);
		pm.registerEvents(new VariousListener(), this);
		pm.registerEvents(new CustomDeathMessage(), this);
		pm.registerEvents(new CobbleEvent(), this);
		pm.registerEvents(new XpEvent(), this);
		pm.registerEvents(new GiveAll(this), this);
	}

	public void registerOther() {
		Registers.rtpManager = new RtpManager();
		Registers.repairManager = new RepairManager();
		Registers.rewardManager = new RewardManager();
		Registers.autoMessage = new AutoMessage().messageLaunch();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		Utilities.economy = (Economy) rsp.getProvider();
		return Utilities.economy != null;
	}

	public void getDonationFolder() {
		this.donationFolder = new File(getDataFolder(), "donations");
		if (!this.donationFolder.exists()) {
			this.donationFolder.mkdirs();
		}
	}
	
}
