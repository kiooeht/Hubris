package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ArmorPiercingPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:ArmorPiercing";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ArmorPiercingPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = -1;
        priority = -10;
        updateDescription();
        img = ImageMaster.loadImage("images/powers/32/envenom.png");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }
}
