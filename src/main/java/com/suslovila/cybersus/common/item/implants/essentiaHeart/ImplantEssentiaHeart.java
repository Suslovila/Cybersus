package com.suslovila.cybersus.common.item.implants.essentiaHeart;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.common.item.implants.ItemCybersusImplant;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import scala.actors.threadpool.Arrays;
import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ImplantEssentiaHeart extends ItemCybersusImplant {
    public static final ArrayList<AbilitySimpleEssentiaFormShift> abilities = new ArrayList<>();

    //    public static final HashMap<Aspect, Ability> abilityByAspect = new HashMap<>() {{
//       put(DarkAspects.ENVY, new AbilityPassive("envy") {
//
//           @Override
//           public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {
//               return null;
//           }
//       })
//    }};
    public ImplantEssentiaHeart() {
        super(ImplantType.HEART);

    }

    @Override
    public String getName() {
        return "essentia_heart";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities.stream().filter(ability -> ability.aspect == DarkAspects.LUST).collect(Collectors.toList());
    }

    static {
        abilities.add(new AbilitySimpleEssentiaFormShift(Aspect.WATER) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    event.ammount /= 2;
                    event.entityLiving.attackEntityFrom(DamageSource.generic, event.ammount);
                }
            }
        });

        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.ENVY) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    float damageBoost = Math.min(6.0f, event.entityLiving.prevHealth / player.prevHealth);
                    event.ammount *= damageBoost;
                }
            }
        });
        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.PRIDE) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    float damageBoost = Math.min(6.0f, player.prevHealth / event.entityLiving.prevHealth);
                    event.ammount *= damageBoost;
                }
            }
        });
        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.SLOTH) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    if (player.worldObj.getTotalWorldTime() - KhariumSusNBTHelper.getOrCreateLong(tag, "last_time_get_hit", 0) <= 5 * 20) {
                        float damageBoost = Math.min(6.0f, tag.getInteger("hits_amount"));
                        event.ammount *= damageBoost;
                        tag.setInteger("hits_amount", 0);
                    } else {
                        tag.setInteger("hits_amount", 0);
                        tag.setLong("last_time_get_hit", player.worldObj.getTotalWorldTime());
                    }
                }
            }

            @Override
            public void onPlayerHurtEventIfVictim(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    tag.setInteger("hits_amount", KhariumSusNBTHelper.getOrCreateInteger(tag, "hits_amount", 0) + 1);
                    tag.setLong("last_time_get_hit", player.worldObj.getTotalWorldTime());
                }
            }
        });

        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.LUST) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    long lastAttackTime = KhariumSusNBTHelper.getOrCreateLong(tag, "last_time_hit", 0);
                    int lastAttackAccumulation = KhariumSusNBTHelper.getOrCreateInteger(tag, "hits_amount", 0);
                    if (player.worldObj.getTotalWorldTime() - lastAttackTime <= 20 * 4) {
                        tag.setInteger("hits_amount", lastAttackAccumulation + 1);
                        event.entityLiving.hurtResistantTime -= Math.min(16, 1 * lastAttackAccumulation);
                        tag.setLong("last_time_hit", lastAttackTime);
                    } else {
                        tag.setInteger("hits_amount", 0);
                        tag.setLong("last_time_hit", player.worldObj.getTotalWorldTime());
                    }
                }
            }

            @Override
            public void onPlayerAttackEntityEvent(AttackEntityEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (event.entity == null) {
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    tag.setInteger("hits_amount", 0);
                }
            }
        });
        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.WRATH) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    long lastAttackTime = KhariumSusNBTHelper.getOrCreateLong(tag, "last_time_hit", 0);
                    int lastAttackAccumulation = KhariumSusNBTHelper.getOrCreateInteger(tag, "hits_amount", 0);
                    if (player.worldObj.getTotalWorldTime() - lastAttackTime <= 20 * 5) {
                        tag.setInteger("hits_amount", lastAttackAccumulation + 1);
                        event.ammount *= Math.min(6.0f, lastAttackAccumulation);
                        tag.setLong("last_time_hit", lastAttackTime);
                    } else {
                        tag.setInteger("hits_amount", 1);
                        tag.setLong("last_time_hit", player.worldObj.getTotalWorldTime());
                    }
                }
            }

            @Override
            public void onPlayerAttackEntityEvent(AttackEntityEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (event.entity == null) {
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    tag.setInteger("hits_amount", 0);
                }
            }
        });

        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.GLUTTONY) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    FoodStats foodStats = player.getFoodStats();
                    foodStats.setFoodLevel(foodStats.getFoodLevel() + 2 + itemRand.nextInt(2));
                    player.heal(event.ammount);
                }
            }

            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                if(isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    FoodStats foodStats = player.getFoodStats();
                    foodStats.setFoodLevel(foodStats.getFoodLevel() - 2);
                }
            }
        });

        abilities.add(new AbilitySimpleEssentiaFormShift(DarkAspects.NETHER) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant) && player.worldObj.provider.dimensionId == -1) {
                    event.ammount *= 4;
                }
            }

            @Override
            public void onPlayerHurtEventIfVictim(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant) && player.worldObj.provider.dimensionId == -1) {
                    event.ammount /= 4;
                }
                if(isActive(implant)) {
                    player.extinguish();
                    if(event.source.isFireDamage()) {
                        event.setCanceled(true);
                    }
                }
            }
        });
        abilities.add(new AbilitySimpleEssentiaFormShift(Aspect.ARMOR) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                if(isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100, 3));
                }
            }
        });
        abilities.add(new AbilitySimpleEssentiaFormShift(Aspect.DARKNESS) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                long worldTime = player.worldObj.getWorldTime();

                // Проверяем, является ли текущее время ночью
                boolean isNight = worldTime >= 13000 && worldTime < 23000;
                if(isActive(implant) && isNight) {
                    if( player.worldObj.getTotalWorldTime() % 20 == 0) {
                        player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100, 2));
                        player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 100, 4));
                        player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 4));
                        player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 4));
                        player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, 2));
                    }
                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                    tag.setBoolean("has_disabled_fly", false);
                    player.capabilities.allowFlying = true;
                    notifyClient(player, index, implant);
                }

                else {
                    if (player.worldObj.getTotalWorldTime() % 20 == 0) {

                        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                        boolean hasDisabledFly = KhariumSusNBTHelper.getOrCreateBoolean(tag, "has_disabled_fly", false);
                        if (!hasDisabledFly) {
                            player.capabilities.allowFlying = false;
                            tag.setBoolean("has_disabled_fly", true);

                            notifyClient(player, index, implant);
                        }
                    }
                }


            }
        });

        abilities.add(new AbilitySimpleEssentiaFormShift(Aspect.PLANT) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                long worldTime = player.worldObj.getWorldTime();

                // Проверяем, является ли текущее время ночью
                boolean isNight = worldTime >= 13000 && worldTime < 23000;
                if(isActive(implant) && !isNight && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    if(player.worldObj.canBlockSeeTheSky((int) player.posX, (int) player.posY, (int) player.posZ)) {
                        player.heal(10);
                    }
                }
            }
        });

        abilities.add(new AbilitySimpleEssentiaFormShift(Aspect.BEAST) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                long worldTime = player.worldObj.getWorldTime();

                // Проверяем, является ли текущее время ночью
                if(isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100, 2));
                    player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 100, 3));
                    player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 3));
                    player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 2));
                }
            }

//            @Override
//            public void onPlayerInteract(PlayerInteractEvent event, EntityPlayer player, int index, ItemStack implant) {
//                if(event.action == PlayerInteractEvent.Action.)
//            }
        });
//        abilities.add(new AbilitySimpleEssentiaFormShift(Aspect.) {
//            @Override
//            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
//                long worldTime = player.worldObj.getWorldTime();
//
//                if(isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
//                    player.addPotionEffect(new PotionEffect(Potion.resistance.id, 20, 2));
//                    player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20, 3));
//                    player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 20, 3));
//                    player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 2));
//                }
//            }
//
//        });

    }
}