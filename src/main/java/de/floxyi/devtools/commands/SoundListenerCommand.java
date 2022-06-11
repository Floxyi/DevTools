package de.floxyi.devtools.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import de.floxyi.devtools.Devtools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoundListenerCommand implements TabExecutor {

    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    ArrayList<UUID> players = new ArrayList<>();
    ArrayList<String> lastMessage = new ArrayList<>();
    boolean isActive = false;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You need to be a Player in order to " +
                    "execute this command!");
            return false;
        }

        if(manager == null) {
            sendDownloadMessage(player);
            return false;
        }

        if(args.length != 0) {
            player.sendMessage(Devtools.getPrefix() + ChatColor.RED + "You can't specify any arguments, please just use" + ChatColor.GOLD + "/material" + ChatColor.RED + "!");
            return false;
        }

        if(players.contains(player.getUniqueId())) {
            players.remove(player.getUniqueId());
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "You " + ChatColor.GOLD + "deactivated" + ChatColor.GREEN + " the sound Listener!");
        } else {
            players.add(player.getUniqueId());
            player.sendMessage(Devtools.getPrefix() + ChatColor.GREEN + "You " + ChatColor.GOLD + "activated" + ChatColor.GREEN + " the sound Listener!");
        }

        if(!isActive) {
            isActive = true;
            packetListener();
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }

    private void sendDownloadMessage(Player player) {
        TextComponent prefix = new TextComponent(Devtools.getPrefix());
        TextComponent message = new TextComponent(ChatColor.GREEN + " You need to install ProtocolLib on your server in order to run this command!");
        TextComponent copy = new TextComponent("[" + ChatColor.AQUA + "Download" + ChatColor.GRAY + "]" + ChatColor.RESET);
        copy.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/protocollib.1997/"));
        copy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Download ProtocolLib on the official SpigotMC site.")));

        TextComponent combinedMessage = new TextComponent(prefix, copy, message);
        player.spigot().sendMessage(combinedMessage);
    }

    private void packetListener() {
        PacketAdapter packetAdapter = new PacketAdapter(Devtools.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {

            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                    return;
                }

                Sound sound = event.getPacket().getSoundEffects().read(0);
                float volume = event.getPacket().getFloat().read(0);
                float pitch = event.getPacket().getFloat().read(1);

                for(UUID uuid : players) {
                    if(event.getPlayer().getUniqueId().toString().equals(uuid.toString())) {

                        DecimalFormat df = new DecimalFormat("#.##");

                        String text = ChatColor.GREEN + "♪ " + ChatColor.GOLD + sound.name() +
                                ChatColor.GREEN + " v: " + df.format(volume) + ", p: " + df.format(pitch);

                        int playerIndex = players.indexOf(uuid);

                        // TODO: make this a setting (skipping same sounds)
                        if(lastMessage.isEmpty() || !lastMessage.get(playerIndex).equals(text)) {
                            sendSoundMessage(event.getPlayer(), text, sound, volume, pitch);
                            lastMessage.add(playerIndex, text);
                        }

                    }
                }
            }

        };

        manager.addPacketListener(packetAdapter);
    }

    private void sendSoundMessage(Player player, String text, Sound sound, float volume, float pitch) {
        TextComponent prefix = new TextComponent(Devtools.getPrefix());
        TextComponent message = new TextComponent(" " + text);
        TextComponent play = new TextComponent(ChatColor.GRAY + "[" + ChatColor.AQUA + "Play again" + ChatColor.GRAY + "]" + ChatColor.RESET);
        play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sound " + sound.name() + " " + volume + " " + pitch));
        play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Plays this sound again!")));

        TextComponent combinedMessage = new TextComponent(prefix, play, message);
        player.spigot().sendMessage(combinedMessage);
    }
}
