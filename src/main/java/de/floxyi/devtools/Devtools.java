package de.floxyi.devtools;

import de.floxyi.devtools.commands.LocationCommand;
import de.floxyi.devtools.commands.PlaySoundCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Devtools extends JavaPlugin {

    @Override
    public void onEnable() {

        int pluginId = 13046;
        Metrics metrics = new Metrics(this, pluginId);

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
        return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "DevTools" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    private void commandRegistration() {
        Objects.requireNonNull(getCommand("loc")).setExecutor(new LocationCommand());
        Objects.requireNonNull(getCommand("sound")).setExecutor(new PlaySoundCommand());
    }
}
