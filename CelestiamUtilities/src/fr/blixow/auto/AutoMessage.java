package fr.blixow.auto;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import fr.blixow.plugin.Core;

public class AutoMessage {

	List<String> messageList = Arrays.asList(new String[] {
			"§8§m--------------------------------------------§e\n \nPour signaler un tricheur ou une insulte, \nutilisez la commande /report\n \n§cAttention, \ntout abus de cette commande sera sanctionné \n \n§8§m--------------------------------------------",
			"§8§m--------------------------------------------§b\n \nRejoignez notre discord pour être au courrant des\nnouveautés et de l'actualité du serveur :\n \n§bhttps://discord.gg/ebYbTKF  \n \n§8§m--------------------------------------------",
			"§8§m--------------------------------------------§b\n \n§eVous pouvez proposer vos idées dans \nle channel §6suggestion §edu discord\n \n§8§m--------------------------------------------" }
					.clone());
	private int i = 0;

	public AutoMessage messageLaunch() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (messageList.isEmpty()) {
					cancel();
					return;
				}
				if (i >= messageList.size()) {
					i = 0;
				}

				Bukkit.broadcastMessage(messageList.get(i));
				i++;
			}
		}.runTaskTimer(Core.getInstance(), 10 * 20L, 360 * 20L);
		return null;
	}

}
