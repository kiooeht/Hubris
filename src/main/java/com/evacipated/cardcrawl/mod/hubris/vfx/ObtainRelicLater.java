package com.evacipated.cardcrawl.mod.hubris.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ObtainRelicLater extends AbstractGameEffect
{
    private AbstractRelic relic;

    public ObtainRelicLater(AbstractRelic relic)
    {
        this.relic = relic;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        relic.instantObtain();
        isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {

    }
}
