package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

public class Teleporter extends HubrisRelic
{
    public static final String ID = "hubris:Teleporter";

    public Teleporter()
    {
        this(2);
    }

    public Teleporter(int charges)
    {
        super(ID, "shortRangeTeleporter.png", RelicTier.RARE, LandingSound.HEAVY);
        setCounter(charges);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter == 1) {
            return DESCRIPTIONS[0] + counter + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[0] + counter + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }

    @Override
    public void setCounter(int c)
    {
        counter = c;
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void onVictory()
    {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite
                || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            flash();
            setCounter(counter + 1);
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (energyBased) {
            if (!pulse) {
                beginLongPulse();
            }
        } else {
            stopPulse();
        }

        energyBased = false;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Teleporter(counter);
    }
}
