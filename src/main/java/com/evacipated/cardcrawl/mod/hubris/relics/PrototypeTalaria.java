package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.powers.FlightPlayerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public class PrototypeTalaria extends AbstractRelic
{
    public static final String ID = "hubris:PrototypeTalaria";
    private static final int COUNT = 2;
    private boolean cardSelected = true;
    private List<AbstractCard> cards = new ArrayList<>(COUNT);

    public PrototypeTalaria()
    {
        super(ID, "prototypeTalaria.png", RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlightPlayerPower(AbstractDungeon.player, 1)));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PrototypeTalaria();
    }
}
