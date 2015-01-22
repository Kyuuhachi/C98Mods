package c98.wiiu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import c98.WiiU;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WiiURootServer implements HttpHandler {
	
	@Override public void handle(HttpExchange e) {
		try {
			String resource = e.getRequestURI().getQuery();
			if(resource == null || resource.isEmpty()) resource = "c98:wiiu/index.html";

			String contentType = null;
			if(resource.endsWith(".png")) contentType = "image/png";
			if(resource.endsWith(".html")) contentType = "text/html";
			if(resource.endsWith(".js")) contentType = "application/javascript";
			
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(resource));

			ByteArrayOutputStream o = new ByteArrayOutputStream();
			IOUtils.copy(r.getInputStream(), o);

			byte[] b = o.toByteArray();

			if(resource.equals("c98:wiiu/index.html")) {
				Charset charset = Charset.forName("utf-8");
				b = new String(b, charset).replace("/* IP */", '"' + WiiU.WS_IP + '"').getBytes(charset);
			}
			if(contentType.equals("image/png")) {
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(b));
				BufferedImage img2 = new BufferedImage(img.getWidth() * 2, img.getHeight() * 2, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = img2.createGraphics();
				g.drawImage(img, 0, 0, img2.getWidth(), img2.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
				g.dispose();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img2, "png", baos);
				b = baos.toByteArray();
			}

			if(contentType != null) e.getResponseHeaders().set("Content-type", contentType);
			e.sendResponseHeaders(200, b.length);
			OutputStream os = e.getResponseBody();
			os.write(b);
			os.close();

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
