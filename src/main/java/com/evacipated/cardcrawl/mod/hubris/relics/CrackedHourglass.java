package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.SuicidePlayerAction;
import com.evacipated.cardcrawl.mod.hubris.powers.TimeStopPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;

public class CrackedHourglass extends HubrisRelic
{
    public static final String ID = "hubris:CrackedHourglass";
    private static final int TIME_LIMIT_M = 3; // minutes
    private static final int TIME_LIMIT_S = TIME_LIMIT_M * 60;

    private static Field offsetX_field;

    private float timeCounter = -1;
    private float waitTimer = 0;

    static
    {
        try {
            offsetX_field = AbstractRelic.class.getDeclaredField("offsetX");
            offsetX_field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public CrackedHourglass()
    {
        super(ID, "crackedHourglass.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[2] + DESCRIPTIONS[0] + TIME_LIMIT_M + DESCRIPTIONS[1];
    }

    @Override
    public void setCounter(int counter)
    {
        if (counter != this.counter) {
            if (counter == 60) {
                beginLongPulse();
            }
            if (counter <= 30) {
                stopPulse();
                flash();
            }
        }
        super.setCounter(counter);
    }

    @Override
    public void atBattleStart()
    {
        flash();
        waitTimer = flashTimer;
        timeCounter = TIME_LIMIT_S;
        setCounter((int)timeCounter);
    }

    @Override
    public void update()
    {
        super.update();
        if (counter > 0) {
            if (!isStopped()) {
                if (waitTimer <= 0) {
                    timeCounter -= Gdx.graphics.getDeltaTime();
                } else {
                    waitTimer -= Gdx.graphics.getDeltaTime();
                }
                setCounter((int)timeCounter);
            }
        } else if (counter == 0) {
            if (waitTimer <= 0) {
                if (!AbstractDungeon.player.isDead) {
                    flash();
                    AbstractDungeon.actionManager.addToTop(new SuicidePlayerAction());
                    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    waitTimer = 1.0f;
                }
            } else {
                waitTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }

    private boolean isStopped()
    {
        if (CardCrawlGame.stopClock) {
            return true;
        }
        if (AbstractDungeon.player.hasPower(TimeStopPower.POWER_ID)) {
            return true;
        }
        return false;
    }

    @Override
    public void onVictory()
    {
        setCounter(-1);
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel)
    {
        if (counter > -1) {
            float offsetX = 0;
            try {
                offsetX = offsetX_field.getFloat(this);
            } catch (IllegalAccessException ignored) {
            }
            Color c = Color.RED.cpy().lerp(Color.WHITE, (float)counter / (float)TIME_LIMIT_S);
            if (inTopPanel) {
                FontHelper.renderFontRightTopAligned(
                        sb,
                        FontHelper.topPanelInfoFont,
                        Integer.toString(counter),
                        offsetX + currentX + 30.0F * Settings.scale,
                        currentY - 7.0F * Settings.scale,
                        c
                );
            } else {
                FontHelper.renderFontRightTopAligned(
                        sb,
                        FontHelper.topPanelInfoFont,
                        Integer.toString(this.counter),
                        offsetX + currentX + 30.0F * Settings.scale,
                        currentY - 7.0F * Settings.scale,
                        c
                );
            }
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new CrackedHourglass();
    }
}
