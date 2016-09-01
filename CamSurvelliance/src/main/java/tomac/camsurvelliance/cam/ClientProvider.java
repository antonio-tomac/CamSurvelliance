package tomac.camsurvelliance.cam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import lombok.Data;
import tomac.camsurvelliance.comunication.ImageMessage;
import tomac.camsurvelliance.server.Config;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ClientProvider {

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex1) {
		}
	}
	
	private static void printUsageAndExit() {
		System.out.println("./runSender <name> <serv-address> <serv-port> [<timeout-ms>]");
		System.exit(-1);
	}

	private static ClientConfig resolveConfig(String[] args) {
		if (args.length != 3 && args.length != 4) {
			printUsageAndExit();
		}
		String name = args[0];
		String address = args[1];
		int port = Integer.parseInt(args[2]);
		int timeout = args.length == 4 ?
			Integer.parseInt(args[3]) :
			Config.DEFAULT_SOCKET_TIMEOUT;
		return new ClientConfig(address, port, name, timeout);
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		CameraShooter cameraShooter = CameraShooter.getCameraShooter();
		ClientConfig clientConfig = resolveConfig(args);
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String dirName = "pics/pics_" + dateFormat.format(new Date());
		File dir = new File(dirName);
		dir.mkdirs();
		int i = 0;
		while (true) {
			try {
				Socket socket = new Socket();
				socket.setSoTimeout(clientConfig.getSocketTimeout());
				socket.connect(
					new InetSocketAddress(
						clientConfig.getServerAddress(), 
						clientConfig.getServerPort()
					), 
					clientConfig.getSocketTimeout()
				);
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				while (true) {
					BufferedImage image = cameraShooter.takePicture();
					long timestamp = System.currentTimeMillis() / 1000;
					ImageMessage imageMessage = new ImageMessage(clientConfig.getSenderName(), timestamp, image);
					System.out.println("sending image...");
					objectOutputStream.writeObject(imageMessage);
					objectOutputStream.flush();
					objectOutputStream.reset();

					File file = new File(dirName + "/image_" + String.format("%05d", i) + ".jpg");
					ImageIO.write(image, "jpg", file);
					i++;
				}
			} catch (IOException ex) {
				sleep(5 * 1000);
			}
		}
	}

	@Data
	private static class ClientConfig {

		private final String serverAddress;
		private final int serverPort;
		private final String senderName;
		private final int socketTimeout;
	}

}
