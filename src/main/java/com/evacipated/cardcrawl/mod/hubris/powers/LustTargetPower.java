package com.evacipated.cardcrawl.mod.hubris.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LustTargetPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:LustTarget";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LustTargetPower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        loadRegion("noattack");
        type = PowerType.BUFF;
        canGoNegative = true;
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + -amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound()
    {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.owner.isPlayer && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(info.owner, owner, -amount));
        }

        return damageAmount;
    }
}
