package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Pocketwatch extends AbstractRelic
{
    public static final String ID = "hubris:Pocketwatch";
    private static final int TIME_LIMIT_M = 5; // minutes
    private static final int TIME_LIMIT_S = TIME_LIMIT_M * 60;

    private float startTime = -1;
    private float waitTimer = 0;

    public Pocketwatch()
    {
        super(ID, "pocketwatch.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[2] + DESCRIPTIONS[0] + TIME_LIMIT_M + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        flash();
        startTime = CardCrawlGame.playtime;
        setCounter(TIME_LIMIT_S);
    }

    @Override
    public void update()
    {
        super.update();
        if (counter > 0) {
            setCounter(TIME_LIMIT_S - (int)(CardCrawlGame.playtime - startTime));
        } else if (counter == 0) {
            if (waitTimer <= 0) {
                if (!AbstractDungeon.player.isDead) {
                    flash();
                    AbstractDungeon.actionManager.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 999));
                    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    waitTimer = 1.0f;
                }
            } else {
                waitTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }

    @Override
    public void onVictory()
    {
        setCounter(-1);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Pocketwatch();
    }
}
