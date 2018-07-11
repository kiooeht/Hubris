package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class VirtuousBlindfold extends AbstractRelic
{
    public static final String ID = "hubris:VirtuousBlindfold";

    public VirtuousBlindfold()
    {
        super(ID, "virtuousBlindfold.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (AbstractDungeon.player != null) {
            return setDescription(AbstractDungeon.player.chosenClass);
        }
        return setDescription(null);
    }

    private String setDescription(AbstractPlayer.PlayerClass c)
    {
        if (c == null) {
            return DESCRIPTIONS[1] + DESCRIPTIONS[0];
        }
        switch (c) {
            case IRONCLAD:
                return DESCRIPTIONS[1] + DESCRIPTIONS[0];
            case THE_SILENT:
                return DESCRIPTIONS[2] + DESCRIPTIONS[0];
            case DEFECT:
                return DESCRIPTIONS[3] + DESCRIPTIONS[0];
        }
        return DESCRIPTIONS[1] + DESCRIPTIONS[0];
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c)
    {
        description = setDescription(c);
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new VirtuousBlindfold();
    }
}
