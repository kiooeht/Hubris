package com.evacipated.cardcrawl.mod.hubris.actions.tempHp;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

import java.lang.reflect.Field;

public class AddTemporaryHPAction extends AbstractGameAction
{
    public AddTemporaryHPAction(AbstractCreature target, AbstractCreature source, int amount)
    {
        setValues(target, source, amount);
        actionType = ActionType.HEAL;
    }

    @Override
    public void update()
    {
        if (duration == 0.5f) {
            try {
                Field f = AbstractCreature.class.getDeclaredField("temporaryHealth");
                f.set(target, ((int)f.get(target)) + amount);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (amount > 0) {
                AbstractDungeon.effectsQueue.add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, amount));
                target.healthBarUpdatedEvent();
            }
        }
        tickDuration();
    }
}
