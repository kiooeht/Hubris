package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.common.RemoveMonsterAction;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ScarierMask extends HubrisRelic
{
    public static final String ID = "hubris:ScarierMask";
    private AbstractMonster toRemoveAfterEscape = null;

    public ScarierMask()
    {
        super(ID, "crystalFace.png", RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle()
    {
        if (AbstractDungeon.getCurrRoom().monsters.monsters.size() > 1) {
            AbstractMonster monsterToRemove = null;

            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m.type != AbstractMonster.EnemyType.BOSS) {
                    if (monsterToRemove == null || m.maxHealth < monsterToRemove.maxHealth) {
                        monsterToRemove = m;
                    }
                }
            }

            if (monsterToRemove != null) {
                toRemoveAfterEscape = monsterToRemove;
                AbstractDungeon.actionManager.addToTop(new EscapeAction(monsterToRemove));
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(monsterToRemove, this));
            }
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (toRemoveAfterEscape != null) {
            if (toRemoveAfterEscape.isEscaping && toRemoveAfterEscape.escapeTimer <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveMonsterAction(toRemoveAfterEscape));
                toRemoveAfterEscape = null;
            }
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ScarierMask();
    }
}
