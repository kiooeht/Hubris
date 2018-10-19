package com.evacipated.cardcrawl.mod.hubris.actions.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AlwaysApplyPowerAction extends ApplyPowerAction
{
    public AlwaysApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast)
    {
        super(target, source, powerToApply, stackAmount, isFast);
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return target == null || target.isDeadOrEscaped();
    }
}
