package c98.zeldaItems;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSpinner extends Item {
	private static final IBehaviorDispenseItem dispenserSpinnerBehavior = new BehaviorDefaultDispenseItem() {
		@Override public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			EnumFacing facing = BlockDispenser.getFacing(source.getBlockMetadata());
			World world = source.getWorld();
			BlockPos pos = source.getBlockPos().offset(facing);
			double x = pos.getX() + facing.getFrontOffsetX();
			double y = pos.getY() + facing.getFrontOffsetY();
			double z = pos.getZ() + facing.getFrontOffsetZ();
			
			EntitySpinner entity = new EntitySpinner(world, x + 0.5, y, z + 0.5);
			
			if(stack.hasDisplayName()) entity.setCustomNameTag(stack.getDisplayName());
			
			world.spawnEntityInWorld(entity);
			stack.splitStack(1);
			return stack;
		}
		
		@Override protected void playDispenseSound(IBlockSource source) {
			source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
		}
	};
	
	public ItemSpinner() {
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabTransport);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserSpinnerBehavior);
	}
	
	@Override public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		pos = pos.offset(side);
		if(!worldIn.isRemote) {
			EntitySpinner entity = new EntitySpinner(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			if(stack.hasDisplayName()) entity.setCustomNameTag(stack.getDisplayName());
			worldIn.spawnEntityInWorld(entity);
		}
		
		--stack.stackSize;
		return true;
	}
}
