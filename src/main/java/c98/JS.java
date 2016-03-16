package c98;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import c98.core.C98Mod;
import c98.core.launch.ASMer;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class JS extends C98Mod {
	public static ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
	public static ICommandSender sender;

	public static class JSCommand extends CommandBase {
		private static final Style ERROR_STYLE = new Style().setColor(TextFormatting.RED);
		private static final Style ERROR_LOC_STYLE = new Style().setUnderlined(true);

		@Override public String getCommandName() {
			return "js";
		}

		@Override public String getCommandUsage(ICommandSender sender_) {
			return "/js <script>";
		}

		@Override public int getRequiredPermissionLevel() {
			return 2;
		}

		@Override public void execute(MinecraftServer s, ICommandSender sender_, String[] args) throws CommandException {
			sender = sender_;
			if(args.length == 0) throw new WrongUsageException(getCommandUsage(sender));
			try {
				reset();
				Object o = engine.eval(buildString(args, 0));
				sender.addChatMessage(new TextComponentString(String.valueOf(o)));
			} catch(Exception e) {
				e.printStackTrace();
				List<ITextComponent> msg = error(e);
				for(ITextComponent c : msg)
					sender.addChatMessage(c);
			}
		}

		private static List<ITextComponent> error(Exception e) {
			if(!(e instanceof ScriptException)) return Arrays.asList(new TextComponentString(e.toString()).setChatStyle(ERROR_STYLE));
			String err = e.getCause().getMessage();
			String[] lines = err.split("\r?\n");
			List<ITextComponent> msg = new ArrayList();
			msg.add(new TextComponentString(lines[0]).setChatStyle(ERROR_STYLE));
			if(lines.length == 3) {
				int idx = lines[2].indexOf('^');
				String text = lines[1] + " ";
				ITextComponent loc = new TextComponentString("").setChatStyle(ERROR_STYLE);
				loc.appendText(text.substring(0, idx));
				loc.appendSibling(new TextComponentString(text.substring(idx, idx + 1)).setChatStyle(ERROR_LOC_STYLE));
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
		public JSCommandManager(MinecraftServer serv) {
			super(serv);
			registerCommand(new JSCommand());
		}
	}
}
