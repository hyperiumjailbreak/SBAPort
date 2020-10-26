package codes.biscuit.skyblockaddons.misc;

import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import codes.biscuit.skyblockaddons.core.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SkyblockKeyBinding {

    @Setter private HyperiumBind keyBinding;
    private String name;
    private int defaultKey;
    private Message message;

    public SkyblockKeyBinding(String name, int defaultKey, Message message) {
        this.name = name;
        this.defaultKey = defaultKey;
        this.message = message;
    }
}
