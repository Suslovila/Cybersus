package com.suslovila.cybersus.client.gui;


import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.common.block.container.ContainerImplantHolder;
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


public class GuiImplantInstaller extends GuiContainer {
    private float mouseX = 0f;
    private float mouseY = 0f;

    EntityPlayer player;

    public GuiImplantInstaller(EntityPlayer player) {
        super(new ContainerImplantHolder(player));
        this.player = player;
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
        renderPlayerModel(guiLeft + xSize / 2, guiTop + ySize / 2, 50, 0f, 0f, player);

//        SusGraphicHelper.popLight();
    }

    @Override
    public void drawSlot(Slot slotIn) {
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



        public void renderPlayerModel(int x, int y, int scale, float yaw, float pitch, EntityLivingBase playerdrawn) {
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x, (float) y, 50.0f);
            GL11.glScalef((-scale), scale, scale);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            float f2 = playerdrawn.renderYawOffset;
            float f3 = playerdrawn.rotationYaw;
            float f4 = playerdrawn.rotationPitch;
            float f5 = playerdrawn.prevRotationYawHead;
            float f6 = playerdrawn.rotationYawHead;
            GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f);
            RenderHelper.enableStandardItemLighting();
            GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef((float) (-Math.atan((pitch / 40.0f)) * 20.0), 1.0f, 0.0f, 0.0f);
            playerdrawn.renderYawOffset = (float) (Math.atan((yaw / 40.0f)) * 20.0);
            playerdrawn.rotationYaw = (float) (Math.atan((yaw / 40.0f)) * 40.0);
            playerdrawn.rotationPitch = (float) (-Math.atan((pitch / 40.0f)) * 20.0);
            playerdrawn.rotationYawHead = playerdrawn.rotationYaw;
            playerdrawn.prevRotationYawHead = playerdrawn.rotationYaw;
            GL11.glTranslatef(0.0f, playerdrawn.yOffset, 0.0f);
            RenderManager.instance.playerViewY = 180.0f;
            RenderManager.instance.renderEntityWithPosYaw(playerdrawn, 0.0, 0.0, 0.0, 0.0f, 1.0f);
            playerdrawn.renderYawOffset = f2;
            playerdrawn.rotationYaw = f3;
            playerdrawn.rotationPitch = f4;
            playerdrawn.prevRotationYawHead = f5;
            playerdrawn.rotationYawHead = f6;
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
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
