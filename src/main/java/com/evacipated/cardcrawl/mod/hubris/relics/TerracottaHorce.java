package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TerracottaHorce extends HubrisRelic
{
    public static final String ID = "hubris:TerracottaHorce";
    private static final int ASCENSION_BOOST = 5;

    public TerracottaHorce()
    {
        super(ID, "terracottaHorce.png", RelicTier.BOSS, LandingSound.SOLID);
    }

    public static boolean allowedInPool()
    {
        if (AbstractDungeon.isAscensionMode && AbstractDungeon.ascensionLevel > (20 - ASCENSION_BOOST)) {
            return false;
        }
        return true;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + ASCENSION_BOOST + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;

        if (!AbstractDungeon.isAscensionMode) {
            AbstractDungeon.isAscensionMode = true;
            AbstractDungeon.ascensionLevel = 0;
        }
        AbstractDungeon.ascensionLevel += ASCENSION_BOOST;
        AbstractDungeon.topPanel.setPlayerName();
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;

        AbstractDungeon.ascensionLevel -= ASCENSION_BOOST;
        if (AbstractDungeon.ascensionLevel <= 0) {
            AbstractDungeon.ascensionLevel = 0;
            AbstractDungeon.isAscensionMode = false;
        }
        AbstractDungeon.topPanel.setPlayerName();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new TerracottaHorce();
    }
}
