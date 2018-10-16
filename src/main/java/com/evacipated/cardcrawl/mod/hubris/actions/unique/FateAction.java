package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.hubris.patches.DiscoveryColorPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class FateAction extends AbstractGameAction
{
    private boolean retrieveCard = false;
    private AbstractCard.CardColor cardColor;
    private String prohibit;
    private boolean upgraded;

    public FateAction(AbstractCard.CardColor color)
    {
        this(color, 3);
    }

    public FateAction(AbstractCard.CardColor color, int amount)
    {
        this(null, color, amount);
    }

    public FateAction(String prohibit, AbstractCard.CardColor color, int amount)
    {
        this(prohibit, color, false, amount);
    }

    public FateAction(String prohibit, AbstractCard.CardColor color, boolean upgraded, int amount)
    {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        cardColor = color;
        this.amount = amount;
        this.prohibit = prohibit;
        this.upgraded = upgraded;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            DiscoveryColorPatch.lookingForColor = cardColor;
            DiscoveryColorPatch.lookingForCount = amount;
            DiscoveryColorPatch.lookingForProhibit = prohibit;
            DiscoveryColorPatch.lookingForUpgraded = upgraded;

            AbstractDungeon.cardRewardScreen.discoveryOpen();
            tickDuration();
            return;
        }
        if (!retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                disCard.current_x = -1000.0f * Settings.scale;
                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            retrieveCard = true;
        }
        tickDuration();
    }
}
