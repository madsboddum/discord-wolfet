package dk.madsboddum.discordwolfet.service;

import dk.madsboddum.discordwolfet.model.GameServer;
import dk.madsboddum.discordwolfet.model.Levelshot;
import dk.madsboddum.discordwolfet.repository.LevelshotRepository;
import dk.madsboddum.quake4j.model.StatusResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class DiscordService {
	
	private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);
	
	private final LevelshotRepository levelshotRepository;
	
	DiscordService(LevelshotRepository levelshotRepository) {
		this.levelshotRepository = levelshotRepository;
	}
	
	public void sendInfoMessage(MessageChannel channel, StatusResponse statusResponse, String joinUrl) {
		String serverName = statusResponse.getMetadataValue("sv_hostname");	// TODO this name includes raw color codes, which is a bit unfortunate. Strip them?
		String mapName = statusResponse.getMetadataValue("mapname");
		int currentPlayers = statusResponse.getPlayers().size();
		String maxPlayers = statusResponse.getMetadataValue("sv_maxclients");
		String modName = statusResponse.getMetadataValue("gamename");
		String version = statusResponse.getMetadataValue("version");
		String description = "Click the server name to join";
		
		Levelshot levelshot = levelshotRepository.getLevelshot(mapName);
		logger.info("Levelshot is {} for map {}", levelshot, mapName);
		File levelshotFile = levelshot.getFile();
		boolean passwordProtected = isPasswordProtected(statusResponse);
		
		try {
			channel.sendMessage("Here is some information about the server")
					.addFile(levelshotFile)
					.embed(new EmbedBuilder()
							.setTitle(serverName, joinUrl)
							.setColor(Color.lightGray)
							.setFooter(passwordProtected ? "\uD83D\uDD12 Password protected" : "\uD83C\uDF0E Public")
							.setDescription(description)
							.addField("Map", mapName, true)
							.addField("Players", currentPlayers + "/" + maxPlayers, true)
							.addField("Mod", modName, true)
							.addField("Version", version, false)
							.setThumbnail("attachment://" + levelshotFile.getName())
							.build())
					.queue(); // This actually sends the information to discord
		} catch (Exception e) {
			logger.error("Unable to send message to Discord", e);
		}
	}
	
	private boolean isPasswordProtected(StatusResponse statusResponse) {
		String needpass = statusResponse.getMetadataValue("g_needpass");
		
		return Objects.equals(needpass, "1");
	}
	
}
