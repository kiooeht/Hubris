package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.RaiseDeadAction;
import com.evacipated.cardcrawl.mod.hubris.powers.CursedLifePower;
import com.evacipated.cardcrawl.mod.hubris.vfx.scene.CursedTotemParticleEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CursedTotem extends AbstractMonster
{
    public static final String ID = "hubris:CursedTotem";
    public static final String NAME = "Cursed Totem";
    public static final String[] MOVES = {};
    public static final int HP = 40;
    private static final int CURSE_AMT = 7;
    private static final int STRENGTH_AMT = 3;

    private static final byte SUMMON = 0;
    private static final byte BUFF1 = 1;
    private static final byte BUFF2 = 2;
    private static final byte DEBUFF1 = 3;

    private static final float PARTICAL_EMIT_INTERVAL = 0.15f;

    private float particleTimer = 0.0f;

    private AbstractMonster[] minions = new AbstractMonster[3];

    private int numTurns = 0;

    public CursedTotem()
    {
        super(NAME, ID, HP, -8.0f, 10.0f, 230, 300, HubrisMod.assetPath("images/monsters/theCity/cursedTotem.png"), 100.0f, -30.0f);
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
    }

    @Override
    public void usePreBattleAction()
    {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CursedLifePower(this, CURSE_AMT), CURSE_AMT));
    }

    @Override
    public void takeTurn()
    {
        switch (nextMove) {
            case SUMMON:
                AbstractDungeon.actionManager.addToBottom(new RaiseDeadAction(minions));
                AbstractDungeon.actionManager.addToBottom(new RaiseDeadAction(minions));
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
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, STRENGTH_AMT), STRENGTH_AMT));
                    }
                }
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num)
    {
        if (numAliveMinions() == 0) {
            setMove("Raise Dead", SUMMON, Intent.UNKNOWN);
        } else {
            setMove(BUFF1, Intent.BUFF);
        }

        ++numTurns;
    }

    private int numAliveMinions()
    {
        int count = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && m != this && !m.isDeadOrEscaped()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public void update()
    {
        super.update();

        // TODO black particles
        particleTimer -= Gdx.graphics.getDeltaTime();
        if (particleTimer < 0) {
            particleTimer = PARTICAL_EMIT_INTERVAL;
            AbstractDungeon.topLevelEffectsQueue.add(new CursedTotemParticleEffect(hb.x + 50.0f * Settings.scale, hb.y + 240.0f * Settings.scale));
            AbstractDungeon.topLevelEffectsQueue.add(new CursedTotemParticleEffect(hb.x + hb.width - 22.0f * Settings.scale, hb.y + 230.0f * Settings.scale));
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
    }
}
