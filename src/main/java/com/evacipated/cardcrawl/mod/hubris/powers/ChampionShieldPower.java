package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.relics.ChampionShield;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
        for (AbstractPower power : owner.powers) {
            if (ChampionShield.debuffsFromMonsters.contains(power)) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, power));
            }
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.type == PowerType.DEBUFF && source instanceof AbstractMonster) {
            flashWithoutSound();
            return false;
        }
        return true;
    }
}
