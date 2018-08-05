package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.hubris.cards.DisguiseKitOption;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisguiseKit extends AbstractRelic
{
    public static final String ID = "hubris:DisguiseKit";
    public AbstractPlayer.PlayerClass chosenClass = null;
    private boolean pickCard = false;
    private Map<AbstractCard.CardRarity, CardGroup> chosenPools = null;

    public DisguiseKit()
    {
        super(ID, "disguiseKit.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (chosenClass == null) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0] + chosenClassName() + DESCRIPTIONS[2];
        }
    }

    private String chosenClassName()
    {
        return "#b" + String.join(" #b", AbstractPlayer.getTitle(chosenClass).split(" "));
    }

    @Override
    public void onEquip()
    {
        flash();

        pickCard = true;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractPlayer.PlayerClass pc : AbstractPlayer.PlayerClass.values()) {
            if (pc != AbstractDungeon.player.chosenClass) {
                group.addToTop(new DisguiseKitOption(pc));
            }
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, "", false);
    }

    @Override
    public void update()
    {
        super.update();

        if (pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            DisguiseKitOption selected = (DisguiseKitOption) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            chosenClass = selected.chosenClass;
            description = getUpdatedDescription();
            tips.clear();
            tips.add(new PowerTip(name, description));
            initializeTips();

            if (chosenClass == AbstractPlayer.PlayerClass.DEFECT && AbstractDungeon.player.masterMaxOrbs == 0) {
                AbstractDungeon.player.masterMaxOrbs = 1;
            }

            ArrayList<AbstractCard> tmpPool = new ArrayList<>();
            try {
                Method addRedCards = AbstractDungeon.class.getDeclaredMethod("addRedCards", ArrayList.class);
                addRedCards.setAccessible(true);
                Method addGreenCards = AbstractDungeon.class.getDeclaredMethod("addGreenCards", ArrayList.class);
                addGreenCards.setAccessible(true);
                Method addBlueCards = AbstractDungeon.class.getDeclaredMethod("addBlueCards", ArrayList.class);
                addBlueCards.setAccessible(true);

                switch (chosenClass) {
                    case IRONCLAD:
                        addRedCards.invoke(CardCrawlGame.dungeon, tmpPool);
                        break;
                    case THE_SILENT:
                        addGreenCards.invoke(CardCrawlGame.dungeon, tmpPool);
                        break;
                    case DEFECT:
                        addBlueCards.invoke(CardCrawlGame.dungeon, tmpPool);
                        break;
                    default:
                        String color = BaseMod.getColor(chosenClass.name());
                        AbstractCard card;
                        for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
                            card = c.getValue();
                            if (card.color.name().equals(color) && card.rarity != AbstractCard.CardRarity.BASIC
                            && (!UnlockTracker.isCardLocked(c.getKey()) || Settings.isDailyRun)) {
                                tmpPool.add(card);
                            }
                        }
                        break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            chosenPools = new HashMap<>();
            for (AbstractCard c : tmpPool) {
                if (!chosenPools.containsKey(c.rarity)) {
                    chosenPools.put(c.rarity, new CardGroup(CardGroup.CardGroupType.CARD_POOL));
                }
                chosenPools.get(c.rarity).addToTop(c);
            }
        }
    }

    public AbstractCard getRewardCard(AbstractCard.CardRarity rarity)
    {
        if (chosenPools.containsKey(rarity)) {
            return chosenPools.get(rarity).getRandomCard(true);
        } else {
            return null;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DisguiseKit();
    }
}
