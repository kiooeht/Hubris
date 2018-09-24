package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.BefuddledAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BefuddledPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Befuddled";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean firstCard = true;

    public BefuddledPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        priority = -999;
        updateDescription();
        loadRegion("confusion");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        //AbstractDungeon.actionManager.addToBottom(new BefuddledAction());
        firstCard = true;
    }

    public void Do(AbstractCard card)
    {
        if (firstCard) {
            AbstractDungeon.actionManager.addToTop(new BefuddledAction(card));
            firstCard = false;
        }
    }
}
