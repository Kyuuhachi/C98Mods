package c98;

import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minecraft.command.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.*;
import c98.core.C98Mod;
import c98.core.launch.ASMer;

public class JS extends C98Mod {
	public static ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
	public static ICommandSender sender;

	public static class JSCommand extends CommandBase {
		private static final ChatStyle ERROR_STYLE = new ChatStyle().setColor(EnumChatFormatting.RED);
		private static final ChatStyle ERROR_LOC_STYLE = new ChatStyle().setUnderlined(true);

		@Override public String getCommandName() {
			return "js";
		}

		@Override public String getCommandUsage(ICommandSender sender_) {
			return "/js <script>";
		}

		@Override public int getRequiredPermissionLevel() {
			return 2;
		}

		@Override public void processCommand(ICommandSender sender_, String[] args) throws CommandException {
			sender = sender_;
			if(args.length == 0) throw new WrongUsageException(getCommandUsage(sender));
			try {
				reset();
				Object o = engine.eval(func_180529_a(args, 0));
				sender.addChatMessage(new ChatComponentText(String.valueOf(o)));
			} catch(Exception e) {
				e.printStackTrace();
				List<IChatComponent> msg = error(e);
				for(IChatComponent c : msg)
					sender.addChatMessage(c);
			}
		}

		private static List<IChatComponent> error(Exception e) {
			if(!(e instanceof ScriptException)) return Arrays.asList(new ChatComponentText(e.toString()).setChatStyle(ERROR_STYLE));
			String err = e.getCause().getMessage();
			String[] lines = err.split("\r?\n");
			List<IChatComponent> msg = new ArrayList();
			msg.add(new ChatComponentText(lines[0]).setChatStyle(ERROR_STYLE));
			if(lines.length == 3) {
				int idx = lines[2].indexOf('^');
				String text = lines[1] + " ";
				IChatComponent loc = new ChatComponentText("").setChatStyle(ERROR_STYLE);
				loc.appendText(text.substring(0, idx));
				loc.appendSibling(new ChatComponentText(text.substring(idx, idx + 1)).setChatStyle(ERROR_LOC_STYLE));
				loc.appendText(text.substring(idx + 1));
				msg.add(loc);
			}
			return msg;
		}

		static private void reset() {
			try {
				engine.put("sender", sender);
				engine.put("world", sender.getEntityWorld());
				engine.put("pos", sender.getPosition());

				importJS(Blocks.class);
				importJS(Items.class);
			} catch(ScriptException e) {
				e.printStackTrace();
			}
		}

		static private void importJS(Class clazz) throws ScriptException {
			engine.eval(clazz.getSimpleName() + "=Java.type(\"" + clazz.getName() + "\")");
		}
	}

	@ASMer static class JSCommandManager extends ServerCommandManager {
		public JSCommandManager() {
			registerCommand(new JSCommand());
		}
	}
}
