package net.awesomepowered.permissionwhitelist;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionWhitelist extends JavaPlugin implements Listener {
	
	public boolean Enabled;
	public String kickMessage;
	private String whitelistPermission;
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		reConf();
	}
	
	public void reConf () {
		Enabled = getConfig().getBoolean("Enabled");
		kickMessage = getConfig().getString("KickReason");
		whitelistPermission = getConfig().getString("Permission");
		this.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (((commandLabel.equalsIgnoreCase("PermissionWhitelist")) || (commandLabel.equalsIgnoreCase("pwl")))) {
			if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "This plugin is created by the almighty" + ChatColor.GREEN + " LaxWasHere");
			sender.sendMessage(ChatColor.GOLD + "Running version: " + this.getDescription().getVersion());
			if (sender.hasPermission("permissionwhitelist.reload")) {
				reConf();
				sender.sendMessage(ChatColor.RED + "PermissionWhitelist reloaded");
				} 
			} else if (args.length == 1 && sender.hasPermission("permissionwhitelist.admin")) {
					if (args[0].equalsIgnoreCase("enable")) {
						Enabled = true;
						this.getConfig().set("Enabled", true);
						reConf();
						sender.sendMessage(ChatColor.GREEN + "PermissionWhitelist Enabled!");
					} if (args[0].equalsIgnoreCase("disable")) {
						Enabled = false;
						this.getConfig().set("Enabled", false);
						reConf();
						sender.sendMessage(ChatColor.RED + "PermissionWhitelist Disabled!");
					}
				}
			}
		return true;
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent ev) {
		Player p = ev.getPlayer();
		if (!p.hasPermission(whitelistPermission) && Enabled) {
			ev.disallow(Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', kickMessage.replace("%PLAYER%", p.getName())));
		}
	}
}
