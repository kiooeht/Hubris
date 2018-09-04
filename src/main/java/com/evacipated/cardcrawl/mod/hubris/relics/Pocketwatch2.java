package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.powers.TimeStopPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.ClickableRelic;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Pocketwatch2 extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:Pocketwatch2";
    private static final int AMT = 6;

    public Pocketwatch2()
    {
        super(ID, "pocketwatch.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        setCounter(AMT);
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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TimeStopPower(p, 1), 1));
        }
    }

    @Override
    public void onVictory()
    {
        boolean beatTimeEater = false;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m instanceof TimeEater) {
                beatTimeEater = true;
                break;
            }
        }

        if (beatTimeEater) {
            flash();
            setCounter(AMT);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Pocketwatch2();
    }
}
