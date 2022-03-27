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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            String world = '"' + player.getWorld().getName() + '"';

            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("block")) {
                    Block target = player.getTargetBlock(null, 5);
                    Location loc = target.getLocation();

                    Bukkit.getLogger().info(Devtools.getPrefix() + "The location of " + target.getBlockData().getMaterial() + " is: new Location(" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                    player.sendMessage(Devtools.getPrefix() + ChatColor.AQUA + "The location of the block (" + target.getBlockData().getMaterial() + ") you are looking at was send to the console!");
                    sendCopyMessage(player, "new Location(Bukkit.getWorld(" + world + "), " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                } else if (args[0].equalsIgnoreCase("view")) {
                    Location loc = player.getLocation();
                    Bukkit.getLogger().info(Devtools.getPrefix() + "The location and viewpoint of " + player.getName() + " is: new Location(" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch() + ")");
                    player.sendMessage(Devtools.getPrefix() + ChatColor.AQUA + "The location and viewpoint of your player (" + player.getName() + ") was send to the console!");
                    sendCopyMessage(player, "new Location(Bukkit.getWorld(" + world + "), " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch() + ")");
                } else {
                    sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to set a valid argument (block / view)!");
                }
            } else if(args.length == 0) {
                Location loc = player.getLocation();
                Bukkit.getLogger().info(Devtools.getPrefix() + "The location of " + player.getName() + " is: new Location(" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                player.sendMessage(Devtools.getPrefix() + ChatColor.AQUA + "The location of your player (" + player.getName() + ") was send to the console!");
                sendCopyMessage(player, "new Location(Bukkit.getWorld(" + world + "), " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
            } else {
                player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to set only one valid argument (block / view)!");
            }
        } else {
            sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to be a Player in order to execute this command!");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("block");
            arguments.add("view");
            return arguments;
        }
        return Collections.emptyList();
    }

    public void sendCopyMessage(Player player, String output) {
        TextComponent message = new TextComponent(Devtools.getPrefix() + "[" + ChatColor.BLUE + "Copy" + ChatColor.GRAY + "]" + ChatColor.RESET + "Click to Copy your location to the chat!");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, output));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Get the location in your chat to copy it!")));
        message.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        message.setBold(true);
        player.spigot().sendMessage(message);
    }
}
