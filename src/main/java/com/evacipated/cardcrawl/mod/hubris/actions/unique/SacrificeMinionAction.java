package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SacrificeMinionAction extends AbstractGameAction
{
    private AbstractCreature parent;
    private AbstractMonster m;

    public SacrificeMinionAction(AbstractCreature parent, AbstractMonster minion)
    {
        this.parent = parent;
        m = minion;
        actionType = ActionType.DAMAGE;
        duration = 0;
    }

    @Override
    public void update()
    {
        if (duration == 0) {
            m.currentHealth = 0;
            m.halfDead = true;
            m.die(false);
            m.isDead = false;
            m.isDying = false;
            m.deathTimer = 0;
            m.healthBarUpdatedEvent();

            AbstractDungeon.actionManager.addToBottom(new RaiseDeadAction(parent, m));
        }
        tickDuration();
    }
}