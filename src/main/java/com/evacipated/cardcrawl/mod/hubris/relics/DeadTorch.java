package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.vfx.scene.DeadTorchParticleEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.scene.LightFlareMEffect;

import java.lang.reflect.Field;

public class DeadTorch extends HubrisRelic
{
    public static final String ID = "hubris:DeadTorch";
    private static final int AMT = 1;
    private static final float PARTICAL_EMIT_INTERVAL = 0.15f;
    private static Field offsetX_field = null;

    private float particleTimer = 0.0f;

    static
    {
        try {
            offsetX_field = AbstractRelic.class.getDeclaredField("offsetX");
            offsetX_field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public DeadTorch()
    {
        super(ID, "deadTorch.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        flash();
        AbstractDungeon.player.increaseMaxHp(AMT, false);
    }

    @Override
    public void onEquip()
    {
        flash();
    }

    @Override
    public void update()
    {
        super.update();

        particleTimer -= Gdx.graphics.getDeltaTime();
        if (particleTimer < 0) {
            particleTimer = PARTICAL_EMIT_INTERVAL;
            float offsetX = 0;
            if (offsetX_field != null) {
                try {
                    offsetX = offsetX_field.getFloat(this);
                } catch (IllegalAccessException ignore) {
                }
            }
            float posX = currentX + offsetX - 28 * Settings.scale;
            if (posX >= 0 && posX <= Settings.WIDTH) {
                AbstractDungeon.topLevelEffectsQueue.add(new DeadTorchParticleEffect(posX, currentY));
                AbstractDungeon.topLevelEffectsQueue.add(new LightFlareMEffect(posX, currentY));
            }
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DeadTorch();
    }
}
