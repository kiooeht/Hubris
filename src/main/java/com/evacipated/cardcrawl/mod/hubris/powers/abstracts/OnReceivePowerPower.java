package com.evacipated.cardcrawl.mod.hubris.powers.abstracts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnReceivePowerPower
{
    void onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source);
}
