package de.floxyi.devtools.updatechecker;

import de.floxyi.devtools.Devtools;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

public class UpdateChecker implements Runnable {
    private static boolean upToDate = false;
    private static String latest;

    public void run() {
        InputStream in;
        try {
            in = new URL("https://raw.githubusercontent.com/Floxyi/DevTools/master/pom.xml").openStream();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, Devtools.getPrefix() + "Unable to check for plugin updates!");
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(in);
        String inLine = "";
        while (scanner.hasNext() && inLine.isBlank()) {
            if(scanner.findInLine("<version>") != null) {
                inLine = scanner.nextLine();
            }
            scanner.nextLine();
        }
        scanner.close();

        latest = inLine.substring(0, inLine.length() - 10);
        Bukkit.getLogger().log(Level.WARNING, Devtools.getPrefix() + "Latest plugin version is " + latest + ".");

        upToDate = getClass().getPackage().getImplementationVersion().equals(latest);
        if (upToDate) {
            Bukkit.getLogger().log(Level.WARNING, Devtools.getPrefix() + "Plugin is on the newest version!");
        } else {
            Bukkit.getLogger().log(Level.WARNING, Devtools.getPrefix() + "Plugin is out of date (" + getCurrentVersion() + "/" + latest + ")! Please update from: https://www.spigotmc.org/resources/devtools.96876/");
        }
    }

    public boolean isNewestVersion() {
        return upToDate;
    }

    public String getLatestVersion() {
        return latest;
    }

    public String getCurrentVersion() {
        return getClass().getPackage().getImplementationVersion();
    }
}
