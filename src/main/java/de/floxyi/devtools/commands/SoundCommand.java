package de.floxyi.devtools.commands;

import de.floxyi.devtools.Devtools;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SoundCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to be a Player in order to " +
                    "execute this command!");
            return false;
        }

        if(args.length == 0 || args.length > 3 || args.length == 2) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to set valid arguments " +
                    ChatColor.GOLD + "/sound \"sound name\" <volume pitch>");
            return false;
        }

        String soundArg = args[0].toUpperCase(Locale.ROOT);
        Sound sound = null;
        for(Sound soundList : Sound.values()) {
            if(soundList.name().equalsIgnoreCase(soundArg)) {
                sound = soundList;
            }
        }

        if(sound == null) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "Entered sound (" +
                    ChatColor.GOLD + args[0] + ChatColor.RED + ") does not exist!");
            return false;
        }

        if(args.length > 1) {
            if(!args[1].matches("\\d+(\\.\\d+)?")) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "Entered volume (" +
                        ChatColor.GOLD + args[1] + ChatColor.RED + ") is not a number!");
                return false;
            }
            float volume = Float.parseFloat(args[1]);

            if(!args[2].matches("\\d+(\\.\\d+)?")) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "Entered pitch (" +
                        ChatColor.GOLD + args[2] + ChatColor.RED + ") is not a number!");
                return false;
            }
            float pitch = Float.parseFloat(args[2]);

            player.playSound(player.getLocation(), sound, volume, pitch);
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The sound " + ChatColor.GOLD +
                    sound.name() + ChatColor.GREEN + " was played in volume " + ChatColor.GOLD + volume +
                    ChatColor.GREEN + " and in pitch " + ChatColor.GOLD + pitch + ChatColor.GREEN + "!");
            return true;
        }

        player.playSound(player.getLocation(), sound, 1f, 1f);
        player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "The sound " + ChatColor.GOLD +
                sound.name() + ChatColor.GREEN + " was played in normal volume and pitch!");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            Sound[] sounds = Sound.values();
            for (Sound sound : sounds) {
                arguments.add(sound.name()); //.toString()
            }
            return arguments;
        }
        return Collections.emptyList();
    }
}
