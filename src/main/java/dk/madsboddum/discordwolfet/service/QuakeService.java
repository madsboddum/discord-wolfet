package dk.madsboddum.discordwolfet.service;

import dk.madsboddum.discordwolfet.model.GameServer;
import dk.madsboddum.quake4j.OutOfBandDispatcher;
import dk.madsboddum.quake4j.OutOfBandTransport;
import dk.madsboddum.quake4j.model.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class QuakeService {
	
	private static final Logger logger = LoggerFactory.getLogger(QuakeService.class);
	
	private static final int TIMEOUT = 1000;
	
	QuakeService() {
	
	}
	
	public StatusResponse getServerStatus(GameServer gameServer) {
		try {
			DatagramSocket datagramSocket = new DatagramSocket();
			datagramSocket.setSoTimeout(TIMEOUT);
			InetAddress address = InetAddress.getByName(gameServer.getAddress());
			int port = gameServer.getPort();
			OutOfBandTransport transport = new OutOfBandTransport(datagramSocket, address, port);
			OutOfBandDispatcher dispatcher = new OutOfBandDispatcher(transport);
			
			logger.info("Sending request to {}", gameServer);
			StatusResponse statusResponse = dispatcher.dispatchStatusRequest();
			logger.info("Received response from {}", gameServer);
			
			return statusResponse;
		} catch (IOException e) {
			logger.error("Unable to request status from " + gameServer, e);
		}
		
		return null;
	}
}
