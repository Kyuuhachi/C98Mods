package c98.minemapMarkers.selector;

import static c98.minemapMarkers.selector.SelectorProperties.addEntity;

import java.util.HashMap;
import java.util.Map;

import c98.minemapMarkers.selector.prop.*;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class EntitySelector {
	public static Map<Class, ResourceLocation> classToId = new HashMap();
	static {
		initClasses();
		initProperties();
	}

	public static void initClasses() {
		classToId.putAll(EntityList.field_191308_b.inverseObjectRegistry);
		classToId.putAll(TileEntity.field_190562_f.inverseObjectRegistry);
		classToId.put(Entity.class, new ResourceLocation("entity"));
		classToId.put(EntityAgeable.class, new ResourceLocation("ageable"));
		classToId.put(EntityLivingBase.class, new ResourceLocation("living"));
		classToId.put(EntityMinecart.class, new ResourceLocation("minecart"));
		classToId.put(EntityLiving.class, new ResourceLocation("non_player"));
		classToId.put(EntityPlayer.class, new ResourceLocation("player"));
		classToId.put(EntityPlayerSP.class, new ResourceLocation("self"));
		classToId.put(EntityFireball.class, new ResourceLocation("fireball_base"));
		classToId.put(EntityHanging.class, new ResourceLocation("Hanging"));
		classToId.put(EntityAmbientCreature.class, new ResourceLocation("ambient_creature"));
		classToId.put(EntityCreature.class, new ResourceLocation("creature"));
		classToId.put(EntityGolem.class, new ResourceLocation("golem"));
		classToId.put(EntityMob.class, new ResourceLocation("hostile"));
		classToId.put(EntityWaterMob.class, new ResourceLocation("water_mob"));
		classToId.put(EntityFlying.class, new ResourceLocation("flying"));
		classToId.put(EntityThrowable.class, new ResourceLocation("throwable"));
		classToId.put(EntityAnimal.class, new ResourceLocation("animal"));
		classToId.put(AbstractSkeleton.class, new ResourceLocation("any_skeleton"));
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
		addEntity(EntityAgeable.class,         "child",        new SimpleBooleanProperty(EntityAgeable.BABY));
		addEntity(EntityLiving.class,          "disabled",     new SimpleBooleanProperty(EntityLiving.AI_FLAGS, 0));
		addEntity(EntityLiving.class,          "lefthanded",   new SimpleBooleanProperty(EntityLiving.AI_FLAGS, 1));
		addEntity(EntityLivingBase.class,      "health",       new SimpleFloatProperty(EntityLivingBase.HEALTH));
		addEntity(EntityTameable.class,        "owner",        new SimpleUUIDProperty(EntityTameable.OWNER_UNIQUE_ID));
		addEntity(EntityTameable.class,        "tame",         new SimpleBooleanProperty(EntityTameable.TAMED, 2));
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
		addEntity(EntityHorse.class,           "type",         new SimpleEnumProperty(EntityHorse.HORSE_VARIANT, "normal", "donkey", "mule", "zombie", "skeleton"));
		addEntity(EntityIronGolem.class,       "created",      new SimpleBooleanProperty(EntityIronGolem.PLAYER_CREATED, 0));
		addEntity(EntityMinecartFurnace.class, "powered",      new SimpleBooleanProperty(EntityMinecartFurnace.POWERED));
		addEntity(EntityOcelot.class,          "type",         new SimpleEnumProperty(EntityOcelot.OCELOT_VARIANT));
		addEntity(EntityPig.class,             "saddle",       new SimpleBooleanProperty(EntityPig.SADDLED));
		addEntity(EntityPlayer.class,          "absorption",   new SimpleFloatProperty(EntityPlayer.ABSORPTION));
		addEntity(EntityPlayer.class,          "hand",         new SimpleEnumProperty(EntityPlayer.MAIN_HAND));
		addEntity(EntityPlayer.class,          "model",        new SimpleEnumProperty(EntityPlayer.PLAYER_MODEL_FLAG));
		addEntity(EntityPlayer.class,          "score",        new SimpleIntProperty(EntityPlayer.PLAYER_SCORE));
		addEntity(EntityRabbit.class,          "type",         new SimpleEnumProperty(EntityRabbit.RABBIT_TYPE));
		addEntity(EntitySheep.class,           "color",        SelectorProperties.STRING, e -> ((EntitySheep)e).getFleeceColor());
		addEntity(EntitySheep.class,           "sheared",      SelectorProperties.BOOLEAN, e -> ((EntitySheep)e).getSheared());
		addEntity(EntitySlime.class,           "size",         new SimpleIntProperty(EntitySlime.SLIME_SIZE));
		addEntity(EntityVillager.class,        "type",         new SimpleEnumProperty(EntityVillager.PROFESSION));
		addEntity(EntityWolf.class,            "color",        new SimpleEnumProperty(EntityWolf.COLLAR_COLOR, colors));
		addEntity(EntityZombie.class,          "child",        new SimpleBooleanProperty(EntityZombie.IS_CHILD));
		addEntity(EntityZombie.class,          "type",         new SimpleEnumProperty(EntityZombie.VILLAGER_TYPE));
		//TODO
		//EntityItem, EntityItemFrame: item stack
		//Creeper, blaze, enderman, ghast, guardian, witch, wolf, tnt: attacking
		//1.10 stuff

		//@on
	}

	public static Selector parse(String selector) {
		return Parser.parse(Tokenizer.getTokens(selector));
	}
}
