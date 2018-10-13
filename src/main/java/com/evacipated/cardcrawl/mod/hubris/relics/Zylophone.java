package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.ZylophoneField;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Zylophone extends HubrisRelic
{
    public static final String ID = "hubris:Zylophone";
    private boolean cardSelected = true;
    private AbstractCard card = null;

    private static final String CONFIG_KEY = "zylophone_";

    public Zylophone()
    {
        super(ID, "zylophone.png", RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    public static void save(SpireConfig config)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Zylophone.ID)) {
            int count = 0;
            for (int i=0; i<AbstractDungeon.player.masterDeck.size(); ++i) {
                AbstractCard c = AbstractDungeon.player.masterDeck.group.get(i);
                if (ZylophoneField.costsX.get(c)) {
                    config.setInt(CONFIG_KEY + count, i);
                    ++count;
                }
            }
        } else {
            config.remove(CONFIG_KEY);
        }
    }

    public static void load(SpireConfig config)
    {
        if (AbstractDungeon.player.hasRelic(ID)) {
            Zylophone relic = (Zylophone) AbstractDungeon.player.getRelic(ID);
            int count = 0;
            while (config.has(CONFIG_KEY + count)) {
                int cardIndex = config.getInt(CONFIG_KEY + count);

                if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
                    AbstractCard card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
                    if (card != null) {
                        if (relic.card == null) {
                            relic.card = card;
                        }
                        ZylophoneField.costsX.set(card, true);
                    }
                }

                ++count;
            }
            relic.setDescriptionAfterLoading();
        }
    }

    public static void clear()
    {
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

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof DuctTapeCard) {
                continue;
            }
            if (c.cost == 0 || c.cost == 1) {
                tmp.addToTop(c);
            }
        }
        AbstractDungeon.gridSelectScreen.open(tmp, 1, DESCRIPTIONS[1] + name + ".",
                false, false, false, false);
    }

    @Override
    public void onUnequip()
    {
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                ZylophoneField.costsX.set(cardInDeck, false);
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
            ZylophoneField.costsX.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }

    private void setDescriptionAfterLoading()
    {
        description = FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Zylophone();
    }
}
