package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.ZylophoneField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(
        clz=CardGroup.class,
        method="getGroupWithoutBottledCards"
)
public class WithoutBottledCardsPatch
{
    public static CardGroup Postfix(CardGroup __result, CardGroup group)
    {
        __result.group.removeIf(ZylophoneField.costsX::get);
        __result.group.removeIf(c -> c instanceof DuctTapeCard);

        return __result;
    }
}
