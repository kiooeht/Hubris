package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FunFungus extends HubrisRelic
{
    public static final String ID = "hubris:FunFungus";
    private static final int AMT = 5;
    private boolean firstTurn = false;
    private boolean healNext = false;

    public FunFungus()
    {
        super(ID, "funFungus.png", RelicTier.RARE, LandingSound.FLAT);

        pulse = false;
    }

    @Override
    public void atPreBattle()
    {
        flash();
        firstTurn = true;
        healNext = true;
        if (!pulse) {
            beginPulse();
            pulse = true;
        }
    }

    @Override
    public void atTurnStart()
    {
        beginPulse();
        pulse = true;
        if (healNext && !firstTurn) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, AMT));
        }
        firstTurn = false;
        healNext = true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type == AbstractCard.CardType.ATTACK) {
            healNext = false;
            pulse = false;
        }
    }

    @Override
    public void onVictory()
    {
        pulse = false;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new FunFungus();
    }
}
