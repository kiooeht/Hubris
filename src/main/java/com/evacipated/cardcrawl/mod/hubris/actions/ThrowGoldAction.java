package com.evacipated.cardcrawl.mod.hubris.actions;

import com.evacipated.cardcrawl.mod.hubris.vfx.combat.ThrowPennyEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ThrowGoldAction extends AbstractGameAction
{
    private int goldAmount;
    private AbstractGameEffect effect;
    private boolean waitForEffect;

    public ThrowGoldAction(AbstractCreature target, AbstractCreature source, int goldAmount, boolean waitForEffect)
    {
        setValues(target, source, goldAmount);
        this.goldAmount = goldAmount;
        this.waitForEffect = waitForEffect;
    }

    @Override
    public void update()
    {
        if (effect == null) {
            CardCrawlGame.sound.play("GOLD_JINGLE");
            float stagger = 0;
            for (int i=0; i<goldAmount; ++i) {
                effect = new ThrowPennyEffect(source, source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, stagger);
                AbstractDungeon.effectList.add(effect);
                stagger += 0.15f;
            }
        }

        if (!waitForEffect || effect == null || effect.isDone) {
            isDone = true;
        }
    }
}
