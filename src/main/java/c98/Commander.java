package c98;

import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.block.Block;
import net.minecraft.client.resources.*;
import net.minecraft.command.common.CommandReplaceItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import c98.commander.CommandHighlighter;
import c98.commander.HighlightNode;
import c98.commander.node.*;
import c98.core.C98Log;
import c98.core.C98Mod;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Commander extends C98Mod implements IResourceManagerReloadListener {
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
			CommandHighlighter.highlighters.clear();
			for(Map.Entry<String, JsonNode> e : commands.entrySet())
				CommandHighlighter.highlighters.put(e.getKey(), getSeqNode(e.getValue()));
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

			case "block": return new ListHighlightNode(getIds(Block.blockRegistry.getKeys()));
			case "item": return new ListHighlightNode(getIds(Item.itemRegistry.getKeys()));
			case "entity": return new ListHighlightNode(() -> EntityList.func_180124_b());
			case "stat": return new ListHighlightNode(() -> map(StatList.allStats, s->s.statId));
			case "effect": return new ListHighlightNode(() -> Arrays.asList(Potion.func_180141_c()));
			case "particle": return new ListHighlightNode(() -> Arrays.asList(EnumParticleTypes.func_179349_a()));
			case "gamerule": return new ListHighlightNode(() -> Arrays.asList(mc.theWorld.getGameRules().getRules()));
			case "slot": return new ListHighlightNode(() -> new ArrayList(CommandReplaceItem.field_175785_a.keySet()));
			case "ench": return new ListHighlightNode(getIds(Enchantment.field_180307_E.keySet()));

			case "command": return new ListHighlightNode(() -> CommandHighlighter.highlighters.keySet());
			case "fullcommand": return CommandHighlighter.INSTANCE;

			case "xp": return new ValHighlightNode(can(s->Integer.parseInt(s.replaceFirst("[lL]$", ""))));

			case "sel": case "sel*": return new SelHighlightNode();
			case "json": return new JsonHighlightNode();
		} //@on
		C98Log.error("[Commander] Unknown parameter type: " + text);
		return new EOLHighlightNode();
	}

	private static Supplier<? extends Collection<String>> getIds(Set<ResourceLocation> reg) {
		return () -> {
			Collection<String> ids = new HashSet();
			reg.forEach(r -> {
				if(r.getResourceDomain().equals("minecraft")) ids.add(r.getResourcePath());
				ids.add(r.toString());
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
