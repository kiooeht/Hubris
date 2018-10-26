package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.hubris.cards.DisguiseKitOption;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisguiseKit extends HubrisRelic implements CustomSavable<String>
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

    @Override
    public String onSave()
    {
        return chosenClass.name();
    }

    @Override
    public void onLoad(String playerClass)
    {
        if (!chooseClass(AbstractPlayer.PlayerClass.valueOf(playerClass))) {
            System.out.println("OH GOD WTF!!");
        }
    }

    private String chosenClassName()
    {
        AbstractPlayer character = BaseMod.findCharacter(chosenClass);
        return BaseMod.colorString(character.getLocalizedCharacterName(), "#" + character.getCardRenderColor().toString());
    }

    @Override
    public void onEquip()
    {
        flash();

        pickCard = true;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractPlayer character : CardCrawlGame.characterManager.getAllCharacters()) {
            if (character.chosenClass != AbstractDungeon.player.chosenClass) {
                group.addToTop(new DisguiseKitOption(character.chosenClass));
            }
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, "", false);
    }

    @Override
    public void update()
    {
        super.update();

        if (pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            pickCard = false;
            DisguiseKitOption selected = (DisguiseKitOption) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            chooseClass(selected.chosenClass);
        }
    }

    private boolean chooseClass(AbstractPlayer.PlayerClass chosenClass)
    {
        this.chosenClass = chosenClass;
        if (chosenClass == null || CardCrawlGame.dungeon == null) {
            return false;
        }
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();

        ArrayList<AbstractCard> tmpPool = new ArrayList<>();
        switch (chosenClass) {
            case IRONCLAD:
                CardLibrary.addRedCards(tmpPool);
                break;
            case THE_SILENT:
                CardLibrary.addGreenCards(tmpPool);
                break;
            case DEFECT:
                CardLibrary.addBlueCards(tmpPool);
                break;
            default:
                AbstractCard.CardColor color = BaseMod.findCharacter(chosenClass).getCardColor();
                AbstractCard card;
                for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
                    card = c.getValue();
                    if (card.color.equals(color) && card.rarity != AbstractCard.CardRarity.BASIC
                            && (!UnlockTracker.isCardLocked(c.getKey()) || Settings.isDailyRun)) {
                        tmpPool.add(card);
                    }
                }
                break;
        }

        chosenPools = new HashMap<>();
        for (AbstractCard c : tmpPool) {
            if (!chosenPools.containsKey(c.rarity)) {
                chosenPools.put(c.rarity, new CardGroup(CardGroup.CardGroupType.CARD_POOL));
            }
            chosenPools.get(c.rarity).addToTop(c);
        }

        return true;
    }

    public AbstractCard getRewardCard(AbstractCard.CardRarity rarity)
    {
        if (chosenPools == null) {
            return null;
        }
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
