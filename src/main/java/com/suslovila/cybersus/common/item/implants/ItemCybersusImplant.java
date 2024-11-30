package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.common.item.ItemImplant;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.List;

public abstract class ItemCybersusImplant extends ItemImplant {
    public final ImplantType implantType;

    public ItemCybersusImplant(ImplantType implantType) {
        super(implantType);
        this.implantType = implantType;
        this.setMaxStackSize(1);

        setUnlocalizedName(getName());
        setTextureName(Cybersus.MOD_ID + ":" + getName());
        setCreativeTab(Cybersus.tab);

        register();

    }

    public void register() {
        GameRegistry.registerItem(this, getName());
    }
    public abstract String getName();
}
