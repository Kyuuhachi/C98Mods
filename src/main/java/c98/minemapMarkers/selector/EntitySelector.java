package c98.minemapMarkers.selector;

import static c98.minemapMarkers.selector.SelectorProperties.addEntity;

import java.util.HashMap;
import java.util.Map;

import c98.minemapMarkers.selector.prop.*;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;

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
		classToId.put(EntityAnimal.class, "Animal");
	}

	public static void initProperties() {
		String[] colors = new String[16];
		for(int i = 0; i < 16; i++)
			colors[i] = EnumDyeColor.byDyeDamage(i).name;
		//@off
		addEntity(Entity.class,                "air",          new SimpleIntProperty(Entity.AIR));
		addEntity(Entity.class,                "burning",      new SimpleBooleanProperty(Entity.FLAGS, 0));
		addEntity(Entity.class,                "elytra",       new SimpleBooleanProperty(Entity.FLAGS, 7));
		addEntity(Entity.class,                "forceNametag", new SimpleBooleanProperty(Entity.CUSTOM_NAME_VISIBLE));
		addEntity(Entity.class,                "glowing",      new SimpleBooleanProperty(Entity.FLAGS, 6));
		addEntity(Entity.class,                "invisible",    new SimpleBooleanProperty(Entity.FLAGS, 5));
		addEntity(Entity.class,                "name",         SelectorProperties.STRING, e -> e.getName());
		addEntity(Entity.class,                "sneaking",     new SimpleBooleanProperty(Entity.FLAGS, 1));
		addEntity(Entity.class,                "sprinting",    new SimpleBooleanProperty(Entity.FLAGS, 3));
		addEntity(EntityAgeable.class,         "child",        new SimpleBooleanProperty(EntityAgeable.field_184751_bv));
		addEntity(EntityLiving.class,          "disabled",     new SimpleBooleanProperty(EntityLiving.AI_FLAGS, 0));
		addEntity(EntityLiving.class,          "lefthanded",   new SimpleBooleanProperty(EntityLiving.AI_FLAGS, 1));
		addEntity(EntityLivingBase.class,      "health",       new SimpleFloatProperty(EntityLivingBase.field_184632_c));
		addEntity(EntityTameable.class,        "owner",        new SimpleUUIDProperty(EntityTameable.field_184756_bw));
		addEntity(EntityTameable.class,        "tame",         new SimpleBooleanProperty(EntityTameable.TAMED, 2));

		//TODO fill a bunch of enums
		addEntity(EntityBoat.class,            "type",         new SimpleEnumProperty(EntityBoat.BOAT_TYPE));
		addEntity(EntityCreeper.class,         "powered",      new SimpleBooleanProperty(EntityCreeper.POWERED));
		addEntity(EntityDragon.class,          "phase",        new SimpleIntProperty(EntityDragon.PHASE));
		addEntity(EntityFishHook.class,        "biting",       SelectorProperties.BOOLEAN, e -> false); //TODO
		addEntity(EntityGuardian.class,        "type",         SelectorProperties.STRING, e -> ""); //TODO can't use simpleenum, since this is just a bit flag
		addEntity(EntityHorse.class,           "armor",        new SimpleIntProperty(EntityHorse.HORSE_ARMOR));
		addEntity(EntityHorse.class,           "chest",        new SimpleBooleanProperty(EntityHorse.STATUS, 3));
		addEntity(EntityHorse.class,           "owner",        new SimpleUUIDProperty(EntityHorse.OWNER_UNIQUE_ID));
		addEntity(EntityHorse.class,           "saddle",       new SimpleBooleanProperty(EntityHorse.STATUS, 2));
		addEntity(EntityHorse.class,           "tame",         new SimpleBooleanProperty(EntityHorse.STATUS, 1));
		addEntity(EntityHorse.class,           "type",         new SimpleEnumProperty(EntityHorse.HORSE_TYPE, "normal", "donkey", "mule", "zombie", "skeleton"));
		addEntity(EntityIronGolem.class,       "created",      new SimpleBooleanProperty(EntityIronGolem.PLAYER_CREATED, 0));
		addEntity(EntityMinecartFurnace.class, "powered",      new SimpleBooleanProperty(EntityMinecartFurnace.field_184275_c));
		addEntity(EntityOcelot.class,          "type",         new SimpleEnumProperty(EntityOcelot.OCELOT_VARIANT));
		addEntity(EntityPig.class,             "saddle",       new SimpleBooleanProperty(EntityPig.SADDLED));
		addEntity(EntityPlayer.class,          "absorption",   new SimpleFloatProperty(EntityPlayer.ABSORPTION));
		addEntity(EntityPlayer.class,          "hand",         new SimpleEnumProperty(EntityPlayer.MAIN_HAND));
		addEntity(EntityPlayer.class,          "model",        new SimpleEnumProperty(EntityPlayer.PLAYER_MODEL_FLAG));
		addEntity(EntityPlayer.class,          "score",        new SimpleIntProperty(EntityPlayer.PLAYER_SCORE));
		addEntity(EntityRabbit.class,          "type",         new SimpleEnumProperty(EntityRabbit.RABBIT_TYPE));
		addEntity(EntitySheep.class,           "color",        SelectorProperties.STRING, e -> colors[e.dataWatcher.get(EntitySheep.DYE_COLOR) & 15]);
		addEntity(EntitySheep.class,           "sheared",      SelectorProperties.BOOLEAN, e -> (e.dataWatcher.get(EntitySheep.DYE_COLOR) & 16) != 0);
		addEntity(EntitySkeleton.class,        "type",         new SimpleEnumProperty(EntitySkeleton.SKELETON_VARIANT, "normal", "wither"));
		addEntity(EntitySlime.class,           "size",         new SimpleIntProperty(EntitySlime.SLIME_SIZE));
		addEntity(EntityVillager.class,        "type",         new SimpleEnumProperty(EntityVillager.PROFESSION));
		addEntity(EntityWolf.class,            "color",        new SimpleEnumProperty(EntityWolf.COLLAR_COLOR, colors));
		addEntity(EntityZombie.class,          "child",        new SimpleBooleanProperty(EntityZombie.IS_CHILD));
		addEntity(EntityZombie.class,          "type",         new SimpleEnumProperty(EntityZombie.VILLAGER_TYPE));
		//TODO
		//EntityItem, EntityItemFrame: item stack
		//Creeper, blaze, enderman, ghast, guardian, witch, wolf, tnt: attacking

		//@on
	}

	public static Selector parse(String selector) {
		return Parser.parse(Tokenizer.getTokens(selector));
	}
}
