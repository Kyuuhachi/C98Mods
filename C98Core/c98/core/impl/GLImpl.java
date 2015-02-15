package c98.core.impl;

import static net.minecraft.client.renderer.GlStateManager.*;
import java.util.LinkedList;

public class GLImpl {
	boolean alpha_enabled;
	int alpha_b;
	float alpha_c;
	boolean lighting_enabled;
	boolean lights_enabled0;
	boolean lights_enabled1;
	boolean lights_enabled2;
	boolean lights_enabled3;
	boolean lights_enabled4;
	boolean lights_enabled5;
	boolean lights_enabled6;
	boolean lights_enabled7;
	boolean colorMaterial_enabled;
	int colorMaterial_b;
	int colorMaterial_c;
	boolean blend_enabled;
	int blend_b;
	int blend_c;
	int blend_d;
	int blend_e;
	boolean depth_enabled;
	boolean depth_b;
	int depth_c;
	boolean fog_enabled;
	int fog_b;
	float fog_c;
	float fog_d;
	float fog_e;
	boolean cull_enabled;
	int cull_face;
	boolean polygonOffset_enabled_a;
	boolean polygonOffset_enabled_b;
	float polygonOffset_c;
	float polygonOffset_d;
	boolean colorLogic_enabled;
	int colorLogic_op;
	boolean texGen_a_enable;
	int texGen_a_b;
	int texGen_a_c;
	boolean texGen_b_enable;
	int texGen_b_b;
	int texGen_b_c;
	boolean texGen_c_enable;
	int texGen_c_b;
	int texGen_c_c;
	boolean texGen_d_enable;
	int texGen_d_b;
	int texGen_d_c;
	double clear_a;
	float clear_b_a;
	float clear_b_b;
	float clear_b_c;
	float clear_b_d;
	int stencil_a_a;
	int stencil_a_b;
	int stencil_a_c;
	int stencil_b;
	int stencil_c;
	int stencil_d;
	int stencil_e;
	boolean normalize_enable;
	int currentTexture;
	boolean texture_enabled0;
	int texture_current0;
	boolean texture_enabled1;
	int texture_current1;
	boolean texture_enabled2;
	int texture_current2;
	boolean texture_enabled3;
	int texture_current3;
	boolean texture_enabled4;
	int texture_current4;
	boolean texture_enabled5;
	int texture_current5;
	boolean texture_enabled6;
	int texture_current6;
	boolean texture_enabled7;
	int texture_current7;
	int field_what;
	boolean rescaleNormal_enable;
	boolean colorMask_a;
	boolean colorMask_b;
	boolean colorMask_c;
	boolean colorMask_d;
	float color_a;
	float color_b;
	float color_c;
	float color_d;
	int viewport_a;
	int viewport_b;
	int viewport_c;
	int viewport_d;
	
