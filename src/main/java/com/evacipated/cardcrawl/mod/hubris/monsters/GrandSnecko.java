package com.evacipated.cardcrawl.mod.hubris.monsters;

import basemod.Pair;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs.MonsterChannelAction;
import com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs.MonsterIncreaseMaxOrbAction;
import com.evacipated.cardcrawl.mod.hubris.orbs.monster.*;
import com.evacipated.cardcrawl.mod.hubris.powers.UnfocusedPower;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrandSnecko extends OrbUsingMonster
{
    public static final String ID = "hubris:GrandSnecko";
    public static final String NAME = "Grand Snecko";
    public static final String[] MOVES = {};
    public static final String[] DIALOG = {
            "@THERE.@ @ARE.@ @FOUR.@ @LIGHTS.@",
            "~Tell~ ~me...~ NL How many lights do you see?"
    };
    public static final int HP = 600;

    private static final int BASE_ORB_SLOTS = 4;
    private static final ArrayList<Pair<Integer, Class<? extends AbstractOrb>>> orbPercents;
    private static int maxPercent;

    private static final List<String> summons = Arrays.asList(
            MonsterHelper.SNECKO_ENC,
            GrandMystic.ID,
            MonsterHelper.CULTIST_ENC,
            MonsterHelper.THREE_SENTRY_ENC,
            MonsterHelper.SPHERE_GUARDIAN_ENC
    );

    static {
        orbPercents = new ArrayList<>();
        orbPercents.add(new Pair<>(70, MonsterLightning.class));
        orbPercents.add(new Pair<>(15, MonsterMiasma.class));
        orbPercents.add(new Pair<>(10, MonsterDraining.class));

        int sum = 0;
        for (Pair<Integer, Class<? extends AbstractOrb>> kv : orbPercents) {
            sum += kv.getKey();
        }
        maxPercent = sum;
    }

    private int ORB_SLOTS = BASE_ORB_SLOTS;
    private int summonCount = 0;
    private int turnCount = 0;

    public GrandSnecko()
    {
        super(NAME, ID, HP, -30.0f, -20.0f, 434, 477, null, -50.0f, 30.0f);
        maxOrbsCap = 12;
        loadAnimation("images/monsters/theCity/reptile/skeleton.atlas", "images/monsters/theCity/reptile/skeleton.json", 0.25F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.4F);

        this.type = AbstractMonster.EnemyType.BOSS;
        this.dialogX = (-400.0F * Settings.scale);
        this.dialogY = (200.0F * Settings.scale);
    }

    private AbstractOrb makeOrb(Class clazz)
    {
        Constructor ctor = null;
        try {
            ctor = clazz.getConstructor(OrbUsingMonster.class);
            return (AbstractOrb)ctor.newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AbstractOrb getRandomOrb()
    {
        int r = AbstractDungeon.aiRng.random(maxPercent);
        int sum = 0;
        for (Pair<Integer, Class<? extends AbstractOrb>> kv : orbPercents) {
            sum += kv.getKey();
            if (r <= sum) {
                return makeOrb(kv.getValue());
            }
        }
        return null;
    }

    private void firstChannel()
    {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0f, 3.0f));

        AbstractDungeon.actionManager.addToBottom(new MonsterIncreaseMaxOrbAction(this, ORB_SLOTS));
        AbstractDungeon.actionManager.addToBottom(new MonsterChannelAction(this, new MonsterHypnotic(this)));
        for (int i=0; i<ORB_SLOTS-1; ++i) {
            AbstractDungeon.actionManager.addToBottom(new MonsterChannelAction(this, new MonsterLightning(this)));
        }
    }

    @Override
    public void usePreBattleAction()
    {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnfocusedPower(this)));
        firstChannel();
    }

    private boolean rerollOrb(ArrayList<String> orbTypesBeingChannelled, AbstractOrb orb)
    {
        if (orb == null) {
            return true;
        }

        // Check player's weak/vulnerable/frail
        int miasmaCount = 0;
        if (orb.ID.equals(MonsterMiasma.ORB_ID)) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power.ID.equals(WeakPower.POWER_ID)
                        || power.ID.equals(VulnerablePower.POWER_ID)
                        || power.ID.equals(FrailPower.POWER_ID)) {
                    ++miasmaCount;
                }
            }
        }

        if (miasmaCount >= 3) {
            return true;
        }
        if (orb.ID.equals(MonsterMiasma.ORB_ID)
                && (orbTypesBeingChannelled.contains(MonsterMiasma.ORB_ID) || hasOrbType(MonsterMiasma.ORB_ID))) {
            return true;
        }
        if (orb.ID.equals(MonsterWarp.ORB_ID)
                && (orbTypesBeingChannelled.contains(MonsterWarp.ORB_ID) || hasOrbType(MonsterWarp.ORB_ID))) {
            return true;
        }
        if (orb.ID.equals(MonsterDraining.ORB_ID)
                && (orbTypesBeingChannelled.contains(MonsterDraining.ORB_ID) || hasOrbType(MonsterDraining.ORB_ID))) {
            return true;
        }

        return false;
    }

    @Override
    public void takeTurn()
    {
        ++turnCount;

        boolean frostTurn = false;

        ArrayList<String> orbTypesBeingChannelled = new ArrayList<>();
        for (int i=0; i<numberToChannel; ++i) {
            AbstractOrb orb;
            if (isMinionDead() && !hasOrbType(MonsterWarp.ORB_ID) && !orbTypesBeingChannelled.contains(MonsterWarp.ORB_ID)) {
                orb = new MonsterWarp(this);
            } else if (!hasOrbType(MonsterFocusing.ORB_ID) && !orbTypesBeingChannelled.contains(MonsterFocusing.ORB_ID)) {
                orb = new MonsterFocusing(this);
            } else {
                if (turnCount >= 3 && orbTypesBeingChannelled.isEmpty()) {
                    turnCount = 0;
                    frostTurn = true;
                }

                if (frostTurn) {
                    orb = new MonsterFrost(this);
                } else {
                    do {
                        orb = getRandomOrb();
                    } while (rerollOrb(orbTypesBeingChannelled, orb));
                }
            }
            orbTypesBeingChannelled.add(orb.ID);
            if (orb instanceof MonsterWarp) {
                String summonId;
                if (summonCount == 0) {
                    summonId = summons.get(0);
                } else if (summonCount == 1) {
                    summonId = summons.get(1);
                } else {
                    summonId = summons.get(AbstractDungeon.aiRng.random(summons.size()-1));
                }
                AbstractMonster monster;
                if (summonId.equals(GrandMystic.ID)) {
                    monster = new GrandMystic();
                } else {
                    monster = MonsterHelper.getEncounter(summonId).monsters.get(0);
                }
                if (summonCount == 0) {
                    // Decrease Snecko's HP to 75%
                    monster.decreaseMaxHealth((int) (monster.maxHealth * 0.25f));
                    monster.name = "Baby " + monster.name;
                }
                ((MonsterWarp)orb).summon = monster;
                ++summonCount;
            }
            AbstractDungeon.actionManager.addToBottom(new MonsterChannelAction(this, orb));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i)
    {
        if (i == 0) {
            getMove(AbstractDungeon.aiRng.random(1, 99));
            return;
        } else if (i <= 33) {
            numberToChannel = ORB_SLOTS - 2;
        } else if (i <= 66) {
            numberToChannel = ORB_SLOTS - 1;
        } else if (i <= 99) {
            numberToChannel = ORB_SLOTS;
        }

        setMove((byte)0, OrbUsingMonster.Enums.CHANNEL_ORBS, numberToChannel);
    }

    private boolean isMinionDead()
    {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.equals(this)) {
                continue;
            }
            if (!m.isDeadOrEscaped()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);

        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            state.setAnimation(0, "Hit", false);
            state.setTimeScale(0.4f);
            state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void die()
    {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            //AbstractPlayer p = AbstractDungeon.player;
            //AbstractDungeon.effectList.add(new SpeechBubble(p.dialogX, p.dialogY, 3.0f, DIALOG[0], p.isPlayer));

            useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.deathTimer += 1.5F;
            super.die();

            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
                }
            }

            onBossVictoryLogic();
            onFinalBossVictoryLogic();
        }
    }
}
