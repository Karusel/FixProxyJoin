package base;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MainFPJ extends JavaPlugin implements Listener {

	public void onEnable() {
		final File directory = new File(Bukkit.getPluginManager().getPlugin("FixProxyJoin").getDataFolder(), "");
		directory.mkdirs();
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) this);
		this.getLogger().info("Start FixProxyJoin");
	}

	public void onDisable() {
		this.getLogger().info("Stop FixProxyJoin");
	}

	@EventHandler
	public void login(PlayerLoginEvent e) {
		String ip = e.getRealAddress().toString().replaceAll("[/]", "");
		String Bungee = e.getHostname().split(":")[0];
		String Bungeecord = e.getHostname();

		if (! Bungee.equals(ip)) {
			e.disallow(null, "Произошла ошибка, возможно вы пытаетесь войти не через Bungeecord");
		}

		String ipConfig = getConfig().getString("Bungeecord-ip");
		String ipPlayer = e.getAddress().toString().replaceAll("[/]", "");
		if (Bungeecord.equals(ipConfig)) {
			Bukkit.getLogger().info("[FixProxyJoin] Легальное соединение с ip: " + ipPlayer);
		} else {
			Bukkit.getConsoleSender().sendMessage("[FixProxyJoin] Нелегальное соединение с ip: " + ipPlayer+" ["+Bungeecord+"]");
			e.disallow(null, "Пожалуйста, войдите с помощью Bungeecord");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fpj")) {
			if (sender.hasPermission("fpj.enter")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + " [FixProxyJoin] " + ChatColor.GREEN + "Адрес сохранён");
				    return false;
				} else {
					FileConfiguration config = getConfig();
					config.set("Bungeecord-ip", args[0]);
					saveConfig();
					sender.sendMessage(ChatColor.GOLD + " [FixProxyJoin] " + ChatColor.GREEN + "Адрес сохранён");
				}
				return true;
			}
		}
		return false;
	}
}
