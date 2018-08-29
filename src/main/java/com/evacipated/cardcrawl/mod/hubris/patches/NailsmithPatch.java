package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.OldNail;
import com.evacipated.cardcrawl.mod.hubris.rooms.NailsmithRoom;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EventRoom;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="generateMap"
)
public class NailsmithPatch
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void AddNailsmithRoom()
    {
        if (AbstractDungeon.id.equals(TheBeyond.ID) && AbstractDungeon.player.hasRelic(OldNail.ID)) {
            List<MapRoomNode> eventNodes = new ArrayList<>();
            for(List<MapRoomNode> rows : AbstractDungeon.map) {
                for (MapRoomNode node : rows) {
                    if (node.room instanceof EventRoom) {
                        eventNodes.add(node);
                    }
                }
            }
            int rand = AbstractDungeon.mapRng.random(eventNodes.size() - 1);
            eventNodes.get(rand).setRoom(new NailsmithRoom());
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MapGenerator.class, "toString");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
