package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs.MonsterAnimateOrbAction;
import com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs.MonsterChannelAction;
import com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs.MonsterEvokeOrbAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public abstract class OrbUsingMonster extends AbstractMonster
{
    public static class Enums
    {
        @SpireEnum
        public static Intent CHANNEL_ORBS;
    }

    protected int maxOrbsCap = 10;
    public ArrayList<AbstractOrb> orbs = new ArrayList<>();
    public int numberToChannel = 0;

    public OrbUsingMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY)
    {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    public OrbUsingMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl)
    {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
    }

    @Override
    public void update()
    {
        super.update();
        for (AbstractOrb o : orbs) {
            o.updateAnimation();
        }
        for (AbstractOrb o : orbs) {
            o.update();
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);
        if (!isDead) {
            for (AbstractOrb o : orbs) {
                o.render(sb);
            }
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        if (hasPower(FocusPower.POWER_ID)) {
            for (AbstractOrb o : orbs) {
                o.updateDescription();
            }
        }
    }

    public void channelOrb(AbstractOrb orbToSet)
    {
        if (orbs.size() <= 0) {
            AbstractDungeon.effectList.add(new ThoughtBubble(dialogX, dialogY, 1.0f, "asdf1", false));
            return;
        }

        int index = -1;
        for (int i=0; i<orbs.size(); ++i) {
            if (orbs.get(i) instanceof EmptyOrbSlot) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            orbToSet.cX = orbs.get(index).cX;
            orbToSet.cY = orbs.get(index).cY;
            orbs.set(index, orbToSet);
            //orbs.get(index).setSlot(index, orbs.size());
            setSlot(orbs.get(index), index, orbs.size());
            orbToSet.playChannelSFX();
            for (AbstractPower p : powers) {
                p.onChannel(orbToSet);
            }
            //AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orbToSet);
            orbToSet.applyFocus();
        } else {
            AbstractDungeon.actionManager.addToTop(new MonsterChannelAction(this, orbToSet));
            AbstractDungeon.actionManager.addToTop(new MonsterEvokeOrbAction(this, 1));
            AbstractDungeon.actionManager.addToTop(new MonsterAnimateOrbAction(this, 1));
        }
    }

    public void evokeOrb()
    {
        if (!orbs.isEmpty() && !(orbs.get(0) instanceof EmptyOrbSlot)) {
            orbs.get(0).onEvoke();
            AbstractOrb orbSlot = new EmptyOrbSlot();
            for (int i=1; i<orbs.size(); ++i) {
                Collections.swap(orbs, i, i-1);
            }
            orbs.set(orbs.size() - 1, orbSlot);
            for (int i=0; i<orbs.size(); ++i) {
                //orbs.get(i).setSlot(i, orbs.size());
                setSlot(orbs.get(i), i, orbs.size());
            }
        }
    }

    public void increaseMaxOrbSlots(int amount, boolean playSfx)
    {
        if (orbs.size() == maxOrbsCap) {
            AbstractDungeon.effectList.add(new ThoughtBubble(dialogX, dialogY, 1.0f, "asdf2", false));
            return;
        }

        if (orbs.size() + amount > maxOrbsCap) {
            amount = maxOrbsCap - orbs.size();
        }

        if (playSfx) {
            CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1f);
        }
        for (int i=0; i<amount; ++i) {
            AbstractOrb orb = new EmptyOrbSlot();
            orb.cX = drawX + hb_x;
            orb.cY = drawY + hb_y + hb_h / 2.0f;
            orbs.add(orb);
        }
        for (int i=0; i<orbs.size(); ++i) {
            //orbs.get(i).setSlot(i, orbs.size());
            setSlot(orbs.get(i), i, orbs.size());
        }
    }

    public void decreaseMaxOrbSlots(int amount)
    {
        if (orbs.size() <= 0) {
            return;
        }
        if (amount > orbs.size()) {
            amount = orbs.size();
        }

        for (int i=0; i<amount; ++i) {
            orbs.remove(orbs.size() - 1);
        }
        for (int i=0; i<orbs.size(); ++i) {
            //orbs.get(i).setSlot(i, orbs.size());
            setSlot(orbs.get(i), i, orbs.size());
        }
    }

    protected void setSlot(AbstractOrb orb, int slotNum, int maxOrbs)
    {
        float dist = 160.0F * Settings.scale + maxOrbs * 10.0F * Settings.scale;
        float angle = 100.0F + maxOrbs * 12.0F;
        float offsetAngle = angle / 2.0F;
        angle *= slotNum / (maxOrbs - 1.0F);
        angle += 90.0F - offsetAngle;
        orb.tX = (-(dist * MathUtils.cosDeg(angle)) + drawX);
        orb.tY = (dist * MathUtils.sinDeg(angle) + drawY + hb_h / 2.0F);
        if (maxOrbs == 1)
        {
            orb.tX = drawX;
            orb.tY = (160.0F * Settings.scale + drawY + hb_h / 2.0F);
        }
        try {
            Field f = AbstractOrb.class.getDeclaredField("hb");
            f.setAccessible(true);
            Hitbox hb = (Hitbox)f.get(orb);
            hb.move(orb.tX, orb.tY);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void triggerEvokeAnimation(int i)
    {
        if (orbs.size() <= 0) {
            return;
        }
        orbs.get(i).triggerEvokeAnimation();
    }
}
