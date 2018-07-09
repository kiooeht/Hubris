package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;

public class UnfocusedPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Unfocused";

    public UnfocusedPower(AbstractCreature owner)
    {
        name = "Unfocused";
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        amount = 1;
        updateDescription();
        loadRegion("confusion");
    }

    @Override
    public void updateDescription()
    {
        description = "On taking Attack damage, loses #b1 Focus. Cannot go below 0 Focus.";
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (damageAmount < owner.currentHealth && damageAmount > 0 && info.owner != null
                && info.type == DamageInfo.DamageType.NORMAL && info.type != DamageInfo.DamageType.THORNS) {
            AbstractPower focus = owner.getPower(FocusPower.POWER_ID);
            if (focus != null && focus.amount > 0) {
                flash();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new FocusPower(owner, -amount), -amount));
            }
        }
        return damageAmount;
    }
}
