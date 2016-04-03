package tomac.camsurvelliance.server;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import tomac.camsurvelliance.comunication.ImageMessage;
import tomac.camsurvelliance.server.ImageCenter.ChangeListener;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ImageExporter implements ChangeListener {

	private final ImageCenter imageCenter;
	private final Random random = new Random();
	private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy.");
	private final String exportPath;

	public ImageExporter(ImageCenter imageCenter, String exportPath) {
		this.imageCenter = imageCenter;
		if (!exportPath.endsWith(File.pathSeparator)) {
			exportPath += File.pathSeparator;
		}
		this.exportPath = exportPath;
	}

	@Override
	public void changed() {
		StringBuilder info = new StringBuilder();
		List<ImageMessage> images = imageCenter.getLastImages();
		System.out.println("exporting images... (" + images.size() + ")");
		images.forEach(imageMessage -> {
			Date date = new Date(imageMessage.getTimestamp() * 1000);
			String datePretty = dateFormat.format(date);
			BufferedImage image = imageMessage.getImage();
			String senderId = imageMessage.getSenderId();
			String imageName = "image_" + senderId + ".jpg";
			String fullImageName = exportPath + imageName;
			File file = new File(fullImageName);
			try {
				ImageIO.write(image, "jpg", file);
			} catch (IOException ex) {
			}
			info.append(imageName).append("|")
				.append(senderId).append("|")
				.append(datePretty).append("\n");

		});
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath + "/images.info"))) {
			writer.write(info.toString());
			writer.flush();
		} catch (IOException ex) {

		}
		System.out.println("exported info:\n" + info.toString());
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		DateFormat df = new SimpleDateFormat("HH_mm_ss-dd_MM_yyyy");
		String format = df.format(date);
		System.out.println(format);
	}
}
