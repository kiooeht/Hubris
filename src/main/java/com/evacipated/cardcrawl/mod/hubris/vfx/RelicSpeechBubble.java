package com.evacipated.cardcrawl.mod.hubris.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;

public class RelicSpeechBubble extends AbstractGameEffect
{
    private static final float WAVY_SPEED = 6.0F * Settings.scale;
    private static final float WAVY_DISTANCE = 2.0F * Settings.scale;
    private static final float SCALE_TIME = 0.3F;
    private static final float ORIGIN_X = (223.0f + 100.0f) * Settings.scale;
    private static final float ORIGIN_Y = (84.0f + 512.0f) * Settings.scale;
    private static final float FADE_TIME = 0.3F;
    private static final float SHADOW_OFFSET = 16.0F * Settings.scale;

    private float scaleTimer;
    private float shadow_offset = 0.0F;
    private float scale_x;
    private float scale_y;
    private float wavy_y;
    private float wavyHelper;
    private float x;
    private float y;
    private AbstractRelic relic;
    private String message;

    public RelicSpeechBubble(AbstractRelic relic, float duration, String msg)
    {
        this.relic = relic;
        this.duration = duration;
        message = msg;

        this.scale_x = (Settings.scale * 0.7F);
        this.scale_y = (Settings.scale * 0.7F);
        this.scaleTimer = SCALE_TIME;
        this.color = new Color(0.8F, 0.9F, 0.9F, 0.0F);

        x = relic.currentX - ORIGIN_X;
        y = relic.currentY - relic.hb.height - ORIGIN_Y;

        AbstractDungeon.effectsQueue.add(
                new SpeechTextEffect(relic.currentX + 30.0f * Settings.scale, relic.currentY - 100.0f * Settings.scale,
                        duration, msg, DialogWord.AppearEffect.BUMP_IN));
    }

    @Override
    public void update()
    {
        updateScale();

        this.wavyHelper += Gdx.graphics.getDeltaTime() * WAVY_SPEED;
        this.wavy_y = (MathUtils.sin(this.wavyHelper) * WAVY_DISTANCE);

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
        if (this.duration > FADE_TIME) {
            this.color.a = MathUtils.lerp(this.color.a, 1.0F, Gdx.graphics.getDeltaTime() * 12.0F);
        } else {
            this.color.a = MathUtils.lerp(this.color.a, 0.0F, Gdx.graphics.getDeltaTime() * 12.0F);
        }
        this.shadow_offset = MathUtils.lerp(this.shadow_offset, SHADOW_OFFSET, Gdx.graphics.getDeltaTime() * 4.0F);
    }

    private void updateScale()
    {
        this.scaleTimer -= Gdx.graphics.getDeltaTime();
        if (this.scaleTimer < 0.0F) {
            this.scaleTimer = 0.0F;
        }
        this.scale_x = Interpolation.circleIn.apply(Settings.scale, Settings.scale * 0.5F, this.scaleTimer / 0.3F);
        this.scale_y = Interpolation.swingIn.apply(Settings.scale, Settings.scale * 0.8F, this.scaleTimer / 0.3F);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(new Color(0.0f, 0.0f, 0.0f, color.a / 4.0f));
        sb.draw(ImageMaster.SHOP_SPEECH_BUBBLE_IMG,
                x + shadow_offset, y - shadow_offset + wavy_y,
                233.0F, 428.0f, 512.0F, 512.0F,
                scale_x * 0.5f, scale_y * 0.5f, rotation,
                0, 0, 512, 512, false, false);

        sb.setColor(color);
        sb.draw(ImageMaster.SHOP_SPEECH_BUBBLE_IMG,
                x, y + wavy_y,
                223.0f, 428.0f, 512.0F, 512.0F,
                scale_x * 0.5f, scale_y * 0.5f, rotation,
                0, 0, 512, 512, false, false);
    }

    @Override
    public void dispose()
    {

    }
}
