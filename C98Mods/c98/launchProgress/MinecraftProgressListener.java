package c98.launchProgress;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;
import java.util.logging.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;
import c98.core.*;

public class MinecraftProgressListener extends ProgressListener {
	private static List<String> strings;
	private static int[] imgdata;
	
	private static String[] loggers = {"C98Mods", "ForgeModLoader"};
	private static int imgWidth;
	private static int imgHeight;
	private static DynamicTexture tex;
	private static ResourceLocation loc;
	private static String msg = "<No message here>";
	private static int prog;
	
	private static Handler logHandler = new Handler() {
		long prevTime;
		Thread correctThread = Thread.currentThread();
		
		@Override public void publish(LogRecord record) {
			if(Thread.currentThread() != correctThread) return;
			long l = System.currentTimeMillis();
			if(l - prevTime > 100) {
				tick();
				prevTime = l;
			}
		}
		
		@Override public void flush() {}
		
		@Override public void close() {}
	};
	
	private static void init() {
		if(imgdata == null) {
			for(String s:loggers)
				Logger.getLogger(s).addHandler(logHandler);
			try {
				strings = IOUtils.readLines(IO.getInputStream(new ResourceLocation("c98/launchprogress", "launch.txt")));
				BufferedImage img = IO.getImage(new ResourceLocation("textures/gui/title/mojang.png"));
				imgWidth = img.getWidth();
				imgHeight = img.getHeight();
				imgdata = new int[imgWidth * imgHeight];
				img.getRGB(0, 0, imgWidth, imgHeight, imgdata, 0, imgWidth);
				tex = new DynamicTexture(img);
				loc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("LaunchProgress", tex);
			} catch(Exception e) {
				C98Log.error("Failed to read LaunchProgress resources", e);
			}
		}
	}
	
	@Override void close() {
		for(String s:loggers)
			Logger.getLogger(s).removeHandler(logHandler);
		logHandler.close();
	}
	
	@Override public void setProgress(int prog2, String msg2) {
		init();
		msg = msg2;
		prog = prog2;
		tick();
	}
	
	public static void tick() {
		img();
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		setupGl(mc, scRes);
		drawTexture(mc, scRes);
		
		Display.update();
		if(Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
	}
	
	private static void img() {
		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		DataBufferInt buff = (DataBufferInt)img.getRaster().getDataBuffer();
		System.arraycopy(imgdata, 0, buff.getData(), 0, imgdata.length);
		
		Graphics2D g = img.createGraphics();
		drawBar(g);
		
		g.setColor(Color.BLACK);
		g.drawString(msg, (imgWidth - g.getFontMetrics().stringWidth(msg)) / 2, imgHeight - 36);
		if(!strings.isEmpty()) {
			String s = strings.get((int)(Math.random() * strings.size()));
			g.drawString(s, (imgWidth - g.getFontMetrics().stringWidth(s)) / 2, imgHeight - 13);
		}
		g.dispose();
		
		System.arraycopy(buff.getData(), 0, tex.getTextureData(), 0, imgdata.length);
		tex.updateDynamicTexture();
	}
	
	private static void drawBar(Graphics2D g) {
		int barwidth = imgWidth;
		int barheight = imgHeight * 3 / 100;
		int fillwidth = barwidth * prog / 100;
		g.setColor(new Color(0x007F00));
		g.fillRect((imgWidth - barwidth) / 2, imgHeight - 32, barwidth, barheight);
		g.setColor(new Color(0x00AF00));
		g.fillRect((imgWidth - barwidth) / 2, imgHeight - 32, fillwidth, barheight);
		g.setColor(new Color(0x00FF00));
		g.fillRect((imgWidth - barwidth) / 2, imgHeight - 32, fillwidth, barheight * 2 / 3);
	}
	
	private static void drawTexture(Minecraft mc, ScaledResolution scRes) {
		mc.getTextureManager().bindTexture(loc);
		
		GL.color(1, 1, 1);
		GL.begin();
		GL.vertex(0, mc.displayHeight);
		GL.vertex(mc.displayWidth, mc.displayHeight);
		GL.vertex(mc.displayWidth, 0);
		GL.vertex(0, 0);
		GL.end();
		drawRect((scRes.getScaledWidth() - 256) / 2, (scRes.getScaledHeight() - 256) / 2, 0, 0, 256, 256);
	}
	
	private static void setupGl(Minecraft mc, ScaledResolution scRes) {
		GL.clearColor(0, 0, 0, 0);
		GL.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);
		GL.matrixMode(GL.PROJECTION);
		GL.loadIdentity();
		GL.ortho(0, scRes.getScaledWidth_double(), scRes.getScaledHeight_double(), 0, 1000, 3000);
		GL.matrixMode(GL.MODELVIEW);
		GL.loadIdentity();
		GL.translate(0, 0, -2000);
		GL.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		
	}
	
	private static void drawRect(int x, int y, int u, int v, int w, int h) {
		float pix = 1F / 256;
		GL.begin();
		GL.vertex(x + 0, y + h, (u + 0) * pix, (v + h) * pix);
		GL.vertex(x + w, y + h, (u + w) * pix, (v + h) * pix);
		GL.vertex(x + w, y + 0, (u + w) * pix, (v + 0) * pix);
		GL.vertex(x + 0, y + 0, (u + 0) * pix, (v + 0) * pix);
		GL.end();
	}
}
