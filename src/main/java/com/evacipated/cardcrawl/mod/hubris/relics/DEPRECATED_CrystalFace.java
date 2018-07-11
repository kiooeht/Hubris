package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.QuietLoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DEPRECATED_CrystalFace extends AbstractRelic
{
    public static final String ID = "hubris:CrystalFace";
    private static final int AMT = 20;

    public DEPRECATED_CrystalFace()
    {
        super(ID, "crystalFace.png", RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        flash();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            int reduction = Math.round(m.maxHealth * (AMT / 100.0f));
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));
            AbstractDungeon.actionManager.addToBottom(new QuietLoseHPAction(m, AbstractDungeon.player, reduction));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DEPRECATED_CrystalFace();
    }
}
