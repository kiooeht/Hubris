package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SlothPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Sloth";

    public SlothPower(AbstractCreature owner)
    {
        name = "Sloth";
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        amount = -1;
        updateDescription();
        img = ImageMaster.loadImage("images/powers/32/slow.png");
    }

    @Override
    public void updateDescription()
    {
        description = "Gain #b1 max energy at the end of each turn.";
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if ((isPlayer && owner == AbstractDungeon.player)
                || (!isPlayer && owner != AbstractDungeon.player)) {
            AbstractDungeon.player.energy.energy += 1;
            flash();
        }
    }
}
