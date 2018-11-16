package com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method=SpirePatch.CLASS
)
public class ZylophoneField
{
    public static SpireField<Boolean> costsX = new CostsXField(() -> false);

    // This is done so `cost` can be set automatically to -1 when this field is set
    private static class CostsXField extends SpireField<Boolean>
    {
        CostsXField(DefaultValue<Boolean> defaultValue)
        {
            super(defaultValue);
        }

        @Override
        public void set(Object __intance, Boolean value)
        {
            super.set(__intance, value);
            if (value && __intance instanceof AbstractCard) {
                ((AbstractCard)__intance).cost = -1;
                ((AbstractCard)__intance).costForTurn = -1;
            } else if (!value && __intance instanceof AbstractCard) {
                AbstractCard copy = ((AbstractCard) __intance).makeCopy();
                ((AbstractCard) __intance).cost = copy.cost;
                ((AbstractCard) __intance).costForTurn = copy.costForTurn;
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopy
    {
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance)
        {
            if (costsX.get(__instance)) {
                costsX.set(__result, true);
                __result.cost = -1;
                __result.costForTurn = -1;
            }
            return __result;
        }
    }
}
