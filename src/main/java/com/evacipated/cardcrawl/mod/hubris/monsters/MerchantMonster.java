package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.AnimationTimeScaleAction;
import com.evacipated.cardcrawl.mod.hubris.actions.StealGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.ThrowGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.utility.ForceWaitAction;
import com.evacipated.cardcrawl.mod.hubris.blights.Reinasbane;
import com.evacipated.cardcrawl.mod.hubris.powers.GoldShieldPower;
import com.evacipated.cardcrawl.mod.hubris.powers.BodyguardPower;
import com.evacipated.cardcrawl.mod.hubris.relics.NiceRug;
import com.evacipated.cardcrawl.mod.hubris.vfx.combat.BlueSmokeBombEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.StrengthPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MerchantMonster extends AbstractMonster
{
    public static final String ID = "hubris:Merchant";
    public static final String NAME = "Merchant";
    public static final String[] MOVES = {};
    public static final String[] DIALOG = {
            "Hey! NL No stealing!",
            "@No@ @Refunds.@",
            new String(new byte[]{0x52, 0x65, 0x69, 0x6e, 0x61, 0x2c, 0x20, 0x70, 0x6c, 0x65, 0x61, 0x73, 0x65, 0x20,
                    0x73, 0x74, 0x6f, 0x70, 0x20, 0x68, 0x75, 0x72, 0x74, 0x69, 0x6e, 0x67, 0x20, 0x6d, 0x65, 0x2e}),
            "Give me back my rug!"
    };
    private static final float DRAW_X = Settings.WIDTH * 0.5F + 34.0F * Settings.scale;
    private static final float DRAW_Y = AbstractDungeon.floorY - 109.0F * Settings.scale;
    private static final int START_HP = 10;
    public static final int REAL_HP = 200;
    private static final float TIME_SCALE = 4.0f;

    // Move bytes
    private static byte ESCAPE = 0;
    private static byte ATTACK = 1;
    private static byte STRENGTH_UP = 2;
    private static byte ATTACK_STRENGTH_UP = 3;
    private static byte HALF_DEAD = 7;

    private static final int METALLICIZE_AMT = 35;
    private static final int ARTIFACT_AMT = 5;
    private static final Map<Byte, Integer> throwAmounts = new HashMap<>();

    private boolean doEscape = true;
    private int turn = -1;
    private boolean thresholdReached = false;
    private int abuse = 0;
    private boolean boss = false;

    static
    {
        throwAmounts.put(ATTACK, 20);
        throwAmounts.put(ATTACK_STRENGTH_UP, 15);
    }

    public MerchantMonster(boolean boss)
    {
        this();
        this.boss = boss;
        if (boss) {
            doEscape = false;
            turn = 1;
            maxHealth = REAL_HP;
            maxHealth *= (abuse >= 3 ? 1.5f : 1.0f);
            currentHealth = maxHealth;
        }
    }

    public MerchantMonster()
    {
        super(NAME, ID, START_HP, -10.0F, -30.0F, 180.0F, 150.0F, null, 0.0F, 0.0F);

        if (CardCrawlGame.playerName.equals(DIALOG[2].substring(0, 5))) {
            if (HubrisMod.otherSaveData != null && HubrisMod.otherSaveData.has("abuse")) {
                abuse = HubrisMod.otherSaveData.getInt("abuse");
            }
        }

        drawX = 1260.0F * Settings.scale;
        drawY = 370.0F * Settings.scale;

        loadAnimation("images/npcs/merchant/skeleton.atlas", "images/npcs/merchant/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.0F);

        type = EnemyType.BOSS;
        dialogX = -200.0F * Settings.scale;
        dialogY = 10.0F * Settings.scale;

        gold = 300;
        halfDead = false;

        damage.add(new DamageInfo(this, 1));
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!boss) {
            if (!isDeadOrEscaped() || AbstractDungeon.getCurrRoom().cannotLose) {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.MERCHANT_RUG_IMG, DRAW_X, DRAW_Y, 512.0F * Settings.scale, 512.0F * Settings.scale);
            }
        }

        super.render(sb);
    }

    @Override
    public void usePreBattleAction()
    {
        AbstractDungeon.getCurrRoom().cannotLose = true;

        //UnlockTracker.markBossAsSeen("MERCHANT");
        if (boss) {
            AbstractDungeon.actionManager.addToTop(new CanLoseAction());
            int artifact_amt = (abuse >= 3 ? (int)(ARTIFACT_AMT * 1.6) : ARTIFACT_AMT);
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new ArtifactPower(this, artifact_amt), artifact_amt));
            int metal_amt = (abuse >= 3 ? 50 : METALLICIZE_AMT);
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(this, this, metal_amt, true));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new GoldShieldPower(this, metal_amt), metal_amt));

            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new BodyguardPower(this)));
            AbstractDungeon.actionManager.addToTop(new AnimationTimeScaleAction(this, TIME_SCALE));
            AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[3], 0.5F, 3.0F));
        } else {
            AbstractDungeon.actionManager.addToTop(new TalkAction(this, (abuse >= 3 ? DIALOG[2] : DIALOG[0]), 0.5F, 3.0F));
        }
    }

    @Override
    public void takeTurn()
    {
        if (nextMove == ESCAPE) {
            AbstractDungeon.getCurrRoom().smoked = true;
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BlueSmokeBombEffect(hb.cX, hb.cY)));
            AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, ESCAPE, Intent.ESCAPE));
        } else if (nextMove == HALF_DEAD) {
            doEscape = false;
            halfDead = false;
            AbstractDungeon.actionManager.addToBottom(new StealGoldAction(this, this, gold, true));
            AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(1.3F, ScreenShake.ShakeDur.XLONG, ScreenShake.ShakeIntensity.HIGH));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntenseZoomEffect(hb.cX, hb.cY, true), 1.3F, true));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.25F));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.25F));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.25F));
            AbstractDungeon.actionManager.addToBottom(new AnimationTimeScaleAction(this, TIME_SCALE));
            maxHealth = REAL_HP;
            maxHealth *= (abuse >= 3 ? 1.5f : 1.0f);
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, maxHealth));
            AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
            int metal_amt = (abuse >= 3 ? 50 : METALLICIZE_AMT);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GoldShieldPower(this, metal_amt), metal_amt));
            int artifact_amt = (abuse >= 3 ? (int)(ARTIFACT_AMT * 1.6) : ARTIFACT_AMT);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, artifact_amt), artifact_amt));
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.5F, 3.0F));
            turn = 0;
        } else if (nextMove == STRENGTH_UP) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
        } else {
            Integer throwAmount = throwAmounts.get(nextMove);

            if (throwAmount != null && throwAmount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ThrowGoldAction(AbstractDungeon.player, this, throwAmount, false));
                AbstractDungeon.actionManager.addToBottom(new ForceWaitAction(1.6f));
                for (int i = 0; i < throwAmount; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), true));
                }
            }

            if (nextMove == ATTACK_STRENGTH_UP) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
            }
        }

        if (turn >= 0) {
            ++turn;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num)
    {
        if (doEscape) {
            setMove(ESCAPE, Intent.ESCAPE);
            return;
        }
        if (turn == 1) {
            setMove(ATTACK, Intent.ATTACK, 1, throwAmounts.get(ATTACK), true);
            return;
        }
        if (turn == 2) {
            setMove(ATTACK_STRENGTH_UP, Intent.ATTACK_BUFF, 1, throwAmounts.get(ATTACK_STRENGTH_UP), true);
            return;
        }
        //setMove((byte)-1, Intent.UNKNOWN);
        if (num < 40) {
            if (lastMove(STRENGTH_UP)) {
                rollMove();
                return;
            }
            setMove(PotionHelper.getPotion(StrengthPotion.POTION_ID).name, STRENGTH_UP, Intent.BUFF);
        } else if (num < 60) {
            setMove(ATTACK_STRENGTH_UP, Intent.ATTACK_BUFF, 1, throwAmounts.get(ATTACK_STRENGTH_UP), true);
        } else {
            setMove(ATTACK, Intent.ATTACK, 1, throwAmounts.get(ATTACK), true);
        }

        //getMove(AbstractDungeon.aiRng.random(20));
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);

        state.setTimeScale(TIME_SCALE * ((float)currentHealth / (float)maxHealth));

        if (currentHealth <= 0 && !halfDead) {
            halfDead = true;
            for (AbstractPower p : powers) {
                p.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }
            powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF);
            setMove(HALF_DEAD, Intent.UNKNOWN);
            createIntent();
            applyPowers();
            AbstractDungeon.actionManager.cardQueue.clear();
            for (AbstractCard c : AbstractDungeon.player.limbo.group) {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
            }
            AbstractDungeon.player.limbo.group.clear();
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
            //AbstractDungeon.actionManager.actions.clear();
        }
    }

    @Override
    public void die()
    {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            if (!boss) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(hb.cX, hb.cY, RelicLibrary.getRelic(NiceRug.ID).makeCopy());
            }

            if (CardCrawlGame.playerName.equals(new String(new byte[]{0x52, 0x65, 0x69, 0x6E, 0x61}))) {
                if (abuse >= 3 && MathUtils.randomBoolean(0.5f)) {
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain(hb.cX, hb.cY, new Reinasbane());
                }

                ++abuse;

                try {
                    HubrisMod.otherSaveData.setInt("abuse", abuse);
                    HubrisMod.otherSaveData.save();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
