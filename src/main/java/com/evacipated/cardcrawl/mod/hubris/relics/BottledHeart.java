package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MedicalKit;
import com.megacrit.cardcrawl.relics.Necronomicon;

public class BottledHeart extends HubrisRelic
{
    public static final String ID = "hubris:BottledHeart";
    private static final int NECRONOMICON_BONUS = 10;

    public BottledHeart()
    {
        super(ID, "bottledHeart.png", RelicTier.SPECIAL, LandingSound.CLINK);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip("Synergy", DESCRIPTIONS[2] + FontHelper.colorString(new Necronomicon().name, "y") + DESCRIPTIONS[3] + NECRONOMICON_BONUS + DESCRIPTIONS[4]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + new Necronomicon().name;
    }

    @Override
    public void onEquip()
    {
        flash();
        int c = AbstractDungeon.player.maxHealth / 2;
        if (AbstractDungeon.player.hasRelic(Necronomicon.ID)) {
            AbstractDungeon.player.getRelic(Necronomicon.ID).flash();
            c += NECRONOMICON_BONUS;
        }
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth - 1);
        setCounter(c);
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, counter));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BottledHeart();
    }
}
