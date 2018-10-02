package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;

public class GainFocusPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:GainFocus";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GainFocusPower(AbstractCreature owner, int newAmount)
    {
        ID = POWER_ID;
        name = NAME;
        this.owner = owner;
        amount = newAmount;
        type = PowerType.BUFF;
        updateDescription();
        loadRegion("focus");
        if (amount > 999) {
            amount = 999;
        } else if (amount < -999) {
            amount = -999;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        fontScale = 8.0f;
        amount += stackAmount;
        if (amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
        }
        if (amount > 999) {
            amount = 999;
        } else if (amount < -999) {
            amount = -999;
        }
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        fontScale = 8.0f;
        amount -= reduceAmount;
        if (amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
        }
        if (amount > 999) {
            amount = 999;
        } else if (amount < -999) {
            amount = -999;
        }
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new FocusPower(owner, amount), amount));
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
    }
}
