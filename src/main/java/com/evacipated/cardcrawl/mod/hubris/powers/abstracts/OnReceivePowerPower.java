package com.evacipated.cardcrawl.mod.hubris.powers.abstracts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnReceivePowerPower
{
    // return: true to continue
    //         false to negate the power, stopping it from applying
    boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source);
}
