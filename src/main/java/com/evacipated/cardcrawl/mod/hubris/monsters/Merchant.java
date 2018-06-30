package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.hubris.actions.AnimationTimeScaleAction;
import com.evacipated.cardcrawl.mod.hubris.actions.StealGoldAction;
import com.evacipated.cardcrawl.mod.hubris.actions.ThrowGoldAction;
import com.evacipated.cardcrawl.mod.hubris.powers.ToriiPower;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.BronzeScales;
import com.megacrit.cardcrawl.relics.ThreadAndNeedle;
import com.megacrit.cardcrawl.relics.Torii;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

import java.lang.reflect.Field;

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
public class Merchant extends AbstractMonster
{
    public static final String ID = "TheMerchant";
    public static final String NAME = "Merchant";
    public static final String[] MOVES = {};
    public static final String[] DIALOG = {
            "~Welcome!~",
            "@Give@ @me@ @all@ @your@ @gold!@",
            "@No@ @Refunds.@",
            "Hey buddy, why'd you do that?",
            "~Ow!~",
            "Hey now, NL don't do that."
    };
    private static final float DRAW_X = Settings.WIDTH * 0.5F + 34.0F * Settings.scale;
    private static final float DRAW_Y = AbstractDungeon.floorY - 109.0F * Settings.scale;
    public static final int HP = 100;
    private static final int PRICE_PER_POTION = 25;
    private static final float TIME_SCALE = 4.0f;

    private static final int FIRE_POTION_DMG = 20;

    // Moves
    private static final byte POTION_BLOCK = 2;
    private static final byte POTION_ANCIENT = 3;
    private static final byte POTION_REGEN = 4;
    private static final byte POTION_STRENGTH = 5;
    private static final byte POTION_FIRE = 6;
    private static final byte POTION_POISON = 7;
    private static final byte POTION_WEAK = 8;

    private boolean firstTurn = true;
    private boolean halfDead = false;

