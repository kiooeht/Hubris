package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class CursedLifePower extends AbstractPower
{
    public static final String POWER_ID = "hubris:CursedLife";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CursedLifePower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        priority = -99;
        loadRegion("brutality");
        //region48 = HubrisMod.powerAtlas.findRegion("48/championShield");
        //region128 = HubrisMod.powerAtlas.findRegion("128/championShield");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + FontHelper.colorString(owner.name, "y")
                + DESCRIPTIONS[1] + amount;
        if (amount == 1) {
            description += DESCRIPTIONS[3];
        } else {
            description += DESCRIPTIONS[2];
        }
        description += DESCRIPTIONS[4];
    }

    @Override
    public void onDeath()
    {
        flash();
        for (int i=0; i<amount; ++i) {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                    AbstractDungeon.returnRandomCurse(),
                    Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f,
                    true));
        }
    }

    @Override
    public void duringTurn()
    {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ID, 1));
        updateDescription();
    }
}
