package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.BefuddledAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BefuddledPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Befuddled";
    private boolean firstCard = true;

    public BefuddledPower(AbstractCreature owner)
    {
        name = "Befuddled";
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
        description = "The first card drawn each turn is transformed.";
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
