package de.floxyi.devtools.utils;

import de.floxyi.devtools.Devtools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ItemAnalyzer {

    public String generateItemName(Material material) {
        String materialName = material.toString();
        materialName = materialName.toLowerCase();

        String itemName = "";
        String[] materialNameParts = materialName.split("_");

        for (String materialNamePart : materialNameParts) {
            String upperNamePart = materialNamePart.substring(0, 1).toUpperCase() + materialNamePart.substring(1);
            itemName = itemName.concat(upperNamePart + " ");
        }

        itemName = itemName.substring(0, itemName.length() - 1);

        return itemName;
    }

    public String enchantmentTranslator(String enchantment) {
        if(enchantment.equals("FLAME")) enchantment = "ARROW_FIRE";
        if(enchantment.equals("POWER")) enchantment = "ARROW_DAMAGE";
        if(enchantment.equals("RESPIRATION")) enchantment = "OXYGEN";
        if(enchantment.equals("SMITE")) enchantment = "DAMAGE_UNDEAD";
        if(enchantment.equals("SHARPNESS")) enchantment = "DAMAGE_ALL";
        if(enchantment.equals("EFFICIENCY")) enchantment = "DIG_SPEED";
        if(enchantment.equals("PUNCH")) enchantment = "ARROW_KNOCKBACK";
        if(enchantment.equals("UNBREAKING")) enchantment = "DURABILITY";
        if(enchantment.equals("FORTUNE")) enchantment = "LOOT_BONUS_MOBS";
        if(enchantment.equals("INFINITY")) enchantment = "ARROW_INFINITE";
        if(enchantment.equals("LOOTING")) enchantment = "LOOT_BONUS_BLOCKS";
        if(enchantment.equals("AQUA_AFFINITY")) enchantment = "WATER_WORKER";
        if(enchantment.equals("FEATHER_FALLING")) enchantment = "PROTECTION_FALL";
        if(enchantment.equals("FIRE_PROTECTION")) enchantment = "PROTECTION_FIRE";
        if(enchantment.equals("PROTECTION")) enchantment = "PROTECTION_ENVIRONMENTAL";
        if(enchantment.equals("BANE_OF_ARTHROPODS")) enchantment = "DAMAGE_ARTHROPODS";
        if(enchantment.equals("BLAST_PROTECTION")) enchantment = "PROTECTION_EXPLOSIONS";
        if(enchantment.equals("PROJECTILE_PROTECTION")) enchantment = "PROTECTION_PROJECTILE";
        return enchantment;
    }

    public void sendItemInfo(Player player, ItemStack itemStack, ItemMeta itemMeta) {
        player.sendMessage(Devtools.getPrefix() + ChatColor.AQUA + generateItemName(itemStack.getType()) + ChatColor.GRAY + " (supported properties):");

        if(itemMeta.hasDisplayName()) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's custom name: " + ChatColor.GOLD + itemMeta.getDisplayName());
        }

        player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's material type: " + ChatColor.GOLD + itemStack.getType());

        if(itemStack.getMaxStackSize() != 1) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's amount: " + ChatColor.GOLD + itemStack.getAmount() + "/" + itemStack.getMaxStackSize());
        }

        if(itemMeta.hasLore()) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's lore: " + ChatColor.GOLD + itemMeta.getLore());
        }

        Damageable damageable = (Damageable) itemMeta;
        if(damageable.getDamage() != 0) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's durability: " + ChatColor.GOLD + (itemStack.getType().getMaxDurability() - damageable.getDamage()) + "/" + itemStack.getType().getMaxDurability());
        }

        if(itemMeta.hasEnchants()) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's enchantments:");

            Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                String enchantmentName = entry.getKey().getKey().getKey();
                enchantmentName = enchantmentName.substring(0, 1).toUpperCase() + enchantmentName.substring(1);

                player.sendMessage(Devtools.getPrefix() + ChatColor.GRAY + "  - " + ChatColor.GREEN + enchantmentName + ": " + ChatColor.GOLD + entry.getValue().toString());
            }
        }

        if(itemStack.getType() == Material.WRITTEN_BOOK || itemStack.getType() == Material.WRITABLE_BOOK) {
            BookMeta bookMeta = (BookMeta) itemMeta;

            if(bookMeta.hasTitle()) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The book's title: " + ChatColor.GOLD + bookMeta.getTitle());
            }
            if(bookMeta.hasAuthor()) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The book's author: " + ChatColor.GOLD + bookMeta.getAuthor());
            }
            if(bookMeta.hasPages()) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The book's pages: " + ChatColor.GOLD + bookMeta.getPageCount());
            }
            if(bookMeta.hasGeneration()) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The book's generation: " + ChatColor.GOLD + Objects.requireNonNull(bookMeta.getGeneration()));
            }
        }
    }

    public File generateItemCodeFile(ItemStack itemStack, ItemMeta itemMeta) {
        File dir = new File(Devtools.getPlugin().getDataFolder() + "\\itemCode");
        if(!dir.exists() && !dir.mkdirs()) {
            return null;
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssL-SSS");

        String fileName = generateItemName(itemStack.getType()) + " " + sdf.format(date) + ".txt";
        File file = new File(dir.getPath(), fileName);

        try {
            if(!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            boolean declaredItemMeta = false;

            FileWriter myWriter = new FileWriter(file.getPath());

            myWriter.write("ItemStack itemStack = new ItemStack(Material." + itemStack.getType() + ");\n");
            myWriter.write("itemStack.setAmount(" + itemStack.getAmount() + ");\n");

            if(itemMeta.hasDisplayName()) {
                myWriter.write("itemStack.getItemMeta().setDisplayName(" + itemMeta.getDisplayName() + ");\n");
            }

            if(itemMeta.hasLore()) {
                myWriter.write("itemStack.getItemMeta().setLore(" + itemMeta.getLore() + ");\n");
            }

            Damageable damageable = (Damageable) itemMeta;
            if(damageable.getDamage() != 0) {
                myWriter.write("Damageable damageable = (Damageable) itemStack.getItemMeta();\n");
                myWriter.write("damageable.setDamage(" + damageable.getDamage() + ");\n");
                myWriter.write("itemStack.setItemMeta(damageable);\n");
            }

            if(itemMeta.hasEnchants()) {
                myWriter.write("ItemMeta itemMeta = itemStack.getItemMeta();\n");
                declaredItemMeta = true;

                Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    String enchantment = enchantmentTranslator(entry.getKey().getKey().getKey().toUpperCase(Locale.ROOT));
                    myWriter.write("itemMeta.addEnchant(Enchantment." + enchantment + ", " + entry.getValue().toString() + ", false);\n");
                }

                myWriter.write("itemStack.setItemMeta(itemMeta);\n");
            }

            if(itemStack.getType() == Material.WRITTEN_BOOK || itemStack.getType() == Material.WRITABLE_BOOK) {
                BookMeta bookMeta = (BookMeta) itemMeta;

                if(declaredItemMeta) {
                    myWriter.write("BookMeta bookMeta = (BookMeta) itemMeta;\n");
                } else {
                    myWriter.write("BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();\n");
                }

                if(bookMeta.hasTitle()) {
                    myWriter.write("bookMeta.setTitle(\"" + bookMeta.getTitle() + "\");\n");
                }

                if(bookMeta.hasAuthor()) {
                    myWriter.write("bookMeta.setAuthor(\"" + bookMeta.getAuthor() + "\");\n");
                }

                if(bookMeta.hasPages()) {
                    myWriter.write("ArrayList<String> pages = new ArrayList<>();\n");

                    for(int i = 1; i <= bookMeta.getPageCount(); i++) {
                        myWriter.write("pages.add(\"" + bookMeta.getPage(i) + "\");\n");
                    }

                    myWriter.write("bookMeta.setPages(pages);\n");
                }

                if(bookMeta.hasGeneration()) {
                    myWriter.write("bookMeta.setGeneration(" +  bookMeta.getGeneration() +"));\n");
                }

                myWriter.write("itemStack.setItemMeta(bookMeta);\n");
            }

            myWriter.close();
            return file;
        } catch (IOException e) {
            return null;
        }
    }
}
