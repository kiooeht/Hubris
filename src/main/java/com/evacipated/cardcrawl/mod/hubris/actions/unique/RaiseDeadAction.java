package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.mod.hubris.powers.UndeadPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Lethality;
import com.megacrit.cardcrawl.daily.mods.TimeDilation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RaiseDeadAction extends AbstractGameAction
{
    private static final Logger logger = LogManager.getLogger(RaiseDeadAction.class.getName());

    private AbstractMonster m;
    private int slotToFill = 0;

    public RaiseDeadAction(AbstractMonster[] minions)
    {
        actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE) {
            startDuration = Settings.ACTION_DUR_FAST;
        } else {
            startDuration = Settings.ACTION_DUR_LONG;
        }
        duration = startDuration;
        int slot = identifySlot(minions);
        if (slot == -1) {
            logger.info("INCORRECTLY ATTEMPTED TO RAISE DEAD");
            return;
        }
        slotToFill = slot;

        m = getRandomMinion(slot);
        minions[slot] = m;
    }

    private static int identifySlot(AbstractMonster[] minions)
    {
        for (int i = 0; i < minions.length; i++) {
            if (minions[i] == null || minions[i].isDeadOrEscaped()) {
                return i;
            }
        }
        return -1;
    }

    private static AbstractMonster getRandomMinion(int slot)
    {
        List<String> pool = new ArrayList<>();
        pool.add("GremlinWarrior");
        pool.add("GremlinWarrior");
        pool.add("GremlinThief");
        pool.add("GremlinThief");
        pool.add("GremlinFat");
        pool.add("GremlinFat");
        pool.add("GremlinTsundere");
        pool.add("GremlinWizard");
        float x;
        float y;
        switch (slot)
        {
            case 0:
                x = -366.0f;
                y = -4.0f;
                break;
            case 1:
                x = -170.0f;
                y = 6.0f;
                break;
            case 2:
                x = -532.0f;
                y = 0.0f;
                break;
            default:
                x = -366.0f;
                y = -4.0f;
        }
        return MonsterHelper.getGremlin(pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1)), x, y);
    }

    public void update()
    {
        if (duration == startDuration)
        {
            m.animY = (-400.0f * Settings.scale);
            m.init();
            m.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(slotToFill, m);
            AbstractPower undead = new UndeadPower(m, 1);
            m.addPower(undead);
            undead.onInitialApplication();
            if (AbstractDungeon.player.hasRelic(PhilosopherStone.ID)) {
                m.addPower(new StrengthPower(m, 2));
            }
            if (ModHelper.isModEnabled(Lethality.ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, 3), 3));
            }
            if (ModHelper.isModEnabled(TimeDilation.ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new SlowPower(m, 0)));
            }
        }
        tickDuration();
        if (isDone) {
            m.animY = 0.0f;
            m.showHealthBar();
            m.usePreBattleAction();
        } else {
            m.animY = Interpolation.fade.apply(0.0f, -400.0f * Settings.scale, duration);
        }
    }
}
