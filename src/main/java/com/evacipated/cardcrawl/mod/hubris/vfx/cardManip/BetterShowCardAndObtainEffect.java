package com.evacipated.cardcrawl.mod.hubris.vfx.cardManip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class BetterShowCardAndObtainEffect extends AbstractGameEffect
{
    private static final float EFFECT_DUR = 2.0f;
    private static final float FAST_DUR = 0.5f;
    private static final float PADDING = 30.0f * Settings.scale;

    private AbstractCard card;
    private boolean converge;

    public BetterShowCardAndObtainEffect(AbstractCard card, float x, float y, boolean convergeCards)
    {
        if ((card.color == AbstractCard.CardColor.CURSE || card.type == AbstractCard.CardType.CURSE)
                && AbstractDungeon.player.hasRelic(Omamori.ID)
                && AbstractDungeon.player.getRelic(Omamori.ID).counter != 0) {
            ((Omamori)AbstractDungeon.player.getRelic(Omamori.ID)).use();
            duration = 0.0F;
            isDone = true;
        }
        UnlockTracker.markCardAsSeen(card.cardID);

        CardHelper.obtain(card.cardID, card.rarity, card.color);

        converge = convergeCards;
        this.card = card;
        if (Settings.FAST_MODE) {
            duration = FAST_DUR;
        } else {
            duration = EFFECT_DUR;
        }
        identifySpawnLocation(x, y);
        AbstractDungeon.effectsQueue.add(new CardPoofEffect(card.target_x, card.target_y));
        card.drawScale = 0.01f;
        card.targetDrawScale = 1.0f;
    }

    public BetterShowCardAndObtainEffect(AbstractCard card, float x, float y)
    {
        this(card, x, y, true);
    }

    private void identifySpawnLocation(float x, float y)
    {
        int effectCount = 0;
        for (AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof BetterShowCardAndObtainEffect) {
                effectCount++;
            }
        }
        card.current_x = x;
        card.current_y = y;
        if (converge) {
            card.target_x = (Settings.WIDTH * 0.5F);
            card.target_y = (Settings.HEIGHT * 0.5F);
            switch (effectCount) {
                case 0:
                    card.target_x = (Settings.WIDTH * 0.5F);
                    break;
                case 1:
                    card.target_x = (Settings.WIDTH * 0.5F - PADDING - AbstractCard.IMG_WIDTH);
                    break;
                case 2:
                    card.target_x = (Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH);
                    break;
                case 3:
                    card.target_x = (Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH) * 2.0F);
                    break;
                case 4:
                    card.target_x = (Settings.WIDTH * 0.5F + (PADDING + AbstractCard.IMG_WIDTH) * 2.0F);
                    break;
                default:
                    card.target_x = MathUtils.random(Settings.WIDTH * 0.1F, Settings.WIDTH * 0.9F);
                    card.target_y = MathUtils.random(Settings.HEIGHT * 0.2F, Settings.HEIGHT * 0.8F);
                    break;
            }
        } else {
            card.target_x = card.current_x;
            card.target_y = card.current_y;
        }
    }

    public void update()
    {
        duration -= Gdx.graphics.getDeltaTime();
        card.update();
        if (duration < 0.0F)
        {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onObtainCard(card);
            }
            isDone = true;
            card.shrink();
            AbstractDungeon.getCurrRoom().souls.obtain(card, true);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMasterDeckChange();
            }
        }
    }

    public void render(SpriteBatch sb)
    {
        if (!isDone) {
            card.render(sb);
        }
    }

    @Override
    public void dispose()
    {

    }
}
