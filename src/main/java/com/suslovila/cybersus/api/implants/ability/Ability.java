package com.suslovila.cybersus.api.implants.ability;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.client.RenderHelper;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.implant.PacketImplantSync;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public abstract class Ability {
    public final String name;
    public final String COOLDOWN_NBT;
    public final ResourceLocation texture;

    public Ability(String name) {
        this.name = name;
        this.COOLDOWN_NBT = Cybersus.prefixAppender.doAndGet(":" + name + ":cooldown");
        this.texture = new ResourceLocation(Cybersus.MOD_ID, "textures/implants/ability_" + name + ".png");
    }

    public Ability(String name, String MOD_ID) {
        this.name = name;
        this.COOLDOWN_NBT = Cybersus.prefixAppender.doAndGet(name + ":cooldown");
        this.texture = new ResourceLocation(MOD_ID + ":textures/implants/ability_" + name + ".png");
    }

    public Ability(String name, String MOD_ID, ResourceLocation resourceLocation) {
        this.name = name;
        this.COOLDOWN_NBT = MOD_ID + (":" + name + ":cooldown");
        this.texture = resourceLocation;
    }


    public abstract void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant);

    /**
     * @param implant
     * @return if the implant is on cooldown now
     */
    public boolean isOnCooldown(ItemStack implant) {
        return KhariumSusNBTHelper.getOrCreateInteger(KhariumSusNBTHelper.getOrCreateTag(implant), COOLDOWN_NBT, 0) > 0;
    }

    /**
     * returns fuelKit player must have to activate ability
     *
     * @param implant
     * @return required fuel. MUST NOT RETURN NULL. Instead, return FuelEmpty.INSTANCE
     */
    public abstract FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant);

    /**
     * @param implant
     * @return total cooldown in whole
     */
    public abstract int getCooldownTotal(EntityPlayer player, int index, ItemStack implant);

    /**
     * @param implant
     * @return if implant is active. Is used in render and can be used by other implants
     */
    public abstract boolean isActive(ItemStack implant);

    /**
     * @param implant
     * @return actual cooldown left value
     */
    public int getCooldown(ItemStack implant) {
        return KhariumSusNBTHelper.getOrCreateInteger(KhariumSusNBTHelper.getOrCreateTag(implant), COOLDOWN_NBT, 0);
    }

    /**
     * sends implant to cooldown
     *
     * @param implant
     */
    public void sendToCooldown(EntityPlayer player, int index, ItemStack implant) {
        KhariumSusNBTHelper.getOrCreateTag(implant).setInteger(COOLDOWN_NBT, getCooldownTotal(player, index, implant));
    }


    /**
     * ticks down cooldown every tick. Instead of storing time when it was sent to cooldown, we tick down
     * it every tick.
     * pros: no problems when player is logging out and entering back the server
     * cons: excessive operations. At least we do not send packets to client
     *
     * @param implant
     */
    private void tickCooldown(ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        int cooldown = KhariumSusNBTHelper.getOrCreateInteger(tag, COOLDOWN_NBT, 0);
        if (cooldown > 0) {
            tag.setInteger(COOLDOWN_NBT, cooldown - 1);
        }
    }

    // here are just a lot of methods that are just fired from connected forge events

    public void onRenderWorldLastEvent(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onRenderWorldLastEventIndividually(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onRenderHandEvent(RenderHandEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onRenderPlayerSpecialPost(RenderPlayerEvent.Specials.Post event, EntityPlayer player, int index, ItemStack implant, RenderHelper.RenderType type, EventPriority priority) {
    }

    public void onPlayerAttackEntityEvent(AttackEntityEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerHealEvent(LivingHealEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
        tickCooldown(implant);
    }

    public void onPlayerHurtEventIfVictim(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerInteract(PlayerInteractEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerDeathEvent(LivingDeathEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerBeingAttackedEvent(LivingAttackEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onPlayerSetAttackTargetEvent(LivingSetAttackTargetEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    public void onBreakBlockEvent(BlockEvent event, EntityPlayer player, int index, ItemStack implant) {
    }

    // sends changes
    public void notifyClient(EntityPlayer implantWearer, int index, ItemStack implant) {
        if (implantWearer instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) implantWearer;
            CybersusPacketHandler.INSTANCE.sendTo(new PacketImplantSync(implantWearer, index, implant), entityPlayerMP);
            World world = entityPlayerMP.worldObj;
            if (world instanceof WorldServer) {
                WorldServer server = (WorldServer) world;
                server.getEntityTracker().getTrackingPlayers(implantWearer).forEach(trackedPlayer -> {
                            if (trackedPlayer instanceof EntityPlayerMP) {
                                CybersusPacketHandler.INSTANCE.sendTo(new PacketImplantSync(implantWearer, index, implant), (EntityPlayerMP) trackedPlayer);
                            }
                        }
                );
            }
        }
    }

    /**
     * used if you want to remove some info when implant is put in slot
     *
     * @param implant
     */
    public void clearData(ItemStack implant) {

    }

    public void onUnequipped(EntityPlayer player, int index, ItemStack implant) {
    }

    public boolean hasFuel(EntityPlayer player, int index, ItemStack implant) {
        return getFuelConsumeOnActivation(player, index, implant).hasPlayerEnough(player);
    }
}