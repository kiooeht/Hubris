package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(
        cls = "relicFilter.patches.com.megacrit.cardcrawl.relics.AbstractRelic.AbstractRelicConstructorPatch",
        method = "Insert",
        requiredModId = "RelicFilter"
)
public class FixRelicFilterPatch
{
    public static void Postfix(AbstractRelic __instance, String relicName, String relicImg, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx)
    {
        if (__instance.tier == null) {
            __instance.tier = tier;
        }
    }
}
