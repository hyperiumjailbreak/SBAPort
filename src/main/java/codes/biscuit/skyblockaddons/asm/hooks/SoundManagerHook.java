package codes.biscuit.skyblockaddons.asm.hooks;

import codes.biscuit.skyblockaddons.Reflector;
import codes.biscuit.skyblockaddons.SkyblockAddons;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;

public class SoundManagerHook {

    public static float getNormalizedVolume(SoundManager soundManager, ISound sound, SoundPoolEntry entry, SoundCategory category) {
        SkyblockAddons main = SkyblockAddons.getInstance();
        if (main != null && main.getUtils() != null && main.getUtils().isPlayingSound()) {
            return 1;
        } else {
            Object o = Reflector.invoke(SoundManager.class, soundManager, "getNormalizedVolume", new Class[]{ISound.class, SoundPoolEntry.class, SoundCategory.class}, new Object[]{sound, entry, category});
            if (o == null) {
                System.err.println("AAAHHHHH WTF");
            }
            return (float) o;
        }
    }
}
