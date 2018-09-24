package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.powers.abstracts.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class GoldShieldPower extends AbstractPower implements OnReceivePowerPower
{
    public static final String POWER_ID = "hubris:GoldShield";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean thorns = false;

    public GoldShieldPower(AbstractCreature owner, int metellicizeAmount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = metellicizeAmount;
        type = PowerType.BUFF;
        priority = -9;
        updateDescription();
        region48 = region128 = ImageMaster.COPPER_COIN_1;
    }

    @Override
    public void updateDescription()
    {
        if (owner.isPlayer) {
            description = MetallicizePower.DESCRIPTIONS[0] + amount + MetallicizePower.DESCRIPTIONS[1];
        } else {
            description = MetallicizePower.DESCRIPTIONS[2] + amount + MetallicizePower.DESCRIPTIONS[3];
        }
        description += DESCRIPTIONS[0];
    }

    @Override
    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.play("POWER_METALLICIZE", 0.05F);
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
        flash();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, amount));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (thorns && info.type == DamageInfo.DamageType.THORNS && damageAmount > 1) {
            return 1;
        }
        return damageAmount;
    }

    @Override
    public void onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.ID.equals(WeakPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, power.ID));
        }
    }
}
