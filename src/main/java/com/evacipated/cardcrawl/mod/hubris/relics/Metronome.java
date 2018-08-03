package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Metronome extends AbstractRelic
{
    public static final String ID = "hubris:Metronome";
    private static final int AMT = 1;

    public Metronome()
    {
        super(ID, "metronome.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + counter + DESCRIPTIONS[2];
    }

    @Override
    public void setCounter(int c)
    {
        super.setCounter(c);
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        int prevCounter = counter;
        if (card.type == AbstractCard.CardType.ATTACK) {
            setCounter(counter + AMT);
        } else {
            setCounter(0);
        }

        if (counter != prevCounter) {
            int change = counter - prevCounter;
            flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, change), change));
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public void onEquip()
    {
        setCounter(0);
    }

    @Override
    public void atPreBattle()
    {
        if (counter > 0) {
            flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, counter), counter));
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Metronome();
    }
}
