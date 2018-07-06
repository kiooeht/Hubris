package com.evacipated.cardcrawl.mod.hubris.vfx.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleMEffect;

import java.lang.reflect.Field;

public class DeadTorchParticleEffect extends TorchParticleMEffect
{
    public DeadTorchParticleEffect(float x, float y)
    {
        super(x, y);
        color = new Color(MathUtils.random(0.2f, 0.3f), MathUtils.random(0.65f, 0.75f), MathUtils.random(0.75f, 0.85f), 0.01f);
        renderBehind = false;

        try {
            Field f = TorchParticleMEffect.class.getDeclaredField("vY");
            f.setAccessible(true);
            float vY = MathUtils.random(1.0f, 5.0f) * Settings.scale;
            vY *= vY;
            f.set(this, vY);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
