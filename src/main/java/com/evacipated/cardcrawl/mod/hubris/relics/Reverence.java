package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.BulletFireAction;
import com.evacipated.cardcrawl.mod.hubris.cards.colorless.Bullet;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Reverence extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:Reverence";
    private static final int DAMAGE = 6;
    public boolean firing = false;

    public Reverence()
    {
        super(ID, "test5.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        String ret = CLICKABLE_DESCRIPTIONS()[0];
        if (counter >= 0) {
            ret += DESCRIPTIONS[0] + counter;
            if (counter == 1) {
                ret += DESCRIPTIONS[1];
            } else {
                ret += DESCRIPTIONS[2];
            }
        }
        ret += DESCRIPTIONS[3] + DAMAGE + DESCRIPTIONS[4] + DESCRIPTIONS[5];
        return ret;
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void onEquip()
    {
        setCounter(3);
    }

    @Override
    public void atBattleStart()
    {
        flash();
        firing = false;
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Bullet(), 1, true, true));
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained || counter <= 0 || firing) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            flash();
            firing = true;
            AbstractDungeon.actionManager.addToBottom(
                    new BulletFireAction(
                            this,
                            AbstractDungeon.getMonsters().getRandomMonster(true),
                            new DamageInfo(AbstractDungeon.player, DAMAGE),
                            counter
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
