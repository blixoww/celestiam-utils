package fr.blixow.deathmessage;

public enum EnchantName {
    DAMAGE_ALL("Sharpness"), 
    DURABILITY("Unbreaking"), 
    LOOT_BONUS_MOBS("Looting"), 
    DIG_SPEED("Efficiency"), 
    DAMAGE_UNDEAD("Smite"), 
    DAMAGE_ARTHROPODS("Bane Of Arthropods"), 
    KNOCKBACK("Knockback"), 
    FIRE_ASPECT("Fire Aspect");
    
    private String name;
    
    private EnchantName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

}
