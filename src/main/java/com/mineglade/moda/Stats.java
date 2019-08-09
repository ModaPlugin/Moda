package com.mineglade.moda;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;

import com.mineglade.moda.modules.Module;

public class Stats extends Metrics {

	public Stats(final Plugin plugin) {
		super(plugin);

		this.addCustomChart(new SimplePie("storage_type", () -> Moda.instance.getStorageType().name().toString()));
		this.addCustomChart(new SimplePie("debug", () -> Moda.instance.getConfig().getBoolean("debug") + ""));

		this.addCustomChart(new AdvancedPie("loaded_modules", () -> {
			final Map<String, Integer> map = new HashMap<>();

			for (final Module<?> module : Module.LOADED) {
				map.put(module.getName(), 1);
			}

			return map;
		}));

		this.addCustomChart(new AdvancedPie("enabled_modules", () -> {
			final Map<String, Integer> map = new HashMap<>();

			for (final Module<?> module : Module.ENABLED) {
				map.put(module.getName(), 1);
			}

			return map;
		}));
	}

}