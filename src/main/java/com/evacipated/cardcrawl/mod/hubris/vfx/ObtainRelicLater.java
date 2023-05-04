package com.evacipated.cardcrawl.mod.hubris.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.relics.Icosahedron;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ObtainRelicLater extends AbstractGameEffect
{
    private AbstractRelic relic;
    private float x;
    private float y;

    public ObtainRelicLater(AbstractRelic relic, float x, float y)
    {
        this.relic = relic;
        this.x = x;
        this.y = y;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(x, y, relic);
        isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {

    }

    @Override
    public void dispose()
    {

    }
}
