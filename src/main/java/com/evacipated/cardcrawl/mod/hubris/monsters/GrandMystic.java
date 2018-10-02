package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.hubris.powers.GainFocusPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.lang.reflect.Field;

public class GrandMystic extends Healer
{
    public static final String ID = "hubris:GrandMystic";
    public static final String NAME = "Grand Mystic";
    private static final int HP = 110;
    private int strAmt = 2;

    public GrandMystic()
    {
        super(0,0);
        name = NAME;
        id = ID;
        setHp(HP);
        hb_x = 0.0f * Settings.scale;
        hb_y = -20.0f * Settings.scale;
        hb_w = 230.0f * Settings.scale;
        hb_h = 300.0f * Settings.scale;
        hb = new Hitbox(hb_w, hb_h);
        healthHb = new Hitbox(hb_w, 72.0f* Settings.scale);
        refreshHitboxLocation();
        refreshIntentHbLocation();

        try {
            Field f = Healer.class.getDeclaredField("strAmt");
            f.setAccessible(true);
            strAmt = f.getInt(this);

            f = Healer.class.getDeclaredField("healAmt");
            f.setAccessible(true);
            f.setInt(this, f.getInt(this) * 3);
        } catch (IllegalAccessException | NoSuchFieldException ignore) {
        }

        loadAnimation("images/monsters/theCity/healer/skeleton.atlas", "images/monsters/theCity/healer/skeleton.json", 0.75f);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.2F);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.state.setTimeScale(0.6F);
    }

    @SpireOverride
    protected void playSfx()
    {
        SpireSuper.call();
    }

    @Override
    public void takeTurn()
    {
        switch (nextMove) {
            case 3:
                playSfx();
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "STAFF_RAISE"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25f));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.isEscaping) {
                        if (m == this) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, strAmt), strAmt));
                        } else {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new GainFocusPower(m, strAmt), strAmt));
                        }
                    }
                }
                break;
            default:
                super.takeTurn();
                return;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        state.setTimeScale(0.6f);
    }
}
