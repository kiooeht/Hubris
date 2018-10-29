package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.SacrificeMinionAction;
import com.evacipated.cardcrawl.mod.hubris.powers.CursedLifePower;
import com.evacipated.cardcrawl.mod.hubris.powers.FakeDeathPower;
import com.evacipated.cardcrawl.mod.hubris.vfx.scene.NecromanticTotemParticleEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class NecromanticTotem extends AbstractMonster
{
    public static final String ID = "hubris:NecromanticTotem";
    public static final String NAME = "Necromantic Totem";
    public static final String[] MOVES = {};
    public static final int HP = 50;
    private static final int CURSE_AMT = 7;
    private static final int STRENGTH_AMT = 3;
    private static final int INTANGIBLE_AMT = 1;
    private static final int MEGA_DEBUFF_AMT = 3;

    private static final byte SUMMON = 0;
    private static final byte BUFF1 = 1;
    private static final byte BUFF2 = 2;
    private static final byte DEBUFF1 = 3;
    private static final byte DEBUFF2 = 4;

    private static final float PARTICAL_EMIT_INTERVAL = 0.15f;

    private static ShaderProgram shader = new ShaderProgram(
            Gdx.files.internal(HubrisMod.assetPath("shaders/totem/vertexShader.vs")),
            Gdx.files.internal(HubrisMod.assetPath("shaders/totem/fragShader.fs"))
    );

    private Texture faceImg;
    private float particleTimer = 0.0f;

    private int numTurns = 0;
    private float doneMove = -1;

    public NecromanticTotem()
    {
        super(NAME, ID, HP, -8.0f, 10.0f, 230, 300, HubrisMod.assetPath("images/monsters/theCity/necromanticTotem.png"), 100.0f, -30.0f);

        faceImg = ImageMaster.loadImage(HubrisMod.assetPath("images/monsters/theCity/necromanticTotemFace.png"));

        this.type = AbstractMonster.EnemyType.BOSS;
        this.dialogX = (-400.0F * Settings.scale);
        this.dialogY = (200.0F * Settings.scale);

        damage.add(0, new DamageInfo(this, 50));
        damage.add(1, new DamageInfo(this, 10));
        damage.add(2, new DamageInfo(this, 16));
    }

    @Override
    public void usePreBattleAction()
    {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CursedLifePower(this, CURSE_AMT), CURSE_AMT));

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != this) {
                m.powers.add(new FakeDeathPower(m));
            }
        }
    }

    @Override
    public void takeTurn()
    {
        switch (nextMove) {
            case SUMMON:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != this) {
                        if (!m.isDeadOrEscaped()) {
                            AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(m.hb.cX, m.hb.cY), 2.0F));
                        }
                        AbstractDungeon.actionManager.addToBottom(new SacrificeMinionAction(this, m));
                    }
                }
                break;
            case BUFF1:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != this && !m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, STRENGTH_AMT), STRENGTH_AMT));
                    }
                }
                break;
            case BUFF2:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != this && !m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new IntangiblePower(m, INTANGIBLE_AMT), INTANGIBLE_AMT));
                    }
                }
                break;
            case DEBUFF1:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(hb.cX, hb.cY), 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, MEGA_DEBUFF_AMT, true), MEGA_DEBUFF_AMT));
                //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, MEGA_DEBUFF_AMT, true), MEGA_DEBUFF_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, MEGA_DEBUFF_AMT, true), MEGA_DEBUFF_AMT));
                break;
            case DEBUFF2:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
                break;
        }

        doneMove = 0;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num)
    {
        if (doneMove < 0) {
            setMove("Raise Dead", SUMMON, Intent.UNKNOWN);
        } else if (num < 50) {
            setMove(BUFF1, Intent.BUFF);
        } else if (num < 70) {
            setMove(DEBUFF2, Intent.DEBUFF);
        } else {
            setMove(DEBUFF1, Intent.STRONG_DEBUFF);
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (doneMove >= 1.0f && !isDeadOrEscaped()) {
            particleTimer -= Gdx.graphics.getDeltaTime();
            if (particleTimer < 0) {
                particleTimer = PARTICAL_EMIT_INTERVAL;
                AbstractDungeon.topLevelEffectsQueue.add(new NecromanticTotemParticleEffect(hb.x + 88.0f * Settings.scale, hb.y + 207.0f * Settings.scale));
                AbstractDungeon.topLevelEffectsQueue.add(new NecromanticTotemParticleEffect(hb.x + 130.0f * Settings.scale, hb.y + 205.0f * Settings.scale));
            }
        } else if (doneMove >= 0) {
            doneMove += Gdx.graphics.getDeltaTime();
            if (doneMove > 1.0f) {
                doneMove = 1.0f;
            }
        }
    }

    @Override
    public void die()
    {
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        deathTimer += 1.5F;
        super.die();
        onBossVictoryLogic();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.halfDead) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2f));
            }
        }
    }

    private float renderTimer = 0;
    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        if (MathUtils.random(100) < 10) {
            animX = MathUtils.random(-10.0f, 5.0f);
            if (animX <= -9.9f) {
                animX = -15.0f;
            } else if (animX < -5.0f) {
                animX = -5.0f;
            }
            animX *= Settings.scale;
            animY = MathUtils.random(-3.0f, 3.0f) * Settings.scale;
        }
        renderFace(sb);
        animX = 0;
        animY = 0;
    }

    private void renderFace(SpriteBatch sb)
    {
        if (doneMove <= 0.0f) {
            return;
        }

        renderTimer += Gdx.graphics.getDeltaTime() * MathUtils.random(0.5f, 2.0f);

        sb.end();
        shader.begin();
        shader.setUniformf("timer", renderTimer);
        shader.setUniformf("fadeIn", doneMove);
        shader.setUniformf("white", animX < -4.0f * Settings.scale ? 0.25f : 0.0f);
        sb.setShader(shader);
        sb.begin();

        sb.setColor(tint.color);
        sb.draw(
                faceImg,
                drawX - faceImg.getWidth() * Settings.scale / 2.0f + animX, drawY + animY + AbstractDungeon.sceneOffsetY,
                faceImg.getWidth() * Settings.scale, faceImg.getHeight() * Settings.scale,
                0, 0,
                faceImg.getWidth(), faceImg.getHeight(),
                flipHorizontal, flipVertical
        );

        sb.end();
        shader.end();
        sb.setShader(null);
        sb.begin();
    }
}
