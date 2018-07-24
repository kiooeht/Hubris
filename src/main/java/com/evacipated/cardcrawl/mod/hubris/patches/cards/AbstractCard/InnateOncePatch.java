package com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class InnateOncePatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method=SpirePatch.CLASS
    )
    public static class Field
    {
        public static SpireField<Boolean> isInnateOnce = new SpireField<>(false);
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopy
    {
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance)
        {
            if (Field.isInnateOnce.get(__instance)) {
                __result.isInnate = true; // TODO: Bad. Duplicate shrine makes copy permanently Innate
                Field.isInnateOnce.set(__instance, false);
            }

            return __result;
        }
    }
}
