package com.evacipated.cardcrawl.mod.hubris.trials;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.trials.CustomTrial;

public class MercantileTrial
{
    @SpirePatch(
            clz=CustomTrial.class,
            method=SpirePatch.CLASS
    )
    public static class MercantileOverrides
    {
        public static SpireField<Integer> startingGold = new SpireField<>(() -> null);
        public static SpireField<Integer> startingOrbSlots = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz=CustomTrial.class,
            method="setupPlayer"
    )
    public static class SetupPlayer
    {
        public static AbstractPlayer Postfix(AbstractPlayer __result, CustomTrial __instance, AbstractPlayer player)
        {
            Integer goldOverride = MercantileOverrides.startingGold.get(__instance);
            if (goldOverride != null) {
                __result.gold = goldOverride;
            }
            Integer orbSlotsOverride = MercantileOverrides.startingOrbSlots.get(__instance);
            if (orbSlotsOverride != null) {
                __result.masterMaxOrbs = orbSlotsOverride;
            }
            return __result;
        }
    }
}
