package de.floxyi.devtools.commands;

import de.floxyi.devtools.Devtools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MaterialCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to be a player in order to execute this command!");
            return false;
        }

        if(args.length != 0) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You can't specify any arguments, please just use" + ChatColor.GOLD + "/material" + ChatColor.RED + "!");
            return false;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta == null) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You don't have any item in your main hand!");
            return false;
        }

        sendItemInfo(player, itemStack, itemMeta);

        File codeFile = generateCodeFile(itemStack, itemMeta);
        if(codeFile == null) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "Code file cannot be created!");
            return false;
        }

        player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + ChatColor.GREEN + "Successfully created code file in the plugin's folder!");
        sendPathMessage(player, codeFile);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private File generateCodeFile(ItemStack itemStack, ItemMeta itemMeta) {
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

                Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    String enchantment = enchantmentTranslator(entry.getKey().getKey().getKey().toUpperCase(Locale.ROOT));
                    myWriter.write("itemMeta.addEnchant(Enchantment." + enchantment + ", " + entry.getValue().toString() + ", false);\n");
                }

                myWriter.write("itemStack.setItemMeta(itemMeta);\n");
            }

            myWriter.close();

            return file;
        } catch (IOException e) {
            return null;
        }
    }

    private void sendPathMessage(Player player, File file) {
        String path = System.getProperty("user.dir") + "\\" + file.getPath();

        TextComponent prefix = new TextComponent(Devtools.getPrefix());
        TextComponent message = new TextComponent(ChatColor.GREEN + " Click to get the local file path!");
        TextComponent copy = new TextComponent("[" + ChatColor.AQUA + "copy path to clipboard" + ChatColor.GRAY + "]" + ChatColor.RESET);
        copy.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, path));
        copy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("If you are running a local testing server you can click to copy the file path!")));

        TextComponent combinedMessage = new TextComponent(prefix, copy, message);
        player.spigot().sendMessage(combinedMessage);
    }

    private String enchantmentTranslator(String enchantment) {
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

    private void sendItemInfo(Player player, ItemStack itemStack, ItemMeta itemMeta) {
        player.sendMessage(Devtools.getPrefix() + ChatColor.AQUA + generateItemName(itemStack.getType()) + ChatColor.GRAY + " (detected properties):");

        if(itemMeta.hasDisplayName()) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's custom name: " + ChatColor.GOLD + itemMeta.getDisplayName());
        }

        player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's material type: " + ChatColor.GOLD + itemStack.getType());

        player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's amount: " + ChatColor.GOLD + itemStack.getAmount() + "/" + itemStack.getMaxStackSize());

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

        if(itemMeta.hasLore()) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The item's lore: " + ChatColor.GOLD + itemMeta.getLore());
        }
    }

    private String generateItemName(Material material) {
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
}