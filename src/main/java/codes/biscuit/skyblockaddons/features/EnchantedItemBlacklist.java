package codes.biscuit.skyblockaddons.features;

import codes.biscuit.skyblockaddons.core.Feature;

public enum EnchantedItemBlacklist {

    ENCHANTED_LAVA_BUCKET("ENCHANTED_LAVA_BUCKET", false, Feature.AVOID_PLACING_ENCHANTED_ITEMS, true),
    ENCHANTED_DIAMOND_BLOCK("ENCHANTED_DIAMOND_BLOCK", false, Feature.AVOID_PLACING_ENCHANTED_ITEMS,true),
    ENCHANTED_SNOW("ENCHANTED_SNOW_BLOCK", false, Feature.AVOID_PLACING_ENCHANTED_ITEMS, true),
    ENCHANTED_STRING("ENCHANTED_STRING", false, Feature.AVOID_PLACING_ENCHANTED_ITEMS, true),
    ENCHANTED_WOOL("ENCHANTED_WOOL", true, Feature.AVOID_PLACING_ENCHANTED_ITEMS, true),
    WEIRD_TUBA("WEIRD_TUBA", false, Feature.AVOID_PLACING_ENCHANTED_ITEMS, true),
    EMBER_ROD("EMBER_ROD", true, Feature.DISABLE_EMBER_ROD, false);

    private String itemId;
    private boolean onlyOnIsland;
    private Feature feature;
    private boolean onlyBlockPlacement;

    /**
     * Adds a new entry to the enchanted item blacklist.
     *
     * @param itemId the Skyblock Item ID of the item
     * @param onlyOnIsland block the item on the player's island only if true
     * @param feature the feature that controls the blocking of this item
     * @param onlyBlockPlacement stop the item from being placed, but not from being used.
     */
    EnchantedItemBlacklist(String itemId, boolean onlyOnIsland, Feature feature, boolean onlyBlockPlacement) {
        this.itemId = itemId;
        this.onlyOnIsland = onlyOnIsland;
        this.feature = feature;
        this.onlyBlockPlacement = onlyBlockPlacement;
    }

    /**
     * Checks if the given item from this blacklist is a bucket.
     * This is used to handle buckets separately from other blocks and items.
     *
     * @return true if the item is a bucket, false otherwise
     * @see codes.biscuit.skyblockaddons.listeners.PlayerListener#onBucketEvent(FillBucketEvent)
     */
    private boolean isBucket() {
        return itemId.contains("BUCKET");
    }
}