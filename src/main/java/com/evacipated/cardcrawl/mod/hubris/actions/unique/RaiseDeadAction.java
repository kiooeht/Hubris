package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.powers.UndeadPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Lethality;
import com.megacrit.cardcrawl.daily.mods.TimeDilation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.vfx.TintEffect;

public class RaiseDeadAction extends AbstractGameAction
{
    private AbstractCreature parent;
    private AbstractMonster m;

    public RaiseDeadAction(AbstractCreature parent, AbstractMonster minion)
    {
        m = minion;
        this.parent = parent;
        actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE) {
            startDuration = Settings.ACTION_DUR_FAST;
        } else {
            startDuration = Settings.ACTION_DUR_LONG;
        }
        startDuration = Settings.ACTION_DUR_LONG;
        duration = startDuration;
    }

    @Override
    public void update()
    {
        if (duration == startDuration)
        {
            m.isDying = false;
            m.heal(m.maxHealth, true);
            m.healthBarRevivedEvent();
            m.deathTimer = 0;
            m.tint = new TintEffect();
            m.tintFadeOutCalled = false;
            m.isDead = false;
            m.halfDead = false;
            m.hideHealthBar();

            //m.animY = (-400.0f * Settings.scale);
            m.init();
            m.applyPowers();
            AbstractPower undead = new UndeadPower(m, parent, 1);
            m.powers.clear();
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
            //m.animY = Interpolation.fade.apply(0.0f, -400.0f * Settings.scale, duration);
        }
    }
}
