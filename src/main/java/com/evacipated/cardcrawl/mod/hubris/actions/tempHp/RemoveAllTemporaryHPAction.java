package com.evacipated.cardcrawl.mod.hubris.actions.tempHp;

import com.evacipated.cardcrawl.mod.hubris.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class RemoveAllTemporaryHPAction extends AbstractGameAction
{
    public RemoveAllTemporaryHPAction(AbstractCreature target, AbstractCreature source)
    {
        setValues(target, source);
    }

    @Override
    public void update()
    {
        TempHPField.tempHp.set(target, 0);
        isDone = true;
    }
}