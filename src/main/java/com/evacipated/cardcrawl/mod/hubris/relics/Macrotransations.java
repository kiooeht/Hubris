package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class Macrotransations extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:Macrotransations";
    private static final int GOLD_COST = 100;

    public Macrotransations()
    {
        super(ID, "macrotransactions.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + GOLD_COST + DESCRIPTIONS[1];
    }

    @Override
    public void atPreBattle()
    {
        if (AbstractDungeon.player.gold >= GOLD_COST) {
            beginLongPulse();
        }
    }

    @Override
    public void update()
    {
        if (canUse()) {
            if (!pulse) {
                beginLongPulse();
            }
        } else {
            stopPulse();
        }

        super.update();
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained) {
            return;
        }

        if (canUse()) {
            flash();
            AbstractDungeon.player.loseGold(GOLD_COST);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && m.type != AbstractMonster.EnemyType.BOSS && m.type != AbstractMonster.EnemyType.ELITE) {
                    AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                    AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2f));
                }
            }
        }
    }

    private boolean canUse()
    {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (AbstractDungeon.player.gold >= GOLD_COST) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && m.type != AbstractMonster.EnemyType.BOSS && m.type != AbstractMonster.EnemyType.ELITE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Macrotransations();
    }
}
