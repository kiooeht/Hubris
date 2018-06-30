package com.evacipated.cardcrawl.mod.hubris.patches.falselife;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import javassist.*;

@SpirePatch(
        cls="com.megacrit.cardcrawl.core.AbstractCreature",
        method="ctor"
)
public class AddTempHP
{
    public static void Raw(CtBehavior ctMethodToPatch)
    {
        AddField(ctMethodToPatch.getDeclaringClass());
    }

    static void AddField(CtClass ctClass)
    {
        try {
            ctClass.getDeclaredField("temporaryHealth");
        } catch (NotFoundException e) {
            try {
                CtField f = CtField.make("public int temporaryHealth = 0;", ctClass);
                ctClass.addField(f);
            } catch (CannotCompileException e1) {
                e1.printStackTrace();
            }
        }
    }
}
