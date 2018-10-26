package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.common.PureDamageAction;
import com.evacipated.cardcrawl.mod.hubris.powers.ArmorPiercingPower;
import com.evacipated.cardcrawl.mod.hubris.powers.IrradiatedRoundsPower;
import com.evacipated.cardcrawl.mod.hubris.vfx.scene.DeadTorchParticleEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class MusketHawk extends AbstractMonster
{
    public static final String ID = "hubris:MusketHawk";
    public static final String NAME = "Musket Hawk";
    public static final String[] MOVES = {};
    public static final int HP = 350;

    private static final byte LOAD_CANNON = 0;
    private static final byte FIRE_CANNON = 1;
    private static final byte WOUNDING_SHOT = 2;
    private static final byte NORMAL_SHOT = 3;
    private static final byte IRRADIATE_BUFF = 4;

    private static final float PARTICAL_EMIT_INTERVAL = 0.15f;

    private float particleTimer = 0.0f;

    private int numTurns = 0;

    private boolean firstTurn = true;
    private int woundAmt;
    private int irradiateAmt;

    private int savedIrradiatedStack = 0;

    public MusketHawk()
    {
        super(NAME, ID, HP, -8.0f, 80.0f, 330, 530, HubrisMod.assetPath("images/monsters/theCity/musketHawk.png"), -100.0f, -100.0f);
        /*
        loadAnimation("images/monsters/theBottom/cultist/skeleton.atlas", "images/monsters/theBottom/cultist/skeleton.json", 0.75F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "waving", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        */

        this.type = AbstractMonster.EnemyType.BOSS;
        this.dialogX = (-400.0F * Settings.scale);
        this.dialogY = (200.0F * Settings.scale);

        damage.add(0, new DamageInfo(this, 50));
        damage.add(1, new DamageInfo(this, 10));
        damage.add(2, new DamageInfo(this, 16));

        woundAmt = 1;
        irradiateAmt = 3;
    }

    @Override
    public void usePreBattleAction()
    {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArmorPiercingPower(this)));
    }

    private void queueDamageAction(DamageInfo damageInfo)
    {
        int basePureDamage = damageInfo.output / 2;
        int baseNormalDamage = damageInfo.output - basePureDamage;
        if (hasPower(ArmorPiercingPower.POWER_ID) && AbstractDungeon.player.currentBlock >= baseNormalDamage) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, baseNormalDamage, DamageInfo.DamageType.NORMAL)));
            AbstractDungeon.actionManager.addToBottom(new PureDamageAction(AbstractDungeon.player, new DamageInfo(null, basePureDamage, DamageInfo.DamageType.HP_LOSS)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damageInfo));
        }
    }

    @Override
    public void takeTurn()
    {
        if (nextMove == LOAD_CANNON) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, ArmorPiercingPower.POWER_ID));
            if (hasPower(IrradiatedRoundsPower.POWER_ID)) {
                savedIrradiatedStack = getPower(IrradiatedRoundsPower.POWER_ID).amount;
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, IrradiatedRoundsPower.POWER_ID));
            } else {
                savedIrradiatedStack = 0;
            }
        } else if (nextMove == FIRE_CANNON) {
            queueDamageAction(damage.get(0));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArmorPiercingPower(this)));
            if (savedIrradiatedStack > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IrradiatedRoundsPower(this, savedIrradiatedStack), savedIrradiatedStack));
                savedIrradiatedStack = 0;
            }
        } else if (nextMove == WOUNDING_SHOT) {
            queueDamageAction(damage.get(1));

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this,
                    new WeakPower(AbstractDungeon.player, woundAmt, true), woundAmt));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this,
                    new FrailPower(AbstractDungeon.player, woundAmt, true), woundAmt));
        } else if (nextMove == NORMAL_SHOT) {
            queueDamageAction(damage.get(2));
        } else if (nextMove == IRRADIATE_BUFF) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IrradiatedRoundsPower(this, irradiateAmt), irradiateAmt));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num)
    {
        if (lastMove(LOAD_CANNON)) {
            setMove(FIRE_CANNON, Intent.ATTACK, damage.get(0).base);
            return;
        }
        if (numTurns == 4) {
            setMove("Load Cannon", LOAD_CANNON, Intent.UNKNOWN);
            numTurns = 0;
        } else if (firstTurn || num < 40) {
            setMove(WOUNDING_SHOT, Intent.ATTACK_DEBUFF, damage.get(1).base);
            firstTurn = false;
        } else if (num < 60 && !lastMove(IRRADIATE_BUFF)) {
            setMove(IRRADIATE_BUFF, Intent.BUFF);
        } else {
            setMove(NORMAL_SHOT, Intent.ATTACK, damage.get(2).base);
        }

        ++numTurns;
    }

    @Override
    public void update()
    {
        super.update();

        if (nextMove == FIRE_CANNON) {
            particleTimer -= Gdx.graphics.getDeltaTime();
            if (particleTimer < 0) {
                particleTimer = PARTICAL_EMIT_INTERVAL;
                AbstractDungeon.topLevelEffectsQueue.add(new DeadTorchParticleEffect(hb.x + hb.width - 15.0f * Settings.scale, hb.y + 150.0f * Settings.scale));
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
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_CULTIST_2A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_CULTIST_2B");
        } else {
            CardCrawlGame.sound.play("VO_CULTIST_2C");
        }
        onBossVictoryLogic();
    }
}
