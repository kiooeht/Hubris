package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.NiceRug;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz=AbstractRoom.class,
        method="render"
)
public class NiceRugRender
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("render")) {
                    System.out.println(m.getLineNumber());
                    m.replace("{" +
                            NiceRug.class.getName() + ".renderRug($1, $0);" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }
}
