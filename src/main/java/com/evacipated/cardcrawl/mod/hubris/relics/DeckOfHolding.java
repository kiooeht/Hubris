package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.powers.RetainHandPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class DeckOfHolding extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:DeckOfHolding";
    private boolean usedThisCombat = false;

    public DeckOfHolding()
    {
        super(ID, "test5.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0];
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained || usedThisCombat) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            usedThisCombat = true;
            flash();
            stopPulse();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new RetainHandPower(AbstractDungeon.player)));
        }
    }

    @Override
    public void atPreBattle()
    {
        usedThisCombat = false;
        beginLongPulse();
    }

    @Override
    public void onVictory()
    {
        stopPulse();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DeckOfHolding();
    }
}
