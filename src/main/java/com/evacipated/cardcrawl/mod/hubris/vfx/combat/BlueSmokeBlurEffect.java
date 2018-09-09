package com.evacipated.cardcrawl.mod.hubris.vfx.combat;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;

public class BlueSmokeBlurEffect extends SmokeBlurEffect
{
    public BlueSmokeBlurEffect(float x, float y)
    {
        super(x, y);
        color.r = 0.2f;
        color.g = MathUtils.random(0.5f, 0.6f);
        color.b = color.g + MathUtils.random(0.0f, 0.2f);
    }
}
