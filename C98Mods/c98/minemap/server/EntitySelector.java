package c98.minemap.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import c98.core.Console;
import c98.core.IO;
import c98.minemap.server.selector.*;

public class EntitySelector {
	public static class DataWatchObj {
		public String type;
		int op = 0;
		int opPar = 0;
		int num;
		
		public DataWatchObj(String t, String v) {
			type = t;
			if(!v.isEmpty()) if(v.contains(">")) { //Bitshift
				op = 2;
				opPar = Integer.parseInt(v.substring(1 + v.indexOf('>')));
				num = Integer.parseInt(v.substring(0, v.indexOf('>')));
			} else if(v.contains("&")) { //And
				op = 1;
				opPar = Integer.parseInt(v.substring(1 + v.indexOf('&')));
				num = Integer.parseInt(v.substring(0, v.indexOf('&')));
			} else num = Integer.parseInt(v);
		}
		
		public Object get(Entity e) {
			Object ob = e.getDataWatcher().getWatchedObject(num);
			if(op == 1) return ((Number)ob).intValue() & opPar;
			if(op == 2) return ((Number)ob).intValue() >>> opPar;
			return ob;
			
		}
		
		@Override public String toString() {
			return type + " " + op + opPar + " " + num;
		}
	}
	
	public static Map<String, Map<String, DataWatchObj>> attributes;
	public static Map<Class, String> classToId;
	private static final ResourceLocation datawatcher = new ResourceLocation("c98", "Minemap/datawatcher.txt");
	
	public static DataWatchObj getParam(Entity e, String key) {
		for(Class c = e.getClass(); c != null; c = c.getSuperclass()) {
			String s = classToId.get(c);
			if(attributes.get(s) != null && attributes.get(s).containsKey(key)) return attributes.get(s).get(key);
		}
		return null;
	}
	
	public static void reloadConfig() {
		try(BufferedReader rdr = new BufferedReader(new InputStreamReader(IO.getInputStream(datawatcher)))) {
			String s;
			Map<String, Map<String, DataWatchObj>> map = new HashMap();
			Map<String, DataWatchObj> watches = null;
			while((s = rdr.readLine()) != null)
				if(!s.startsWith("\t")) {
					watches = new HashMap();
					map.put(s, watches);
				} else {
					String[] st = s.split("\t+");
					watches.put(st[3], new DataWatchObj(st[1], st[2]));
				}
			map.get("Entity").put("name", new DataWatchObj("S", "") {
				@Override public Object get(Entity e) {
					return e.getCommandSenderName();
				}
			});
			attributes = map;
			
			Map<Class, String> m = new HashMap();
			m.putAll(EntityList.classToStringMapping);
			m.putAll(TileEntity.classToNameMap);
			m.put(Entity.class, "Entity");
			m.put(EntityAgeable.class, "Ageable");
			m.put(EntityLivingBase.class, "Living");
			m.put(EntityMinecart.class, "Minecart");
			m.put(EntityLiving.class, "NonPlayer");
			m.put(EntityPlayer.class, "Player");
			m.put(EntityClientPlayerMP.class, "Self");
			m.put(EntityFireball.class, "FireballBase");
			m.put(EntityHanging.class, "Hanging");
			m.put(EntityAmbientCreature.class, "AmbientCreature");
			m.put(EntityCreature.class, "Creature");
			m.put(EntityGolem.class, "Golem");
			m.put(EntityMob.class, "Hostile");
			m.put(EntityWaterMob.class, "WaterMob");
			m.put(EntityFlying.class, "Flying");
			m.put(EntityThrowable.class, "Throwable");
			classToId = m;
		} catch(IOException e) {
			Console.error(e);
		}
	}
	
	public static Selector parse(String s) {
		return Parser.parse(Lexer.lex(s));
	}
	
}
