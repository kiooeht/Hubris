package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.relics.EvacipatedBox;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;

import java.util.ArrayList;

public class EvacipatedBoxPatch
{
    @SpirePatch(
            clz=BossRelicSelectScreen.class,
            method="open"
    )
    public static class Open
    {
        @SpireInsertPatch(
                rloc=26
        )
        public static void Insert(BossRelicSelectScreen __instance, ArrayList<AbstractRelic> chosenRelics)
        {
            if (AbstractDungeon.player.hasRelic(EvacipatedBox.ID)) {
                float SLOT_2_X = __instance.relics.get(1).currentX;
                float SLOT_3_X = __instance.relics.get(2).currentX;
                float SLOT_1_Y = __instance.relics.get(0).currentY;

                // Spawn 4th relic
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
                HubrisMod.logger.info("Created 4th boss relic: " + relic.relicId);
                relic.spawn(SLOT_3_X, SLOT_1_Y);
                relic.hb.move(relic.currentX, relic.currentY);
                __instance.relics.add(relic);

                // Move 1st relic
                AbstractRelic first = __instance.relics.get(0);
                first.currentX = SLOT_2_X;
                first.hb.move(first.currentX, first.currentY);
            }
        }
    }
}
