package com.evacipated.cardcrawl.mod.hubris.vfx.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleMEffect;

import java.lang.reflect.Field;

public class CursedTotemParticleEffect extends TorchParticleMEffect
{
    public CursedTotemParticleEffect(float x, float y)
    {
        super(x, y);
        color = new Color(MathUtils.random(0.35f, 0.45f), MathUtils.random(0.05f, 0.1f), MathUtils.random(0.35f, 0.45f), 0.01f);
        renderBehind = false;
        scale = MathUtils.random(1.5f, 3.0f);

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
