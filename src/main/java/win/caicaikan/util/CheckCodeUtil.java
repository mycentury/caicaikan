package win.caicaikan.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CheckCodeUtil {
	public static BufferedImage getCheckCodeImg(String checkCode, int width, int height) {

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(180, 255));

		g.fillRect(0, 0, width, height);

		g.setFont(new Font(Font.SANS_SERIF, 0, 18));

		for (int i = 0; i < 50; i++) {
			g.setColor(getRandColor(140, 180));
			g.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width),
					random.nextInt(height));
		}

		g.setColor(getRandColor(110, 130));
		g.drawString(checkCode, 5, 17);
		g.dispose();
		return image;
	}

	public static Color getRandColor(int start, int end) {
		Random random = new Random();
		if ((start > 255) || (start < 0))
			start = random.nextInt(255);
		if ((end > 255) || (start < 0))
			end = 255;
		int r = random.nextInt(end - start) + start;
		int g = random.nextInt(end - start) + start;
		int b = random.nextInt(end - start) + start;
		return new Color(r, g, b);
	}

	public static String getRandomCode(String source, int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(source.charAt(random.nextInt(source.length())));
		}
		return sb.toString();
	}

}
