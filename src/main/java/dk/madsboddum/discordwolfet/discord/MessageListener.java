package dk.madsboddum.discordwolfet.discord;

import dk.madsboddum.discordwolfet.model.GameServer;
import dk.madsboddum.discordwolfet.service.DiscordService;
import dk.madsboddum.discordwolfet.service.GameServerService;
import dk.madsboddum.discordwolfet.service.QuakeService;
import dk.madsboddum.discordwolfet.service.ServiceProvider;
import dk.madsboddum.quake4j.model.StatusResponse;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.MDC;

public class MessageListener {
	
	private static final String USER_KEY = "user";
	private static final String SERVER_KEY = "server";
	
	private final ServiceProvider serviceProvider;
	
	public MessageListener(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	
	@SubscribeEvent
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		String name = event.getAuthor().getName();
		String discriminator = event.getAuthor().getDiscriminator();
		String user = name + "#" + discriminator;
		
		MDC.put(USER_KEY, user);
		
		try {
			handleMessageReceivedEvent(event);
		} finally {
			MDC.remove(USER_KEY);
		}
	}
	
	private void handleMessageReceivedEvent(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		GameServerService gameServerService = serviceProvider.getGameServerService();
		QuakeService quakeService = serviceProvider.getQuakeService();
		DiscordService discordService = serviceProvider.getDiscordService();
		
		String contentDisplay = event.getMessage().getContentDisplay();
		GameServer gameServer = gameServerService.createGameServer(contentDisplay);
		
		if (gameServer == null) {
			return;
		}
		
		try {
			MDC.put(SERVER_KEY, gameServer.toString());
			StatusResponse statusResponse = quakeService.getServerStatus(gameServer);
			
			if (statusResponse == null) {
				return;
			}
			
			MessageChannel channel = event.getChannel();
			String joinUrl = gameServerService.createJoinUrl(gameServer);
			discordService.sendInfoMessage(channel, statusResponse, joinUrl);
		} finally {
			MDC.remove(SERVER_KEY);
		}
	}
	
}
