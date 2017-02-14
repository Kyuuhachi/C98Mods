package c98.minemapWaypoints;

import java.util.List;

import org.lwjgl.input.Mouse;

import c98.MinemapWaypoints;
import c98.MinemapWaypoints.Config.Waypoint;
import c98.core.GL;
import c98.minemap.api.IconStyle;
import c98.minemap.api.MapUtils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiWaypoints extends GuiScreen {
	private static final ResourceLocation BACKGROUND = new ResourceLocation("c98/minemapwaypoints", "waypoints.png");

	private static final int GUI_W = 158, GUI_H = 168;
	private static final int ITEM_X = 8, ITEM_Y = 17, ITEM_H = 20, ITEM_C = 6;
	private static final int BUTTON_X = ITEM_X - 1, BUTTON_Y = ITEM_Y + ITEM_H * ITEM_C + 4, BUTTON_H = 20;
	private static final int SCROLL_Y = ITEM_Y, SCROLL_W = 14, SCROLL_H = BUTTON_Y + BUTTON_H - SCROLL_Y, SCROLL_X = GUI_W - 7 - SCROLL_W;
	private static final int ITEM_W = SCROLL_X - 4 - ITEM_X, BUTTON_W = ITEM_W + 2;
	private static final int NUB_X = SCROLL_X + 1, NUB_W = SCROLL_W - 2, NUB_H = 15;

	private static final int ICON_X = 2, ICON_Y = 2, ICON_W = 16, ICON_H = ICON_W;
	private static final int NAME_X = ICON_X + ICON_W + 2, NAME_Y = ICON_Y;
	private static final int COORDS_X = NAME_X, COORDS_Y = NAME_Y + ICON_H / 2;
	private static final int MOVE_Y = 0, MOVE_W = 16, MOVE_H = 10, MOVE_X = ITEM_W - MOVE_W;
	private static final int DELETE_W = 12, DELETE_H = DELETE_W, DELETE_X = MOVE_X - DELETE_W, DELETE_Y = (ITEM_H - DELETE_H) / 2;

	private int GUI_X() {
		return (width - GUI_W) / 2;
	}

	private int GUI_Y() {
		return (height - GUI_H) / 2;
	}

	private int NUB_Y() {
		int trackh = SCROLL_H - 2 - NUB_H;
		return SCROLL_Y + 1 + (int)(trackh * currentScroll);
	}

	private float currentScroll;
	private boolean wasClicking;
	private boolean isScrolling;
	private World w;
	private List<Waypoint> points;

	public GuiWaypoints(World world) {
		w = world;
		points = MinemapWaypoints.getPoints(world);
	}

	private int numPoints() {
		return points.size();
	}

	private int maxScroll() {
		return numPoints() - ITEM_C;
	}

	private boolean needsScrollBars() {
		return maxScroll() > 0;
	}

	private void updateScroll() {
		if(currentScroll > 1) currentScroll = 1;
		if(currentScroll < 0) currentScroll = 0;
	}

	@Override public void initGui() {
		super.initGui();
		buttonList.clear();
		buttonList.add(new GuiButton(0, GUI_X() + BUTTON_X, GUI_Y() + BUTTON_Y, BUTTON_W, BUTTON_H, "Add"));
	}

	@Override public void actionPerformed(GuiButton button) {
		if(button.id == 0) mc.displayGuiScreen(new GuiEditWaypoint(w, new Waypoint(mc.thePlayer.getPosition())));
	}

	@Override public void handleMouseInput() {
		super.handleMouseInput();
		int steps = Mouse.getEventDWheel();

		if(steps != 0 && needsScrollBars()) {
			if(steps > 0) steps = 1;

			if(steps < 0) steps = -1;

			currentScroll -= steps / maxScroll();
			updateScroll();
		}
	}

	@Override public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseButton == 0) for(int i = 0; isIndex(i); i++) {
			int x = ITEM_X;
			int y = ITEM_Y + ITEM_H * i;
			Waypoint wp = getPoint(i);
			clickPoint(wp, mouseX - GUI_X() - x, mouseY - GUI_Y() - y);
		}
	}

	@Override public void keyTyped(char typedChar, int keyCode) {
		if(keyCode == 1 || keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) mc.thePlayer.closeScreen();
	}

	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		boolean clicking = Mouse.isButtonDown(0);
		int scrollL = GUI_X() + SCROLL_X;
		int scrollT = GUI_Y() + SCROLL_Y;
		int scrollR = scrollL + SCROLL_W;
		int scrollB = scrollT + SCROLL_H;

		if(!wasClicking && clicking && in(scrollL + 1, mouseX - 1, scrollR) && in(scrollT + 1, mouseY, scrollB)) isScrolling = needsScrollBars();
		if(!clicking) isScrolling = false;

		wasClicking = clicking;

		if(isScrolling) {
			currentScroll = (mouseY - scrollT - NUB_H / 2F) / (scrollB - scrollT - NUB_H);
			updateScroll();
		}

		GL.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BACKGROUND);
		drawTexturedModalRect(GUI_X(), GUI_Y(), 0, 0, GUI_W, GUI_H);

		super.drawScreen(mouseX, mouseY, partialTicks);

		GL.pushMatrix();
		GL.translate(GUI_X(), GUI_Y());

		GL.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BACKGROUND);
		drawTexturedModalRect(NUB_X, NUB_Y(), 256 - NUB_W * (needsScrollBars() ? 2 : 1), 0, NUB_W, NUB_H);

		fontRendererObj.drawString("Waypoints", 8, 6, 0x404040);

		for(int i = 0; isIndex(i); i++) {
			int x = ITEM_X;
			int y = ITEM_Y + ITEM_H * i;
			Waypoint wp = getPoint(i);
			GL.translate(x, y);
			drawPoint(wp, mouseX - GUI_X() - x, mouseY - GUI_Y() - y);
			GL.translate(-x, -y);
		}

		GL.popMatrix();
	}

	private Waypoint getPoint(int i) {
		return points.get(scrollIndex() + i);
	}

	private boolean isIndex(int i) {
		return i < ITEM_C && i + scrollIndex() < numPoints();
	}

	private int scrollIndex() {
		return Math.round(currentScroll * maxScroll());
	}

	private void drawPoint(Waypoint wp, int mx, int my) {
		IconStyle style = wp.style.clone();
		MapUtils.drawIcon(style.shape, style.color.getRGB() | 0xFF000000, ICON_X + ICON_W / 2, ICON_Y + ICON_H / 2, 2);

		fontRendererObj.drawString(wp.name, NAME_X, NAME_Y, 0xFFFFFFFF);
		StringBuilder str = new StringBuilder();
		for(int p : wp.position)
			str.append(", ").append(p);
		fontRendererObj.drawString(str.substring(2), COORDS_X, COORDS_Y, 0xFFFFFFFF);

		if(in(0, mx, ITEM_W) && in(0, my, ITEM_H)) {
			mc.getTextureManager().bindTexture(BACKGROUND);
			int DOWN_Y = MOVE_Y + MOVE_H;
			if(isUpHovered(mx, my) && !isFirst(wp)) drawTexturedModalRect(MOVE_X, MOVE_Y, 200 + MOVE_W, 0, MOVE_W, MOVE_H);
			else if(!isFirst(wp)) drawTexturedModalRect(MOVE_X, MOVE_Y, 200, 0, MOVE_W, MOVE_H);
			if(isDownHovered(mx, my) && !isLast(wp)) drawTexturedModalRect(MOVE_X, DOWN_Y, 200 + MOVE_W, MOVE_H, MOVE_W, MOVE_H);
			else if(!isLast(wp)) drawTexturedModalRect(MOVE_X, DOWN_Y, 200, MOVE_H, MOVE_W, MOVE_H);

			if(isDeleteHovered(mx, my)) drawTexturedModalRect(DELETE_X, DELETE_Y, 256 - DELETE_W, NUB_H + DELETE_H, DELETE_W, DELETE_H);
			else drawTexturedModalRect(DELETE_X, DELETE_Y, 256 - DELETE_W, NUB_H, DELETE_W, DELETE_H);
		}
	}

	private void clickPoint(Waypoint wp, int mx, int my) {
		boolean consume = false;
		int index = -1;
		if(isUpHovered(mx, my) && !isFirst(wp)) {
			index = points.indexOf(wp) - 1;
			currentScroll -= 1F / maxScroll();
			consume = true;
		}
		if(isDownHovered(mx, my) && !isLast(wp)) {
			index = points.indexOf(wp);
			currentScroll += 1F / maxScroll();
			consume = true;
		}
		if(isDeleteHovered(mx, my)) {
			points.remove(wp);
			consume = true;
		}
		if(index >= 0) {
			Waypoint a = points.get(index);
			Waypoint b = points.get(index + 1);
			points.set(index, b);
			points.set(index + 1, a);
		}

		updateScroll();
		if(!consume && in(0, mx, ITEM_W) && in(0, my, ITEM_H)) mc.displayGuiScreen(new GuiEditWaypoint(w, wp));
	}

	private static boolean isUpHovered(int mx, int my) {
		boolean inX = in(MOVE_X, mx, MOVE_X + MOVE_W);
		boolean inY = in(MOVE_Y, my, MOVE_Y + MOVE_H);
		return inX && inY;
	}

	private static boolean isDownHovered(int mx, int my) {
		int DOWN_Y = MOVE_Y + MOVE_H;
		boolean inX = in(MOVE_X, mx, MOVE_X + MOVE_W);
		boolean inY = in(DOWN_Y, my, DOWN_Y + MOVE_H);
		return inX && inY;
	}

	private static boolean isDeleteHovered(int mx, int my) {
		boolean inX = in(DELETE_X, mx, DELETE_X + DELETE_W);
		boolean inY = in(DELETE_Y, my, DELETE_Y + DELETE_H);
		return inX && inY;
	}

	private boolean isLast(Waypoint wp) {
		return wp == points.get(numPoints() - 1);
	}

	private boolean isFirst(Waypoint wp) {
		return wp == points.get(0);
	}

	private static boolean in(int min, int n, int max) {
		return min <= n && n < max;
	}

	@Override public boolean doesGuiPauseGame() {
		return false;
	}

	@Override public void updateScreen() {
		super.updateScreen();
		if(!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead) mc.thePlayer.closeScreen();
	}

	@Override public void onGuiClosed() {
		MinemapWaypoints.save();
	}
}
