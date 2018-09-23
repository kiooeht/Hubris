package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.SpotWeaknessAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.lang.reflect.Field;

public class CounterAction extends AbstractGameAction
{
    private AbstractMonster targetMonster;

    public CounterAction(AbstractMonster m)
    {
        duration = 0.0f;
        actionType = ActionType.WAIT;
        targetMonster = m;
    }

    @Override
    public void update()
    {
        if (targetMonster != null
                && (targetMonster.intent == AbstractMonster.Intent.ATTACK
                || targetMonster.intent == AbstractMonster.Intent.ATTACK_BUFF
                || targetMonster.intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || targetMonster.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
            try {
                Field f = AbstractMonster.class.getDeclaredField("move");
                f.setAccessible(true);
                EnemyMoveInfo move = (EnemyMoveInfo)f.get(targetMonster);
                int multiplier = 1;
                if (move.isMultiDamage) {
                    multiplier = move.multiplier;
                }

                f = AbstractMonster.class.getDeclaredField("intentDmg");
                f.setAccessible(true);
                move.baseDamage = f.getInt(targetMonster);

                for (int i=0; i<multiplier; ++i) {
                    AbstractDungeon.actionManager.addToTop(new DamageAction(targetMonster, new DamageInfo(AbstractDungeon.player, move.baseDamage, damageType), AttackEffect.FIRE));
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f, SpotWeaknessAction.TEXT[0], true));
        }
        isDone = true;
    }
}
