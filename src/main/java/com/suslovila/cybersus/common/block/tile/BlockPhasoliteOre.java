// BlockIcarusOre.java
package com.suslovila.cybersus.common.block.tile;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.ModItems;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockPhasoliteOre extends Block {

    public static final String name = "phasolite_ore";

    public BlockPhasoliteOre() {
        super(Material.rock);
        setUnlocalizedName(name);
        setCreativeTab(Cybersus.tab);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", 3);
        GameRegistry.registerBlock(this, name);

    }

    // Что именно дропается
    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        // если хотите, чтобы падал сам блок — верните Item.getItemFromBlock(this)
        return ModItems.phasolite != null ? ModItems.phasolite : null;
    }

    // Базовое количество дропа
    @Override
    public int quantityDropped(Random rand) {
        // 1–2 штуки, если это «жемчуг/осколок». Для блока — верните 1.
        return 1;
    }

    // Учитываем Fortune
    @Override
    public int quantityDroppedWithBonus(int fortune, Random rand) {
        return 1;
    }

    // Опыт как у руд с «гемами» (если падает не сам блок)
    @Override
    public int getExpDrop(IBlockAccess world, int meta, int fortune) {
        return (ModItems.phasolite != null && ModItems.phasolite != Item.getItemFromBlock(this))
                ? 1 : 0;
    }

    // silk touch: если нужно, чтобы можно было добыть «сам блок»
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}
