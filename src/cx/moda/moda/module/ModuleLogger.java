package cx.moda.moda.module;

import java.util.logging.Logger;

import cx.moda.moda.module.storage.ModuleStorageHandler;
import cx.moda.moda.Moda;

public class ModuleLogger {

	private final Logger logger;
	private final String name;

	ModuleLogger(final Logger logger, final Module<? extends ModuleStorageHandler> module){
		this.logger = logger;
		this.name = module.getName();
	}

	public void debug(final String message, final Object... args) {
		if (Moda.instance.getConfig().getBoolean("debug", false)) {
			this.info("[Debug] " + message, args);
		}
	}

	public void info(final String message, final Object... args) {
		this.logger.info(String.format("[" + this.name + "] " + message, args));
	}

	public void warning(final String message, final Object... args) {
		this.logger.warning(String.format("[" + this.name + "] " + message, args));
	}

	public void severe(final String message, final Object... args) {
		this.logger.info(String.format("[" + this.name + "] " + message, args));
	}

}
