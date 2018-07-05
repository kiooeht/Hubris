package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DeadTorch extends AbstractRelic
{
    public static final String ID = "hubris:DeadTorch";
    private static final int AMT = 1;

    public DeadTorch()
    {
        super(ID, "test2.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
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
    public AbstractRelic makeCopy()
    {
        return new DeadTorch();
    }
}
