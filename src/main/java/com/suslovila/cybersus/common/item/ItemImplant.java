package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.client.RenderHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;


/**
 * main class for implants
 * extend this class to create new implants
 */
public abstract class ItemImplant extends Item {
    public final ImplantType implantType;

    public ItemImplant(ImplantType implantType) {
        this.implantType = implantType;
        this.setMaxStackSize(1);

    }

    /**
     * gets current abilities that are available now
     *
     * @return list of abilities
     */
    public abstract List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant);

    // events for abilities. Override method to provide logic

    public void onRenderWorldLastEvent(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onRenderWorldLastEvent(event, player, index, implant);
        }
    }

    public void onRenderWorldLastEventIndividually(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onRenderWorldLastEventIndividually(event, player, index, implant);
        }
    }

    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onRenderPlayerEvent(event, player, index, implant);
        }
    }


    public void onRenderHandEvent(RenderHandEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onRenderHandEvent(event, player, index, implant);
        }
    }

    public void onRenderPlayerEventEquipment(RenderPlayerEvent.Specials.Post event, EntityPlayer player, int index, ItemStack implant, RenderHelper.RenderType type, EventPriority priority) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onRenderPlayerSpecialPost(event, player, index, implant, type, priority);
        }
    }

    public void onPlayerAttackEntityEvent(AttackEntityEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerAttackEntityEvent(event, player, index, implant);
        }
    }

    public void onPlayerHealEvent(LivingHealEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerHealEvent(event, player, index, implant);
        }
    }

    public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerUpdateEvent(event, player, index, implant);
        }
    }

    public void onPlayerHurtEventIfVictim(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerHurtEventIfVictim(event, player, index, implant);
        }
    }

    public void onPlayerInteract(PlayerInteractEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerInteract(event, player, index, implant);
        }
    }

    public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerHurtEventIfAttacker(event, player, index, implant);
        }
    }

    public void onPlayerDeathEvent(LivingDeathEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerDeathEvent(event, player, index, implant);
        }
    }

    public void onPlayerBeingAttackedEvent(LivingAttackEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerBeingAttackedEvent(event, player, index, implant);
        }
    }

    public void onPlayerSetAttackTargetEvent(LivingSetAttackTargetEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onPlayerSetAttackTargetEvent(event, player, index, implant);
        }
    }

    public void onBreakBlockEvent(BlockEvent event, EntityPlayer player, int index, ItemStack implant) {
        for (Ability ability : getAbilities(player, index, implant)) {
            ability.onBreakBlockEvent(event, player, index, implant);
        }
    }

    public void onEquipped(EntityPlayer player, int index, ItemStack implant) {
    }

    public void onUnequipped(EntityPlayer player, int index, ItemStack implant) {
    }
}
