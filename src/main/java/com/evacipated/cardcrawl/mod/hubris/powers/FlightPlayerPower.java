package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.FlightPower;

public class FlightPlayerPower extends FlightPower
{
    public static final String POWER_ID = "hubris:FlightPlayer";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int startAmount;

    public FlightPlayerPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        startAmount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + startAmount;
        if (amount == 1) {
            description += DESCRIPTIONS[1];
        } else {
            description += DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurn()
    {
        if (amount > 0) {
            amount = startAmount;
        }
    }

    private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type)
    {
        if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
            return damage / 2.0F;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        boolean willLive = calculateDamageTakenAmount(damageAmount, info.type) < owner.currentHealth;
        if (info.owner != null
                && info.type != DamageInfo.DamageType.HP_LOSS
                && info.type != DamageInfo.DamageType.THORNS
                && damageAmount > 0
                && willLive) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
            damageAmount = Math.round(calculateDamageTakenAmount(damageAmount, info.type));
        }
        return damageAmount;
    }

    @Override
    public void onRemove()
    {
    }
}
