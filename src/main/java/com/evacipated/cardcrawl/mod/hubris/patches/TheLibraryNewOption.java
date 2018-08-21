package com.evacipated.cardcrawl.mod.hubris.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.thecity.TheLibrary;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtBehavior;

import java.util.*;
import java.util.function.Predicate;

public class TheLibraryNewOption
{
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("hubris:TheLibraryExtra");
    private static Map<AbstractCard.CardRarity, CardGroup> pools = null;

    private static AbstractCard getCard(AbstractCard.CardRarity rarity)
    {
        if (pools == null) {
            return null;
        }
        if (pools.containsKey(rarity)) {
            return pools.get(rarity).getRandomCard(true);
        } else {
            return null;
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.events.thecity.TheLibrary",
            method=SpirePatch.CONSTRUCTOR
    )
    public static class ChangeOptionText
    {
        public static void Postfix(TheLibrary __instance)
        {
            if (AbstractDungeon.eventRng.random(3) == 0) {
                __instance.imageEventText.updateDialogOption(0, eventStrings.OPTIONS[0]);

                ArrayList<AbstractCard> tmpPool = new ArrayList<>();

                AbstractCard.CardColor playerCardColor;
                if (BaseMod.isBaseGameCharacter(AbstractDungeon.player.chosenClass)) {
                    switch (AbstractDungeon.player.chosenClass) {
                        case IRONCLAD:
                            playerCardColor = AbstractCard.CardColor.RED;
                            break;
                        case THE_SILENT:
                            playerCardColor = AbstractCard.CardColor.GREEN;
                            break;
                        case DEFECT:
                            playerCardColor = AbstractCard.CardColor.BLUE;
                            break;
                        default:
                            playerCardColor = null;
                    }
                } else {
                    playerCardColor = BaseMod.getColor(AbstractDungeon.player.chosenClass);
                }

                AbstractCard card;
                for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
                    card = c.getValue();
                    if (card.color == playerCardColor
                            || card.color == AbstractCard.CardColor.COLORLESS
                            || card.color == AbstractCard.CardColor.CURSE
                            || card.type == AbstractCard.CardType.CURSE
                            || card.type == AbstractCard.CardType.STATUS) {
                        continue;
                    }
                    if (card.rarity != AbstractCard.CardRarity.BASIC) {
                        tmpPool.add(card);
                    }
                }

                pools = new HashMap<>();
                for (AbstractCard c : tmpPool) {
                    if (!pools.containsKey(c.rarity)) {
                        pools.put(c.rarity, new CardGroup(CardGroup.CardGroupType.CARD_POOL));
                    }
                    pools.get(c.rarity).addToTop(c);
                }
            } else {
                pools = null;
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.events.thecity.TheLibrary",
            method="buttonEffect"
    )
    public static class ChangeOptionEffect
    {
        private static <T> boolean any(Iterable<T> collection, Predicate<T> predicate)
        {
            for (T obj : collection) {
                if (predicate.test(obj)) {
                    return true;
                }
            }
            return false;
        }

        @SpireInsertPatch
        public static SpireReturn Insert(TheLibrary __instance, int buttonPressed)
        {
            if (pools != null) {
                __instance.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);

                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (int i = 0; i < 20; ++i) {
                    final AbstractCard card = getCard(AbstractDungeon.rollRarity());
                    if (card != null
                            && card.color != BaseMod.getColor(AbstractDungeon.player.chosenClass)
                            && !any(group.group, c -> c.cardID.equals(card.cardID))) {
                        AbstractCard copy = card.makeCopy();
                        if ((copy.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasRelic(MoltenEgg2.ID))
                                || (copy.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasRelic(ToxicEgg2.ID))
                                || (copy.type == AbstractCard.CardType.POWER && AbstractDungeon.player.hasRelic(FrozenEgg2.ID))) {
                            copy.upgrade();
                        }
                        group.addToBottom(copy);
                        UnlockTracker.markCardAsSeen(card.cardID);
                    } else {
                        --i;
                    }
                }
                AbstractDungeon.gridSelectScreen.open(group, 1, TheLibrary.OPTIONS[4], false);

                pools = null;
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.NewExprMatcher("com.megacrit.cardcrawl.cards.CardGroup");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
