package com.evacipated.cardcrawl.mod.hubris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

public class StealGoldAction extends AbstractGameAction
{
    private int goldAmount;
    private AbstractGameEffect effect;
    private boolean waitForEffect;

    public StealGoldAction(AbstractCreature target, AbstractCreature source, int goldAmount, boolean waitForEffect)
    {
        setValues(target, source, goldAmount);
        this.goldAmount = goldAmount;
        this.waitForEffect = waitForEffect;
    }

    @Override
    public void update()
    {
        if (effect == null) {
            if (target.gold > 0) {
                CardCrawlGame.sound.play("GOLD_JINGLE");
                if (target.gold < goldAmount) {
                    goldAmount = target.gold;
                }
                target.gold -= goldAmount;
                for (int i=0; i<goldAmount; ++i) {
                    if (source.isPlayer) {
                        effect = new GainPennyEffect(target.hb.cX, target.hb.cY);
                    } else {
                        effect = new GainPennyEffect(source, target.hb.cX, target.hb.cY, source.hb.cX, source.hb.cY, false);
                    }
                    AbstractDungeon.effectList.add(effect);
                }
            }
        }

        if (!waitForEffect || effect == null || effect.isDone) {
            isDone = true;
        }
    }
}
