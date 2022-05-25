package de.floxyi.devtools.commands;

import de.floxyi.devtools.Devtools;
import de.floxyi.devtools.utils.ItemAnalyzer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
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

        if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You don't have any item in your main hand!");
            return false;
        }

        ItemAnalyzer itemAnalyzer = new ItemAnalyzer(player.getInventory().getItemInMainHand());

        itemAnalyzer.sendItemInfo(player);
        File codeFile = itemAnalyzer.generateItemCodeFile();

        if(codeFile == null) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "Code file cannot be created!");
            return false;
        }

        sendPathMessage(player, codeFile);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private void sendPathMessage(Player player, File file) {
        String path = System.getProperty("user.dir") + "\\" + file.getPath();

        TextComponent prefix = new TextComponent(Devtools.getPrefix());
        TextComponent message = new TextComponent(ChatColor.GREEN + " Click to get the path to the code file!");
        TextComponent copy = new TextComponent("[" + ChatColor.AQUA + "Copy path" + ChatColor.GRAY + "]" + ChatColor.RESET);
        copy.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, path));
        copy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("If you are running a local test server, you can click to copy the file path!")));

        TextComponent combinedMessage = new TextComponent(prefix, copy, message);
        player.spigot().sendMessage(combinedMessage);
    }
}