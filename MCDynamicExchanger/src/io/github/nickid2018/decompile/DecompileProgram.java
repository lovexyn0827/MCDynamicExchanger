package io.github.nickid2018.decompile;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.ProgramMain;

public class DecompileProgram {

    public static void decompileSimple(CommandResult result) {
        ProgramMain.logger = new DefaultConsoleLogger();
        String decompiler = result.getStringOrDefault("--backend", "cfr");
        Decompiler instance = Decompiler.getInstance(decompiler);
        if (instance == null)
            return;
        instance.doDecompileSimple(result);
    }
}
