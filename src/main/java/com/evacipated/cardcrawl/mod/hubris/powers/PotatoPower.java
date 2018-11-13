package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PotatoPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Potato";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean duringTurn = false;

    public PotatoPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        priority = -99999;
        updateDescription();
        region48 = region128 = ImageMaster.COPPER_COIN_1;
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication()
    {
        duringTurn = true;
    }

    @Override
    public void atStartOfTurn()
    {
        duringTurn = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        duringTurn = false;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (duringTurn) {
            flash();
            return 0;
        }
        return damageAmount;
    }
}
