package com.suslovila.cybersus.client.gui;


import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneType;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.common.block.container.ContainerRuneInstaller;
import com.suslovila.cybersus.common.block.runeInstaller.TileRuneInstaller;
import com.suslovila.cybersus.common.item.ModItems;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.PacketRuneInstallerButtonClicked;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

 public class GuiRuneInstaller extends GuiContainer {
    private static final ResourceLocation backTexture = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/runeInstaller.png");

    private final TileRuneInstaller tile;
    private final EntityPlayer player;

    public GuiRuneInstaller(TileRuneInstaller tile, EntityPlayer player) {
        super(new ContainerRuneInstaller(tile, player));
        this.tile = tile;
        this.player = player;
        this.xSize = 481;
        this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();

        int center = xSize / 2;
        int offset = 34;
        int runeAmount = RuneType.values().length;
        int startXPosition = (int) (center - (runeAmount / 2.0)) * offset;
        int xOffset = 0;
        int startYPosition = guiTop + 120;

        int[][] buttonIndexes = new int[runeAmount][2];
        for (int index = 0; index < runeAmount; index++) {
            buttonIndexes[index][0] = index * 2;
            buttonIndexes[index][1] = index * 2 + 1;
        }

        for (int[] buttonIndexesPair : buttonIndexes) {
            GuiButton removeButton = new GuiButton(buttonIndexesPair[0], startXPosition + xOffset, startYPosition, 4, 4, "-");
            GuiButton addButton = new GuiButton(buttonIndexesPair[1], startXPosition + xOffset + 6, startYPosition, 4, 4, "+");
            buttonList.add(addButton);
            buttonList.add(removeButton);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        CybersusPacketHandler.INSTANCE.sendToServer(new PacketRuneInstallerButtonClicked(
                tile,
                RuneType.values()[button.id / 2],
                button.id % 2 != 0
        ));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(backTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 225, ySize);

        renderRunes();
    }

    private void renderRunes() {
        ItemStack stack = tile.inventory.getStackInSlot(0);
        int center = (guiLeft + width / 2);
        int runeAmount = RuneType.values().length;
        int offset = 40;
        int startXPosition = (int) (center - (runeAmount / 2.0) * offset);

        for (int i = 0; i < runeAmount; i++) {
            SusGraphicHelper.drawStack(itemRender, new ItemStack(ModItems.itemRune, 1, i), startXPosition + i * offset, 100, 200f);
            RuneType runeType = RuneType.values()[i];
            if (stack != null) {
                int currentAmount = RuneUsingItem.getRuneAmountOfType(stack, runeType);
                fontRendererObj.drawString(Integer.toString(currentAmount), startXPosition, 84, 7877878);
            }
        }
    }
}

