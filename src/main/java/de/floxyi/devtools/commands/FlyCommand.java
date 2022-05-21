package de.floxyi.devtools.commands;

import de.floxyi.devtools.Devtools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to be a Player in order to " +
                    "execute this command!");
            return false;
        }

        if(args.length > 1) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You can only set one argument " +
                    ChatColor.GOLD + "/fly <\"float speed (0.0 - 1.0\">");
            return false;
        }

        if(args.length == 1) {
            float speed = Float.parseFloat(args[0]);

            if(speed > 1) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You can't set your flying speed above " + ChatColor.GOLD + " 1" + ChatColor.RED + " speed !");
                return false;
            }

            if(!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }

            player.setFlySpeed(speed);
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "You enabled flying in " + ChatColor.GOLD + speed + ChatColor.GREEN + " speed !");

            return true;
        }


        if(player.getGameMode() == GameMode.SURVIVAL) {
            player.setAllowFlight(!player.getAllowFlight());
            player.setFlySpeed(0.1f);

            if(player.getAllowFlight()) {
                player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "You enabled flying in " + ChatColor.GOLD + "0.1" + ChatColor.GREEN + " speed !");
            } else {
                player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "You disabled flying!");
            }
            return true;
        }

        player.setFlySpeed(0.1f);
        player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "You set your flying speed to " + ChatColor.GOLD + "0.1" + ChatColor.GREEN + "!");



        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> arg = new ArrayList<>();

        if (args.length == 1) {
            for (int i = 1; i < 10; i++) {
                arg.add("0." + i);
            }
            arg.add("1");
        }

        return arg;
    }
}
