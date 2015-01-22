package c98;

import static org.lwjgl.opengl.GL11.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.IntBuffer;
import java.util.*;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL12;
import c98.core.C98Core;
import c98.core.C98Mod;

public class ItemTexturizer extends C98Mod {
	int n = 16;
	KeyBinding k = new KeyBinding("asdf", Keyboard.KEY_I, C98Core.KEYBIND_CAT);
	
	@Override public void load() {
//		C98Core.registerKey(k, false);
		super.load();
	}
	
	@Override public void keyboardEvent(KeyBinding key) {
		if(key != k) return;
		List<String> names = new LinkedList(Item.itemRegistry.getKeys());
		Collections.sort(names, new Comparator<String>() {
			@Override public int compare(String arg0, String arg1) {
				int id0 = Item.itemRegistry.getIDForObject(Item.itemRegistry.getObject(arg0));
				int id1 = Item.itemRegistry.getIDForObject(Item.itemRegistry.getObject(arg1));
				return Integer.compare(id0, id1);
			}
		});
		Collections.sort(names, new Comparator<String>() {
			@Override public int compare(String arg0, String arg1) {
				return -String.CASE_INSENSITIVE_ORDER.compare(arg0.split(":")[0], arg1.split(":")[0]);
			}
		});

		Framebuffer fb = new Framebuffer(names.size() * n, 16 * n, true);
		RenderItem ri = new RenderItem();
		initGl(names, fb, ri);

		File dir = new File("C:/Users/admusr/Desktop/mcmods");
		PrintStream out = null;
		try {
			out = new PrintStream(new File(dir, "items.txt"));
		} catch(FileNotFoundException e) {}
		
		int x = 0;
		boolean c98 = false;
		List<String> allnames = new LinkedList();

		String[] wood = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};
		String[] color = {"white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
		String[] slab = {"stone", "sandstone", "wood_old", "cobblestone", "brick", "stone_brick", "nether_brick", "quartz"};
		for(String s : names) {
			if(s.startsWith("c98") && !c98) {
				c98 = true;
				while(x % 16 != 0)
					x++;
			}
			Item i = (Item)Item.itemRegistry.getObject(s);
			List l = new LinkedList();
			try {
				for(int j = 0; j < 16; j++) {
					ItemStack is = new ItemStack(i, 1, j);

					String un = s;
					if(un.startsWith("minecraft:")) un = un.substring(10);
					if(un.startsWith("c98:")) un = un.substring(4);

					//TODO maybe: anvil fish cookedfish spawnegg tallergrass

					if(i == Item.getItemFromBlock(Blocks.wool)) un += "_" + color[j];
					if(i == Item.getItemFromBlock(Blocks.stained_hardened_clay)) un += "_" + color[j];
					if(i == Item.getItemFromBlock(Blocks.stained_glass)) un += "_" + color[j];
					if(i == Item.getItemFromBlock(Blocks.stained_glass_pane)) un += "_" + color[j];
					if(i == Item.getItemFromBlock(Blocks.carpet)) un += "_" + color[j];
					if(i == Items.dye) un += "_" + color[15 - j];

					if(i == Item.getItemFromBlock(Blocks.log)) un += "_" + wood[j % 4];
					if(i == Item.getItemFromBlock(Blocks.log2)) un += "_" + wood[j % 4 + 4];
					if(i == Item.getItemFromBlock(Blocks.leaves)) un += "_" + wood[j % 4];
					if(i == Item.getItemFromBlock(Blocks.leaves2)) un += "_" + wood[j % 4 + 4];
					if(i == Item.getItemFromBlock(Blocks.sapling)) un += "_" + wood[j];
					if(i == Item.getItemFromBlock(Blocks.wooden_slab)) un += "_" + wood[j];
					if(i == Item.getItemFromBlock(Blocks.double_wooden_slab)) un += "_" + wood[j];
					if(i == Item.getItemFromBlock(Blocks.planks)) un += "_" + wood[j];

					if(i == Item.getItemFromBlock(Blocks.sandstone)) un = new String[] {"", "chiseled_", "smooth_"}[j] + un;
					if(i == Item.getItemFromBlock(Blocks.tallgrass)) un = new String[] {"dead_bush", "tall_grass", "fern"}[j];
					if(i == Item.getItemFromBlock(Blocks.yellow_flower)) un = "dandelion";
					if(i == Item.getItemFromBlock(Blocks.red_flower)) un = new String[] {"poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy"}[j];
					if(i == Item.getItemFromBlock(Blocks.stone_slab)) un += "_" + slab[j];
					if(i == Item.getItemFromBlock(Blocks.double_stone_slab)) un += "_" + slab[j];
					if(i == Item.getItemFromBlock(Blocks.monster_egg)) un += "_" + new String[] {"stone", "cobblestone", "stone_brick", "mossy_brick", "cracked_brick", "chiseled_brick"}[j];
					if(i == Item.getItemFromBlock(Blocks.stonebrick)) un = new String[] {"stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick"}[j];
					if(i == Item.getItemFromBlock(Blocks.cobblestone_wall)) un = new String[] {"cobblestone_wall", "mossy_cobblestone_wall"}[j];
					if(i == Item.getItemFromBlock(Blocks.quartz_block)) un += "_" + new String[] {"default", "chiseled", "lines"}[j];
					if(i == Items.skull) un += "_" + new String[] {"skeleton", "wither_skeleton", "zombie", "player", "creeper"}[j];
					if(i == Items.coal && j == 1) un = "charcoal";

					if(c98) un = "c98-" + un;

					if(l.contains(un)) break;
					l.add(un);
					if(allnames.contains(un)) System.err.println(un);
					else {
						allnames.add(un);
						out.printf("@include texticon(\'%s\', %d, %d);%n", un, x / 16, x % 16);
						ri.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), is, x / 16 * 16, x % 16 * 16);
						x++;
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {}
		}
		out.close();
		saveImage(fb, x, new File(dir, "items.png"));
		System.exit(0);
	}
	
	private void initGl(List<String> names, Framebuffer fb, RenderItem ri) {
		fb.bindFramebuffer(true);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0);
		RenderHelper.enableGUIStandardItemLighting();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, names.size() * 16, 16 * 16, 0, -256, 256);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		ri.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(Blocks.chest), 0, 240);
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	private void saveImage(Framebuffer fb, int x, File f) {
		fb.unbindFramebuffer();
		fb.bindFramebufferTexture();
		IntBuffer buf = BufferUtils.createIntBuffer(fb.framebufferWidth * fb.framebufferHeight);
		glGetTexImage(GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buf);
		fb.unbindFramebufferTexture();
		BufferedImage img = new BufferedImage(fb.framebufferWidth, fb.framebufferHeight, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < img.getWidth(); i++)
			for(int j = 0; j < img.getHeight(); j++)
				img.setRGB(i, j, buf.get(i + (img.getHeight() - 1 - j) * img.getWidth()));
		img = img.getSubimage(0, 0, (x + 16) / 16 * n, 16 * n);
//		JOptionPane.showMessageDialog(null, "", "", 0, new ImageIcon(img));
		try {
			ImageIO.write(img, "PNG", f);
		} catch(IOException e) {}
	}
}
