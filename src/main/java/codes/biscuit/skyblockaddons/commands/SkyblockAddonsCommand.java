package codes.biscuit.skyblockaddons.commands;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.utils.EnumUtils;
import codes.biscuit.skyblockaddons.utils.DevUtils;
import codes.biscuit.skyblockaddons.utils.ColorCode;
import codes.biscuit.skyblockaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Scoreboard;

import cc.hyperium.commands.BaseCommand;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SkyblockAddonsCommand implements BaseCommand {
    private final SkyblockAddons main = SkyblockAddons.getInstance();

    @Override
    public String getName() {
        return "skyblockaddons";
    }

    /**
     * Returns the aliases of this command
     */
    public List<String> getCommandAliases()
    {
        return Arrays.asList("sba", "sbaddons");
    }

    @Override
    public String getUsage() {
        return "skyblockaddons";
    }

    /**
     * Opens the main gui, or locations gui if they type /sba edit
     */
    @Override
    public void onExecute(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("edit")) {
                main.getUtils().setFadingIn(false);
                main.getRenderListener().setGuiToOpen(EnumUtils.GUIType.EDIT_LOCATIONS, 0, null);

            } else if (args[0].equalsIgnoreCase("dev") || args[0].equalsIgnoreCase("nbt")) {
                main.setDevMode(!main.isDevMode());

                if (main.isDevMode()) {
                    main.getUtils().sendMessage(ColorCode.GREEN + "Developer mode enabled! TIP: Press right ctrl to copy nbt!");
                } else {
                    main.getUtils().sendMessage(ColorCode.RED + "Developer mode disabled!");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                Integer number = null;
                if (args.length >= 3) {
                    try {
                        number = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ex) {
                        main.getUtils().sendErrorMessage("Invalid number to set!");
                        return;
                    }
                }
                if (number == null) {
                    main.getUtils().sendErrorMessage("Invalid number to set!");
                    return;
                }

                if (args[1].equalsIgnoreCase("totalzealots") || args[1].equalsIgnoreCase("total")) {
                    main.getPersistentValues().setTotalKills(number);
                    main.getUtils().sendMessage("Set total zealot count to: "+number+"!");
                } else if (args[1].equalsIgnoreCase("zealots")) {
                    main.getPersistentValues().setKills(number);
                    main.getUtils().sendMessage("Set current zealot count to: "+number+"!");
                } else if (args[1].equalsIgnoreCase("eyes")) {
                    main.getPersistentValues().setSummoningEyeCount(number);
                    main.getUtils().sendMessage("Set total summoning eye count to: "+number+"!");
                } else {
                    main.getUtils().sendErrorMessage("Invalid selection! Please choose 'zealots', 'totalzealots/total', 'eyes'");
                }
            }  else if (args[0].equalsIgnoreCase("folder")) {
                try {
                    Desktop.getDesktop().open(Utils.getSBAFolder());
                } catch (IOException e) {
                    e.printStackTrace();
                    main.getUtils().sendErrorMessage("Failed to open mods folder.");
                }
            }  else if (args[0].equalsIgnoreCase("warp")) {
                main.getRenderListener().setGuiToOpen(EnumUtils.GUIType.WARP);
            } else if (main.isDevMode()) {
                if (args[0].equalsIgnoreCase("sidebar")) {
                    Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();

                    if (args.length < 2) {
                        DevUtils.copyScoreboardSideBar(scoreboard);

                    } else if (args.length == 2 && args[1].equalsIgnoreCase("formatted")) {
                        DevUtils.copyScoreboardSidebar(scoreboard, false);

                    } else {
                        main.getUtils().sendMessage(getUsage(), false);
                    }
                } else if (args[0].equalsIgnoreCase("brand")) {
                    main.getUtils().sendMessage(DevUtils.getServerBrand(Minecraft.getMinecraft()));

                } else {
                    main.getUtils().sendMessage(getUsage(), false);
                }
            } else {
                main.getUtils().sendMessage(getUsage(), false);
            }
        } else {
            // If there's no arguments given, open the main GUI
            main.getUtils().setFadingIn(true);
            main.getRenderListener().setGuiToOpen(EnumUtils.GUIType.MAIN, 1, EnumUtils.GuiTab.MAIN);
        }
    }
}
