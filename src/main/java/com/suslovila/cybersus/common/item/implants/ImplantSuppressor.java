
package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.common.item.ItemSuppressed;
import com.suslovila.cybersus.common.item.ModItems;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.suslovila.cybersus.common.item.ItemSuppressed.SUPPRESSED_STACK_KEY;
import static com.suslovila.cybersus.common.item.ItemSuppressed.TIME_SUPPRESSED_LEFT;

public class ImplantSuppressor extends ItemCybersusImplant {
    public static final String name = "suppressor";
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public static final int maxSuppressedItems = 5;
    public static final int suppressTime = 20 * 10;


    public ImplantSuppressor() {
        super(ImplantType.BRAIN);

    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityHack("suppress") {
            @Override
            public int getRequiredHackTime() {
                return 20 * 1;
            }

            @Override
            public double getLockDistance(EntityPlayer player, int index, ItemStack implant) {
                return 30;
            }

            @Override
            public double getLoseDistance(EntityPlayer player, int index, ItemStack implant) {
                return 60;
            }

            @Override
            public int getTargetTimeLose(EntityPlayer player, int index, ItemStack implant) {
                return 20 * 3;
            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                FuelComposite composite = FuelComposite.allRequired();
                composite.addRequiredFuel(new FuelEssentia(new AspectList().add(CybersusAspect.HUMILITAS, 64)));
                composite.addRequiredFuel(new FuelEssentia(new AspectList().add(Aspect.TOOL, 256)));

                return composite;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 20 * 1;
            }

            @Override
            public void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
//                hacker.worldObj.createExplosion(hacker, victim.posX, victim.posY, victim.posZ, 4.0f, true);
                if (victim instanceof EntityPlayer) {
                    EntityPlayer playerVictim = (EntityPlayer) victim;
                    int maxSuppressedItems = 5;
                    int alreadySuppressed = 0;
                    ItemStack[] armorInventory = playerVictim.inventory.armorInventory;
                    ItemStack[] mainInventory = playerVictim.inventory.mainInventory;

                    long notNullAmount = Arrays.stream(armorInventory).filter(Objects::nonNull).count() + Arrays.stream(playerVictim.inventory.mainInventory).filter(Objects::nonNull).count();
                    if (notNullAmount == 0) return;
//                    for (int i = 0; i < armorInventory.length && alreadySuppressed < maxSuppressedItems; i++) {
//                        ItemStack stackIn = armorInventory[i];
//                        if (stackIn == null) continue;
//                        if (itemRand.nextInt(100) / 100.0 < maxSuppressedItems / (float) notNullAmount) {
//                            ItemStack suppressedItem = new ItemStack(ModItems.itemSuppressed);
//                            NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(suppressedItem);
//                            NBTTagCompound innerTag = new NBTTagCompound();
//                            stackIn.writeToNBT(innerTag);
//
//                            tagCompound.setTag(SUPPRESSED_STACK_KEY, innerTag);
//                            tagCompound.setInteger(TIME_SUPPRESSED_LEFT, suppressTime);
//                            playerVictim.inventory.armorInventory[i] = suppressedItem;
//
//                            alreadySuppressed++;
//                        }
//                    }

                    for (int i = 0; i < mainInventory.length && alreadySuppressed < maxSuppressedItems; i++) {
                        ItemStack stackIn = mainInventory[i];
                        if (stackIn == null || stackIn.getItem() instanceof ItemSuppressed) continue;
                        if (itemRand.nextInt(100) / 100.0 < maxSuppressedItems / (float) notNullAmount) {
                            ItemStack suppressedItem = new ItemStack(ModItems.itemSuppressed);
                            NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(suppressedItem);
                            NBTTagCompound innerTag = new NBTTagCompound();
                            stackIn.writeToNBT(innerTag);

                            tagCompound.setTag(SUPPRESSED_STACK_KEY, innerTag);
                            tagCompound.setInteger(TIME_SUPPRESSED_LEFT, suppressTime);
                            playerVictim.inventory.mainInventory[i] = suppressedItem;

                            alreadySuppressed++;
                        }
                    }

                }
            }
        });
    }


    @Override
    public String getName() {
        return name;
    }
}
