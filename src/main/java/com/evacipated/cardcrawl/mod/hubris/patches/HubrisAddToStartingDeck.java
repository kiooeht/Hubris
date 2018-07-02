package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.Hubris;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

@SpirePatch(
        cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
        method="dungeonTransitionSetup"
)
public class HubrisAddToStartingDeck
{
    public static void Postfix()
    {
        if (AbstractDungeon.floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
            AbstractDungeon.player.masterDeck.addToTop(new Hubris());
            UnlockTracker.markRelicAsSeen(Hubris.ID);
        }
    }
}
