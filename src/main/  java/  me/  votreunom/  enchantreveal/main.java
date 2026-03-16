package me.votreunom.enchantreveal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    private final Map<String, String> frenchNames = new HashMap<>();

    @Override
    public void onEnable() {
        loadTranslations();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("RevealEnchant (FR) activé !");
    }

    private void loadTranslations() {
        // Armures
        frenchNames.put("PROTECTION_ENVIRONMENTAL", "Protection");
        frenchNames.put("PROTECTION_FIRE", "Protection contre le feu");
        frenchNames.put("PROTECTION_FALL", "Chute amortie");
        frenchNames.put("PROTECTION_EXPLOSIONS", "Protection contre les explosions");
        frenchNames.put("PROTECTION_PROJECTILE", "Protection contre les projectiles");
        frenchNames.put("OXYGEN", "Apnée");
        frenchNames.put("WATER_WORKER", "Affinité aquatique");
        frenchNames.put("THORNS", "Épines");
        frenchNames.put("DEPTH_STRIDER", "Agilité aquatique");
        frenchNames.put("FROST_WALKER", "Semelles de givre");

        // Épées / Outils
        frenchNames.put("DAMAGE_ALL", "Tranchant");
        frenchNames.put("DAMAGE_UNDEAD", "Châtiment");
        frenchNames.put("DAMAGE_ARTHROPODS", "Fléau des arthropodes");
        frenchNames.put("KNOCKBACK", "Recul");
        frenchNames.put("FIRE_ASPECT", "Aura de feu");
        frenchNames.put("LOOT_BONUS_MOBS", "Butin");
        frenchNames.put("DIG_SPEED", "Efficacité");
        frenchNames.put("SILK_TOUCH", "Toucher de soie");
        frenchNames.put("DURABILITY", "Solidité");
        frenchNames.put("LOOT_BONUS_BLOCKS", "Fortune");

        // Arcs
        frenchNames.put("ARROW_DAMAGE", "Puissance");
        frenchNames.put("ARROW_KNOCKBACK", "Frappe");
        frenchNames.put("ARROW_FIRE", "Flamme");
        frenchNames.put("ARROW_INFINITE", "Infinité");

        // Divers
        frenchNames.put("LUCK", "Chance de la mer");
        frenchNames.put("LURE", "Appât");
        frenchNames.put("MENDING", "Ravaudage");
        frenchNames.put("VANISHING_CURSE", "Malédiction de disparition");
        frenchNames.put("BINDING_CURSE", "Malédiction du lien");
    }

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        // On attend 1 tick pour que le serveur génère les offres
        Bukkit.getScheduler().runTaskLater(this, () -> {
            EnchantmentOffer[] offers = event.getOffers();
            if (offers == null || offers[0] == null) return;

            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();

            lore.add(""); 
            lore.add(ChatColor.GOLD + "✨ PRÉDICTIONS :");
            
            for (int i = 0; i < offers.length; i++) {
                if (offers[i] != null) {
                    String rawName = offers[i].getEnchantment().getName();
                    String displayName = frenchNames.getOrDefault(rawName, rawName);
                    int lvl = offers[i].getEnchantmentLevel();
                    int cost = offers[i].getCost();
                    
                    lore.add(ChatColor.GRAY + " Slot " + (i + 1) + " (" + cost + " LVL): " 
                             + ChatColor.AQUA + displayName + " " + lvl);
                }
            }
            lore.add("");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }, 1L);
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        // Supprime le texte une fois l'objet enchanté
        ItemMeta meta = event.getItem().getItemMeta();
        meta.setLore(null);
        event.getItem().setItemMeta(meta);
    }
}