    public Merchant()
    {
        super(NAME, ID, 3, -10.0F, -30.0F, 180.0F, 150.0F, null, 0.0F, 0.0F);

        drawX = 1260.0F * Settings.scale;
        drawY = 370.0F * Settings.scale;

        loadAnimation("images/npcs/merchant/skeleton.atlas", "images/npcs/merchant/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.0F);

        type = EnemyType.BOSS;
        dialogX = -200.0F * Settings.scale;
        dialogY = 10.0F * Settings.scale;

        gold = 500;

        damage.add(new DamageInfo(this, FIRE_POTION_DMG, DamageInfo.DamageType.NORMAL));
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
        AbstractDungeon.getCurrRoom().cannotLose = true;
        //UnlockTracker.markBossAsSeen("MERCHANT");
        AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[0], 0.5F, 3.0F));
    }

    private void gainRelics()
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SharpHidePower(this, 3), 3));
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new BronzeScales()));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 5), 5));
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new ThreadAndNeedle()));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new Torii()));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ToriiPower(this)));
    }

    private void gainPotions(int gold)
    {
        int potion_count = gold / PRICE_PER_POTION;
        System.out.println("Drinking " + potion_count + " potions");
        for (int i=0; i<potion_count; ++i) {
            usePotion((byte)AbstractDungeon.aiRng.random(2, 5));
        }
    }

    @Override
    public void changeState(String key)
    {
        if (key.equals("BUYOUT")) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

            maxHealth = HP;

            AbstractDungeon.actionManager.addToTop(new GainBlockAction(this, this, AbstractDungeon.player.gold));
            AbstractDungeon.actionManager.addToTop(new HealAction(this, this, HP));
            AbstractDungeon.actionManager.addToTop(new RemoveDebuffsAction(this));
            AbstractDungeon.actionManager.addToTop(new AnimationTimeScaleAction(this, TIME_SCALE));
            AbstractDungeon.actionManager.addToTop(new StealGoldAction(AbstractDungeon.player, this, AbstractDungeon.player.gold, true));
            AbstractDungeon.actionManager.addToTop(new ShoutAction(this, DIALOG[1], 0.5F, 2.0F));

            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            gainRelics();
            gainPotions(AbstractDungeon.player.gold);
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2], 0.5F, 3.5F));
        }
    }

    @Override
    public void takeTurn()
    {
        if (nextMove == 0) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(this, new IntenseZoomEffect(hb.cX, hb.cY, true), 1.3F, true));
            AbstractDungeon.actionManager.addToTop(new ShakeScreenAction(1.3F, ScreenShake.ShakeDur.XLONG, ScreenShake.ShakeIntensity.HIGH));
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BUYOUT"));
        } else if (nextMove == 1) {
        } else {
            usePotion(nextMove);
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void usePotion(byte potion)
    {
        switch (potion) {
            case POTION_POISON:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, PoisonPotion.NAME));
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new PoisonPower(AbstractDungeon.player, this, 6), 6));
                break;
            case POTION_ANCIENT:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, AncientPotion.NAME));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1), 1));
                break;
            case POTION_BLOCK:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, BlockPotion.NAME));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 12));
                break;
            case POTION_FIRE:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, FirePotion.NAME));
                try {
                    Field moveField = AbstractMonster.class.getDeclaredField("move");
                    moveField.setAccessible(true);
                    EnemyMoveInfo move = (EnemyMoveInfo) moveField.get(this);
                    /*
                    for (int i = 0; i < move.multiplier; ++i) {
                        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                    }
                    //*/
                    AbstractDungeon.actionManager.addToTop(new ThrowGoldAction(AbstractDungeon.player, this, 2000, true));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                break;
            case POTION_REGEN:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, RegenPotion.NAME));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 5), 5));
                break;
            case POTION_STRENGTH:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, StrengthPotion.NAME));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
                break;
            case POTION_WEAK:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, WeakenPotion.NAME));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 3, true), 3));
                break;
        }
    }

    @Override
    protected void getMove(int num)
    {
        if (!halfDead) {
            setMove((byte)-1, Intent.NONE);
            return;
        }

        /*if (firstTurn) {
            firstTurn = false;
            setMove((byte)1, Intent.UNKNOWN);
            return;
        }*/
        if (num < 21) { // Potion
            byte type = (byte)((num / 3) + 2);
            if (lastTwoMoves(type)) {
                getMove(AbstractDungeon.aiRng.random(20));
                return;
            }
            type = POTION_FIRE;
            Intent intent = Intent.DEBUG;
            switch (type) {
                case POTION_BLOCK:
                    intent = Intent.DEFEND;
                    break;
                case POTION_FIRE:
                    intent = Intent.ATTACK;
                    break;
                case POTION_REGEN:
                case POTION_STRENGTH:
                case POTION_ANCIENT:
                    intent = Intent.BUFF;
                    break;
                case POTION_WEAK:
                case POTION_POISON:
                    intent = Intent.DEBUFF;
                    break;
            }
            System.out.println(type);
            if (intent == Intent.ATTACK) {
                setMove(type, intent, damage.get(0).base, AbstractDungeon.aiRng.random(1, 3), true);
            } else {
                setMove(type, intent);
            }
            return;
        }

        getMove(AbstractDungeon.aiRng.random(20));
    }

    @Override
    public void damage(DamageInfo info)
    {
        int oldHealth = currentHealth;
        super.damage(info);

        if (currentHealth <= 0 && !halfDead) {
            halfDead = true;
            state.setTimeScale(0.0F);
            AbstractDungeon.actionManager.addToTop(new ClearCardQueueAction());
            setMove((byte) 0, Intent.UNKNOWN);
            createIntent();
        } else if (!halfDead && currentHealth < oldHealth) {
            if (oldHealth == maxHealth || MathUtils.random(2) == 0) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[MathUtils.random(3, 5)], 0.5F, 2.0F));
            }
        }
        if (halfDead) {
            state.setTimeScale(TIME_SCALE * ((float)currentHealth / (float)maxHealth));
        }
    }

    @Override
    public void die()
    {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
}
