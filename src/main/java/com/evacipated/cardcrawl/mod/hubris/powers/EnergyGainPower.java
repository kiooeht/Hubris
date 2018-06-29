package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class EnergyGainPower extends EnergizedPower
{
    public static final String POWER_ID = "hubris:EnergyGain";
    public static final String NAME = "Energy Gain";
    public static final String[] DESCRIPTIONS = {"Gain #b", " additional [E] each turn.", " additional [E] each turn."};

    public EnergyGainPower(AbstractCreature owner, int energyAmt)
    {
        super(owner, energyAmt);
        name = NAME;
        ID = POWER_ID;
    }

    @Override
    public void updateDescription()
    {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onEnergyRecharge()
    {
        flash();
        AbstractDungeon.player.gainEnergy(amount);
    }
}
