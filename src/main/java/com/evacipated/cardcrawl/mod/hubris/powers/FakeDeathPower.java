package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FakeDeathPower extends AbstractPower implements InvisiblePower
{
    public static final String POWER_ID = "hubris:FakeDeath";
    public static final String NAME = "FAKEDEATH";
    public static final String[] DESCRIPTIONS = new String[]{"You shouldn't see this."};

    public FakeDeathPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        priority = -99;
        loadRegion("minion");
        //region48 = HubrisMod.powerAtlas.findRegion("48/championShield");
        //region128 = HubrisMod.powerAtlas.findRegion("128/championShield");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onDeath()
    {
        owner.isDying = false;
        owner.halfDead = true;
        ((AbstractMonster) owner).intent = AbstractMonster.Intent.NONE;
        owner.tint.fadeOut();
    }

    @Override
    public void onRemove()
    {
        // Add a copy, only one will be removed
        owner.powers.add(0, this);
        // Cancel the removal text effect
        AbstractDungeon.effectList.remove(AbstractDungeon.effectList.size() - 1);
    }
}
