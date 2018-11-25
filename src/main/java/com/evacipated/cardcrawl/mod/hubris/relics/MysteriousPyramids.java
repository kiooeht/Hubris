package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PyramidsField;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AutoplayCardAction;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class MysteriousPyramids extends HubrisRelic implements CustomBottleRelic, CustomSavable<List<Integer>>
{
    public static final String ID = "hubris:MysteriousPyramids";
    private static final int COUNT = 2;
    private boolean cardSelected = true;
    private List<AbstractCard> cards = new ArrayList<>(COUNT);
    private boolean recursive = false;

    public MysteriousPyramids()
    {
        super(ID, "pyramids.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public Predicate<AbstractCard> isOnCard()
    {
        return PyramidsField.inPyramids::get;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    private void setDescriptionAfterLoading()
    {
        description = DESCRIPTIONS[2] + FontHelper.colorString(cards.get(0).name, "y")
                + DESCRIPTIONS[3] + FontHelper.colorString(cards.get(1).name, "y") + DESCRIPTIONS[4];
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public List<Integer> onSave()
    {
        List<Integer> cardIndices = new ArrayList<>();
        for (int i=0; i<COUNT; ++i) {
            cardIndices.add(AbstractDungeon.player.masterDeck.group.indexOf(cards.get(i)));
        }
        return cardIndices;
    }

    @Override
    public void onLoad(List<Integer> cardIndices)
    {
        if (cardIndices == null) {
            return;
        }
        for (int i=0; i<COUNT; ++i) {
            if (cardIndices.size() > i && AbstractDungeon.player.masterDeck.size() > cardIndices.get(i)) {
                AbstractCard c = AbstractDungeon.player.masterDeck.group.get(cardIndices.get(i));
                if (c == null) {
                    cards.clear();
                    return;
                }
                PyramidsField.inPyramids.set(c, true);
                cards.add(c);
            } else {
                cards.clear();
                return;
            }
        }
        setDescriptionAfterLoading();
    }

    public static void clear()
    {
    }

    @Override
    public void onCardDraw(AbstractCard c)
    {
        if (recursive) {
            return;
        }

        if (PyramidsField.inPyramids.get(c)) {
            boolean fullHandDialog = false;
            for (Iterator<AbstractCard> it = AbstractDungeon.player.drawPile.group.iterator(); it.hasNext();) {
                AbstractCard card = it.next();
                if (PyramidsField.inPyramids.get(card)) {
                    flash();
                    it.remove();
                    if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        if (AutoplayField.autoplay.get(card)) {
                            AbstractDungeon.actionManager.addToBottom(new AutoplayCardAction(card, AbstractDungeon.player.hand));
                        }
                        card.triggerWhenDrawn();
                        AbstractDungeon.player.drawPile.moveToHand(card, AbstractDungeon.player.drawPile);
                        recursive = true;
                        for (AbstractRelic r : AbstractDungeon.player.relics) {
                            r.onCardDraw(card);
                        }
                    } else {
                        if (!fullHandDialog) {
                            AbstractDungeon.player.createHandIsFullDialog();
                            fullHandDialog = true;
                        }
                        AbstractDungeon.player.drawPile.moveToDiscardPile(card);
                    }
                }
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
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

        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck),
                COUNT, DESCRIPTIONS[1] + name + ".",
                false, false, false, false);
    }

    @Override
    public void update()
    {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == COUNT) {
            cardSelected = true;

            cards.clear();
            for (int i=0; i<COUNT; ++i) {
                cards.add(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
                PyramidsField.inPyramids.set(cards.get(i), true);
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            setDescriptionAfterLoading();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new MysteriousPyramids();
    }
}
