package fr.blixow.utils;

public enum Description {

	HELP("Permet d'avoir le descriptif des commandes"),
	DISCORD("Permet d'afficher le lien du discord"),
	SITE("Permet d'afficher le lien du site"),
	FURNACE("Permet de faire cuire des items dans la main"),
	LOT("Permet de tirer au hasard un joueur"),
	MSG("Permet d'envoyer un message � un joueur"),
	POUBELLE("Permet de se d�barasser d'items"),
	RESPOND("Permet de r�pondre � un message"),
	COBBLE("Permet de ne plus r�cup�rer la cobble"),
	DONS("Permet de faire un don � un joueur"),
	KILL("Permet d'afficher ses statistiques"),
	GIVE("Permet de give � tous les joueurs"),
	REPAIR("Permet de r�parer son armure"),
	REWARD("Permet de recevoir de l'argent"),
	RTP("Permet de se rtp"),
	TRADE("Permet d'�changer des items"),
	ACCEPT("Permet d'accepter un �change"),
	XP("Permet de stocker l'xp");
	
	
	
	
	private String message;

	Description(String message) {
		this.message = message;
	}

	public String getDescriptionCommand() {
		return this.message;
	}
	
}
