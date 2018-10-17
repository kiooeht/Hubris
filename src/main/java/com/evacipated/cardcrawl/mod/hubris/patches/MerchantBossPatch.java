package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.mod.hubris.monsters.MerchantMonster;
import com.evacipated.cardcrawl.mod.hubris.relics.NiceRug;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

@SpirePatch(
        clz=MonsterRoomBoss.class,
        method="onPlayerEntry"
)
public class MerchantBossPatch
{
    @SpireInsertPatch(
            rloc=1
    )
    public static void Insert(MonsterRoomBoss __instance)
    {
        // TODO: Balance Merchant in boss fights
        if (true) { return; }
        if (AbstractDungeon.id.equals(TheBeyond.ID) && AbstractDungeon.player.hasRelic(NiceRug.ID)) {
            MerchantMonster merchant = new MerchantMonster(true);
            System.out.println(AbstractDungeon.lastCombatMetricKey);
            switch (AbstractDungeon.lastCombatMetricKey) {
                case MonsterHelper.DONU_DECA_ENC:
                    merchant.drawX += 50.0f * Settings.scale;
                    merchant.drawY += 60.0f * Settings.scale;
                    __instance.monsters.addMonster(1, merchant);
                    break;
                case MonsterHelper.AWAKENED_ENC:
                    merchant.drawX += 500.0f * Settings.scale;
                    merchant.drawY += 40.0f * Settings.scale;
                    __instance.monsters.addMonster(merchant);
                    break;
                case GrandSnecko.ID:
                    merchant.drawX -= 70.0f * Settings.scale;
                    merchant.drawY += 66.0f * Settings.scale;
                    __instance.monsters.addMonster(0, merchant);
                    break;
                default:
                    merchant.drawX += 500.0f * Settings.scale;
                    __instance.monsters.addMonster(merchant);
                    break;
            }
        }
    }
}
