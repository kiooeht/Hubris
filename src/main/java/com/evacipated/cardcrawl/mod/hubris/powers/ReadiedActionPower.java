package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.RunicPyramid;

public class ReadiedActionPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:ReadiedAction";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ReadiedActionPower(AbstractCreature owner, int numCards)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = numCards;
        updateDescription();
        loadRegion("retain");
    }

    @Override
    public void updateDescription()
    {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (isPlayer && !AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.player.hasRelic(RunicPyramid.ID)) {
            AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(owner, amount));
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
    }
}
