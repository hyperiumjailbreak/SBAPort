package codes.biscuit.skyblockaddons.listeners;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.events.SkyblockJoinedEvent;
import codes.biscuit.skyblockaddons.events.SkyblockLeftEvent;
import org.apache.logging.log4j.Logger;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerLeaveEvent;

public class NetworkListener {
    private final SkyblockAddons main;
    private final Logger logger;

    public NetworkListener() {
        main = SkyblockAddons.getInstance();
        logger = main.getLogger();
    }

    @InvokeEvent
    public void onLeave(ServerLeaveEvent event) {
        // Leave Skyblock when the player disconnects
        EventBus.INSTANCE.post(new SkyblockLeftEvent());
    }

    @InvokeEvent
    public void onSkyblockJoined(SkyblockJoinedEvent event) {
        logger.info("Joined Skyblock");
        main.getUtils().setOnSkyblock(true);
    }

    @InvokeEvent
    public void onSkyblockLeft(SkyblockLeftEvent event) {
        logger.info("Left Skyblock");
        main.getUtils().setOnSkyblock(false);
    }
}