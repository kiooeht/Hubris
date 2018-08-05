package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GlazedTorus extends AbstractRelic
{
    public static final String ID = "hubris:GlazedTorus";
    private int turn;

    public GlazedTorus()
    {
        super(ID, "glazedTorus.png", RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart()
    {
        turn = 0;
    }

    @Override
    public void atTurnStart()
    {
        ++turn;
        if (turn % 2 == 1) {
            beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        if (turn % 2 == 1) {
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new GlazedTorus();
    }
}
