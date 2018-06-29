package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BlackHole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;

@SpirePatch(
        cls="com.megacrit.cardcrawl.ui.panels.ExhaustPanel",
        method="updatePositions"
)
public class BlackHoleMoveExhaustPile
{
    public static void Prefix(ExhaustPanel __instance)
    {
        if (!__instance.isHidden && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BlackHole.ID)) {
            __instance.target_y = 70.0f * Settings.scale;
        }
    }
}
