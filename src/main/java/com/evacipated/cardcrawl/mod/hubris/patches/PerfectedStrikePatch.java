package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.red.StunningStrike;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.red.PerfectedStrike",
        method="isStrike"
)
public class PerfectedStrikePatch
{
    public static boolean Postfix(boolean __result, AbstractCard c)
    {
        return __result || c instanceof StunningStrike;
    }
}
