package de.floxyi.devtools;

import de.floxyi.devtools.commands.LocationCommand;
import de.floxyi.devtools.commands.SoundCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Devtools extends JavaPlugin {

    @Override
    public void onEnable() {

        new Metrics(this, 13046);

        Bukkit.getLogger().info(getPrefix() + " _____          _______          _     ");
        Bukkit.getLogger().info(getPrefix() + "|  __ \\        |__   __|        | |    ");
        Bukkit.getLogger().info(getPrefix() + "| |  | | _____   _| | ___   ___ | |___ ");
        Bukkit.getLogger().info(getPrefix() + "| |  | |/ _ \\ \\ / / |/ _ \\ / _ \\| / __|");
        Bukkit.getLogger().info(getPrefix() + "| |__| |  __/\\ V /| | (_) | (_) | \\__ \\");
        Bukkit.getLogger().info(getPrefix() + "|_____/ \\___| \\_/ |_|\\___/ \\___/|_|___/");
        Bukkit.getLogger().info(getPrefix() + "");
        Bukkit.getLogger().info(getPrefix() + "                             by Floxyii");
        Bukkit.getLogger().info(getPrefix() + "");
        Bukkit.getLogger().info(getPrefix() + "Plugin is getting activated!");

        commandRegistration();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(getPrefix() + "Plugin is getting deactivated!");
    }

    public static String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + "DevTools" + ChatColor.GRAY + "] " + ChatColor.GRAY;
    }

    private void commandRegistration() {
        Objects.requireNonNull(getCommand("location")).setExecutor(new LocationCommand());
        Objects.requireNonNull(getCommand("sound")).setExecutor(new SoundCommand());
    }
}
