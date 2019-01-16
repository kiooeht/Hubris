package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SlothPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Sloth";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SlothPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = -1;
        updateDescription();
        loadRegion("slow");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
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
