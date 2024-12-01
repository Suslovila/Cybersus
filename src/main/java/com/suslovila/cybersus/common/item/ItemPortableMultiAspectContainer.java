package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.IEssentiaHolderItem;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneType;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.client.gui.CybersusGui;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemPortableMultiAspectContainer extends Item implements RuneUsingItem, IEssentiaHolderItem {
    public static final String name = "multi_aspect_holder";

    public ItemPortableMultiAspectContainer() {
        setUnlocalizedName(name);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setMaxStackSize(1);
        setCreativeTab(Cybersus.tab);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean wtfIsThisVariable) {
        AspectList aspects = getStoredAspects(stack);
        if (aspects.size() > 0) {
            for (Aspect aspect : aspects.getAspectsSorted()) {
                if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), aspect)) {
                    list.add(aspect.getName() + " : " + aspects.getAmount(aspect));
                } else {
                    list.add(StatCollector.translateToLocal("tc.aspect.unknown"));
                }
            }
        }
        super.addInformation(stack, player, list, wtfIsThisVariable);
    }

    @Override
    public AspectList getStoredAspects(ItemStack stack) {
        if (stack.hasTagCompound()) {
            AspectList aspects = new AspectList();
            aspects.readFromNBT(stack.getTagCompound());
            return aspects;
        }
        return new AspectList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
            return super.getIconFromDamageForRenderPass(par1, renderPass);
    }
    @Override
    public void setStoredAspects(ItemStack stack, AspectList aspects) {
        aspects.writeToNBT(KhariumSusNBTHelper.getOrCreateTag(stack));
    }

    @Override
    public int addAspect(ItemStack stack, Aspect aspect, int amount) {
        AspectList aspects = getStoredAspects(stack);
        int emptySpace = getMaxVisCountForEachAspect(stack) - aspects.getAmount(aspect);
        int toAdd = Math.min(emptySpace, amount);
        aspects.add(aspect, toAdd);
        aspects.writeToNBT(KhariumSusNBTHelper.getOrCreateTag(stack));

        return amount - toAdd;
    }

    public int getMaxVisCountForEachAspect(ItemStack stack) {
        return 128;
    }

    @Override
    public int getMaxRuneAmount() {
        return 5;
    }

    public static Aspect getRequiredAspect(ItemStack stack) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
        if (!tag.hasKey(REQUIRED_ASPECT_NBT)) return null;
        return Aspect.getAspect(tag.getString(REQUIRED_ASPECT_NBT));
    }

    public static int getRequiredAmount(ItemStack stack) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
        if (!tag.hasKey(REQUIRED_ASPECT_AMOUNT_NBT)) return 0;
        return tag.getInteger(REQUIRED_ASPECT_AMOUNT_NBT);
    }

    public int getMaxAspectTypesAmount(ItemStack stack) {
        return 8;

    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            ItemStack stack = event.entityPlayer.getHeldItem();
            if (stack != null && stack.getItem() instanceof ItemPortableMultiAspectContainer) {
                event.entityPlayer.openGui(
                        Cybersus.MOD_ID,
                        CybersusGui.ITEM_ASPECT_HOLDER.ordinal(),
                        event.world,
                        (int) event.entityPlayer.posX,
                        (int) event.entityPlayer.posY,
                        (int) event.entityPlayer.posZ
                );
            }
            return;
        }
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack stack = event.entityPlayer.getHeldItem();
            if (stack != null) {
                TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
                if (tile instanceof IAspectContainer && stack.getItem() instanceof ItemPortableMultiAspectContainer) {
                    ItemPortableMultiAspectContainer itemType = (ItemPortableMultiAspectContainer) stack.getItem();
                    IAspectContainer iAspectContainer = (IAspectContainer) tile;
                    Aspect requiredAspect = itemType.getRequiredAspect(stack);
                    if (requiredAspect != null) {
                        int requiredAmount = itemType.getRequiredAmount(stack);
                        AspectList stored = itemType.getStoredAspects(stack);
                        if (!event.entityPlayer.isSneaking()) {
                            boolean hasTileEnough = iAspectContainer.doesContainerContainAmount(requiredAspect, requiredAmount);
                            int maxAspectAmount = itemType.getMaxAspectTypesAmount(stack);
                            boolean haveAlreadyAspect = stored.aspects.containsKey(requiredAspect);
                            boolean hasSpaceForNewAspect = true;
                            if (!haveAlreadyAspect) {
                                long currentAspectAmount = stored.aspects.entrySet().stream().filter(aspect -> aspect.getValue() != 0).count();
                                hasSpaceForNewAspect = currentAspectAmount < maxAspectAmount;
                            }
                            if (hasTileEnough && (haveAlreadyAspect || hasSpaceForNewAspect)) {
                                int overlapped = itemType.addAspect(stack, requiredAspect, requiredAmount);
                                iAspectContainer.takeFromContainer(requiredAspect, requiredAmount - overlapped);
                            }
                        } else {
                            boolean hasPortableContainerEnough = stored.getAmount(requiredAspect) >= requiredAmount;
                            boolean hasSpaceForNewAspect = iAspectContainer.doesContainerAccept(requiredAspect);

                            if (hasPortableContainerEnough && hasSpaceForNewAspect) {
                                int overlapped = iAspectContainer.addToContainer(requiredAspect, requiredAmount);
                                AspectList result = itemType.getStoredAspects(stack).remove(requiredAspect, requiredAmount - overlapped);
                                itemType.setStoredAspects(stack, result);
                            }
                        }
                    }
                }
            }
        }
    }

    public static final String REQUIRED_ASPECT_NBT = Cybersus.prefixAppender.doAndGet("required_aspects");
    public static final String REQUIRED_ASPECT_AMOUNT_NBT = Cybersus.prefixAppender.doAndGet("required_amount");


}