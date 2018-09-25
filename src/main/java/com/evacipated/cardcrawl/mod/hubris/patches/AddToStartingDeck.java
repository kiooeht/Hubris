package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.Hubris;
import com.evacipated.cardcrawl.mod.hubris.relics.TinFlute;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="dungeonTransitionSetup"
)
public class AddToStartingDeck
{
    public static void Postfix()
    {
        if (AbstractDungeon.floorNum <= 1 && HubrisMod.startingHubris()) {
            AbstractDungeon.player.masterDeck.addToTop(new Hubris());
            UnlockTracker.markCardAsSeen(Hubris.ID);
        }

        AbstractCard savedCard = TinFlute.getSavedItem();
        if (savedCard != null) {
            AbstractDungeon.player.masterDeck.addToTop(savedCard);
            UnlockTracker.markCardAsSeen(savedCard.cardID);
            TinFlute.deleteSave();
        }
    }
}
