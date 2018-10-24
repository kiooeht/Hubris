package com.evacipated.cardcrawl.mod.hubris.vfx.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleMEffect;

import java.lang.reflect.Field;

public class NecromanticTotemParticleEffect extends TorchParticleMEffect
{
    public NecromanticTotemParticleEffect(float x, float y)
    {
        super(x, y);
        color = new Color(MathUtils.random(0.1f, 0.2f), MathUtils.random(0.45f, 0.65f), MathUtils.random(0.15f, 0.3f), 0.01f);
        renderBehind = false;
        scale = MathUtils.random(0.5f, 0.7f);

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
