package dk.madsboddum.discordwolfet.service;

import dk.madsboddum.discordwolfet.repository.LevelshotRepository;

public final class ServiceProvider {
	
	private static ServiceProvider serviceProvider;
	
	public static ServiceProvider getServiceProvider() {
		if (serviceProvider == null) {
			serviceProvider = new ServiceProvider();
		}
		
		return serviceProvider;
	}
	
	private final QuakeService quakeService;
	private final GameServerService gameServerService;
	private final DiscordService discordService;
	
	private ServiceProvider() {
		LevelshotRepository levelshotRepository = new LevelshotRepository();
		
		this.quakeService = new QuakeService();
		this.gameServerService = new GameServerService();
		this.discordService = new DiscordService(levelshotRepository);
	}
	
	public QuakeService getQuakeService() {
		return quakeService;
	}
	
	public GameServerService getGameServerService() {
		return gameServerService;
	}
	
	public DiscordService getDiscordService() {
		return discordService;
	}
}
