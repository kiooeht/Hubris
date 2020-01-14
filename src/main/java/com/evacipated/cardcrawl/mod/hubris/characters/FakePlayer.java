package com.evacipated.cardcrawl.mod.hubris.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.stats.CharStat;

import java.util.ArrayList;

public class FakePlayer extends AbstractPlayer
{
    public FakePlayer()
    {
        super("FAKE", PlayerClass.DEFECT);
    }

    @Override
    public String getPortraitImageName()
    {
        return null;
    }

    @Override
    public ArrayList<String> getStartingDeck()
    {
        return null;
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        return null;
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return null;
    }

    @Override
    public String getTitle(PlayerClass playerClass)
    {
        return null;
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return null;
    }

    @Override
    public Color getCardRenderColor()
    {
        return null;
    }

    @Override
    public String getAchievementKey()
    {
        return null;
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> arrayList)
    {
        return null;
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return null;
    }

    @Override
    public Color getCardTrailColor()
    {
        return null;
    }

    @Override
    public String getLeaderboardCharacterName()
    {
        return null;
    }

    @Override
    public Texture getEnergyImage()
    {
        return null;
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return 0;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return null;
    }

    @Override
    public void renderOrb(SpriteBatch spriteBatch, boolean b, float v, float v1)
    {

    }

    @Override
    public void updateOrb(int energyCount)
    {

    }

    @Override
    public String getSaveFilePath()
    {
        return null;
    }

    @Override
    public Prefs getPrefs()
    {
        return null;
    }

    @Override
    public void loadPrefs()
    {

    }

    @Override
    public CharStat getCharStat()
    {
        return null;
    }

    @Override
    public int getUnlockedCardCount()
    {
        return 0;
    }

    @Override
    public int getSeenCardCount()
    {
        return 0;
    }

    @Override
    public int getCardCount()
    {
        return 0;
    }

    @Override
    public boolean saveFileExists()
    {
        return false;
    }

    @Override
    public String getWinStreakKey()
    {
        return null;
    }

    @Override
    public String getLeaderboardWinStreakKey()
    {
        return null;
    }

    @Override
    public void renderStatScreen(SpriteBatch spriteBatch, float v, float v1)
    {

    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {

    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return null;
    }

    @Override
    public Texture getCustomModeCharacterButtonImage()
    {
        return null;
    }

    @Override
    public CharacterStrings getCharacterString()
    {
        return null;
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return null;
    }

    @Override
    public void refreshCharStat()
    {

    }

    @Override
    public AbstractPlayer newInstance()
    {
        return null;
    }

    @Override
    public TextureAtlas.AtlasRegion getOrb()
    {
        return null;
    }

    @Override
    public String getSpireHeartText()
    {
        return null;
    }

    @Override
    public Color getSlashAttackColor()
    {
        return null;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        return new AbstractGameAction.AttackEffect[0];
    }

    @Override
    public String getVampireText()
    {
        return null;
    }

    @Override
    public void initializeStarterDeck()
    {

    }

    @Override
    protected void initializeStarterRelics(PlayerClass chosenClass)
    {

    }
}
