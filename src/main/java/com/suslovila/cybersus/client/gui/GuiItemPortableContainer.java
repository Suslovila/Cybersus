package com.suslovila.cybersus.client.gui;

import com.suslovila.cybersus.common.item.ItemPortableMultiAspectContainer;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.PacketItemPortableContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;
import java.util.List;

public class GuiItemPortableContainer extends GuiScreen {
    protected int enterButtonKeyboardId = 28;
    protected int xTextureSize = 511;
    protected int yTextureSize = 367;
    protected int guiLeft = 0;
    protected int guiTop = 0;

    // obviously, you should not fire this field until "Aspect" class is fully initialised
    public static List<Aspect> buttonAssociations = new ArrayList<>(Aspect.aspects.values());

    public GuiTextField requiredAmount;
    public Aspect currentAspect;

    public ItemStack portableContainer;

    public GuiItemPortableContainer(ItemStack itemIn) {
        this.currentAspect = ItemPortableMultiAspectContainer.getRequiredAspect(itemIn);
        portableContainer = itemIn;
    }

    @Override
    public void initGui() {
        super.initGui();
        xTextureSize = 256;
        yTextureSize = 198;
        guiLeft = (width - this.xTextureSize) / 2;
        guiTop = (height - this.yTextureSize) / 2;
        buttonList.clear();

        int buttonSize = 24;
        int offsetBetweenButtons = 4;
        int lineCounter = 1;
        int rowCounter = 0;
        int maxAspectAmountInLine = 9;

        for (int index = 0; index < buttonAssociations.size(); index++) {
            Aspect aspect = buttonAssociations.get(index);
            buttonList.add(new GuiSynthesizerButtonAspect(
                    this,
                    index,
                    guiLeft + rowCounter * (buttonSize + offsetBetweenButtons),
                    guiTop + lineCounter * (buttonSize + offsetBetweenButtons),
                    buttonSize,
                    buttonSize,
                    ""
            ));
            rowCounter += 1;
            if (rowCounter % maxAspectAmountInLine == 0 ||
                    rowCounter * (buttonSize + offsetBetweenButtons) > guiLeft + width) {
                rowCounter = 0;
                lineCounter += 1;
            }
        }

        requiredAmount = new GuiTextField(fontRendererObj, guiLeft + 100, guiTop + 13, 50, 12);
        setDefaultTextSettings(requiredAmount);
        requiredAmount.setText(String.valueOf(ItemPortableMultiAspectContainer.getRequiredAmount(portableContainer)));
    }


    @Override
    public void drawScreen(int x, int y, float p_73863_3_) {
        super.drawScreen(x, y, p_73863_3_);
        requiredAmount.drawTextBox();
    }

    @Override
    public void drawBackground(int p_146278_1_) {
    }

    @Override
    public void actionPerformed(GuiButton button) {
        Aspect clickedAspect = buttonAssociations.get(button.id);
        currentAspect = clickedAspect;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    private void setDefaultTextSettings(GuiTextField textField) {
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(true);
        textField.setMaxStringLength(40);
    }

    @Override
    public void keyTyped(char par1, int keyId) {
        if (requiredAmount.textboxKeyTyped(par1, keyId)) return;
        if (keyId == enterButtonKeyboardId) {
            if (requiredAmount.isFocused()) {
                requiredAmount.setFocused(false);
                return;
            }
        }
        super.keyTyped(par1, keyId);
    }

    @Override
    public void mouseClicked(int x, int y, int par3) {
        super.mouseClicked(x, y, par3);
        if (isMouseOnField(requiredAmount, x - guiLeft, y - guiTop)) {
            requiredAmount.setFocused(true);
        }
    }

    public boolean isMouseOnField(GuiTextField field, int xClick, int yClick) {
        return ((xClick + guiLeft) < (field.xPosition + field.width) && (xClick + guiLeft) > field.xPosition)
                && ((yClick + guiTop) < (field.yPosition + field.height) && (yClick + guiTop) > field.yPosition);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        int castedAmount;
        try {
            castedAmount = Integer.parseInt(requiredAmount.getText());
        } catch (Exception exception) {
            castedAmount = 0;
        }
        if (currentAspect != null) {
            CybersusPacketHandler.INSTANCE.sendToServer(
                    new PacketItemPortableContainer(
                            currentAspect.getTag(),
                            castedAmount
                    )
            );
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
