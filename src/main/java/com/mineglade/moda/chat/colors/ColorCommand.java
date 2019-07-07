package com.mineglade.moda.chat.colors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.moda.Moda;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.UUIDFetcher;

public class ColorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1) {
				new ColorMenu(player, player).open();
			} else {
				try {
				OfflinePlayer target = Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(args[0]));
				new ColorMenu(player, target).open();
				} catch (IllegalArgumentException e) {
					sender.spigot().sendMessage(new ComponentBuilder("")
							.append(Moda.getPrefix())
							.append(Colors.toComponent(Moda.messages.getString("color.errors.target-invalid")))
							.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
							.create());
				}
			}
		} else {
			sender.sendMessage("this command is not functional for the console yet.");
		}
		return true;
	}
	
}