package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MedicalKit;

public class MedicalManual extends AbstractRelic
{
    public static final String ID = "hubris:MedicalManual";
    public static final int HEAL = 1;

    private boolean firstStatus = true;

    public MedicalManual()
    {
        super(ID, "test6.png", RelicTier.RARE, LandingSound.CLINK);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip("Synergy", DESCRIPTIONS[1] + FontHelper.colorString(new MedicalKit().name, "y") + DESCRIPTIONS[2] + HEAL + DESCRIPTIONS[3]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + new MedicalKit().name;
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
