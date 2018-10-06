package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.FireCardAction;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Reverence extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:Reverence";
    private boolean usedThisTurn;

    public Reverence()
    {
        super(ID, "reverence.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw()
    {
        usedThisTurn = false;
        beginLongPulse();
    }

    @Override
    public void onVictory()
    {
        stopPulse();
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained || usedThisTurn) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            stopPulse();
            flash();
            usedThisTurn = true;
            AbstractDungeon.actionManager.addToTop(
                    new FireCardAction(
                            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRng),
                            AbstractCard.CardType.ATTACK
                    )
            );
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Reverence();
    }
}
