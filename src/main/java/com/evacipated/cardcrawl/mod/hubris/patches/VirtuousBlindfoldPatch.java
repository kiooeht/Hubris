package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.Teleporter;
import com.evacipated.cardcrawl.mod.hubris.relics.VirtuousBlindfold;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        cls="com.megacrit.cardcrawl.map.MapRoomNode",
        method="render"
)
public class VirtuousBlindfoldPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.map.MapRoomNode",
            method="render"
    )
    public static class Nested
    {
        public static void Raw(CtMethod ctMethodToPatch) throws CannotCompileException
        {
            String src = "if (!com.evacipated.cardcrawl.mod.hubris.patches.VirtuousBlindfoldPatch.doNodeRender($0)) { return; }";

            ctMethodToPatch.insertBefore(src);

            ctMethodToPatch.instrument(new ExprEditor()
            {
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.map.MapEdge") && m.getMethodName().equals("render")) {
                        m.replace("if (com.evacipated.cardcrawl.mod.hubris.patches.VirtuousBlindfoldPatch.doEdgeRender(this, $0)) { $_ = $proceed($$); }");
                    }
                }
            });
        }
    }

    @SuppressWarnings("unused")
    public static boolean doNodeRender(MapRoomNode node)
    {
        if (!AbstractDungeon.player.hasRelic(VirtuousBlindfold.ID)) {
            return true;
        }

        if (node.taken) {
            return true;
        }
        if (AbstractDungeon.firstRoomChosen && node.equals(AbstractDungeon.getCurrMapNode())) {
            return true;
        }
        if (AbstractDungeon.getCurrMapNode().isConnectedTo(node)) {
            return true;
        }
        if (AbstractDungeon.getCurrMapNode().wingedIsConnectedTo(node)) {
            return true;
        }
        for (MapRoomNode parent : node.getParents()) {
            if (parent.taken) {
                return true;
            }
        }
        if (node.y == 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean doEdgeRender(MapRoomNode node, MapEdge edge)
    {
        if (!AbstractDungeon.player.hasRelic(VirtuousBlindfold.ID)) {
            return true;
        }

        if (TeleporterPatch.pseudoTaken(edge)) {
            return true;
        }
        if (node.taken) {
            return true;
        }
        if (AbstractDungeon.firstRoomChosen && node.equals(AbstractDungeon.getCurrMapNode())) {
            return true;
        }
        if (AbstractDungeon.player.hasRelic(Teleporter.ID)
                && TeleporterPatch.isDirectlyConnectedTo(AbstractDungeon.getCurrMapNode(), node)) {
            return true;
        }
        return false;
    }
}
