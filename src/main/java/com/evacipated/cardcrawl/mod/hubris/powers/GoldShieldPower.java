package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GoldShieldPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:GoldShield";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean thorns = false;

    public GoldShieldPower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        priority = -9;
        updateDescription();
        region48 = region128 = ImageMaster.COPPER_COIN_1;
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn()
    {
        thorns = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        thorns = false;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (thorns && info.type == DamageInfo.DamageType.THORNS && damageAmount > 1) {
            return 1;
        }
        return damageAmount;
    }
}
