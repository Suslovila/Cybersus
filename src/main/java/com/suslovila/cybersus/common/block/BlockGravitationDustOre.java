// BlockIcarusOre.java
package com.suslovila.cybersus.common.block;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.CybersusItems;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockGravitationDustOre extends Block {

    public static final String name = "gravitation_dust_ore";

    public BlockGravitationDustOre() {
        super(Material.rock);
        setUnlocalizedName(name);                    // unlocalized name
//        setBlock("cybersus:icarus_ore");   // assets/cybersus/textures/blocks/icarus_ore.png
        setCreativeTab(Cybersus.tab);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", 3); // 0-дерево,1-камень,2-железо,3-алмаз
        GameRegistry.registerBlock(this, name);

    }

    // Что именно дропается
    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        // если хотите, чтобы падал сам блок — верните Item.getItemFromBlock(this)
        return CybersusItems.gravitationDust != null ? CybersusItems.gravitationDust : null;
    }

    // Базовое количество дропа
    @Override
    public int quantityDropped(Random rand) {
        // 1–2 штуки, если это «жемчуг/осколок». Для блока — верните 1.
        return (CybersusItems.gravitationDust != null) ? (1 + rand.nextInt(4)) : 1;
    }

    // Учитываем Fortune
    @Override
    public int quantityDroppedWithBonus(int fortune, Random rand) {
        if (CybersusItems.gravitationDust != null && fortune > 0) {
            int bonus = rand.nextInt(fortune + 2) - 1;
            if (bonus < 0) bonus = 0;
            return quantityDropped(rand) * (bonus + 1);
        }
        return quantityDropped(rand);
    }

    // Опыт как у руд с «гемами» (если падает не сам блок)
    @Override
    public int getExpDrop(IBlockAccess world, int meta, int fortune) {
        return (CybersusItems.gravitationDust != null && CybersusItems.gravitationDust != Item.getItemFromBlock(this))
                ? (1 + new Random().nextInt(4)) : 0;
    }

    // silk touch: если нужно, чтобы можно было добыть «сам блок»
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}
