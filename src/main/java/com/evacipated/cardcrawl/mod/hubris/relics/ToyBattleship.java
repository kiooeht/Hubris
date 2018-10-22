package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ToyBattleship extends HubrisRelic
{
    public static final String ID = "hubris:ToyBattleship";
    private static final int DRAW = 1;

    public ToyBattleship()
    {
        super(ID, "battleship.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DRAW + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStartPostDraw()
    {
        beginLongPulse();
    }

    @Override
    public void onVictory()
    {
        stopPulse();
    }

    @Override
    public void onUseCard(AbstractCard abstractCard, UseCardAction useCardAction)
    {
        if (pulse) {
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, DRAW));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ToyBattleship();
    }
}
