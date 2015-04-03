package c98.resourcefulEntities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;
import com.google.common.base.Charsets;
import com.google.gson.*;

public class Part extends Component {
	public Part(ModelJSON o) {
		super(o);
	}
	
	public List<Component> children = new ArrayList();
	private ResourceLocation texture;
	private int[] texSize;
	private int expand;
	private float[] uvOffset;
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
	
	@Override protected void parse(JsonObject o) throws IOException {
		if(o.has("children")) {
			JsonArray a = o.get("children").getAsJsonArray();
			for(int i = 0; i < a.size(); i++)
				children.add(parsePart(owner, a.get(i).getAsJsonObject()));
		}
		if(o.has("source")) {
			ResourceLocation resloc = new ResourceLocation(o.get("source").getAsString());
			ResourceLocation l = new ResourceLocation(resloc.getResourceDomain(), "models/components/" + resloc.getResourcePath() + ".json");
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
			JsonObject part = new JsonParser().parse(new InputStreamReader(r.getInputStream(), Charsets.UTF_8)).getAsJsonObject();
			JsonArray elements = part.get("elements").getAsJsonArray();
			for(JsonElement e : elements) {
				JsonObject box = e.getAsJsonObject();
				BoxPart mbox = new BoxPart(owner);
				parseTransforms(box, mbox);
				if(!box.has("from")) throw new IllegalArgumentException("from can't be null");
				if(!box.has("to")) throw new IllegalArgumentException("to can't be null");
				if(!box.has("uv")) throw new IllegalArgumentException("uv can't be null");
				JsonArray from = box.get("from").getAsJsonArray();
				JsonArray to = box.get("to").getAsJsonArray();
				JsonArray uv = box.get("uv").getAsJsonArray();
				mbox.x1 = from.get(0).getAsFloat();
				mbox.y1 = from.get(1).getAsFloat();
				mbox.z1 = from.get(2).getAsFloat();
				mbox.x2 = to.get(0).getAsFloat();
				mbox.y2 = to.get(1).getAsFloat();
				mbox.z2 = to.get(2).getAsFloat();
				mbox.u = uv.get(0).getAsFloat();
				mbox.v = uv.get(1).getAsFloat();
				children.add(mbox);
			}
		}
		if(o.has("texture")) {
			texture = new ResourceLocation(o.get("texture").getAsString());
			if(!o.has("texsize")) throw new IllegalArgumentException("texsize can't be null if texture exists");
			JsonArray ts = o.get("texsize").getAsJsonArray();
			texSize = new int[] {ts.get(0).getAsInt(), ts.get(1).getAsInt()};
		}
		if(o.has("uvoffset")) {
			JsonArray uv = o.get("uvoffset").getAsJsonArray();
			uvOffset = new float[] {uv.get(0).getAsFloat(), uv.get(1).getAsFloat()};
		}
		if(o.has("expand")) expand = o.get("expand").getAsInt();
		mirroruv = mirror;
		if(o.has("mirroruv")) mirroruv ^= o.get("mirroruv").getAsBoolean();
	}
}
