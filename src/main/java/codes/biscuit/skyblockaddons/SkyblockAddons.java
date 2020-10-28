package codes.biscuit.skyblockaddons;

import codes.biscuit.skyblockaddons.commands.SkyblockAddonsCommand;
import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.config.PersistentValues;
import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.core.OnlineData;
import codes.biscuit.skyblockaddons.gui.IslandWarpGui;
import codes.biscuit.skyblockaddons.gui.SkyblockAddonsGui;
import codes.biscuit.skyblockaddons.listeners.NetworkListener;
import codes.biscuit.skyblockaddons.listeners.PlayerListener;
import codes.biscuit.skyblockaddons.listeners.RenderListener;
import codes.biscuit.skyblockaddons.misc.scheduler.Scheduler;
import codes.biscuit.skyblockaddons.misc.scheduler.NewScheduler;
import codes.biscuit.skyblockaddons.misc.scheduler.SkyblockRunnable;
import codes.biscuit.skyblockaddons.utils.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.client.PreInitializationEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.internal.addons.IAddon;

import java.io.File;
import java.util.*;

public class SkyblockAddons implements IAddon {

    public static final String MOD_ID = "skyblockaddons";
    public static final String MOD_NAME = "SkyblockAddons";
    public static String VERSION = "@VERSION@";

    private static SkyblockAddons instance;

    public static SkyblockAddons getInstance() {
        return instance;
    }

    private Logger logger;

    private ConfigValues configValues;
    private PersistentValues persistentValues;
    private PlayerListener playerListener;
    private RenderListener renderListener;
    private InventoryUtils inventoryUtils;
    private Utils utils;
    private OnlineData onlineData;
    private Scheduler scheduler;
    private NewScheduler newScheduler;

    private boolean devMode;
    private List<HyperiumBind> keyBindings = new LinkedList<>();

    private final Set<Integer> registeredFeatureIDs = new HashSet<>();

    @Override
    public void onLoad() {
        instance = this;
        EventBus.INSTANCE.register(this);
        logger = LogManager.getLogger();
        logger.info("Loading SBA!");

        playerListener = new PlayerListener();
        renderListener = new RenderListener();
        inventoryUtils = new InventoryUtils();
        utils = new Utils();
        scheduler = new Scheduler();
        newScheduler = new NewScheduler();
    }

    @InvokeEvent
    public void preInit(PreInitializationEvent e) {
        configValues = new ConfigValues(new File("sba.conf"));
        persistentValues = new PersistentValues(Hyperium.folder);
    }

    @InvokeEvent
    public void init(InitializationEvent e) {
        EventBus.INSTANCE.register(new NetworkListener());
        EventBus.INSTANCE.register(playerListener);
        EventBus.INSTANCE.register(renderListener);
        EventBus.INSTANCE.register(scheduler);
        EventBus.INSTANCE.register(newScheduler);

        Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(new SkyblockAddonsCommand());

        addKeybinds(new HyperiumBind("SBA Settings", Keyboard.KEY_NONE),
                new HyperiumBind("Edit SBA Locations", Keyboard.KEY_NONE),
                new HyperiumBind("Lock Slot with SBA", Keyboard.KEY_L),
                new HyperiumBind("Freeze Backpack Preview", Keyboard.KEY_F));
    }

    @InvokeEvent(priority = Priority.LOW)
    public void postInit(InitializationEvent e) {
        onlineData = new Gson().fromJson(new JsonReader(utils.getBufferedReader("data.json")), OnlineData.class);
        configValues.loadValues();
        persistentValues.loadValues();

        utils.pullOnlineData();
        scheduleMagmaBossCheck();

        for (Feature feature : Feature.values()) {
            if (feature.isGuiFeature()) feature.getSettings().add(EnumUtils.FeatureSetting.GUI_SCALE);
            if (feature.isColorFeature()) feature.getSettings().add(EnumUtils.FeatureSetting.COLOR);
        }

        if (configValues.isEnabled(Feature.FANCY_WARP_MENU)) {
            // Load in these textures so they don't lag the user loading them in later...
            for (IslandWarpGui.Island island : IslandWarpGui.Island.values()) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(island.getResourceLocation());
            }
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(SkyblockAddonsGui.LOGO);
        Minecraft.getMinecraft().getTextureManager().bindTexture(SkyblockAddonsGui.LOGO_GLOW);
    }

    private void scheduleMagmaBossCheck() {
        // Loop every 5s until the player is in game, where it will pull once.
        newScheduler.scheduleRepeatingTask(new SkyblockRunnable() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
                    utils.fetchMagmaBossEstimate();
                    cancel();
                }
            }
        }, 20*5, 20*5);
    }

    public HyperiumBind getOpenSettingsKey() {
        return keyBindings.get(0);
    }

    public HyperiumBind getOpenEditLocationsKey() {
        return keyBindings.get(1);
    }

    public HyperiumBind getLockSlotKey() {
        return keyBindings.get(2);
    }

    public HyperiumBind getFreezeBackpackKey() {
        return keyBindings.get(3);
    }

    public void addKeybinds(HyperiumBind... keybinds) {
        for (HyperiumBind b : keybinds) {
            Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(b);
            keyBindings.add(b);
        }
    }

    @Override
    public void onClose() {
        EventBus.INSTANCE.unregister(this);
    }

    public InventoryUtils getInventoryUtils() {
        return inventoryUtils;
    }

    public Logger getLogger() {
        return logger;
    }

    public NewScheduler getNewScheduler() {
        return newScheduler;
    }

    public PersistentValues getPersistentValues() {
        return persistentValues;
    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public OnlineData getOnlineData() {
        return onlineData;
    }

    public RenderListener getRenderListener() {
        return renderListener;
    }

    public List<HyperiumBind> getKeyBindings() {
        return keyBindings;
    }

    public Utils getUtils() {
        return utils;
    }

    public ConfigValues getConfigValues() {
        return configValues;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public Set<Integer> getRegisteredFeatureIDs() {
        return registeredFeatureIDs;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void setOnlineData(OnlineData onlineData) {
        this.onlineData = onlineData;
    }
}
