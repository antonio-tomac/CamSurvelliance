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

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ImageExporter implements Runnable {

	private final ImageCenter imageCenter;
	private final Random random = new Random();
	private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy.");
	private final String exportPath;

	public ImageExporter(ImageCenter imageCenter, String exportPath) {
		this.imageCenter = imageCenter;
		this.exportPath = exportPath;
	}

	@Override
	public void run() {

		StringBuilder info = new StringBuilder();
		List<ImageMessage> images = imageCenter.getLastImages();
		System.out.println("exporting images... (" + images.size() + ")");
		images.forEach(imageMessage -> {
			Date date = new Date(imageMessage.getTimestamp() * 1000);
			String datePretty = dateFormat.format(date);
			BufferedImage image = imageMessage.getImage();
			int id = random.nextInt(Integer.MAX_VALUE);
			String senderId = imageMessage.getSenderId();
			String imageName = exportPath + "/image_" + senderId + ".jpg";
			File file = new File(imageName);
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
