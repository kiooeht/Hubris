package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Rogue extends HubrisRelic implements OnReceivePowerRelic
{
    public static final String ID = "hubris:Rogue";
    private static final int BUFFER = 10;

    public Rogue()
    {
        super(ID, "test5.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + BUFFER + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        flash();
        AbstractDungeon.player.maxHealth = 1;
        AbstractDungeon.player.currentHealth = 1;
        AbstractDungeon.player.healthBarUpdatedEvent();

        setCounter(BUFFER);
    }

    public int onMaxHPChange(int amount)
    {
        flash();
        return 0;
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new BufferPower(AbstractDungeon.player, counter), counter));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Rogue();
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature)
    {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature source, int stackAmount)
    {
        if (power.ID.equals(BufferPower.POWER_ID)) {
            System.out.println(stackAmount);
            if (stackAmount < 0) {
                counter += stackAmount;
            }
        }
        return stackAmount;
    }
}