	private void store() {
		alpha_enabled = alphaState.field_179208_a.currentState;
		alpha_b = alphaState.field_179206_b;
		alpha_c = alphaState.field_179207_c;
		lighting_enabled = lightingState.currentState;
		lights_enabled0 = field_179159_c[0].currentState;
		lights_enabled1 = field_179159_c[1].currentState;
		lights_enabled2 = field_179159_c[2].currentState;
		lights_enabled3 = field_179159_c[3].currentState;
		lights_enabled4 = field_179159_c[4].currentState;
		lights_enabled5 = field_179159_c[5].currentState;
		lights_enabled6 = field_179159_c[6].currentState;
		lights_enabled7 = field_179159_c[7].currentState;
		colorMaterial_enabled = colorMaterialState.field_179191_a.currentState;
		colorMaterial_b = colorMaterialState.field_179189_b;
		colorMaterial_c = colorMaterialState.field_179190_c;
		blend_enabled = blendState.field_179213_a.currentState;
		blend_b = blendState.field_179211_b;
		blend_c = blendState.field_179212_c;
		blend_d = blendState.field_179209_d;
		blend_e = blendState.field_179210_e;
		depth_enabled = depthState.field_179052_a.currentState;
		depth_b = depthState.field_179050_b;
		depth_c = depthState.field_179051_c;
		fog_enabled = fogState.field_179049_a.currentState;
		fog_b = fogState.field_179047_b;
		fog_c = fogState.field_179048_c;
		fog_d = fogState.field_179045_d;
		fog_e = fogState.field_179046_e;
		cull_enabled = cullState.field_179054_a.currentState;
		cull_face = cullState.field_179053_b;
		polygonOffset_enabled_a = polygonOffsetState.field_179044_a.currentState;
		polygonOffset_enabled_b = polygonOffsetState.field_179042_b.currentState;
		polygonOffset_c = polygonOffsetState.field_179043_c;
		polygonOffset_d = polygonOffsetState.field_179041_d;
		colorLogic_enabled = colorLogicState.field_179197_a.currentState;
		colorLogic_op = colorLogicState.field_179196_b;
		texGen_a_enable = texGenState.field_179064_a.field_179067_a.currentState;
		texGen_a_b = texGenState.field_179064_a.field_179065_b;
		texGen_a_c = texGenState.field_179064_a.field_179066_c;
		texGen_b_enable = texGenState.field_179062_b.field_179067_a.currentState;
		texGen_b_b = texGenState.field_179062_b.field_179065_b;
		texGen_b_c = texGenState.field_179062_b.field_179066_c;
		texGen_c_enable = texGenState.field_179063_c.field_179067_a.currentState;
		texGen_c_b = texGenState.field_179063_c.field_179065_b;
		texGen_c_c = texGenState.field_179063_c.field_179066_c;
		texGen_d_enable = texGenState.field_179061_d.field_179067_a.currentState;
		texGen_d_b = texGenState.field_179061_d.field_179065_b;
		texGen_d_c = texGenState.field_179061_d.field_179066_c;
		clear_a = clearState.field_179205_a;
		clear_b_a = clearState.field_179203_b.field_179195_a;
		clear_b_b = clearState.field_179203_b.green;
		clear_b_c = clearState.field_179203_b.blue;
		clear_b_d = clearState.field_179203_b.alpha;
		stencil_a_a = stencilState.field_179078_a.field_179081_a;
		stencil_a_b = stencilState.field_179078_a.field_179079_b;
		stencil_a_c = stencilState.field_179078_a.field_179080_c;
		stencil_b = stencilState.field_179076_b;
		stencil_c = stencilState.field_179077_c;
		stencil_d = stencilState.field_179074_d;
		stencil_e = stencilState.field_179075_e;
		normalize_enable = normalizeState.currentState;
		currentTexture = field_179162_o;
		texture_enabled0 = field_179174_p[0].field_179060_a.currentState;
		texture_current0 = field_179174_p[0].field_179059_b;
		texture_enabled1 = field_179174_p[1].field_179060_a.currentState;
		texture_current1 = field_179174_p[1].field_179059_b;
		texture_enabled2 = field_179174_p[2].field_179060_a.currentState;
		texture_current2 = field_179174_p[2].field_179059_b;
		texture_enabled3 = field_179174_p[3].field_179060_a.currentState;
		texture_current3 = field_179174_p[3].field_179059_b;
		texture_enabled4 = field_179174_p[4].field_179060_a.currentState;
		texture_current4 = field_179174_p[4].field_179059_b;
		texture_enabled5 = field_179174_p[5].field_179060_a.currentState;
		texture_current5 = field_179174_p[5].field_179059_b;
		texture_enabled6 = field_179174_p[6].field_179060_a.currentState;
		texture_current6 = field_179174_p[6].field_179059_b;
		texture_enabled7 = field_179174_p[7].field_179060_a.currentState;
		texture_current7 = field_179174_p[7].field_179059_b;
		field_what = field_179173_q;
		rescaleNormal_enable = rescaleNormalState.currentState;
		colorMask_a = colorMaskState.field_179188_a;
		colorMask_b = colorMaskState.field_179186_b;
		colorMask_c = colorMaskState.field_179187_c;
		colorMask_d = colorMaskState.field_179185_d;
		color_a = colorState.field_179195_a;
		color_b = colorState.green;
		color_c = colorState.blue;
		color_d = colorState.alpha;
		viewport_a = field_179169_u.field_179058_a;
		viewport_b = field_179169_u.field_179056_b;
		viewport_c = field_179169_u.field_179057_c;
		viewport_d = field_179169_u.field_179055_d;
	}
	
