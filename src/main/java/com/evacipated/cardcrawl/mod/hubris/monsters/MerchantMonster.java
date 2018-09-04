package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.hubris.actions.AnimationTimeScaleAction;
import com.evacipated.cardcrawl.mod.hubris.actions.StealGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.ThrowGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.utility.ForceWaitAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.StrengthPotion;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

import java.util.HashMap;
import java.util.Map;

// Ideas
// First turn
//   Steal all money
//   Power up based on money stolen (entering fight with loads of money = bad)
//   OR Lose health equal to money stolen (entering fight with loads of money = good)
// Moves
//   Steal money and heal (incompatible with above)
//   Activate relic
//     Bronze Scales
//     Lee's Waffle: Raise max hp by 7, heal all HP (use when low hp)
//     Cauldron: Use 5 random potions
//     Torii: When you would receive 5 or less unblocked Attack damage, reduce it to 1
//     Thread and Needle: Gain 5 Plated Armor
//     Red Skull: While HP <50%, gain 3 Strength
//     Letter Opener: Every time player plays 3 Skills in a single turn, deal 5 damage to player
//     Shuriken: Every time player plays 3 Attacks in a single turn, gain 1 Strength
//   Use potion
//     Block Potion
//     Ancient Potion
//     Fire Potion
//     Poison Potion
//     Regen Potion
//     Strength Potion
//     Weak Potion
public class MerchantMonster extends AbstractMonster
{
    public static final String ID = "hubris:Merchant";
    public static final String NAME = "Merchant";
    public static final String[] MOVES = {};
    public static final String[] DIALOG = {
            "Hey! NL No stealing!",
            "@Give@ @me@ @all@ @your@ @gold!@",
            "@No@ @Refunds.@",
            "Hey buddy, why'd you do that?",
            "~Ow!~",
            "Hey now, NL don't do that."
    };
    private static final float DRAW_X = Settings.WIDTH * 0.5F + 34.0F * Settings.scale;
    private static final float DRAW_Y = AbstractDungeon.floorY - 109.0F * Settings.scale;
    public static final int HP = 100;
    private static final float TIME_SCALE = 4.0f;

    private static final int METALLICIZE_AMT = 25;
    private static final Map<Integer, Integer> throwAmounts = new HashMap<>();

    private boolean firstTurn = true;

    // Moves

    static
    {
        throwAmounts.put(1, 20);
    }

    public MerchantMonster()
    {
        super(NAME, ID, HP, -10.0F, -30.0F, 180.0F, 150.0F, null, 0.0F, 0.0F);

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

        damage.add(new DamageInfo(this, 1));
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.MERCHANT_RUG_IMG, DRAW_X, DRAW_Y, 512.0F * Settings.scale, 512.0F * Settings.scale);

        super.render(sb);
    }

    @Override
    public void usePreBattleAction()
    {
        //UnlockTracker.markBossAsSeen("MERCHANT");
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(this, this, METALLICIZE_AMT));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new MetallicizePower(this, METALLICIZE_AMT), METALLICIZE_AMT));
        AbstractDungeon.actionManager.addToTop(new AnimationTimeScaleAction(this, TIME_SCALE));
        AbstractDungeon.actionManager.addToTop(new StealGoldAction(this, this, gold, true));
        AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[0], 0.5F, 3.0F));
    }

    @Override
    public void takeTurn()
    {
        if (nextMove == 0) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(this, new IntenseZoomEffect(hb.cX, hb.cY, true), 1.3F, true));
            AbstractDungeon.actionManager.addToTop(new ShakeScreenAction(1.3F, ScreenShake.ShakeDur.XLONG, ScreenShake.ShakeIntensity.HIGH));
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BUYOUT"));
        } else if (nextMove == 2) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
        } else {
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
        if (firstTurn) {
            firstTurn = false;
            setMove((byte) 1, Intent.ATTACK, 1, throwAmounts.get(1), true);
            return;
        }
        //setMove((byte)-1, Intent.UNKNOWN);
        if (num < 40) {
            setMove(StrengthPotion.NAME, (byte)2, Intent.BUFF);
        } else {
            setMove((byte) 1, Intent.ATTACK, 1, throwAmounts.get(1), true);
        }

        //getMove(AbstractDungeon.aiRng.random(20));
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);

        state.setTimeScale(TIME_SCALE * ((float)currentHealth / (float)maxHealth));
    }
}
