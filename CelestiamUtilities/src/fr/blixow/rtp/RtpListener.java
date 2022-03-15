package fr.blixow.rtp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import fr.blixow.utils.Registers;

public class RtpListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if ((Registers.getRtpManager().getIsTeleporting().contains(event.getPlayer().getName()))
				&& ((event.getFrom().getX() != event.getTo().getX()))) {
			event.getPlayer().sendMessage("�cT�l�portation annul�e.");
			Registers.getRtpManager().getIsTeleporting().remove(event.getPlayer().getName());
		}
	}
}
