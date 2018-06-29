package com.evacipated.cardcrawl.mod.hubris.orbs.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;

public class MonsterMiasma extends AbstractOrb
{
    public static final String ORB_ID = "hubris:MonsterMiasma";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbStrings.DESCRIPTION;
    private static final float ORB_BORDER_SCALE = 1.2f;
    private float vfxTimer = 0.5f;
    private static final float VFX_INTERVAL_TIME = 0.25f;

    private OrbUsingMonster owner;

    public MonsterMiasma(OrbUsingMonster owner)
    {
        this.owner = owner;
        ID = ORB_ID;
        img = ImageMaster.ORB_DARK;
        name = orbStrings.NAME;
        baseEvokeAmount = 1;
        evokeAmount = baseEvokeAmount;
        basePassiveAmount = 0;
        passiveAmount = basePassiveAmount;
        showEvokeValue();
        updateDescription();
        channelAnimTimer = vfxTimer;
    }

    @Override
    public void updateDescription()
    {
        applyFocus();
        description = DESC[0] + evokeAmount + DESC[1] + evokeAmount + DESC[2] + evokeAmount + DESC[3];
    }

    @Override
    public void applyFocus()
    {
        AbstractPower power = owner.getPower(FocusPower.POWER_ID);
        if (power != null && !ID.equals(Plasma.ORB_ID)) {
            passiveAmount = Math.max(0, basePassiveAmount + power.amount);
            evokeAmount = Math.max(0, baseEvokeAmount + power.amount);
        }
    }

    @Override
    public void onEvoke()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, owner, new FrailPower(p, evokeAmount, true), evokeAmount));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, owner, new VulnerablePower(p, evokeAmount, true), evokeAmount));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, owner, new WeakPower(p, evokeAmount, true), evokeAmount));
    }

    @Override
    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ORB_DARK_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 120.0F;

        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F)
        {
            AbstractDungeon.effectList.add(new DarkOrbPassiveEffect(this.cX, this.cY));
            this.vfxTimer = 0.25F;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);

        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.c.a / 3.0F));
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.2F, this.scale * 1.2F, this.angle / 1.2F, 0, 0, 96, 96, false, false);

        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.5F, this.scale * 1.5F, this.angle / 1.4F, 0, 0, 96, 96, false, false);

        sb.setBlendFunction(770, 771);
        renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.1F);
    }

    @Override
    public AbstractOrb makeCopy()
    {
        return new MonsterMiasma(owner);
    }
}
