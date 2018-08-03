package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.ClockworkCow;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import constructmod.cards.AbstractCycleCard;

@SpirePatch(
        cls="constructmod.cards.AbstractCycleCard",
        method="cycle",
        optional=true
)
public class ClockworkCowPatch
{
    @SpireInsertPatch(
            rloc=5
    )
    public static void Insert(AbstractCycleCard __instance)
    {
        ClockworkCow relic = (ClockworkCow) AbstractDungeon.player.getRelic(ClockworkCow.ID);
        if (relic != null) {
            relic.onCycleCard(__instance);
        }
    }
}
