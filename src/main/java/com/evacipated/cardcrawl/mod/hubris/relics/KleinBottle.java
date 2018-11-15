package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.shop.BloodShopScreen;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import infinitespire.helpers.CardHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class KleinBottle extends HubrisRelic
{
    public static final String ID = "hubris:KleinBottle";
    private static final int OPTIONS = 3;

    public KleinBottle()
    {
        super(ID, "kleinBottle.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (int i=0; i<OPTIONS; ++i) {
            cards.add(CardHelper.getRandomBlackCard().makeCopy());
        }
        AbstractDungeon.CurrentScreen screen = AbstractDungeon.screen;
        AbstractDungeon.cardRewardScreen.open(cards, null, RewardItem.TEXT[4]);
        try {
            Field f = CardRewardScreen.class.getDeclaredField("skipButton");
            f.setAccessible(true);
            SkipCardButton skipButton = (SkipCardButton) f.get(AbstractDungeon.cardRewardScreen);
            skipButton.hideInstantly();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        AbstractDungeon.overlayMenu.cancelButton.hideInstantly();
        if (screen == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
            AbstractDungeon.previousScreen = screen;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new KleinBottle();
    }
}
