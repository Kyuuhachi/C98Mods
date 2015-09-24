package c98;

import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.resources.*;
import net.minecraft.command.common.CommandReplaceItem;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import c98.commander.*;
import c98.commander.node.*;
import c98.core.C98Log;
import c98.core.C98Mod;
import c98.core.launch.ASMer;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Commander extends C98Mod implements IResourceManagerReloadListener {
	@ASMer static class Chat extends GuiChat {
		@Override public void initGui() {
			super.initGui();
			inputField = new GuiCommandTextField(0, fontRendererObj, 4, height - 12, width - 4, 12, true);
			inputField.setMaxStringLength(100);
			inputField.setEnableBackgroundDrawing(false);
			inputField.setFocused(true);
			inputField.setText(defaultInputFieldText);
			inputField.setCanLoseFocus(false);
		}
	}
	
	@ASMer static class CmdBlock extends GuiCommandBlock {
		public CmdBlock(CommandBlockLogic p_i45032_1_) {
			super(p_i45032_1_);
		}
		
		@Override public void initGui() {
			super.initGui();
			commandTextField = new GuiCommandTextField(2, fontRendererObj, width / 2 - 150, 50, 300, 20, false);
			commandTextField.setMaxStringLength(32767);
			commandTextField.setFocused(true);
			commandTextField.setText(localCommandBlock.getCustomName());
		}
	}
	
	private static final ResourceLocation LOC = new ResourceLocation("c98/commander", "commands.json");
	
	@Override public void onResourceManagerReload(IResourceManager p_110549_1_) {
		try {
			ObjectMapper m = new ObjectMapper();
			m.enable(Feature.ALLOW_COMMENTS);
			List<IResource> resources = p_110549_1_.getAllResources(LOC);
			Map<String, JsonNode> commands = new HashMap();
			for(IResource r : resources) {
				JsonNode n = m.readTree(r.getInputStream());
				for(String s : (Iterable<String>)() -> n.fieldNames())
					commands.put(s, n.get(s));
			}
			CommandHighlighter.reset();
			for(Map.Entry<String, JsonNode> e : commands.entrySet())
				CommandHighlighter.register(e.getKey(), getSeqNode(e.getValue()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static HighlightNode getSeqNode(JsonNode j) {
		return new SeqHighlightNode(map(j, n -> {
			if(n.isTextual()) return getSpecial(n.asText());
			if(isKeywords(n)) return new KeywordHighlightNode(map(n, JsonNode::asText));
			if(isAny(j)) return new AnyHighlightNode(map(n, Commander::getSeqNode));
			return null;
		}));
	}
	
	private static boolean isKeywords(JsonNode j) {
		if(!j.isArray()) return false;
		for(JsonNode j2 : j)
			if(!j2.isTextual()) return false;
		return true;
	}
	
	private static boolean isAny(JsonNode j) {
		return j.isArray() && !isKeywords(j);
	}
	
	private static <A extends Iterable<C>, B, C> List<B> map(A j, Function<C, B> func) {
		return StreamSupport.stream(j.spliterator(), false).map(func).collect(Collectors.toList());
	}
	
	private static HighlightNode getSpecial(String text) {
		switch(text) {//@off
			case "?": return new OptHighlightNode();
			case "text*": return new EOLHighlightNode();
			case "text": return new ValHighlightNode(s -> true);
			case "i": return new ValHighlightNode(can(s -> Integer.parseInt(s)));
			case "f": return new ValHighlightNode(can(s -> Float.parseFloat(s)));
			case "bool": return new ValHighlightNode(can(s -> Boolean.parseBoolean(s)));
			case "coords": return new CoordsHighlightNode(3);
			case "coords2": return new CoordsHighlightNode(2);
			
			case "block": return new ListHighlightNode(getIds(Block.blockRegistry));
			case "item": return new ListHighlightNode(getIds(Item.itemRegistry));
			case "entity": return new ListHighlightNode(() -> EntityList.func_180124_b());
			case "stat": return new ListHighlightNode(() -> map(StatList.allStats, s->s.statId));
			case "effect": return new ListHighlightNode(() -> Arrays.asList(Potion.func_180141_c()));
			case "particle": return new ListHighlightNode(() -> Arrays.asList(EnumParticleTypes.func_179349_a()));
			case "gamerule": return new ListHighlightNode(() -> Arrays.asList(mc.theWorld.getGameRules().getRules()));
			case "slot": return new ListHighlightNode(() -> new ArrayList(CommandReplaceItem.field_175785_a.keySet()));
			case "ench": return new ListHighlightNode(() -> Arrays.asList(Enchantment.enchantmentsList));
			
			case "xp": return new ValHighlightNode(can(s->Integer.parseInt(s.replaceFirst("[lL]$", ""))));
			
			case "sel": case "sel*": return new SelHighlightNode();
			case "json": return new JsonHighlightNode(); //TODO
		} //@on
		C98Log.error("[Commander] Unknown parameter type: " + text);
		return null;
	}
	
	private static Supplier<? extends Collection> getIds(RegistryNamespaced reg) {
		return () -> {
			Collection<String> ids = new HashSet();
			reg.getKeys().forEach(r -> {
				ResourceLocation loc = (ResourceLocation)r;
				if(loc.getResourceDomain().equals("minecraft")) ids.add(loc.getResourcePath());
				ids.add(loc.toString());
			});
			return ids;
		};
	}
	
	private static Predicate<String> can(Consumer<String> func) {
		return s -> {
			try {
				func.accept(s);
				return true;
			} catch(Exception e) {
				return false;
			}
		};
	}
}
