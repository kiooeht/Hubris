package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.BottleRainField;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PyramidsField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.CardGroup",
        method="getGroupWithoutBottledCards"
)
public class WithoutBottledCardsPatch
{
    public static CardGroup Postfix(CardGroup __result, CardGroup group)
    {
        __result.group.removeIf(BottleRainField.inBottleRain::get);
        __result.group.removeIf(PyramidsField.inPyramids::get);

        return __result;
    }
}
