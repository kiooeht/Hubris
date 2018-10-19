package com.evacipated.cardcrawl.mod.hubris.monsters;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.powers.CursedLifePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CursedTotem extends AbstractMonster
{
    public static final String ID = "hubris:CursedTotem";
    public static final String NAME = "Cursed Totem";
    public static final String[] MOVES = {};
    public static final int HP = 200;
    private static final int CURSE_AMT = 7;

    private static final byte SUMMON = 0;

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
                AbstractDungeon.actionManager.addToBottom(new SummonGremlinAction(minions));
                AbstractDungeon.actionManager.addToBottom(new SummonGremlinAction(minions));
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
            setMove((byte) 1, Intent.BUFF);
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
