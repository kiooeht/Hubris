package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class QuietLoseHPAction extends LoseHPAction
{
    private static final float DURATION = 0.33f;

    public QuietLoseHPAction(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(target, source, amount);
        attackEffect = AttackEffect.NONE;
    }

    @Override
    public void update()
    {
        if (duration == DURATION && target.currentHealth > 0) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
        }
        tickDuration();
        if (isDone) {
            target.currentHealth -= amount;
            target.healthBarUpdatedEvent();
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
        }
    }
}
