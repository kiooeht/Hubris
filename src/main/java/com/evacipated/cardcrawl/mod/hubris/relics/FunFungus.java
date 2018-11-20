package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FunFungus extends HubrisRelic
{
    public static final String ID = "hubris:FunFungus";

    public FunFungus()
    {
        super(ID, "funFungus.png", RelicTier.RARE, LandingSound.FLAT);

        pulse = false;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn()
    {
        if (EnergyPanel.totalCount > 0) {
            flash();
            AbstractPower healNextTurn = new AbstractPower()
            {
                @Override
                public void updateDescription()
                {
                    description = "Heal #b" + amount + " HP at the start of your next turn.";
                    loadRegion("regen");
                }

                @Override
                public void atStartOfTurn()
                {
                    flash();
                    AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, amount));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
                }
            };
            healNextTurn.ID = "hubris:HealNextTurn";
            healNextTurn.name = "Heal";
            healNextTurn.owner = AbstractDungeon.player;
            healNextTurn.amount = EnergyPanel.totalCount;
            healNextTurn.updateDescription();
            healNextTurn.priority = 20;
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, healNextTurn));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new FunFungus();
    }
}
