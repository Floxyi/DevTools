package de.floxyi.devtools.commands;

import de.floxyi.devtools.Devtools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to be a Player in order to execute this command!");
            return false;
        }

        if(args.length == 0 || args.length > 2) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to set valid arguments " + ChatColor.GOLD + "/location <player \"player name\"> <block \"player name\"> <view \"player name\">");
            return false;
        }

        String world = '"' + player.getWorld().getName() + '"';

        Player locPlayer;
        if(args.length == 1) {
            locPlayer = player;
        } else {
            Player argPlayer = null;
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getDisplayName().equals(args[1])) {
                    argPlayer = onlinePlayer;
                }
            }
            if(argPlayer == null) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "The specified player (" + ChatColor.GOLD + args[1] + ChatColor.RED + ") is not online!");
                return false;
            }
            locPlayer = argPlayer;
        }


        if(args[0].equalsIgnoreCase("player")) {
            Location loc = locPlayer.getLocation();
            Bukkit.getLogger().info(Devtools.getPrefix() + "The location of " + locPlayer.getName() + " is: new Location(" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The location of the player (" + ChatColor.GOLD + player.getName() + ChatColor.GREEN + ") is in the console!");
            sendCopyMessage(player, "new Location(Bukkit.getWorld(" + world + "), " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
            return true;
        }

        if(args[0].equalsIgnoreCase("block")) {
            Block target = locPlayer.getTargetBlock(null, 5);
            Location loc = target.getLocation();
            Bukkit.getLogger().info(Devtools.getPrefix() + "The location of " + target.getBlockData().getMaterial() + " is: new Location(" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The location of the block (" + ChatColor.GOLD + target.getBlockData().getMaterial() + ChatColor.GREEN + ") is in the console!");
            sendCopyMessage(player, "new Location(Bukkit.getWorld(" + world + "), " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
            return true;
        }

        if (args[0].equalsIgnoreCase("view")) {
            Location loc = locPlayer.getLocation();
            Bukkit.getLogger().info(Devtools.getPrefix() + "The location and viewpoint of " + player.getName() + " is: new Location(" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch() + ")");
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The location and viewpoint of the player (" + ChatColor.GOLD + player.getName() + ChatColor.GREEN + ") is in the console!");
            sendCopyMessage(player, "new Location(Bukkit.getWorld(" + world + "), " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch() + ")");
            return true;
        }

        player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "Entered arguments aren't existing!");
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("block");
            arguments.add("player");
            arguments.add("view");
            return arguments;
        }

        if(args.length == 2) {
            List<String> players = new ArrayList<>();
            for(Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getDisplayName());
            }
            return players;
        }

        return Collections.emptyList();
    }

    public void sendCopyMessage(Player player, String output) {
        TextComponent message = new TextComponent(Devtools.getPrefix() + "[" + ChatColor.AQUA + "Copy" + ChatColor.GRAY + "]" + ChatColor.RESET + ChatColor.GREEN + " Click to copy the location to the chat!");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, output));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Put location in the chat (STRG + A & STRG + C)")));
        player.spigot().sendMessage(message);
    }
}
