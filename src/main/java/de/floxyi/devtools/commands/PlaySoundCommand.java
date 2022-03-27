package de.floxyi.devtools.commands;

import de.floxyi.devtools.Devtools;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class PlaySoundCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(args.length >= 1) {
                String sound = args[0].toUpperCase(Locale.ROOT);
                boolean played = false;
                for (Sound soundList : Sound.values()) {
                    if(soundList.toString().equalsIgnoreCase(sound)) {
                        if(args.length == 2) {
                            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to set 2 numbers (floats) after the sound for volume and pitch!");
                        } else if (args.length > 1 && args.length < 4) {
                            float volume = Float.parseFloat(args[1]);
                            float pitch = Float.parseFloat(args[2]);
                            player.playSound(player.getLocation(), soundList, volume, pitch);
                        } else {
                            player.playSound(player.getLocation(), soundList, 1f, 1f);
                        }
                        played = true;
                        player.sendMessage(Devtools.getPrefix() + ChatColor.AQUA + "The sound " + soundList + " was played!");
                    }
                }
                if(!played) {
                    player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "This sound does not exist!");
                }
            } else {
                player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to set one valid argument (SoundName)!");
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
            Sound[] sounds = Sound.values();
            for (Sound sound : sounds) {
                arguments.add(sound.toString());
            }
            return arguments;
        }
        return Collections.emptyList();
    }
}
