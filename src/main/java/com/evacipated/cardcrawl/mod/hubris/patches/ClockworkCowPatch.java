package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.ClockworkCow;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import constructmod.cards.AbstractCycleCard;

@SpirePatch(
        cls="constructmod.cards.AbstractCycleCard",
        method="canCycle",
        optional=true
)
public class ClockworkCowPatch
{
    private static boolean recursive = false;

    public static void Prefix(AbstractCycleCard __instance)
    {
        if (!recursive) {
            ClockworkCow relic = (ClockworkCow) AbstractDungeon.player.getRelic(ClockworkCow.ID);
            if (relic != null) {
                --__instance.timesCycled;
            }
        }
    }

    public static boolean Postfix(boolean __result, AbstractCycleCard __instance)
    {
        if (!recursive) {
            ClockworkCow relic = (ClockworkCow) AbstractDungeon.player.getRelic(ClockworkCow.ID);
            if (relic != null) {
                ++__instance.timesCycled;
                recursive = true;
                if (__result && !__instance.canCycle()) {
                    //relic.flash();
                }
                recursive = false;
            }
        }
        return __result;
    }
}
