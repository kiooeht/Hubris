package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.powers.SpecializedCircuitryPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class SpecializedCircuitryPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method="channelOrb"
    )
    public static class ReplaceOrbs
    {
        public static void Prefix(AbstractPlayer __instance, @ByRef AbstractOrb[] orbToSet)
        {
            if (__instance.hasPower(SpecializedCircuitryPower.POWER_ID)) {
                SpecializedCircuitryPower power = (SpecializedCircuitryPower) __instance.getPower(SpecializedCircuitryPower.POWER_ID);
                orbToSet[0] = power.getOrb();
            }
        }
    }
}
