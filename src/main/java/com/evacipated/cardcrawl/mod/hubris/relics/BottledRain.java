package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.BottleRainField;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BottledRain extends AbstractRelic
{
    public static final String ID = "hubris:BottledRain";
    private boolean cardSelected = true;
    private AbstractCard card = null;

    private static final String CONFIG_KEY = "bottledRain";
    private static int cardIndex = -1;

    public BottledRain()
    {
        super(ID, "bottledRain.png", RelicTier.UNCOMMON, LandingSound.CLINK);

        if (card == null && cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                BottleRainField.inBottleRain.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    public AbstractCard getCard()
    {
        return card.makeCopy();
    }

    public static void save(SpireConfig config)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BottledRain.ID)) {
            BottledRain relic = (BottledRain) AbstractDungeon.player.getRelic(ID);
            config.setInt(CONFIG_KEY, AbstractDungeon.player.masterDeck.group.indexOf(relic.card));
        } else {
            config.remove(CONFIG_KEY);
        }
    }

    public static void load(SpireConfig config)
    {
        if (config.has(CONFIG_KEY)) {
            cardIndex = config.getInt(CONFIG_KEY);
        } else {
            cardIndex = -1;
        }
    }

    @Override
    public void onEquip()
    {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, DESCRIPTIONS[1] + name + ".",
                false, false, false, false);
    }

    @Override
    public void onUnequip()
    {
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                BottleRainField.inBottleRain.set(cardInDeck, false);
            }
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            BottleRainField.inBottleRain.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }

    public void setDescriptionAfterLoading()
    {
        description = FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BottledRain();
    }
}
