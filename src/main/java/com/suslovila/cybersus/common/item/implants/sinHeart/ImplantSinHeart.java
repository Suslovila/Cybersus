package com.suslovila.cybersus.common.item.implants.sinHeart;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneType;
import com.suslovila.cybersus.common.item.implants.ItemCybersusImplant;
import com.suslovila.cybersus.utils.CollectionUtils;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImplantSinHeart extends ItemCybersusImplant {
    public static final ArrayList<AbilitySinHeartFormShift> abilities = new ArrayList<>();
    int subTypesAmount = 8;
    public static IIcon coloredOverlay;

    public static List<Aspect> sinAspects;

    static {
        sinAspects = CollectionUtils.arrayListOf(DarkAspects.ENVY, DarkAspects.LUST, DarkAspects.PRIDE, DarkAspects.SLOTH, DarkAspects.GLUTTONY, DarkAspects.WRATH, Aspect.GREED);
    }
    public ImplantSinHeart() {
        super(ImplantType.HEART);
        setHasSubtypes(true);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        coloredOverlay = register.registerIcon(Cybersus.MOD_ID + ":sin_heart_overlay");
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        return pass == 0 ? itemIcon : (stack.getMetadata() == sinAspects.size() ? itemIcon : coloredOverlay);

    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return getUnlocalizedName();
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < subTypesAmount; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
//
//        if (renderPass == 0) {
//            return super.getIconFromDamageForRenderPass(par1, renderPass);
//        }
//
//        GL11.glEnable(3042);
//        GL11.glBlendFunc(770, 771);
//        return coloredOverlay;
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if(renderPass == 0)
            return 0xFFFFFF;

        int metadata = stack.getMetadata();
        if (metadata < sinAspects.size() && metadata >= 0) {
            return sinAspects.get(metadata).getColor();
        }
        return super.getColorFromItemStack(stack, renderPass);
    }

    @Override
    public String getName() {
        return "sin_heart";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        if(implant.getMetadata() == sinAspects.size()) return new ArrayList<>();
        return abilities.stream().filter(ability -> ability.aspect == sinAspects.get(implant.getMetadata())).collect(Collectors.toList());
    }

    static {
//        abilities.add(new AbilitySinHeartFormShift(Aspect.WATER) {
//            @Override
//            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
//                if (isActive(implant)) {
//                    event.ammount /= 2;
//                    event.entityLiving.attackEntityFrom(DamageSource.generic, event.ammount);
//                }
//            }
//        });

        abilities.add(new AbilitySinHeartFormShift(DarkAspects.ENVY) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    float damageBoost = Math.min(6.0f, event.entityLiving.prevHealth / player.prevHealth);
                    event.ammount *= damageBoost;
                }
            }
        });
        abilities.add(new AbilitySinHeartFormShift(DarkAspects.PRIDE) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    float damageBoost = Math.min(6.0f, player.prevHealth / event.entityLiving.prevHealth);
                    event.ammount *= damageBoost;
                }
            }
        });
        abilities.add(new AbilitySinHeartFormShift(DarkAspects.SLOTH) {
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

        abilities.add(new AbilitySinHeartFormShift(DarkAspects.LUST) {
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
        abilities.add(new AbilitySinHeartFormShift(DarkAspects.WRATH) {
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

        abilities.add(new AbilitySinHeartFormShift(DarkAspects.GLUTTONY) {
            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant)) {
                    FoodStats foodStats = player.getFoodStats();
                    foodStats.setFoodLevel(Math.min(20, foodStats.getFoodLevel() + 2 + itemRand.nextInt(2)));
                    player.heal(event.ammount);
                }
            }

            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                super.onPlayerUpdateEvent(event, player, index, implant);
                if (isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    FoodStats foodStats = player.getFoodStats();
                    foodStats.setFoodLevel(Math.max(-100, foodStats.getFoodLevel() - 2));
                }
            }
        });

        abilities.add(new AbilitySinHeartFormShift(DarkAspects.NETHER) {
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
                if (isActive(implant)) {
                    player.extinguish();
                    if (event.source.isFireDamage()) {
                        event.setCanceled(true);
                    }
                }
            }
        });
        abilities.add(new AbilitySinHeartFormShift(Aspect.ARMOR) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100, 3));
                }
            }
        });
        abilities.add(new AbilitySinHeartFormShift(Aspect.DARKNESS) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                long worldTime = player.worldObj.getWorldTime();

                // Проверяем, является ли текущее время ночью
                boolean isNight = worldTime >= 13000 && worldTime < 23000;
                if (isActive(implant) && isNight) {
                    if (player.worldObj.getTotalWorldTime() % 20 == 0) {
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
                } else {
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

        abilities.add(new AbilitySinHeartFormShift(Aspect.PLANT) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                long worldTime = player.worldObj.getWorldTime();

                // Проверяем, является ли текущее время ночью
                boolean isNight = worldTime >= 13000 && worldTime < 23000;
                if (isActive(implant) && !isNight && player.worldObj.getTotalWorldTime() % 20 == 0) {
                    if (player.worldObj.canBlockSeeTheSky((int) player.posX, (int) player.posY, (int) player.posZ)) {
                        player.heal(10);
                    }
                }
            }
        });

        abilities.add(new AbilitySinHeartFormShift(Aspect.BEAST) {
            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                long worldTime = player.worldObj.getWorldTime();

                // Проверяем, является ли текущее время ночью
                if (isActive(implant) && player.worldObj.getTotalWorldTime() % 20 == 0) {
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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        if(stack.getMetadata() == sinAspects.size()) {
            list.add(ChatFormatting.DARK_RED + StatCollector.translateToLocal("cybersus.not_infused_heart"));
            return;
        }
        list.add(ChatFormatting.DARK_RED + StatCollector.translateToLocal("cybersus.sin_heart_infused." + stack.getMetadata()));
    }

}