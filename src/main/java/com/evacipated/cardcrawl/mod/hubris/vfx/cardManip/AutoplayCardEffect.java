package com.evacipated.cardcrawl.mod.hubris.vfx.cardManip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AutoplayCardEffect extends AbstractGameEffect
{
    private static final float EFFECT_DUR = 2.0f;
    private static final float FAST_DUR = 0.5f;
    private AbstractCard card;

    public AutoplayCardEffect(AbstractCard card)
    {
        this.card = card;
        if (Settings.FAST_MODE) {
            duration = FAST_DUR;
        } else {
            duration = EFFECT_DUR;
        }
        card.targetAngle = 0;
    }

    @Override
    public void update()
    {
        duration -= Gdx.graphics.getDeltaTime();
        card.target_x = Settings.WIDTH / 2.0F;
        card.target_y = Settings.HEIGHT / 2.0F;
        card.update();
        if (duration < 0) {
            isDone = true;
            AbstractDungeon.actionManager.addToBottom(new QueueCardAction(card, null));
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!isDone) {
            card.render(sb);
        }
    }
}
