package tomac.camsurvelliance.comunication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ImageMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String senderId;
	private final long timestamp;
	private transient BufferedImage image;

	public ImageMessage(String senderId, long timestamp, BufferedImage image) {
		this.senderId = senderId;
		this.timestamp = timestamp;
		this.image = image;
	}

	public String getSenderId() {
		return senderId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public BufferedImage getImage() {
		return image;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(image, "png", out);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		BufferedImage readImage = ImageIO.read(in);
		this.image = readImage;
	}

}
