package de.floxyi.devtools.updatechecker;

import de.floxyi.devtools.Devtools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(event.getPlayer().isOp()&& !Devtools.getPlugin().getUpdateChecker().isNewestVersion()) {
            String text = ChatColor.GREEN +
                    "Plugin is out of date! " + ChatColor.GOLD +
                    Devtools.getPlugin().getUpdateChecker().getCurrentVersion() +
                    ChatColor.GREEN + " / " + ChatColor.GOLD +
                    Devtools.getPlugin().getUpdateChecker().getLatestVersion();
            TextComponent message = new TextComponent(Devtools.getPrefix() + "[" + ChatColor.AQUA + "Download here" + ChatColor.GRAY + "] " + ChatColor.RESET + text);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/devtools.96876/"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open official Plugin Website!")));
            event.getPlayer().spigot().sendMessage(message);
        }
    }
}
