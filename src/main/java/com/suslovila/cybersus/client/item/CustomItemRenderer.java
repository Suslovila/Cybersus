package com.suslovila.cybersus.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class SuppressedItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true; // Указываем, что предмет можно рендерить
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true; // Указываем, что можем использовать рендер-хелперы
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {

//        Minecraft.getMinecraft()..renderItemIntoGUI(stackToRender, 0, 0);
    }
}
