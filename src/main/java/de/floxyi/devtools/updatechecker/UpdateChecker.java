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
        upToDate = compareVersion(getClass().getPackage().getImplementationVersion(), latest);

        if(upToDate) {
            Bukkit.getLogger().log(Level.INFO, Devtools.getPrefix() + "You are running on the newest version!");
        } else {
            Bukkit.getLogger().log(Level.WARNING, Devtools.getPrefix() + "Plugin is out of date (" + getCurrentVersion() + "/" + latest + ")! Please update from: https://www.spigotmc.org/resources/devtools.96876/");
        }
    }

    private boolean compareVersion(String version, String newest) {
        version = version.replace(".", "");
        newest = newest.replace(".", "");
        int versionNumber = Integer.parseInt(version);
        int newestNumber = Integer.parseInt(newest);

        return versionNumber >= newestNumber;
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
