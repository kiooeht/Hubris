package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.Torii;

public class ToriiPower extends AbstractPower
{
    private static final int dmgAmount = 5;

    public ToriiPower(AbstractCreature owner)
    {
        name = "Torii";
        ID = "Torii";
        this.owner = owner;
        amount = 0;
        type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = "When receiving #b" + dmgAmount + " or less unblocked #yAttack damage, reduce it to #b1.";
        img = ImageMaster.loadImage("images/relics/torii.png");
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 1 && damageAmount <= dmgAmount) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(owner, new Torii()));
            return 1;
        }
        return damageAmount;
    }
}
