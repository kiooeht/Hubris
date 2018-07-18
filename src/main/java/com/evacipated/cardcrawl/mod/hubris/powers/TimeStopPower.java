package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.StartNewTurnAction;
import com.evacipated.cardcrawl.mod.hubris.cards.blue.TimeStop;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;

public class TimeStopPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:TimeStop";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public TimeStopPower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        updateDescription();
        loadRegion("time");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn()
    {
        if (amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ID, 1));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new NoAttackPower(owner)));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new BlurPower(owner, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new StartNewTurnAction());
        }
    }
}
