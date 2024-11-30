package com.suslovila.cybersus.common.block.runeInstaller;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.client.gui.CybersusGui;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockRuneInstaller extends BlockContainer {

    public BlockRuneInstaller(String name) {
        super(Material.iron);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setCreativeTab(Cybersus.tab);
        GameRegistry.registerBlock(this, name);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ) {
        if (player == null) return true;

        if (!player.isSneaking()) {
            player.openGui(Cybersus.instance, CybersusGui.RUNE_INSTALLER.ordinal(), world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileRuneInstaller();
    }
}


