package com.evacipated.cardcrawl.mod.hubris.vfx.combat;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.List;

public class ShowRollResult extends AbstractGameEffect
{
    public static final String ID = "hubris:Icosahedron";
    public static final String IMG = HubrisMod.assetPath("images/cards/icosahedron.png");
    private static final float EFFECT_DUR = 2.0f;
    private int roll;
    private AbstractCard rollCard;
    private List<AbstractGameAction> actions;

    public ShowRollResult(int roll)
    {
        this(roll, null);
    }

    public ShowRollResult(int roll, List<AbstractGameAction> actions)
    {
        this.roll = roll;
        this.actions = actions;
        rollCard = new Roll(roll);
        duration = EFFECT_DUR;
        startingDuration = EFFECT_DUR;

        rollCard.target_x = rollCard.current_x = Settings.WIDTH * 0.5f;
        rollCard.target_y = rollCard.current_y = Settings.HEIGHT * 0.5f;
        rollCard.drawScale = 0.01f;
        rollCard.targetDrawScale = 1.0f;
    }

    @Override
    public void update()
    {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.6f) {
            rollCard.fadingOut = true;

            if (actions != null) {
                for (AbstractGameAction action : actions) {
                    AbstractDungeon.actionManager.addToBottom(action);
                }
            }
        }
        rollCard.update();
        if (duration < 0.0f) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!isDone) {
            rollCard.render(sb);
        }
    }

    public static class Roll extends CustomCard
    {
        private int roll;

        Roll(int roll)
        {
            super(makeID(roll), String.valueOf(roll), IMG, -2, getDescription(roll), CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

            this.roll = roll;
        }

        static String makeID(int roll)
        {
            return ID + "_" + String.valueOf(roll);
        }

        public static String getDescription(int roll)
        {
            return CardCrawlGame.languagePack.getCardStrings(makeID(roll)).DESCRIPTION;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
        {

        }

        @Override
        public boolean canUpgrade()
        {
            return false;
        }

        @Override
        public void upgrade()
        {

        }

        @Override
        public AbstractCard makeCopy()
        {
            return new Roll(roll);
        }
    }
}
