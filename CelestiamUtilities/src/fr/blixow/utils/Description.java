package fr.blixow.utils;

public enum Description {

	HELP("Permet d'avoir le descriptif des commandes"),
	DISCORD("Permet d'afficher le lien du discord"),
	SITE("Permet d'afficher le lien du site"),
	FURNACE("Permet de faire cuire des items dans la main"),
	LOT("Permet de tirer au hasard un joueur"),
	MSG("Permet d'envoyer un message à un joueur"),
	POUBELLE("Permet de se débarasser d'items"),
	RESPOND("Permet de répondre à un message"),
	COBBLE("Permet de ne plus récupérer la cobble"),
	DONS("Permet de faire un don à un joueur"),
	KILL("Permet d'afficher ses statistiques"),
	GIVE("Permet de give à tous les joueurs"),
	REPAIR("Permet de réparer son armure"),
	REWARD("Permet de recevoir de l'argent"),
	RTP("Permet de se rtp"),
	TRADE("Permet d'échanger des items"),
	ACCEPT("Permet d'accepter un échange"),
	XP("Permet de stocker l'xp");
	
	
	
	
	private String message;

	Description(String message) {
		this.message = message;
	}

	public String getDescriptionCommand() {
		return this.message;
	}
	
}
