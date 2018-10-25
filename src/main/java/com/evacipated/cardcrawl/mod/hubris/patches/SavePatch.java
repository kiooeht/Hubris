package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class SavePatch
{
    @SpirePatch(
            clz=SaveAndContinue.class,
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
            clz=SaveAndContinue.class,
            method="deleteSave"
    )
    public static class DeleteSave
    {
        public static void Prefix(AbstractPlayer p)
        {
            HubrisMod.clearData();
        }
    }
}
