package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.powers.abstracts.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ChampionShieldPower extends AbstractPower implements OnReceivePowerPower
{
    public static final String POWER_ID = "hubris:ChampionShieldPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ChampionShieldPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = -1;
        updateDescription();
        priority = 4;
        region48 = HubrisMod.powerAtlas.findRegion("48/championShield");
        region128 = HubrisMod.powerAtlas.findRegion("128/championShield");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication()
    {
        AbstractDungeon.actionManager.addToTop(new RemoveDebuffsAction(owner));
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.type == PowerType.DEBUFF) {
            flashWithoutSound();
            return false;
        }
        return true;
    }
}
