package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.TinFlute;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

@SpirePatch(
        cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
        method="dungeonTransitionSetup"
)
public class TinFlutePatch
{
    public static void Postfix()
    {
        if (AbstractDungeon.floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
            AbstractCard savedCard = TinFlute.getSavedItem();
            if (savedCard != null) {
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new TinFlute()));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(savedCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                TinFlute.deleteSave();
            }
        }
    }
}
