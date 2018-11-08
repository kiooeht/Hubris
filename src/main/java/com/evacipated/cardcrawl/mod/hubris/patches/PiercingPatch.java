package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PiercingTag;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

@SpirePatch(
        clz=AbstractCard.class,
        method="resetAttributes"
)
public class PiercingPatch
{
    public static void Postfix(AbstractCard __instance)
    {
        if (__instance.hasTag(PiercingTag.HUBRIS_PIERCING)) {
            __instance.damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
        }
    }
}
