package com.evacipated.cardcrawl.mod.hubris.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

public class BlueSmokeBombEffect extends SmokeBombEffect
{
    protected float x;
    protected float y;

    public BlueSmokeBombEffect(float x, float y)
    {
        super(x, y);

        this.x = x;
        this.y = y;
    }

    @Override
    public void update()
    {
        if (duration == 0.2f) {
            CardCrawlGame.sound.play("ATTACK_WHIFF_2");
            for (int i=0; i<90; ++i) {
                AbstractDungeon.effectsQueue.add(new BlueSmokeBlurEffect(x, y));
            }
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0f) {
            CardCrawlGame.sound.play("APPEAR");
            isDone = true;
        }
    }
}
