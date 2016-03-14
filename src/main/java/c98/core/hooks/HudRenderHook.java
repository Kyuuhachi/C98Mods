package c98.core.hooks;

import java.util.HashMap;
import java.util.Map;
import c98.core.GL;
import c98.core.impl.HookImpl;

public interface HudRenderHook {
	public static enum HudElement {
		ALL("$all"),
		BOSS_HEALTH("bossHealth"),
		SLEEP("sleep"),
		OVERLAY_MESSAGE("overlayMessage"),
		TITLE("titleAndSubtitle"),
		CHAT("chat"),
		JUMP_BAR("jumpBar"),
		EXP_BAR("expBar"),
		EXP_LEVEL("expLevel"),
		TOOL_HIGHLIGHT("toolHighlight"),
		DEMO("demo"),
		ARMOR("armor"),
		HEALTH("health"),
		FOOD("food"),
		MOUNT_HEALTH("mountHealth"),
		AIR("air");

		public static Map<String, HudElement> bySection = new HashMap();
		static {
			for(HudElement e : values())
				bySection.put(e.section, e);
		}

		public final String section;

		private HudElement(String sec) {
			section = sec;
		}

		public void pre() {
			GL.pushAttrib();
			HookImpl.hudRenderHooks.forEach(a -> a.preRenderHud(this));
			GL.popAttrib();
		}

		public void post() {
			GL.pushAttrib();
			HookImpl.hudRenderHooks.forEach(a -> a.postRenderHud(this));
			GL.popAttrib();
		}
	}

	default void preRenderHud(HudElement e) {}

	default void postRenderHud(HudElement e) {}
}
