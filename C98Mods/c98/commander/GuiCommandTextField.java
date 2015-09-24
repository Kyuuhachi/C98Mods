package c98.commander;

import net.minecraft.client.gui.*;

public class GuiCommandTextField extends GuiTextField {
	private boolean needSlash;
	
	public GuiCommandTextField(int p_i45542_1_, FontRenderer p_i45542_2_, int p_i45542_3_, int p_i45542_4_, int p_i45542_5_, int p_i45542_6_, boolean needSlash) {
		super(p_i45542_1_, p_i45542_2_, p_i45542_3_, p_i45542_4_, p_i45542_5_, p_i45542_6_);
		this.needSlash = needSlash;
	}
	
	@Override public void drawTextBox() {
		if(getVisible()) {
			if(getEnableBackgroundDrawing()) {
				drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, -6250336);
				drawRect(xPosition, yPosition, xPosition + width, yPosition + height, -16777216);
			}
			
			int color = isEnabled ? enabledColor : disabledColor;
			int vcolor = 0xD0D0D0;
			int selStart = cursorPosition - lineScrollOffset;
			int selEnd = selectionEnd - lineScrollOffset;
			String sub = fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
			boolean visibleSel = selStart >= 0 && selStart <= sub.length();
			boolean drawCursor = isFocused && cursorCounter / 6 % 2 == 0 && visibleSel;
			int x = enableBackgroundDrawing ? xPosition + 4 : xPosition;
			int y = enableBackgroundDrawing ? yPosition + (height - 8) / 2 : yPosition;
			int selX = x;
			
			if(selEnd > sub.length()) selEnd = sub.length();
			
			if(sub.length() > 0) {
				String beforeSel = visibleSel ? sub.substring(0, selStart) : sub;
				selX = x + fontRendererInstance.getStringWidth(beforeSel) + 1;
			}
			
			boolean verticalCursor = cursorPosition < text.length() || text.length() >= getMaxStringLength();
			int cursorX = selX;
			
			if(!visibleSel) cursorX = selStart > 0 ? x + width : x;
			else if(verticalCursor) cursorX--;
			
			if(sub.length() > 0) {
				String toDraw = sub;
				if(!needSlash || text.startsWith("/")) {
					int start = lineScrollOffset;
					int end = start + sub.length();
					HighlightResult r = CommandHighlighter.highlight(text, start, end);
					if(r.error) vcolor = color = fontRendererInstance.colorCode[HighlightNode.ERROR.getColor().ordinal()];
					toDraw = r.text.getFormattedText();
				}
				fontRendererInstance.func_175063_a(toDraw, x, y, color);
			}
			
			if(drawCursor) if(verticalCursor) Gui.drawRect(cursorX, y - 1, cursorX + 1, y + 1 + fontRendererInstance.FONT_HEIGHT, 0xFF000000 | vcolor);
			else fontRendererInstance.func_175063_a("_", cursorX, y, color);
			
			if(selEnd != selStart) {
				int selX2 = x + fontRendererInstance.getStringWidth(sub.substring(0, selEnd));
				drawCursorVertical(cursorX, y - 1, selX2 - 1, y + 1 + fontRendererInstance.FONT_HEIGHT);
			}
		}
	}
}
