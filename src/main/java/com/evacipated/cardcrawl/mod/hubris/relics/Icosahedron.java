package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.NaturalOne;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class Icosahedron extends AbstractRelic
{
    public static final String ID = "hubris:Icosahedron";

    public Icosahedron()
    {
        super(ID, "icosahedron.png", RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        if (counter < 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new com.evacipated.cardcrawl.mod.hubris.cards.colorless.Icosahedron(), 1, false));
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        switch (counter) {
            case 1:
                return DESCRIPTIONS[2] + DESCRIPTIONS[1];
            case 20: {
                return DESCRIPTIONS[3] + DESCRIPTIONS[1];
            }
            default:
                return DESCRIPTIONS[0];
        }
    }

    @Override
    public void setCounter(int c)
    {
        super.setCounter(c);
        flash();
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
        if (counter == 20) {
            AbstractDungeon.player.energy.energy += 1;
            AbstractDungeon.player.energy.energyMaster += 1;
        } else if (counter == 1) {
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new NaturalOne(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Icosahedron();
    }
}
