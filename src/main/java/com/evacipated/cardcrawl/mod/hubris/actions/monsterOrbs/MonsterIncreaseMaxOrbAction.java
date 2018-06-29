package com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs;

import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class MonsterIncreaseMaxOrbAction extends AbstractGameAction
{
    private OrbUsingMonster monster;

    public MonsterIncreaseMaxOrbAction(OrbUsingMonster monster, int slotIncrase)
    {
        duration = Settings.ACTION_DUR_FAST;
        amount = slotIncrase;
        this.monster = monster;
        actionType = ActionType.BLOCK;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            monster.increaseMaxOrbSlots(amount, true);
        }
        tickDuration();
    }
}
