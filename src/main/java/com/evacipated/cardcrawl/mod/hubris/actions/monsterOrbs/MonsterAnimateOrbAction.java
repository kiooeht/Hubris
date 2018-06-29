package com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs;

import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class MonsterAnimateOrbAction extends AbstractGameAction
{
    private OrbUsingMonster owner;
    private int orbCount;

    public MonsterAnimateOrbAction(OrbUsingMonster owner, int amount)
    {
        this.owner = owner;
        orbCount = amount;
    }

    @Override
    public void update()
    {
        for (int i=0; i<orbCount; ++i) {
            owner.triggerEvokeAnimation(i);
        }
        isDone = true;
    }
}
