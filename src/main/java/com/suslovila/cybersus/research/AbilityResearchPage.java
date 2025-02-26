package com.suslovila.cybersus.research;

import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.common.item.ItemImplant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import thaumcraft.api.research.ResearchPage;

import static thaumcraft.api.research.ResearchPage.PageType.TEXT;
import static thaumcraft.api.research.ResearchPage.PageType.TEXT_CONCEALED;

public class AbilityResearchPage extends ResearchPage {
    public AbilityResearchPage(ItemImplant implant, Ability ability) {
        super(ability.getThaumonomiconText(implant));
        this.type = TEXT;
    }

    public String getTranslatedText() {
        return text;
    }
}
