package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.AsyncSaver;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;

import java.util.HashMap;

public class TinFlute extends HubrisRelic
{
    public static final String ID = "hubris:TinFlute";
    private boolean chosen = true;

    public TinFlute()
    {
        super(ID, "tinFlute.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    private static String getSavePath()
    {
        return SaveAndContinue.SAVE_PATH + "hubris" + ".autosave" + (Settings.isBeta ? "BETA" : "");
    }

    public static AbstractCard getSavedItem()
    {
        if (Gdx.files.local(getSavePath()).exists()) {
            Gson gson = new Gson();
            String savestr = loadSaveString(getSavePath());
            Save save = gson.fromJson(savestr, Save.class);
            return CardLibrary.getCopy(save.tinFlute.id, save.tinFlute.upgrades, save.tinFlute.misc);
        }

        return null;
    }

    public static void deleteSave()
    {
        Gdx.files.local(getSavePath()).delete();
    }

    private static String loadSaveString(String filePath)
    {
        FileHandle file = Gdx.files.local(filePath);
        String data = file.readString();
        if (SaveFileObfuscator.isObfuscated(data)) {
            return SaveFileObfuscator.decode(data, "key");
        }
        return data;
    }

    public void onDeath()
    {
        chosen = false;
        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, "Choose one to take to your next life.", false);
    }

    @Override
    public void update()
    {
        super.update();

        if (!chosen && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            chosen = true;

            CardSave cardSave = new CardSave(card.cardID, card.timesUpgraded, card.misc);
            HashMap<Object, Object> params = new HashMap<>();

            params.put("tinFlute", cardSave);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String data = gson.toJson(params);
            AsyncSaver.save(getSavePath(), data);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new TinFlute();
    }

    private class Save
    {
        public CardSave tinFlute;

        public Save(CardSave cardSave)
        {
            tinFlute = cardSave;
        }
    }
}
