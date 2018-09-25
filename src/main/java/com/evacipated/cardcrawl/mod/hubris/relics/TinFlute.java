package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return ConfigUtils.CONFIG_DIR + File.separator + "Hubris" + File.separator + "TinFlute" + ".autosave" + (Settings.isBeta ? "BETA" : "");
    }

    public static AbstractCard getSavedItem()
    {
        if (Gdx.files.absolute(getSavePath()).exists()) {
            Gson gson = new Gson();
            String savestr = loadSaveString(getSavePath());
            System.out.println(savestr);
            Save save = gson.fromJson(savestr, Save.class);
            if (save.tinFlute.size() == 2) {
                // Duct Tape
                List<AbstractCard> cards = new ArrayList<>();
                cards.add(CardLibrary.getCopy(save.tinFlute.get(0).id, save.tinFlute.get(0).upgrades, save.tinFlute.get(0).misc));
                cards.add(CardLibrary.getCopy(save.tinFlute.get(1).id, save.tinFlute.get(1).upgrades, save.tinFlute.get(1).misc));
                return new DuctTapeCard(cards);
            } else {
                return CardLibrary.getCopy(save.tinFlute.get(0).id, save.tinFlute.get(0).upgrades, save.tinFlute.get(0).misc);
            }
        }

        return null;
    }

    public static void deleteSave()
    {
        Gdx.files.absolute(getSavePath()).delete();
    }

    private static String loadSaveString(String filePath)
    {
        FileHandle file = Gdx.files.absolute(filePath);
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

            HashMap<Object, Object> params = new HashMap<>();
            if (card instanceof DuctTapeCard) {
                List<CardSave> cardSaves = ((DuctTapeCard) card).makeCardSaves();
                params.put("tinFlute", cardSaves);
            } else {
                CardSave cardSave = new CardSave(card.cardID, card.timesUpgraded, card.misc);
                params.put("tinFlute", cardSave);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String data = gson.toJson(params);
            try {
                Files.write(Paths.get(getSavePath()), data.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new TinFlute();
    }

    private class Save
    {
        public ArrayList<CardSave> tinFlute;

        public Save(ArrayList<CardSave> cardSave)
        {
            tinFlute = cardSave;
        }
    }
}
