package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.hubris.actions.AnimationTimeScaleAction;
import com.evacipated.cardcrawl.mod.hubris.actions.StealGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.ThrowGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.utility.ForceWaitAction;
import com.evacipated.cardcrawl.mod.hubris.powers.GoldShieldPower;
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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.StrengthPotion;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

import java.util.HashMap;
import java.util.Map;

public class MerchantMonster extends AbstractMonster
{
    public static final String ID = "hubris:Merchant";
    public static final String NAME = "Merchant";
    public static final String[] MOVES = {};
    public static final String[] DIALOG = {
            "Hey! NL No stealing!",
            "@No@ @Refunds.@"
    };
    private static final float DRAW_X = Settings.WIDTH * 0.5F + 34.0F * Settings.scale;
    private static final float DRAW_Y = AbstractDungeon.floorY - 109.0F * Settings.scale;
    private static final int START_HP = 10;
    public static final int REAL_HP = 100;
    private static final float TIME_SCALE = 4.0f;

    // Move bytes
    private static byte ESCAPE = 0;
    private static byte ATTACK = 1;
    private static byte STRENGTH_UP = 2;
    private static byte HALF_DEAD = 7;

    private static final int METALLICIZE_AMT = 25;
    private static final Map<Integer, Integer> throwAmounts = new HashMap<>();

    private Merchant npc;
    private boolean doEscape = true;
    private boolean firstTurn = true;
    private boolean thresholdReached = false;

    static
    {
        throwAmounts.put(1, 20);
    }

    public MerchantMonster(MerchantMonster merchantMonster)
    {
        this(merchantMonster.npc);
    }

    public MerchantMonster(Merchant npc)
    {
        super(NAME, ID, START_HP, -10.0F, -30.0F, 180.0F, 150.0F, null, 0.0F, 0.0F);

        this.npc = npc;

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
        AbstractDungeon.getCurrRoom().cannotLose = true;

        damage.add(new DamageInfo(this, 1));
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!isDeadOrEscaped() || AbstractDungeon.getCurrRoom().cannotLose) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.MERCHANT_RUG_IMG, DRAW_X, DRAW_Y, 512.0F * Settings.scale, 512.0F * Settings.scale);
        }

        super.render(sb);
    }

    @Override
    public void usePreBattleAction()
    {
        //UnlockTracker.markBossAsSeen("MERCHANT");
        AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[0], 0.5F, 3.0F));
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
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, maxHealth));
            AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GoldShieldPower(this, 1)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, METALLICIZE_AMT), METALLICIZE_AMT));
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.5F, 3.0F));
        } else if (nextMove == STRENGTH_UP) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
        } else {
            firstTurn = false;
            Integer throwAmount = throwAmounts.get((int)nextMove);

            if (throwAmount != null && throwAmount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ThrowGoldAction(AbstractDungeon.player, this, throwAmount, false));
                AbstractDungeon.actionManager.addToBottom(new ForceWaitAction(1.6f));
                for (int i = 0; i < throwAmount; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), true));
                }
            }
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
        if (firstTurn) {
            setMove(ATTACK, Intent.ATTACK, 1, throwAmounts.get(1), true);
            return;
        }
        //setMove((byte)-1, Intent.UNKNOWN);
        if (num < 40) {
            if (lastMove(STRENGTH_UP)) {
                rollMove();
                return;
            }
            setMove(StrengthPotion.NAME, STRENGTH_UP, Intent.BUFF);
        } else {
            setMove(ATTACK, Intent.ATTACK, 1, throwAmounts.get(1), true);
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
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(npc.hb.cX, npc.hb.cY, RelicLibrary.getRelic(NiceRug.ID).makeCopy());
        }
    }
}
