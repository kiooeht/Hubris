package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.vfx.scene.DeadTorchParticleEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.scene.LightFlareMEffect;

public class DeadTorch extends AbstractRelic
{
    public static final String ID = "hubris:DeadTorch";
    private static final int AMT = 1;
    private static final float PARTICAL_EMIT_INTERVAL = 0.15f;

    private float particleTimer = 0.0f;

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
            AbstractDungeon.topLevelEffectsQueue.add(new DeadTorchParticleEffect(currentX - 28 * Settings.scale, currentY));
            AbstractDungeon.topLevelEffectsQueue.add(new LightFlareMEffect(currentX - 28 * Settings.scale, currentY));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DeadTorch();
    }
}
