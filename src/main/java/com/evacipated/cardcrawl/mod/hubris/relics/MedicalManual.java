package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MedicalManual extends AbstractRelic
{
    public static final String ID = "hubris:MedicalManual";
    public static final int HEAL = 1;

    private boolean firstStatus = true;

    public MedicalManual()
    {
        super(ID, "test6.png", RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + HEAL + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart()
    {
        firstStatus = true;

        beginPulse();
        pulse = true;
    }

    @Override
    public void onCardDraw(AbstractCard card)
    {
        if (firstStatus && card.type == AbstractCard.CardType.STATUS) {
            firstStatus = false;
            pulse = false;

            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new MedicalManual();
    }
}
