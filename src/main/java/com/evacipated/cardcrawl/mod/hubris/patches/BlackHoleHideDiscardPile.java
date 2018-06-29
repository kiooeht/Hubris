package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BlackHole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel;

@SpirePatch(
        cls="com.megacrit.cardcrawl.ui.panels.DiscardPilePanel",
        method="updatePositions"
)
public class BlackHoleHideDiscardPile
{
    public static void Prefix(DiscardPilePanel __instance)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BlackHole.ID)) {
            __instance.hide();
        }
    }
}
