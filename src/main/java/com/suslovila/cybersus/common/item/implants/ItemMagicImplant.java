package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.common.item.ItemImplant;

public abstract class ItemMagicImplant extends ItemImplant implements RuneUsingItem {
    public ItemMagicImplant(ImplantType implantType) {
        super(implantType);
    }
}
