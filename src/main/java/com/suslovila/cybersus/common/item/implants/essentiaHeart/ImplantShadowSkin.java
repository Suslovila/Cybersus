package com.suslovila.cybersus.common.item.implants.essentiaHeart;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.client.clientProcess.processes.shadowGates.ProcessShadowGates;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.item.implants.ItemCybersusImplant;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantShadowSkin extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public static final String SHADOW_MOD_TAG = "shadow_mod_active";
    public static final String PREPARATION_TIMER = "preparation_timer";

    public ImplantShadowSkin() {
        super(ImplantType.SKIN);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityPassive("shadow_travel") {
            @Override
            public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(Aspect.DARKNESS, 2)));
//                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 20;
            }


            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(Aspect.DARKNESS, 64)));
//                return FuelComposite.EMPTY;
            }


            @Override
            public void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant) {
                if (isPreparing(implant)) return;
                super.onEnableButtonClicked(player, index, implant);
            }

            @Override
            public void onAbilityStatusSwitched(EntityPlayer player, int index, ItemStack implant) {
                KhariumSusNBTHelper.getOrCreateTag(implant).setInteger(PREPARATION_TIMER, 30);
                ProcessShadowGates processShadowGates = new ProcessShadowGates(player.getEntityId(), 30);
                if(!isActive(implant)) {
                    player.worldObj.playSoundAtEntity(
                            player,
                            Cybersus.MOD_ID + ":appear_from_shadows",
                            1f,
                            1.4f + player.worldObj.rand.nextFloat() * 0.2f
                    );
                }
                else {
                    player.worldObj.playSoundAtEntity(
                            player,
                            Cybersus.MOD_ID + ":hide_in_shadows",
                            1f,
                            1.4f + player.worldObj.rand.nextFloat() * 0.2f
                    );
                }
                CustomWorldData.syncProcess(processShadowGates, player.worldObj.provider.dimensionId);
                if (!isActive(implant)) player.removePotionEffect(Potion.invisibility.id);

                notifyClient(player, index, implant);
            }

            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                super.onPlayerUpdateEvent(event, player, index, implant);

                NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(implant);
                int currentTimer = KhariumSusNBTHelper.getOrCreateInteger(tagCompound, PREPARATION_TIMER, 0);
                if (currentTimer > 0) {
                    tagCompound.setInteger(PREPARATION_TIMER, currentTimer - 1);
                }

                if (!player.worldObj.isRemote) {
                    if (isActive(implant) && !isPreparing(implant)) {
                        if (player.worldObj.getTotalWorldTime() % 20 == 0) {
//                            float radius = 10.0f;
//                            List<Entity> entityList = player.worldObj.getEntitiesWithinAABBExcludingEntity(
//                                    null,
//                                    AxisAlignedBB.getBoundingBox(
//                                            player.posX - radius,
//                                            player.posY - radius,
//                                            player.posZ - radius,
//
//                                            player.posX + radius,
//                                            player.posY + radius,
//                                            player.posZ + radius
//                                    )
//                            );
//
//                            for (Entity entity : entityList) {
//                                if (!(entity instanceof EntityLivingBase)) continue;
//                                EntityLivingBase livingBase = (EntityLivingBase) entity;
//                                if (livingBase.getAITarget() == player) {
//                                    ((EntityLivingBase) entity).setRevengeTarget(null);
//                                }
//                            }
//
//
                            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 2));
                            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 4));
                            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, 0));
                            player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 100, 0));

                        }
                        if (player.worldObj.getTotalWorldTime() % 10 == 0) {

                            int light = player.worldObj.getBlockLightValue((int) player.posX, (int) player.posY, (int) player.posZ);
                            if (light >= 14) {
                                sendToCooldown(player, index, implant);
                                player.removePotionEffect(Potion.invisibility.id);
                                player.attackEntityFrom(DamageSource.outOfWorld, player.getMaxHealth() / 2);
                                notifyClient(player, index, implant);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPlayerBeingAttackedEvent(LivingAttackEvent event, EntityPlayer player, int index, ItemStack implant) {

                if (isActive(implant) && !isPreparing(implant)) {
                    if (event.source.isFireDamage()) {
                        sendToCooldown(player, index, implant);
                        player.removePotionEffect(Potion.invisibility.id);
                        player.attackEntityFrom(DamageSource.outOfWorld, player.getMaxHealth() / 2);
                        notifyClient(player, index, implant);
                        return;
                    }
                    event.setCanceled(true);
                }
            }

            @Override
            public void onPlayerAttackEntityEvent(AttackEntityEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant) && !isPreparing(implant)) {
                    event.setCanceled(true);
                }
            }


            public boolean isPreparing(ItemStack implant) {
                NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                return (KhariumSusNBTHelper.getOrCreateInteger(tag, PREPARATION_TIMER, 0) > 0);
            }

            @Override
            public void onRenderPlayerEvent(RenderPlayerEvent.Pre event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant) && !isPreparing(implant) || (!isActive(implant) && isPreparing(implant))) {
                    event.setCanceled(true);
                }
            }

            @Override
            public void onBreakBlockEvent(BlockEvent event, EntityPlayer player, int index, ItemStack implant) {
//                if(isActive(implant) && !isPreparing(implant)) {
//                    event.setCanceled(true);
//                }
            }

            @Override
            public void onPlayerInteract(PlayerInteractEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant) && !isPreparing(implant)) {
                    event.setCanceled(true);
                }
            }

            @Override
            public void sendToCooldown(EntityPlayer player, int index, ItemStack implant) {
                super.sendToCooldown(player, index, implant);
                KhariumSusNBTHelper.getOrCreateTag(implant).setInteger(PREPARATION_TIMER, 0);
            }
        });
    }

    @Override
    public String getName() {
        return "shadow_skin";
    }
}

