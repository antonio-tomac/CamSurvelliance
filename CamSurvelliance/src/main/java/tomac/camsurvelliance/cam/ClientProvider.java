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

	public static void main(String[] args) throws ClassNotFoundException {
		CameraShooter cameraShooter = CameraShooter.getCameraShooter();
		String serverAddress = Config.DEFAULT_SERVER_ADDRESS;
		int serverPort = Config.DEFAULT_SERVER_PORT;
		String senderName = Config.DEFAULT_SENDER_NAME;
		int socketTimeout = Config.DEFAULT_SOCKET_TIMEOUT;
		if (args.length == 1) {
			senderName = args[0];
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String dirName = "pics/pics_" + dateFormat.format(new Date());
		File dir = new File(dirName);
		dir.mkdirs();
		int i = 0;
		while (true) {
			try {
				Socket socket = new Socket();
				socket.setSoTimeout(socketTimeout);
				socket.connect(new InetSocketAddress(serverAddress, serverPort), socketTimeout);
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				while (true) {
					BufferedImage image = cameraShooter.takePicture();
					long timestamp = System.currentTimeMillis() / 1000;
					ImageMessage imageMessage = new ImageMessage(senderName, timestamp, image);
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
}
