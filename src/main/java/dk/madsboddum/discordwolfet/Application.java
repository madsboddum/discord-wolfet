package dk.madsboddum.discordwolfet;

import dk.madsboddum.discordwolfet.discord.MessageListener;
import dk.madsboddum.discordwolfet.service.ServiceProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Add to your discord server with: https://discord.com/oauth2/authorize?client_id=825442835557777408&scope=bot
 */
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private static final String CONFIG_FILE = "config/config.properties";
	
	public static void main(String[] args) throws LoginException, InterruptedException, IOException {
		logger.info("Starting");
		ServiceProvider serviceProvider = ServiceProvider.getServiceProvider();
		
		Properties properties = new Properties();
		File file = new File(CONFIG_FILE);
		
		if (file.exists()) {
			handleConfigExists(serviceProvider, properties, file);
		} else {
			handleConfigMissing();
		}
	}
	
	private static void handleConfigMissing() {
		logger.error("Unable to find config file at {}", CONFIG_FILE);
	}
	
	private static void handleConfigExists(ServiceProvider serviceProvider, Properties properties, File file) throws IOException, LoginException, InterruptedException {
		properties.load(new FileInputStream(file));
		String token = (String) properties.get("token");
		
		AnnotatedEventManager eventManager = new AnnotatedEventManager();
		eventManager.register(new MessageListener(serviceProvider));
		
		JDA jda = JDABuilder.createDefault(token)
				.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)	// Disable parts of the cache
				.setBulkDeleteSplittingEnabled(false)	// Enable the bulk delete event
				.setCompression(Compression.NONE)	// Disable compression (not recommended)
				.setEventManager(eventManager)
				.build();
		
		jda.awaitReady();
		
		logger.info("Started");
	}
}
