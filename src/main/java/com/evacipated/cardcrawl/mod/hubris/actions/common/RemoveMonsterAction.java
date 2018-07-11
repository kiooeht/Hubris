package com.evacipated.cardcrawl.mod.hubris.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RemoveMonsterAction extends AbstractGameAction
{
    private AbstractMonster monster;

    public RemoveMonsterAction(AbstractMonster monster)
    {
        this.monster = monster;
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.getCurrRoom().monsters.monsters.remove(monster);
        }
        tickDuration();
    }
}
