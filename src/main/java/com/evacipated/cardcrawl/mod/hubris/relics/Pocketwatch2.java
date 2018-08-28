package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.colorless.TimeStop;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Pocketwatch2 extends HubrisRelic
{
    public static final String ID = "hubris:Pocketwatch2";
    private static final int AMT = 6;

    public Pocketwatch2()
    {
        super(ID, "pocketwatch.png", RelicTier.BOSS, LandingSound.HEAVY);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TimeStop.NAME, TimeStop.NAME + " is a #b" + TimeStop.COST + " cost Skill card. NL " + TimeStop.DESCRIPTION + TimeStop.EXTENDED_DESCRIPTION[0]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        setCounter(AMT);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        if (counter > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new TimeStop(), 1, true, false));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Pocketwatch2();
    }
}
