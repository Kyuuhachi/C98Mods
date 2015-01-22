package c98.distortionWorld;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDistortionPortal extends BlockContainer {

	public BlockDistortionPortal() {
		super(Material.Portal);
		setBlockBounds(0, 0.5F, 0, 1, 0.5F, 1);
		setBlockTextureName("portal");
		setHardness(-1.0F);
		setStepSound(soundTypeGlass);
		setLightLevel(0.75F);
		setBlockName("portal");
	}

	@Override public TileEntity createNewTileEntity(World var1, int meta) {
		return new TileEntityDistortionPortal();
	}

	@Override public int getRenderType() {
		return -1;
	}

	@Override public boolean isOpaqueCube() {
		return false;
	}

	@Override public boolean renderAsNormalBlock() {
		return false;
	}
}
