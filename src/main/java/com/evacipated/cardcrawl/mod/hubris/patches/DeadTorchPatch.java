package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.DeadTorch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.DeadBranch;
import com.megacrit.cardcrawl.vfx.scene.InteractableTorchEffect;

@SpirePatch(
        cls="com.megacrit.cardcrawl.vfx.scene.InteractableTorchEffect",
        method="update"
)
public class DeadTorchPatch
{
    @SpireInsertPatch(
            rloc=2,
            localvars={"activated"}
    )
    public static void Insert(InteractableTorchEffect __instance, boolean activated)
    {
        if (activated && AbstractDungeon.player != null) {
            int index = -1;
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(DeadBranch.ID)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                RelicLibrary.getRelic(DeadTorch.ID).instantObtain(AbstractDungeon.player, index, true);
            }
        }
    }
}
