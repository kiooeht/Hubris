package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.ClickableRelic;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;

public class BundleOfHerbs extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:BundleOfHerbs";
    private static final int REGEN = 3;
    private static final int START_CHARGE = 2;
    private static final int PER_REST = 1;

    public BundleOfHerbs()
    {
        super(ID, "bundleOfHerbs.png", RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + REGEN + DESCRIPTIONS[1] + PER_REST + DESCRIPTIONS[2] + START_CHARGE + DESCRIPTIONS[3];
    }

    @Override
    public void onEquip()
    {
        startingHerbs();
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (room instanceof RestRoom) {
            flash();
            gainHerbs();
        }
    }

    private void startingHerbs()
    {
        setCounter(START_CHARGE);
    }

    private void gainHerbs()
    {
        if (counter < 0) {
            counter = 0;
        }
        setCounter(counter + PER_REST);
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained || counter <= 0) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            setCounter(counter - 1);
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RegenPower(p, REGEN), REGEN));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BundleOfHerbs();
    }
}
