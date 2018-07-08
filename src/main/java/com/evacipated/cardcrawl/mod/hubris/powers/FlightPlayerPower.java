package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FlightPower;

public class FlightPlayerPower extends FlightPower
{
    public static final String POWER_ID = "hubris:FlightPlayer";

    private int startAmount;

    public FlightPlayerPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);
        name = "Flight";
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        startAmount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = "Reduce #yAttack damage taken by #b50%. Cancelled for the combat if dealt #yAttack damage #b" + startAmount;
        if (amount == 1) {
            description += " time in one turn.";
        } else {
            description += " times in one turn.";
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
