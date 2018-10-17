package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BodyguardPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Bodyguard";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BodyguardPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        priority = -99;
        region48 = HubrisMod.powerAtlas.findRegion("48/championShield");
        region128 = HubrisMod.powerAtlas.findRegion("128/championShield");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
    {
        int aliveCount = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                ++aliveCount;
            }
        }
        if (aliveCount > 1) {
            return 0;
        }
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        return damage;
    }
}
