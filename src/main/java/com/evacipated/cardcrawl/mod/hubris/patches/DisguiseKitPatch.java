package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.DisguiseKit;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
        method="initializeCardPools"
)
public class DisguiseKitPatch
{
    private static AbstractPlayer.PlayerClass realClass;

    public static void Prefix(AbstractDungeon __instance)
    {
        realClass = AbstractDungeon.player.chosenClass;
        DisguiseKit relic = (DisguiseKit) AbstractDungeon.player.getRelic(DisguiseKit.ID);
        if (relic != null && relic.chosenClass != null) {
            AbstractDungeon.player.chosenClass = relic.chosenClass;
        }
    }

    public static void Postfix(AbstractDungeon __instance)
    {
        AbstractDungeon.player.chosenClass = realClass;
    }
}
