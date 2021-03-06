package cx.moda.moda.module;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import cx.moda.moda.Moda;
import cx.moda.moda.module.storage.ModuleStorageHandler;
import cx.moda.moda.placeholder.ModaPlaceholderAPI;
import xyz.derkades.derkutils.bukkit.Colors;

public class LangFile {

	private final FileConfiguration file;

	public LangFile(final File fileFile, final Module<? extends ModuleStorageHandler> module) throws IOException {
		this.file = YamlConfiguration.loadConfiguration(fileFile);

		boolean changes = false;

		for (final IMessage message : module.getMessages()) {
			if (!this.file.contains(message.getPath())) {
				module.getLogger().debug("Adding language option to config %s: %s", message.getPath(), message.getDefault());
				this.file.set(message.getPath(), message.getDefault());
				changes = true;
			}
		}

		if (changes) {
			this.file.save(fileFile);
		}
	}

	public String getMessage(final IMessage message) {
		return ModaPlaceholderAPI.parsePlaceholders(
				Optional.empty(),
				Colors.parseColors(
						Moda.getPrefix() + this.file.getString(message.getPath(), message.getDefault())
						)
				);
	}
	
	public String getMessage(final IMessage message, final Player player) {
		return ModaPlaceholderAPI.parsePlaceholders(
				Optional.of(player),
				Colors.parseColors(
						Moda.getPrefix() + this.file.getString(message.getPath(), message.getDefault())
						)
				);
	}

	/**
	 * Uses {@link #getMessage()} then replaces placeholders.
	 * <br><br>
	 * "Visit {LINK} {NUMBER} times"
	 * @param placeholders ["link", "https://example.com", "number", 3]
	 * @return "Visit https://example.com 3 times"
	 */
	public String getMessage(final IMessage message, final Object... placeholders) {
		return ModaPlaceholderAPI.parsePlaceholders(
				Optional.empty(),
				replacePlaceholders(this.getMessage(message), placeholders)
				);
	}
	
	/**
	 * Uses {@link #getMessage()} then replaces placeholders.
	 * <br><br>
	 * "Visit {LINK} {NUMBER} times"
	 * @param placeholders ["link", "https://example.com", "number", 3]
	 * @return "Visit https://example.com 3 times"
	 */
	public String getMessage(final IMessage message, final Player player, final Object... placeholders) {
		return ModaPlaceholderAPI.parsePlaceholders(
				Optional.of(player),
				replacePlaceholders(this.getMessage(message), placeholders)
				);
	}
	
	private String replacePlaceholders(String string, final Object[] placeholders) {
		if (placeholders.length % 2 != 0) {
			throw new IllegalArgumentException("Placeholder array length must be an even number");
		}
		
		if (placeholders.length == 0) {
			return string;
		}
		
		final Map<String, String> placeholderMap = new HashMap<>();
	
		Object key = null;
	
		for (final Object object : placeholders) {
			if (key == null) {
				// 'placeholder' is a key
				key = object;
			} else {
				// Key has been set previously, 'placeholder' must be a value
				placeholderMap.put(key.toString(), object.toString());
				key = null; // Next 'placeholder' is a key
			}
		}
	
		for (final Map.Entry<String, String> entry : placeholderMap.entrySet()) {
			string = string.replace("{" + entry.getKey() + "}", entry.getValue());
		}
		
		return string;
	}

	public void send(final CommandSender sender, final IMessage message) {
		sender.sendMessage(this.getMessage(message));
	}
	
	public void send(final Player player, final IMessage message) {
		player.sendMessage(this.getMessage(message, player));
	}

	public void send(final CommandSender sender, final IMessage message, final Object... placeholders) {
		sender.sendMessage(this.getMessage(message, placeholders));
	}
	
	public void send(final Player player, final IMessage message, final Object... placeholders) {
		player.sendMessage(this.getMessage(message, player, placeholders));
	}

}
