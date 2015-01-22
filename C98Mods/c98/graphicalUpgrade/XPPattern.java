package c98.graphicalUpgrade;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class XPPattern {
	public static int[] defColors;
	
	static int max = 8;
	static int min = 2;
	static int time = 32;
	int[] colors;
	private int offset;
	private BufferedImage img;
	
	public XPPattern(int seed) {
		Random rand = new Random(seed);
		colors = new int[rand.nextInt(max - min) + min + 1];
		int last = -1;
		int first = -1;
		for(int i = 0; i < colors.length; i++) {
			int color = last;
			while(color == last || i == colors.length - 1 && color == first)
				color = rand.nextInt(defColors.length);
			last = color;
			if(i == 0) first = color;
			colors[i] = color;
		}
		
		LinearGradientPaint p = new LinearGradientPaint(0, 0, 0, time * colors.length, getFractions(), getColors());
		img = new BufferedImage(1, time * colors.length, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setPaint(p);
		g.fillRect(0, 0, 1, img.getHeight());
		g.dispose();
		offset = rand.nextInt(img.getHeight());
		
//		File f = new File(Minecraft.getMinecraftDir(), "ptrn" + seed + ".png");
//		if(!f.exists()) try {
//			ImageIO.write(img, "PNG", f);
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public int getRGB(int age) {
		age += offset;
		return img.getRGB(0, age % img.getHeight());
	}
	
	private Color[] getColors() {
		Color[] f = new Color[colors.length + 1];
		
		for(int i = 0; i < colors.length; i++)
			f[i] = new Color(defColors[colors[i]]);
		
		f[f.length - 1] = new Color(defColors[colors[0]]);
		
		return f;
	}
	
	private float[] getFractions() {
		float[] f = new float[colors.length + 1];
		float fl = 1F / f.length;
		for(int i = 0; i < f.length; i++)
			f[i] = i * fl;
		f[f.length - 1] = 1;
		return f;
	}
}
