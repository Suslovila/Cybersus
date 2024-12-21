package com.suslovila.cybersus.client.gui;


import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.common.block.container.ContainerImplantHolder;
import com.suslovila.cybersus.common.block.container.envyEye.ContainerBaublesEnvy;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;


public class GuiEnvyEye extends GuiContainer {
    private float mouseX = 0f;
    private float mouseY = 0f;

    EntityPlayer envier;
    EntityPlayer victim;

    public GuiEnvyEye(EntityPlayer envier, EntityPlayer victim) {
        super(new ContainerBaublesEnvy(envier, victim));
        this.envier = envier;
        this.victim = victim;
        this.xSize = 481;
        this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void actionPerformed(GuiButton button) {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mouseX = (float) mouseX;
        this.mouseY = (float) mouseY;


    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        SusGraphicHelper.pushLight();
//        SusGraphicHelper.setMaxBrightness();
        Minecraft.getMinecraft().getTextureManager().bindTexture(textureRight);
        drawTexturedModalRect(guiLeft + 256, guiTop, 0, 0, 225, ySize);
        Minecraft.getMinecraft().getTextureManager().bindTexture(textureLeft);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, ySize);

//        SusGraphicHelper.popLight();
    }

    @Override
    public void drawSlot(Slot slotIn) {
        SusGraphicHelper.bindColor(Color.white.getRGB(), 1.0f, 1.0f);
        int i = slotIn.xDisplayPosition;
        int j = slotIn.yDisplayPosition;
        ItemStack stackInSlot = slotIn.getStack();
        boolean flag = false;
        ItemStack itemstack1 = mc.thePlayer.inventory.getItemStack();
        String s = null;

        if (slotIn == clickedSlot && draggedStack != null && isRightMouseClick && stackInSlot != null) {
            stackInSlot = stackInSlot.copy();
            stackInSlot.stackSize /= 2;
        } else if (dragSplitting && dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
            if (dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && inventorySlots.canDragIntoSlot(slotIn)) {
                stackInSlot = itemstack1.copy();
                flag = true;
                Container.computeStackSize(dragSplittingSlots, dragSplittingLimit, stackInSlot,
                    (slotIn.getStack() == null) ? 0 : slotIn.getStack().stackSize);
                if (stackInSlot.stackSize > stackInSlot.getMaxStackSize()) {
                    s = EnumChatFormatting.YELLOW.toString() + "" + stackInSlot.getMaxStackSize();
                    stackInSlot.stackSize = stackInSlot.getMaxStackSize();
                }
                if (stackInSlot.stackSize > slotIn.getSlotStackLimit()) {
                    s = EnumChatFormatting.YELLOW.toString() + "" + slotIn.getSlotStackLimit();
                    stackInSlot.stackSize = slotIn.getSlotStackLimit();
                }
            } else {
                dragSplittingSlots.remove(slotIn);
                updateDragSplitting();
            }
        }

        zLevel = 100.0f;
        itemRender.zLevel = 100.0f;

        IIcon iicon = slotIn.getBackgroundIconIndex();
        if (iicon != null) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND); // Forge: Blending needs to be enabled for this.
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            drawTexturedModelRectFromIcon(i, j, iicon, 16, 16);
            GL11.glDisable(GL11.GL_BLEND); // Forge: And clean that up
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        if (flag) {
            drawRect(i, j, i + 16, j + 16, -2130706433);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), stackInSlot, i, j);
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), stackInSlot, i, j, s);

        itemRender.zLevel = 0.0f;
        zLevel = 0.0f;
    }

    public void updateScreen() {
        try {
            ((ContainerImplantHolder)this.inventorySlots).implantStorage.blockEvents = false;
        } catch (Exception e) {}
    }


        private static final ResourceLocation[] slotTextures = new ResourceLocation[ImplantType.typeAmount];

        static {
            for (int i = 0; i < ImplantType.typeAmount; i++) {
                slotTextures[i] = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implantSlots/" + ImplantType.values()[i] + ".png");
            }
        }

        private static final ResourceLocation IMAGE_URL = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/assembly_table.png");
        private static final ResourceLocation textureLeft = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/DraconicChestLeft.png");
        private static final ResourceLocation textureRight = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/DraconicChestRight.png");

}
