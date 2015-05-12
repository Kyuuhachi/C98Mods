package c98.resourcefulEntities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

public class Part extends Component {
	public Part(ModelJSON o) {
		super(o);
	}
	
	public List<Component> children = new ArrayList();
	private ResourceLocation texture;
	private int[] texSize;
	private int expand;
	private double[] uvOffset;
	private boolean mirroruv;
	
	@Override public void doRender(RenderParams params) {
		if(!params.hide && texture != null && !params.noTex) {
			GL.pushAttrib();
			GL.bindTexture(owner.getPath(texture));
		}
		
		params.expand(expand);
		if(texSize != null) params.texSize(texSize[0], texSize[1]);
		if(uvOffset != null) params.uvOffset(uvOffset[0], uvOffset[1]);
		if(mirroruv) params.mirroruv();
		
		children.forEach(c -> c.render(params));
		
		if(!params.hide && texture != null && !params.noTex) GL.popAttrib();
	}
	
	@Override protected void parse(JsonNode o) throws IOException {
		if(o.has("children")) {
			JsonNode a = o.get("children");
			for(int i = 0; i < a.size(); i++)
				children.add(parsePart(owner, a.get(i)));
		}
		if(o.has("source")) {
			ResourceLocation resloc = new ResourceLocation(o.get("source").asText());
			ResourceLocation l = new ResourceLocation(resloc.getResourceDomain(), "models/components/" + resloc.getResourcePath() + ".json");
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
			JsonNode part = new ObjectMapper().readTree(new InputStreamReader(r.getInputStream(), Charsets.UTF_8));
			JsonNode elements = part.get("elements");
			for(JsonNode box : elements) {
				BoxPart mbox = new BoxPart(owner);
				parseTransforms(box, mbox);
				if(!box.has("from")) throw new IllegalArgumentException("from can't be null");
				if(!box.has("to")) throw new IllegalArgumentException("to can't be null");
				if(!box.has("uv")) throw new IllegalArgumentException("uv can't be null");
				JsonNode from = box.get("from");
				JsonNode to = box.get("to");
				JsonNode uv = box.get("uv");
				mbox.x1 = from.get(0).asDouble();
				mbox.y1 = from.get(1).asDouble();
				mbox.z1 = from.get(2).asDouble();
				mbox.x2 = to.get(0).asDouble();
				mbox.y2 = to.get(1).asDouble();
				mbox.z2 = to.get(2).asDouble();
				mbox.u = uv.get(0).asDouble();
				mbox.v = uv.get(1).asDouble();
				children.add(mbox);
			}
		}
		if(o.has("texture")) {
			texture = new ResourceLocation(o.get("texture").asText());
			if(!o.has("texsize")) throw new IllegalArgumentException("texsize can't be null if texture exists");
			JsonNode ts = o.get("texsize");
			texSize = new int[] {ts.get(0).asInt(), ts.get(1).asInt()};
		}
		if(o.has("uvoffset")) {
			JsonNode uv = o.get("uvoffset");
			uvOffset = new double[] {uv.get(0).asDouble(), uv.get(1).asDouble()};
		}
		if(o.has("expand")) expand = o.get("expand").asInt();
		mirroruv = mirror;
		if(o.has("mirroruv")) mirroruv ^= o.get("mirroruv").asBoolean();
	}
}
