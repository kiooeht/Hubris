package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class SavePatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue",
            method="loadSaveFile",
            paramtypes={"java.lang.String"}
    )
    public static class LoadGameByString
    {
        public static void Prefix(String filePath)
        {
            HubrisMod.loadData();
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue",
            method="save"
    )
    public static class SaveGame
    {
        public static void Prefix(SaveFile save)
        {
            HubrisMod.saveData();
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue",
            method="deleteSave"
    )
    public static class DeleteSave
    {
        public static void Prefix(AbstractPlayer.PlayerClass pClass)
        {
            HubrisMod.clearData();
        }
    }
}
