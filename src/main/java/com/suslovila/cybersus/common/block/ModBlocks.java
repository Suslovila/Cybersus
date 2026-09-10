package com.suslovila.cybersus.common.block;


import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.block.runeInstaller.BlockRuneInstaller;
import com.suslovila.cybersus.common.block.runeInstaller.TileRuneInstaller;
import com.suslovila.cybersus.common.block.tile.BlockPhasoliteOre;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static BlockImplantInstaller implantInstaller;
    public static BlockRuneInstaller runeInstaller;
    public static BlockGravitationDustOre gravitationDustOre;
    public static BlockPhasoliteOre phasoliteOre;

    public static void register() {
        gravitationDustOre = new BlockGravitationDustOre();
        phasoliteOre = new BlockPhasoliteOre();
//        implantInstaller = new BlockImplantInstaller("implant_installer");
//        runeInstaller = new BlockRuneInstaller("rune_installer");
//
//        GameRegistry.registerTileEntity(TileRuneInstaller.class, Cybersus.MOD_ID + "TileRuneInstaller");


    }

    public static void registerRender() {
    }
}
