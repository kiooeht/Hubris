package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.relics.Reverence;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BulletFireAction extends AbstractGameAction
{
    private static final float DURATION = 0.01f;
    private static final float POST_ATTACK_WAIT_DUR = 0.2f;
    private Reverence reverence;
    private DamageInfo info;
    private int numTimes;

    public BulletFireAction(Reverence reverence, AbstractCreature target, DamageInfo info, int numTimes)
    {
        this.reverence = reverence;
        this.target = target;
        this.info = info;
        this.numTimes = numTimes;
        actionType = ActionType.DAMAGE;
        attackEffect = AttackEffect.FIRE;
        duration = DURATION;
    }

    @Override
    public void update()
    {
        if (target == null) {
            reverence.firing = false;
            isDone = true;
            return;
        }

        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            reverence.firing = false;
            isDone = true;
            return;
        }

        if (target.currentHealth > 0) {
            reverence.setCounter(reverence.counter - 1);
            target.damageFlash = true;
            target.damageFlashFrames = 4;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            info.applyPowers(info.owner, target);
            target.damage(info);
            if (numTimes > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                numTimes -= 1;
                AbstractDungeon.actionManager.addToTop(
                        new BulletFireAction(
                                reverence,
                                AbstractDungeon.getMonsters().getRandomMonster(true),
                                info,
                                numTimes
                        )
                );
            } else {
                reverence.firing = false;
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
        }
        isDone = true;
    }
}