	private void load() {
		alphaState.field_179208_a.currentState = alpha_enabled;
		alphaState.field_179206_b = alpha_b;
		alphaState.field_179207_c = alpha_c;
		lightingState.currentState = lighting_enabled;
		field_179159_c[0].currentState = lights_enabled0;
		field_179159_c[1].currentState = lights_enabled1;
		field_179159_c[2].currentState = lights_enabled2;
		field_179159_c[3].currentState = lights_enabled3;
		field_179159_c[4].currentState = lights_enabled4;
		field_179159_c[5].currentState = lights_enabled5;
		field_179159_c[6].currentState = lights_enabled6;
		field_179159_c[7].currentState = lights_enabled7;
		colorMaterialState.field_179191_a.currentState = colorMaterial_enabled;
		colorMaterialState.field_179189_b = colorMaterial_b;
		colorMaterialState.field_179190_c = colorMaterial_c;
		blendState.field_179213_a.currentState = blend_enabled;
		blendState.field_179211_b = blend_b;
		blendState.field_179212_c = blend_c;
		blendState.field_179209_d = blend_d;
		blendState.field_179210_e = blend_e;
		depthState.field_179052_a.currentState = depth_enabled;
		depthState.field_179050_b = depth_b;
		depthState.field_179051_c = depth_c;
		fogState.field_179049_a.currentState = fog_enabled;
		fogState.field_179047_b = fog_b;
		fogState.field_179048_c = fog_c;
		fogState.field_179045_d = fog_d;
		fogState.field_179046_e = fog_e;
		cullState.field_179054_a.currentState = cull_enabled;
		cullState.field_179053_b = cull_face;
		polygonOffsetState.field_179044_a.currentState = polygonOffset_enabled_a;
		polygonOffsetState.field_179042_b.currentState = polygonOffset_enabled_b;
		polygonOffsetState.field_179043_c = polygonOffset_c;
		polygonOffsetState.field_179041_d = polygonOffset_d;
		colorLogicState.field_179197_a.currentState = colorLogic_enabled;
		colorLogicState.field_179196_b = colorLogic_op;
		texGenState.field_179064_a.field_179067_a.currentState = texGen_a_enable;
		texGenState.field_179064_a.field_179065_b = texGen_a_b;
		texGenState.field_179064_a.field_179066_c = texGen_a_c;
		texGenState.field_179062_b.field_179067_a.currentState = texGen_b_enable;
		texGenState.field_179062_b.field_179065_b = texGen_b_b;
		texGenState.field_179062_b.field_179066_c = texGen_b_c;
		texGenState.field_179063_c.field_179067_a.currentState = texGen_c_enable;
		texGenState.field_179063_c.field_179065_b = texGen_c_b;
		texGenState.field_179063_c.field_179066_c = texGen_c_c;
		texGenState.field_179061_d.field_179067_a.currentState = texGen_d_enable;
		texGenState.field_179061_d.field_179065_b = texGen_d_b;
		texGenState.field_179061_d.field_179066_c = texGen_d_c;
		clearState.field_179205_a = clear_a;
		clearState.field_179203_b.field_179195_a = clear_b_a;
		clearState.field_179203_b.green = clear_b_b;
		clearState.field_179203_b.blue = clear_b_c;
		clearState.field_179203_b.alpha = clear_b_d;
		stencilState.field_179078_a.field_179081_a = stencil_a_a;
		stencilState.field_179078_a.field_179079_b = stencil_a_b;
		stencilState.field_179078_a.field_179080_c = stencil_a_c;
		stencilState.field_179076_b = stencil_b;
		stencilState.field_179077_c = stencil_c;
		stencilState.field_179074_d = stencil_d;
		stencilState.field_179075_e = stencil_e;
		normalizeState.currentState = normalize_enable;
		field_179162_o = currentTexture;
		field_179174_p[0].field_179060_a.currentState = texture_enabled0;
		field_179174_p[0].field_179059_b = texture_current0;
		field_179174_p[1].field_179060_a.currentState = texture_enabled1;
		field_179174_p[1].field_179059_b = texture_current1;
		field_179174_p[2].field_179060_a.currentState = texture_enabled2;
		field_179174_p[2].field_179059_b = texture_current2;
		field_179174_p[3].field_179060_a.currentState = texture_enabled3;
		field_179174_p[3].field_179059_b = texture_current3;
		field_179174_p[4].field_179060_a.currentState = texture_enabled4;
		field_179174_p[4].field_179059_b = texture_current4;
		field_179174_p[5].field_179060_a.currentState = texture_enabled5;
		field_179174_p[5].field_179059_b = texture_current5;
		field_179174_p[6].field_179060_a.currentState = texture_enabled6;
		field_179174_p[6].field_179059_b = texture_current6;
		field_179174_p[7].field_179060_a.currentState = texture_enabled7;
		field_179174_p[7].field_179059_b = texture_current7;
		field_179173_q = field_what;
		rescaleNormalState.currentState = rescaleNormal_enable;
		colorMaskState.field_179188_a = colorMask_a;
		colorMaskState.field_179186_b = colorMask_b;
		colorMaskState.field_179187_c = colorMask_c;
		colorMaskState.field_179185_d = colorMask_d;
		colorState.field_179195_a = color_a;
		colorState.green = color_b;
		colorState.blue = color_c;
		colorState.alpha = color_d;
		field_179169_u.field_179058_a = viewport_a;
		field_179169_u.field_179056_b = viewport_b;
		field_179169_u.field_179057_c = viewport_c;
		field_179169_u.field_179055_d = viewport_d;
	}
	
	private static LinkedList<GLImpl> stack = new LinkedList();
	
	public static void pushAttrib() {
		GLImpl state = new GLImpl();
		state.store();
		stack.push(state);
	}
	
	public static void popAttrib() {
		GLImpl state = stack.pop();
		state.load();
	}
	
}
