package c98.minemapMarkers.selector;

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
import c98.minemapMarkers.selector.prop.*;

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
		final String[] horseTypes = {"normal", "donkey", "mule", "zombie", "skeleton"};
		//@off
		SelectorProperties.addEntity("burning",      Entity.class,                new SimpleBooleanProperty(0, 0));
		SelectorProperties.addEntity("air",          Entity.class,                new SimpleIntProperty(1));
		SelectorProperties.addEntity("age",          EntityAgeable.class,         new SimpleIntProperty(12));
		SelectorProperties.addEntity("health",       EntityLivingBase.class,      new SimpleFloatProperty(6));
		SelectorProperties.addEntity("arrowCount",   EntityLivingBase.class,      new SimpleIntProperty(9));
		SelectorProperties.addEntity("forceNametag", EntityLiving.class,          new SimpleBooleanProperty(11, 0));
		SelectorProperties.addEntity("absorption",   EntityPlayer.class,          new SimpleFloatProperty(17));
		SelectorProperties.addEntity("score",        EntityPlayer.class,          new SimpleIntProperty(18));
		SelectorProperties.addEntity("tame",         EntityTameable.class,        new SimpleBooleanProperty(16, 2));
		SelectorProperties.addEntity("owner",        EntityTameable.class,        new SimpleStringProperty(17));
		SelectorProperties.addEntity("critical",     EntityArrow.class,           new SimpleBooleanProperty(16, 0));
		SelectorProperties.addEntity("charging",     EntityBlaze.class,           new SimpleBooleanProperty(16, 0));
		SelectorProperties.addEntity("fuse",         EntityCreeper.class,         new SimpleIntProperty(16));
		SelectorProperties.addEntity("charged",      EntityBoat.class,            new SimpleBooleanProperty(17, 0));
		SelectorProperties.addEntity("screaming",    EntityEnderman.class,        new SimpleBooleanProperty(18, 0));
		SelectorProperties.addEntity("tame",         EntityHorse.class,           new SimpleBooleanProperty(16, 1));
		SelectorProperties.addEntity("saddle",       EntityHorse.class,           new SimpleBooleanProperty(16, 2));
		SelectorProperties.addEntity("chest",        EntityHorse.class,           new SimpleBooleanProperty(16, 3));
		SelectorProperties.addEntity("type",         EntityHorse.class,           SelectorProperties.STRING, e -> horseTypes[((EntityHorse)e).getHorseType()]);
		SelectorProperties.addEntity("owner",        EntityHorse.class,           new SimpleStringProperty(21));
		SelectorProperties.addEntity("armor",        EntityHorse.class,           new SimpleIntProperty(22));
		SelectorProperties.addEntity("charging",     EntityGhast.class,           new SimpleBooleanProperty(16, 0));
		SelectorProperties.addEntity("powered",      EntityMinecartFurnace.class, new SimpleBooleanProperty(16, 0));
		SelectorProperties.addEntity("skin",         EntityOcelot.class,          new SimpleIntProperty(18));
		SelectorProperties.addEntity("saddled",      EntityPig.class,             new SimpleBooleanProperty(16, 0));
		SelectorProperties.addEntity("color",        EntitySheep.class,           SelectorProperties.STRING, e -> ((EntitySheep)e).func_175509_cj().getName());
		SelectorProperties.addEntity("sheared",      EntitySheep.class,           new SimpleBooleanProperty(16, 4));
		SelectorProperties.addEntity("wither",       EntitySkeleton.class,        new SimpleBooleanProperty(13, 0));
		SelectorProperties.addEntity("size",         EntitySlime.class,           new SimpleIntProperty(16));
		SelectorProperties.addEntity("profession",   EntityVillager.class,        new SimpleIntProperty(16));
		SelectorProperties.addEntity("created",      EntityIronGolem.class,       new SimpleBooleanProperty(16, 0));
		SelectorProperties.addEntity("angry",        EntityWitch.class,           new SimpleBooleanProperty(21, 0));
		SelectorProperties.addEntity("charge",       EntityWither.class,          new SimpleIntProperty(20));
		SelectorProperties.addEntity("angry",        EntityWolf.class,            new SimpleBooleanProperty(16, 1));
		SelectorProperties.addEntity("collarColor",  EntityWolf.class,            new SimpleIntProperty(20));
		SelectorProperties.addEntity("baby",         EntityZombie.class,          new SimpleBooleanProperty(12, 0));
		SelectorProperties.addEntity("villager",     EntityZombie.class,          new SimpleBooleanProperty(12, 0));
		SelectorProperties.addEntity("converting",   EntityZombie.class,          new SimpleBooleanProperty(14, 0));
		SelectorProperties.addEntity("name",         Entity.class,                SelectorProperties.STRING, e -> e.getName());
		//@on
	}
	
	public static Selector parse(String selector) {
		return Parser.parse(Tokenizer.getTokens(selector));
	}
}