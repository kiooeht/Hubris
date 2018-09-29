package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Metronome extends HubrisRelic
{
    public static final String ID = "hubris:Metronome";
    private static final int AMT = 1;
    private static final int MAX = 10;

    public Metronome()
    {
        super(ID, "metronome.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter >= 0) {
            return DESCRIPTIONS[0] + MAX + DESCRIPTIONS[1] + DESCRIPTIONS[2] + counter + DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[0] + MAX + DESCRIPTIONS[1];
        }
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

    private void doMetronome(int newCounter)
    {
        if (newCounter <= MAX) {
            int prevCounter = counter;
            setCounter(newCounter);

            if (counter != prevCounter) {
                int change = counter - prevCounter;
                flash();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, change), change));
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type == AbstractCard.CardType.ATTACK) {
            doMetronome(counter + AMT);
        } else {
            doMetronome(0);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (damageAmount > 0) {
            doMetronome(0);
        }
        return damageAmount;
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
