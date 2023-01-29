package base;

import java.io.File;
import java.util.List;
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
		boolean check = false;
		String ipPlayer = e.getAddress().toString().replaceAll("[/]", "");
		String UUID = e.getPlayer().getUniqueId().toString().replaceAll("-", "");
		String BungeeIp = e.getHostname().strip().replaceAll(ipPlayer, "").replaceAll(UUID, "").replace("\0", "");

		for (String ip : getConfig().getStringList("Bungeecord-ip")) {
			if (BungeeIp.equals(ip)) {
				check = true;
			}
		}

		if (check == true) {
			Bukkit.getLogger().info(ChatColor.GOLD +"[FixProxyJoin]"+ChatColor.GREEN +" Легальное соединение с ip: " + BungeeIp);
		} else {
			Bukkit.getLogger().info(ChatColor.GOLD +"[FixProxyJoin]"+ChatColor.RED +" Неегальное соединение с ip: " + BungeeIp);
			e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Пожалуйста, войдите с помощью Bungeecord");
		}
	}

	public boolean listCheck(String value) {
		boolean check = false;
		for (String ip : getConfig().getStringList("Bungeecord-ip")) {
			if (value.equals(ip)) {
				check = true;
			}
		}
		return check;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
		List<String> list = this.getConfig().getStringList("Bungeecord-ip");
		if (cmd.getName().equalsIgnoreCase("fpj")) {
			if (sender.hasPermission("fpj.enter")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + " [FixProxyJoin] " + ChatColor.GREEN + "Адрес сохранён");
					return false;
				} else {
					boolean check = listCheck(args[0]);
					if (check != true) {
						FileConfiguration config = getConfig();
						list.add(args[0]);
						config.set("Bungeecord-ip", list);
						saveConfig();
						sender.sendMessage(ChatColor.GOLD + " [FixProxyJoin] " + ChatColor.GREEN + "Адрес сохранён");
					}else {
						sender.sendMessage(ChatColor.RED + "Уже есть в списке");
					}
				}
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("fpjdel")) {
			if (sender.hasPermission("fpj.del")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + " [FixProxyJoin] " + ChatColor.GREEN + "Адрес удалён");
					return false;
				} else {
					boolean check = listCheck(args[0]);
					if (check == true) {
						FileConfiguration config = getConfig();
						list.remove(args[0]);
						config.set("Bungeecord-ip", list);
						saveConfig();
						sender.sendMessage(ChatColor.GOLD + " [FixProxyJoin] " + ChatColor.GREEN + "Адрес удалён");
					}else {
						sender.sendMessage(ChatColor.RED + "Не найден");
					}
				}
				return true;
			}
		}
		return false;
	}
}
