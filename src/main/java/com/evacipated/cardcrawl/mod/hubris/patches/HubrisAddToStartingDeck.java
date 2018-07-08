package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.Hubris;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

@SpirePatch(
        cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
        method="dungeonTransitionSetup"
)
public class HubrisAddToStartingDeck
{
    public static void Postfix()
    {
        if (AbstractDungeon.floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Hubris(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
    }
}
