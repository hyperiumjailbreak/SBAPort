package codes.biscuit.skyblockaddons.tweaker;

import cc.hyperium.Hyperium;
import lombok.Getter;

public class PreTransformationChecks {

    @Getter
    private static boolean deobfuscated;
    @Getter
    private static boolean usingNotchMappings;

    static void runChecks() {
        // Environment Obfuscation checks
        deobfuscated = false;

        deobfuscated = Hyperium.INSTANCE.isDevEnv();

        usingNotchMappings = !deobfuscated;
    }
}
