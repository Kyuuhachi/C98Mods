package c98.minemapWaypoints;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import c98.MinemapWaypoints;
import c98.MinemapWaypoints.Config.Waypoint;
import c98.minemap.api.IconStyle;
import c98.minemap.api.MapUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiEditWaypoint extends GuiScreen {
	private class MapButton extends GuiButton {
		public MapButton(int id) {
			super(id, SLOT_X(id), SLOT_Y(id), SLOT_W, SLOT_H, "");
		}

		@Override public void drawButton(Minecraft mc_, int mouseX, int mouseY) {
			boolean selected = false;
			IconStyle style = point.style.clone();
			int x = xPosition;
			int y = yPosition;
			int w = width;
			int h = height;
			if(id < 16) {
				int rgb = 0xFF000000 | fontRendererObj.colorCode[id];
				selected = style.color.getRGB() == rgb;
				drawRect(x, y, x + w, y + h, rgb);
			} else {
				MapUtils.drawIcon(id - 16, -1, x + w / 2, y + h / 2, 2);
				selected = style.shape == id - 16;
			}
			if(isMouseOver()) drawRect(x, y, x + w, y + h, 0x80FFFFFF);
			if(selected) {
				int c = 0xFFFFFF00;
				drawHorizontalLine(x, x + w - 1, y, c);
				drawHorizontalLine(x, x + w - 1, y + h - 1, c);
				drawVerticalLine(x, y, y + h - 1, c);
				drawVerticalLine(x + w - 1, y, y + h - 1, c);
			}
		}
	}

	private static final ResourceLocation BACKGROUND = new ResourceLocation("c98/minemapwaypoints", "edit_waypoint.png");
	private static final int GUI_W = 159, GUI_H = 110;
	private static final int NAME_X = 7 + 3, NAME_Y = 17 + 4, NAME_W = GUI_W - NAME_X * 2, NAME_H = 12;
	private static final int COORDS_X = NAME_X, COORDS_Y = NAME_Y + 18, COORDS_W = 41, COORDS_H = NAME_H, COORDS_D = COORDS_W + 8;
	private static final int SLOT_Y = 54, SLOT_W = 16, SLOT_H = SLOT_W, SLOT_X = (GUI_W - SLOT_W * 8) / 2;

	private int GUI_X() {
		return (width - GUI_W) / 2;
	}

	private int GUI_Y() {
		return (height - GUI_H) / 2;
	}

	private int SLOT_X(int i) {
		return GUI_X() + SLOT_X + SLOT_W * (i % 8);
	}

	private int SLOT_Y(int i) {
		return GUI_Y() + SLOT_Y + SLOT_H * (i / 8);
	}

	private Waypoint point;
	private GuiScreen parent;

	private GuiTextField nameField;
	private GuiTextField xField, yField, zField;
	private GuiTextField[] textFields;

	public GuiEditWaypoint(World world, Waypoint waypoint) {
		point = waypoint;
		MinemapWaypoints.add(world, point);
		parent = Minecraft.getMinecraft().currentScreen;
	}

	@Override public void initGui() {
		Keyboard.enableRepeatEvents(true);
		nameField = new GuiTextField(0, fontRendererObj, GUI_X() + NAME_X, GUI_Y() + NAME_Y, NAME_W, NAME_H);
		initField(nameField);
		nameField.setMaxStringLength(16);
		nameField.setFocused(true);
		nameField.setText(point.name == null ? "" : point.name);
		for(int i = 0; i < 3; i++) {
			GuiTextField f = new GuiTextField(i + 1, fontRendererObj, GUI_X() + COORDS_X + COORDS_D * i, GUI_Y() + COORDS_Y, COORDS_W, COORDS_H);
			if(i == 0) xField = f;
			if(i == 1) yField = f;
			if(i == 2) zField = f;
			initField(f);
			f.setMaxStringLength(6);
		}

		if(point.position.length == 2) {
			xField.setText("" + point.position[0]);
			zField.setText("" + point.position[1]);
		} else {
			xField.setText("" + point.position[0]);
			yField.setText("" + point.position[1]);
			zField.setText("" + point.position[2]);
		}

		textFields = new GuiTextField[] {nameField, xField, yField, zField};

		for(int i = 0; i < 16 + 8; i++)
			buttonList.add(new MapButton(i));
	}

	private static void initField(GuiTextField f) {
		f.setTextColor(-1);
		f.setDisabledTextColour(-1);
		f.setEnableBackgroundDrawing(false);
	}

	@Override public void actionPerformed(GuiButton button) {
		if(button.id < 16) point.style.color = new Color(fontRendererObj.colorCode[button.id]);
		else point.style.shape = button.id - 16;
	}

	@Override public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		MinemapWaypoints.save();
	}

	@Override public boolean doesGuiPauseGame() {
		return false;
	}

	@Override public void updateScreen() {
		super.updateScreen();
		if(!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead) mc.displayGuiScreen(parent);

		point.name = nameField.getText();
		if(validate(xField, false) & validate(yField, true) & validate(zField, false)) {
			boolean y = validate(yField, false);
			int[] pos;
			if(y) pos = new int[] {val(xField), val(yField), val(zField)};
			else pos = new int[] {val(xField), val(zField)};
			point.position = pos;
		}
	}

	private static int val(GuiTextField f) {
		return Integer.parseInt(f.getText());
	}

	private static boolean validate(GuiTextField f, boolean allowEmpty) {
		boolean v = false;
		try {
			val(f);
			v = true;
		} catch(NumberFormatException e) {
			v = allowEmpty && f.getText().isEmpty();
		}
		return v;
	}

	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(BACKGROUND);
		drawTexturedModalRect(GUI_X(), GUI_Y(), 0, 0, GUI_W, GUI_H);
		fontRendererObj.drawString("Edit waypoint", GUI_X() + 8, GUI_Y() + 6, 0x404040);
		super.drawScreen(mouseX, mouseY, partialTicks);
		for(GuiTextField f : textFields)
			f.drawTextBox();
	}

	@Override public void keyTyped(char typedChar, int keyCode) {
		if(keyCode == 1) mc.displayGuiScreen(parent);
		if(keyCode == Keyboard.KEY_TAB) {
			int cur = -1;
			int next = 0;
			for(int i = 0; i < textFields.length; i++)
				if(textFields[i].isFocused()) {
					cur = i;
					next = (i + 1) % textFields.length;
				}

			if(cur != -1) {
				textFields[cur].setFocused(false);
				textFields[cur].setCursorPositionZero();
				textFields[cur].setSelectionPos(0);
			}
			if(next != -1) {
				textFields[next].setFocused(false);
				textFields[next].setCursorPositionZero();
				textFields[next].setSelectionPos(0);
			}
		}
		for(GuiTextField f : textFields)
			f.textboxKeyTyped(typedChar, keyCode);
	}

	@Override public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(GuiTextField f : textFields)
			f.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
