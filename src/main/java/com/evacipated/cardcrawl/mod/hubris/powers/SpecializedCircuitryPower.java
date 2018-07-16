package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpecializedCircuitryPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:SpecializedCircuitry";

    private AbstractOrb orbType;

    public SpecializedCircuitryPower(AbstractCreature owner, AbstractOrb orbType)
    {
        name = orbType.name + " Circuitry";
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = -1;
        this.orbType = orbType;
        updateDescription();
        img = ImageMaster.ORB_PLASMA;
    }

    @Override
    public void updateDescription()
    {
        description = "All orbs you #yChannel are #y" + orbType.name + " orbs.";
    }

    public void setOrbType(AbstractOrb orbType)
    {
        this.orbType = orbType;
    }

    public AbstractOrb getOrb()
    {
        return orbType.makeCopy();
    }
}
