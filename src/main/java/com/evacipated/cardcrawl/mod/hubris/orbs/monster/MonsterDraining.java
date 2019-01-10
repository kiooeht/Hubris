package com.evacipated.cardcrawl.mod.hubris.orbs.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;

public class MonsterDraining extends AbstractOrb
{
    public static final String ORB_ID = "hubris:MonsterDraining";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbStrings.DESCRIPTION;
    private float vfxTimer = 0.5f;
    private static final int AMOUNT = 4;

    private OrbUsingMonster owner;

    public MonsterDraining(OrbUsingMonster owner)
    {
        this.owner = owner;
        ID = ORB_ID;
        img = ImageMaster.loadImage(HubrisMod.assetPath("images/orbs/draining.png"));
        name = orbStrings.NAME;
        baseEvokeAmount = AMOUNT;
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
        description = DESC[0] + evokeAmount + DESC[1];
    }

    @Override
    public void applyFocus()
    {
        // Not affected by Focus
    }

    @Override
    public void onEvoke()
    {
        AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(new Dazed(), evokeAmount, true, true));
    }

    @Override
    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ORB_PLASMA_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.cX, this.cY));
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 120.0F;

        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F)
        {
            AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(this.cX, this.cY));
            this.vfxTimer = 0.25F;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.c.a / 2.0F));
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale +
                MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);

        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.c.a / 2.0F));
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale +
                MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale, -this.angle, 0, 0, 96, 96, false, false);

        sb.setBlendFunction(770, 771);
        renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        CardCrawlGame.sound.play("ORB_PLASMA_CHANNEL", 0.1F);
    }

    @Override
    public AbstractOrb makeCopy()
    {
        return new MonsterDraining(owner);
    }
}
