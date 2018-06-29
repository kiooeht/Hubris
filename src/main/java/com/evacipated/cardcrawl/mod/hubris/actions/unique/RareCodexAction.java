package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RareCodexAction extends AbstractGameAction
{
    private boolean retrieveCard = false;

    public RareCodexAction()
    {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.codexOpen();
            // Fix offered cards to be rare
            ArrayList<AbstractCard> derp = new ArrayList<>();
            while (derp.size() != 3) {
                boolean dupe = false;
                AbstractCard tmp = AbstractDungeon.srcRareCardPool.getRandomCard(AbstractDungeon.cardRandomRng);
                for (AbstractCard c : derp) {
                    if (c.cardID.equals(tmp.cardID)) {
                        dupe = true;
                        break;
                    }
                }
                if (!dupe) {
                    derp.add(tmp.makeCopy());
                }
            }
            AbstractDungeon.cardRewardScreen.rewardGroup = derp;
            try {
                Method placeCards = CardRewardScreen.class.getDeclaredMethod("placeCards", float.class, float.class);
                placeCards.setAccessible(true);
                placeCards.invoke(AbstractDungeon.cardRewardScreen, Settings.WIDTH / 2.0f, Settings.HEIGHT * 0.45f);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            tickDuration();
            return;
        }

        if (!retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.codexCard != null) {
                AbstractCard codexCard = AbstractDungeon.cardRewardScreen.codexCard.makeStatEquivalentCopy();
                codexCard.current_x= -1000.0f * Settings.scale;
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(codexCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                AbstractDungeon.cardRewardScreen.codexCard = null;
            }
            retrieveCard = true;
        }
        tickDuration();
    }
}
