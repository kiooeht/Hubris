package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.BefuddledAction;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.WhileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WhilePower extends AbstractPower
{
    public static final String POWER_ID = "hubris:WhilePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WhilePower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        loadRegion("loop");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        AbstractDungeon.actionManager.addToBottom(new WhileAction());
    }
}
