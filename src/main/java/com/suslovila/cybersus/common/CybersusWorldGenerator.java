package com.suslovila.cybersus.common;

import com.suslovila.cybersus.common.block.ModBlocks;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.biomes.BiomeHandler;

import java.util.Random;

//import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.getBiomeBlacklist;
//
//public class CybersusWorldGenerator {
//    private void generateOres(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
//        BiomeGenBase bgb = world.getBiomeGenForCoords(chunkX * 16 + 8, chunkZ * 16 + 8);
////        this; if (getBiomeBlacklist(bgb.biomeID) != 0) { this; if (getBiomeBlacklist(bgb.biomeID) != 2) {
//
//
//            if (Config.genCinnibar && (newGen || Config.regenCinnibar))
//            {
//                for (int i = 0; i < 18; i++) {
//
//                    int randPosX = chunkX * 16 + random.nextInt(16);
//                    int randPosY = random.nextInt(world.getHeight() / 5);
//                    int randPosZ = chunkZ * 16 + random.nextInt(16);
//                    Block block = world.getBlock(randPosX, randPosY, randPosZ);
//                    if (block != null && block.isReplaceableOreGen(world, randPosX, randPosY, randPosZ, Blocks.stone))
//                    {
//                        world.setBlock(randPosX, randPosY, randPosZ, ConfigBlocks.blockCustomOre, 0, 0);
//                    }
//                }
//            }
//            if (Config.genAmber && (newGen || Config.regenAmber))
//            {
//                for (int i = 0; i < 20; i++) {
//
//                    int randPosX = chunkX * 16 + random.nextInt(16);
//                    int randPosZ = chunkZ * 16 + random.nextInt(16);
//
//                    int randPosY = world.getHeightValue(randPosX, randPosZ) - random.nextInt(25);
//
//                    Block block = world.getBlock(randPosX, randPosY, randPosZ);
//                    if (block != null && block.isReplaceableOreGen(world, randPosX, randPosY, randPosZ, Blocks.stone))
//                    {
//                        world.setBlock(randPosX, randPosY, randPosZ, ConfigBlocks.blockCustomOre, 7, 2);
//                    }
//                }
//            }
//            if (Config.genInfusedStone && (newGen || Config.regenInfusedStone))
//            {
//                for (int i = 0; i < 8; i++) {
//
//                    int randPosX = chunkX * 16 + random.nextInt(16);
//                    int randPosZ = chunkZ * 16 + random.nextInt(16);
//                    int randPosY = random.nextInt(Math.max(5, world.getHeightValue(randPosX, randPosZ) - 5));
//                    int md = random.nextInt(6) + 1;
//                    if (random.nextInt(3) == 0) {
//                        Aspect tag = BiomeHandler.getRandomBiomeTag((world.getBiomeGenForCoords(randPosX, randPosZ)).biomeID, random);
//                        if (tag == null)
//                        { md = 1 + random.nextInt(6);
//                        }
//
//                        else if (tag == Aspect.AIR) { md = 1; }
//                        else if (tag == Aspect.FIRE) { md = 2; }
//                        else if (tag == Aspect.WATER) { md = 3; }
//                        else if (tag == Aspect.EARTH) { md = 4; }
//                        else if (tag == Aspect.ORDER) { md = 5; }
//                        else if (tag == Aspect.ENTROPY) { md = 6; }
//
//                    }
//
//                    try {
//                        (new WorldGenMinable(ConfigBlocks.blockCustomOre, md, 6, Blocks.stone)).generate(world, random, randPosX, randPosY, randPosZ);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return;
//        }  }
//
////    }
////}

public class CybersusWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world,
                         IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        // В 1.7.10: Энд имеет dimensionId == 1
        if (world.provider.dimensionId == 1) {
            generateEnd(world, rand, chunkX * 16, chunkZ * 16);
        }
        if (world.provider.dimensionId == 0) {
            generateOverworld(world, rand, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateEnd(World world, Random rand, int baseX, int baseZ) {
        // Сколько жил на чанк / размер жилы / высоты
        final int veinsPerChunk = 1;   // сколько попыток
        final int veinSize      = 5;   // размер жилы
        final int minY          = 4;
        final int maxY          = 80;  // основная «шапка» острова Энда

        for (int i = 0; i < veinsPerChunk; i++) {
            int x = baseX + rand.nextInt(16);
            int y = minY + rand.nextInt(maxY - minY + 1);
            int z = baseZ + rand.nextInt(16);
            // В Энде копаем в end_stone
            new WorldGenMinable(ModBlocks.gravitationDustOre, veinSize, Blocks.end_stone).generate(world, rand, x, y, z);
        }
    }

    private void generateOverworld(World world, Random rand, int baseX, int baseZ) {
        // Получаем биом по центру чанка
        BiomeGenBase biome = world.getBiomeGenForCoords(baseX + 8, baseZ + 8);

        // Проверяем, что это ЗАРАЖЁННЫЙ БИОМ из Thaumcraft
        // Thaumcraft добавляет биомы с ID 39 и 40 (обычно: taint, taint deep)
        // Лучше свериться в коде/конфиге мода, но 39 — стандартный "Taint".
        if (biome != null && biome.biomeName.contains("Tainted Land")) {

            int veinsPerChunk = 1; // сколько жил на чанк
            int veinSize = 6;      // размер жилы
            int minY = 8;
            int maxY = 48;

            for (int i = 0; i < veinsPerChunk; i++) {
                int x = baseX + rand.nextInt(16);
                int y = minY + rand.nextInt(maxY - minY);
                int z = baseZ + rand.nextInt(16);

                // Фазолит в камне (Blocks.stone)
                new WorldGenMinable(ModBlocks.phasoliteOre, veinSize, Blocks.stone)
                        .generate(world, rand, x, y, z);
            }
        }
    }
}

