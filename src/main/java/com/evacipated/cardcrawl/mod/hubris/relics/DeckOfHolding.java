package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.powers.RetainHandPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class DeckOfHolding extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:DeckOfHolding";
    private boolean usedThisCombat = false;

    public DeckOfHolding()
    {
        super(ID, "deckOfHolding.png", RelicTier.UNCOMMON, LandingSound.FLAT);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip("Synergy",
                DESCRIPTIONS[1] + FontHelper.colorString(new RunicPyramid().name, "y") + DESCRIPTIONS[2]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + new RunicPyramid().name;
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
            if (AbstractDungeon.player.hasRelic(RunicPyramid.ID)) {
                int count = AbstractDungeon.player.hand.size();
                if (count != 0) {
                    AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, count, true));
                }
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new RetainHandPower(AbstractDungeon.player)));
            }
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
