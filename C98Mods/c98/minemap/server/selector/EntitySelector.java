package c98.minemap.server.selector;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.entity.StringEntityProperty;
import c98.minemap.server.selector.prop.simple.*;

public class EntitySelector {
	public static Map<Class, String> classToId = new HashMap();
	static {
		initClasses();
		initProperties();
	}
	
	public static void initClasses() {
		classToId.putAll(EntityList.classToStringMapping);
		classToId.putAll(TileEntity.classToNameMap);
		classToId.put(Entity.class, "Entity");
		classToId.put(EntityAgeable.class, "Ageable");
		classToId.put(EntityLivingBase.class, "Living");
		classToId.put(EntityMinecart.class, "Minecart");
		classToId.put(EntityLiving.class, "NonPlayer");
		classToId.put(EntityPlayer.class, "Player");
		classToId.put(EntityPlayerSP.class, "Self");
		classToId.put(EntityFireball.class, "FireballBase");
		classToId.put(EntityHanging.class, "Hanging");
		classToId.put(EntityAmbientCreature.class, "AmbientCreature");
		classToId.put(EntityCreature.class, "Creature");
		classToId.put(EntityGolem.class, "Golem");
		classToId.put(EntityMob.class, "Hostile");
		classToId.put(EntityWaterMob.class, "WaterMob");
		classToId.put(EntityFlying.class, "Flying");
		classToId.put(EntityThrowable.class, "Throwable");
	}
	
	public static void initProperties() {
		//@off
		SelectorProperties.add(new SimpleBooleanProperty("burning", Entity.class, 0, 0));
		SelectorProperties.add(new SimpleIntProperty    ("air", Entity.class, 1));
		SelectorProperties.add(new SimpleIntProperty    ("age", EntityAgeable.class, 12));
		SelectorProperties.add(new SimpleFloatProperty  ("health", EntityLivingBase.class, 6));
		SelectorProperties.add(new SimpleIntProperty    ("arrowCount", EntityLivingBase.class, 9));
		SelectorProperties.add(new SimpleBooleanProperty("forceNametag", EntityLiving.class, 11, 0));
		SelectorProperties.add(new SimpleFloatProperty  ("absorption", EntityPlayer.class, 17));
		SelectorProperties.add(new SimpleIntProperty    ("score", EntityPlayer.class, 18));
		SelectorProperties.add(new SimpleBooleanProperty("tame", EntityTameable.class, 16, 2));
		SelectorProperties.add(new SimpleStringProperty ("owner", EntityTameable.class, 17));
		SelectorProperties.add(new SimpleBooleanProperty("critical", EntityArrow.class, 16, 0));
		SelectorProperties.add(new SimpleBooleanProperty("charging", EntityBlaze.class, 16, 0));
		SelectorProperties.add(new SimpleIntProperty    ("fuse", EntityCreeper.class, 16));
		SelectorProperties.add(new SimpleBooleanProperty("charged", EntityBoat.class, 17, 0));
		SelectorProperties.add(new SimpleBooleanProperty("screaming", EntityEnderman.class, 18, 0));
		SelectorProperties.add(new SimpleBooleanProperty("tame", EntityHorse.class, 16, 1));
		SelectorProperties.add(new SimpleBooleanProperty("saddle", EntityHorse.class, 16, 2));
		SelectorProperties.add(new SimpleBooleanProperty("chest", EntityHorse.class, 16, 3));
		SelectorProperties.add(new SimpleIntProperty    ("type", EntityHorse.class, 19)); //Maybe use a string instead?
		SelectorProperties.add(new SimpleStringProperty ("owner", EntityHorse.class, 21));
		SelectorProperties.add(new SimpleStringProperty ("armor", EntityHorse.class, 22));
		SelectorProperties.add(new SimpleBooleanProperty("charging", EntityGhast.class, 16, 0));
		SelectorProperties.add(new SimpleBooleanProperty("powered", EntityMinecartFurnace.class, 16, 0));
		SelectorProperties.add(new SimpleIntProperty    ("skin", EntityOcelot.class, 18));
		SelectorProperties.add(new SimpleBooleanProperty("saddled", EntityPig.class, 16, 0));
		//TODO sheep color
		SelectorProperties.add(new SimpleBooleanProperty("sheared", EntitySheep.class, 16, 4));
		SelectorProperties.add(new SimpleBooleanProperty("wither", EntitySkeleton.class, 13, 0));
		SelectorProperties.add(new SimpleIntProperty    ("size", EntitySlime.class, 16));
		SelectorProperties.add(new SimpleIntProperty    ("profession", EntityVillager.class, 16));
		SelectorProperties.add(new SimpleBooleanProperty("created", EntityIronGolem.class, 16, 0));
		SelectorProperties.add(new SimpleBooleanProperty("angry", EntityWitch.class, 21, 0));
		SelectorProperties.add(new SimpleIntProperty    ("charge", EntityWither.class, 20));
		SelectorProperties.add(new SimpleBooleanProperty("angry", EntityWolf.class, 16, 1));
		SelectorProperties.add(new SimpleIntProperty    ("collarColor", EntityWolf.class, 20));
		SelectorProperties.add(new SimpleBooleanProperty("baby", EntityZombie.class, 12, 0));
		SelectorProperties.add(new SimpleBooleanProperty("villager", EntityZombie.class, 12, 0));
		SelectorProperties.add(new SimpleBooleanProperty("converting", EntityZombie.class, 14, 0));
		SelectorProperties.add(new StringEntityProperty ("name", Entity.class) {@Override public String getValue(Entity e) {return e.getName();}});
		//@on
	}
	
	public static Selector parse(String selector) {
		return Parser.parse(Tokenizer.getTokens(selector));
	}
}