package dk.madsboddum.quake4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class OutOfBandTransport {
	
	private static final byte[] SERIAL_NUMBER = {	// Special out-of-band serial number
			(byte) 0xFF,
			(byte) 0xFF,
			(byte) 0xFF,
			(byte) 0xFF
	};
	
	private final DatagramSocket datagramSocket;
	private final InetAddress address;
	private final int port;
	
	public OutOfBandTransport(DatagramSocket datagramSocket, InetAddress address, int port) {
		this.datagramSocket = datagramSocket;
		this.address = address;
		this.port = port;
	}
	
	public String send(String command) throws IOException {
		byte[] commandData = command.getBytes(StandardCharsets.UTF_8);
		byte[] requestData = prependSerialNumber(commandData);
		
		DatagramPacket outboundPacket = new DatagramPacket(requestData, requestData.length, address, port);
		datagramSocket.send(outboundPacket);
		
		byte[] responseData = new byte[65507];
		DatagramPacket inboundPacket = new DatagramPacket(responseData, responseData.length);
		datagramSocket.receive(inboundPacket);
		String response = new String(responseData, StandardCharsets.UTF_8);
		
		return response.trim();
	}
	
	private byte[] prependSerialNumber(byte[] commandData) {
		byte[] result = new byte[OutOfBandTransport.SERIAL_NUMBER.length + commandData.length];
		System.arraycopy(OutOfBandTransport.SERIAL_NUMBER, 0, result, 0, OutOfBandTransport.SERIAL_NUMBER.length);
		System.arraycopy(commandData, 0, result, OutOfBandTransport.SERIAL_NUMBER.length, commandData.length);
		
		return result;
	}
}
