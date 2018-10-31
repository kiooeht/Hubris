package com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class PiercingTag
{
    @SpireEnum
    public static AbstractCard.CardTags HUBRIS_PIERCING;

    @SpirePatch(
            clz=AbstractCard.class,
            method="makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopy
    {
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance)
        {
            if (__instance.hasTag(HUBRIS_PIERCING) != __result.hasTag(HUBRIS_PIERCING)) {
                if (__instance.hasTag(HUBRIS_PIERCING)) {
                    __result.tags.add(HUBRIS_PIERCING);
                } else {
                    __instance.tags.remove(HUBRIS_PIERCING);
                }
            }
            return __result;
        }
    }
}
