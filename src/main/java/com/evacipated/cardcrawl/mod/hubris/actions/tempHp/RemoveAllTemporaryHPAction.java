package com.evacipated.cardcrawl.mod.hubris.actions.tempHp;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.lang.reflect.Field;

public class RemoveAllTemporaryHPAction extends AbstractGameAction
{
    public RemoveAllTemporaryHPAction(AbstractCreature target, AbstractCreature source)
    {
        setValues(target, source);
    }

    @Override
    public void update()
    {
        try {
            Field f = AbstractCreature.class.getDeclaredField("temporaryHealth");
            f.set(target, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        isDone = true;
    }
}