package com.evacipated.cardcrawl.mod.hubris.actions.common;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AlwaysRemoveSpecificPowerAction extends RemoveSpecificPowerAction
{
    public AlwaysRemoveSpecificPowerAction(AbstractCreature target, AbstractCreature source, String powerToRemove)
    {
        super(target, source, powerToRemove);
    }

    public AlwaysRemoveSpecificPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerInstance)
    {
        super(target, source, powerInstance);
    }

    @Override
    public void update()
    {
        boolean isDying = target.isDying;
        boolean halfDead = target.halfDead;
        target.isDying = false;
        target.halfDead = false;
        super.update();
        target.isDying = isDying;
        target.halfDead = halfDead;
    }
}
