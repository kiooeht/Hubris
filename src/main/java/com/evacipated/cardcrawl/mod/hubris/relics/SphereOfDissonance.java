package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.colorless.MarkOfDissonance;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SphereOfDissonance extends AbstractRelic
{
    public static final String ID = "hubris:SphereOfDissonance";

    public SphereOfDissonance()
    {
        super(ID, "test6.png", RelicTier.UNCOMMON, LandingSound.MAGICAL);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(MarkOfDissonance.NAME, "A #b" + MarkOfDissonance.COST + " cost Skill card that applies #yDissonance."));
        initializeTips();
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new MarkOfDissonance(), 1, true, false));
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SphereOfDissonance();
    }
}
