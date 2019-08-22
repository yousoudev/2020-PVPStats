package com.Sagacious_.KitpvpStats.api.hook;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.Sagacious_.KitpvpStats.Core;

import net.milkbowl.vault.chat.Chat;

public class VaultHook implements Listener{
private Chat chat;
private boolean use = false;
private String format;

private boolean setupChat()
{
    RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
    if (chatProvider != null) {
        chat = chatProvider.getProvider();
}
    return (chat != null);
}

public VaultHook() {
	Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	setupChat();
	File f = new File(Core.getInstance().getDataFolder(), "config.yml");
	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
	if(!conf.isSet("format")) {
		conf.set("use-format", false);
		conf.set("format", "&7[%level%&7] %prefix%&7%player%&8: &7%suffix%%message%");
		try {
			conf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}else {
		use = conf.getBoolean("use-format");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("format"));
	}
}

@EventHandler
public void onFormat(AsyncPlayerChatEvent e) {
	if(use) {
		e.setFormat(format.replaceAll("%level%", Core.getInstance().getLevel(Core.getInstance().dh.getData(e.getPlayer()).getKills())).replaceAll("%prefix%", chat.getPlayerPrefix(e.getPlayer())).replaceAll("%player%", e.getPlayer().getName()).replaceAll("%suffix%", chat.getPlayerSuffix(e.getPlayer())).replaceAll("%message%", e.getMessage()));
	}
}
}
