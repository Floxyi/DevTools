package de.floxyi.devtools;

import de.floxyi.devtools.commands.FlyCommand;
import de.floxyi.devtools.commands.LocationCommand;
import de.floxyi.devtools.commands.MaterialCommand;
import de.floxyi.devtools.commands.SoundCommand;
import de.floxyi.devtools.updatechecker.JoinListener;
import de.floxyi.devtools.updatechecker.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Devtools extends JavaPlugin {

    private static Devtools plugin;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {

        new Metrics(this, 13046);

        plugin = this;
        updateChecker = new UpdateChecker();

        Bukkit.getLogger().info(getPrefix() + "");
        Bukkit.getLogger().info(getPrefix() + " _____          _______          _     ");
        Bukkit.getLogger().info(getPrefix() + "|  __ \\        |__   __|        | |    ");
        Bukkit.getLogger().info(getPrefix() + "| |  | | _____   _| | ___   ___ | |___ ");
        Bukkit.getLogger().info(getPrefix() + "| |  | |/ _ \\ \\ / / |/ _ \\ / _ \\| / __|");
        Bukkit.getLogger().info(getPrefix() + "| |__| |  __/\\ V /| | (_) | (_) | \\__ \\");
        Bukkit.getLogger().info(getPrefix() + "|_____/ \\___| \\_/ |_|\\___/ \\___/|_|___/");
        Bukkit.getLogger().info(getPrefix() + "");
        Bukkit.getLogger().info(getPrefix() + "                             by Floxyii");
        Bukkit.getLogger().info(getPrefix() + "");

        Thread updateCheck = new Thread(updateChecker);
        updateCheck.start();

        Bukkit.getLogger().info(getPrefix() + "Plugin is getting activated!");

        commandRegistration();
        listenerRegistration();
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
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("material")).setExecutor(new MaterialCommand());
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(), this);
    }

    public static Devtools getPlugin() {
        return plugin;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}
