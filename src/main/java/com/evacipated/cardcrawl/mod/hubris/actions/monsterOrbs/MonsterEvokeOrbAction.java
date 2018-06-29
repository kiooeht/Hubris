package com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs;

import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class MonsterEvokeOrbAction extends AbstractGameAction
{
    private OrbUsingMonster owner;
    private int orbCount;

    public MonsterEvokeOrbAction(OrbUsingMonster owner, int amount)
    {
        duration = Settings.ACTION_DUR_FAST;
        this.owner = owner;
        orbCount = amount;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            for (int i=0; i<orbCount; ++i) {
                owner.evokeOrb();
            }
        }
        tickDuration();
    }
}
