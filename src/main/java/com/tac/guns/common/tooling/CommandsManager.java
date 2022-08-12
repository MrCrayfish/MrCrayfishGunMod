package com.tac.guns.common.tooling;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.tac.guns.client.handler.command.GuiEditor;
import com.tac.guns.client.handler.command.GunEditor;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.client.handler.command.ScopeEditor;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 * CommandsManager is used in order to have more control over more elements over such as GUI editing, along with soon to come other development commands using
 * the "tdev" prefix, along with the custom and addable "catagory" system
 */
public class CommandsManager
{
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        CommandsManager.register(commandDispatcher);
    }
    public CommandsManager() {}
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> tacCommander
                = Commands.literal("tdev")
                .requires((commandSource) -> commandSource.hasPermissionLevel(1))

                .then(Commands.literal("setCat").then(Commands.argument("catToSet", MessageArgument.message())
                        .executes(commandContext -> {
                            ITextComponent iTextComponent = MessageArgument.getMessage(commandContext, "catToSet");
                            int responseCat;
                            try
                            {
                                responseCat = Integer.parseInt(iTextComponent.getUnformattedComponentText());
                                manageCat(commandContext, responseCat);
                            }
                            catch (Exception e)
                            {
                                sendMessage(commandContext, "INPUT WAS NOT AN INTEGER, PLEASE REFER TO DOCUMENTATION");
                            }
                            return 1;
                        }))
                )

                .then(Commands.literal("getCat").executes(commandContext -> {
                    sendMessage(commandContext,  "Current Catagory: "+CommandsHandler.get().getCatCurrentIndex());
                    return 1;
                }))

                .then(Commands.literal("tac_weapons_category")
                        .then(Commands.literal("setMode")
                                .then(Commands.argument("modeName", MessageArgument.message())//
                                .executes(commandContext ->
                                {

                                    CommandsHandler.get().setCatCurrentIndex(1);
                                    GunEditor.TaCWeaponDevModes mode;
                                    try {
                                        mode = GunEditor.TaCWeaponDevModes.valueOf(MessageArgument.getMessage(commandContext, "modeName").getUnformattedComponentText());
                                    }
                                    catch (Exception e)
                                    {
                                        sendMessage(commandContext,  "FAILED TO SET MODE, PLEASE CHOOSE FROM THE FOLLOWING\n" + GunEditor.formattedModeContext());
                                        return 1;
                                    }
                                    GunEditor.get().setMode(mode);
                                    sendMessage(commandContext,  "Set Category to a TaC Default \n Weapon's ---"+mode.getTagName()+"--- | Editing mode");
                                    GunEditor.get().setResetMode(true);
                                    return 1;
                                }))
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(1);
                                    sendMessage(commandContext,  "FAILED TO SET MODE, PLEASE CHOOSE FROM THE FOLLOWING\n" + GunEditor.formattedModeContext());
                                    return 1;
                                }))
                        .then(Commands.literal("export")
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(1);
                                    try {
                                        GunEditor.get().exportData();
                                    }
                                    catch (Exception e)
                                    {
                                        sendMessage(commandContext,  "FAILED TO EXPORT WEAPON DATA, THIS IS A GENERIC ERROR, REPORT WITH A LOG PLEASE.\n" + GunEditor.formattedModeContext());
                                        return 1;
                                    }
                                    sendMessage(commandContext,  "Exported all weapon data, with adjustments!");
                                    return 1;
                                }))
                        .then(Commands.literal("reset")
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(1);
                                    GunEditor.get().resetData();
                                    sendMessage(commandContext,  "Modified data within this current category, for this weapon has been reset!");
                                    return 1;
                                }))
                    .executes(commandContext -> {
                        CommandsHandler.get().setCatCurrentIndex(1);
                        sendMessage(commandContext,  "Set Category to a TaC Default | Weapon |");
                        return 1;
                }))
                .then(Commands.literal("tac_gui_category")
                        .then(Commands.literal("setElementIndex")
                                .then(Commands.argument("modeName", MessageArgument.message())//
                                        .executes(commandContext ->
                                        {
                                            CommandsHandler.get().setCatCurrentIndex(3);
                                            ITextComponent iTextComponent = MessageArgument.getMessage(commandContext, "modeName");
                                            try
                                            {
                                                GuiEditor.get().currElement = Integer.parseInt(iTextComponent.getUnformattedComponentText());
                                            }
                                            catch (Exception e)
                                            {
                                                sendMessage(commandContext, "INPUT WAS NOT AN INTEGER, PLEASE REFER TO DOCUMENTATION");
                                            }

                                            sendMessage(commandContext,  "TaC GUI Editor \n Element: " + GuiEditor.get().currElement);
                                            return 1;
                                        }))
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(3);
                                    sendMessage(commandContext,  "FAILED TO SET ELEMENT, THE PARAM IS A NUMBER, INTEGERS ONLY!");
                                    return 1;
                                }))
                        .then(Commands.literal("export")
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(3);
                                    try {
                                        GuiEditor.get().exportData();
                                    }
                                    catch (Exception e)
                                    {
                                        sendMessage(commandContext,  "FAILED TO EXPORT WEAPON DATA, THIS IS A GENERIC ERROR, REPORT WITH A LOG PLEASE.\n" + GunEditor.formattedModeContext());
                                        return 1;
                                    }
                                    sendMessage(commandContext,  "Exported all weapon data, with adjustments!");
                                    return 1;
                                }))
                        .executes(commandContext -> {
                            CommandsHandler.get().setCatCurrentIndex(3);
                            sendMessage(commandContext,  "Use \"/tdev tac_gui_category setElementIndex\" to select your adjusting element");
                            return 1;
                        }))
                .then(Commands.literal("tac_obj_category")
                        .then(Commands.literal("setElementIndex")
                                .then(Commands.argument("modeName", MessageArgument.message())//
                                        .executes(commandContext ->
                                        {
                                            CommandsHandler.get().setCatCurrentIndex(4);
                                            ITextComponent iTextComponent = MessageArgument.getMessage(commandContext, "modeName");
                                            try
                                            {
                                                ObjectRenderEditor.get().currElement = Integer.parseInt(iTextComponent.getUnformattedComponentText());
                                            }
                                            catch (Exception e)
                                            {
                                                sendMessage(commandContext, "INPUT WAS NOT AN INTEGER, PLEASE REFER TO DOCUMENTATION");
                                            }

                                            sendMessage(commandContext,  "TaC OBJ_RENDER Editor \n Element: " + GuiEditor.get().currElement);
                                            return 1;
                                        }))
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(3);
                                    sendMessage(commandContext,  "FAILED TO SET ELEMENT, THE PARAM IS A NUMBER, INTEGERS ONLY!");
                                    return 1;
                                }))
                        .then(Commands.literal("export")
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(4);
                                    try {
                                        ObjectRenderEditor.get().exportData();
                                    }
                                    catch (Exception e)
                                    {
                                        sendMessage(commandContext,  "FAILED TO EXPORT RENDER DATA, THIS IS A GENERIC ERROR, REPORT WITH A LOG PLEASE.\n" + GunEditor.formattedModeContext());
                                        return 1;
                                    }
                                    sendMessage(commandContext,  "Exported all weapon data, with adjustments!");
                                    return 1;
                                }))
                        .executes(commandContext -> {
                            CommandsHandler.get().setCatCurrentIndex(4);
                            sendMessage(commandContext,  "Use \"/tdev tac_obj_category setElementIndex\" to select your adjusting element");
                            return 1;
                        }))
                .then(Commands.literal("tac_scope_category")
                        .then(Commands.literal("export")
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(2);
                                    try {
                                        ScopeEditor.get().exportData();
                                    }
                                    catch (Exception e)
                                    {
                                        sendMessage(commandContext,  "FAILED TO EXPORT SCOPE DATA, THIS IS A GENERIC ERROR, REPORT WITH A LOG PLEASE.");
                                        return 1;
                                    }
                                    sendMessage(commandContext,  "Exported all scope data, with adjustments!");
                                    return 1;
                                }))
                        .then(Commands.literal("reset")
                                .executes(commandContext ->
                                {
                                    CommandsHandler.get().setCatCurrentIndex(2);
                                    ScopeEditor.get().resetData();
                                    sendMessage(commandContext,  "All modified data for this scope has been reset!");
                                    return 1;
                                }))
                        .executes(commandContext -> {
                            CommandsHandler.get().setCatCurrentIndex(2);
                            sendMessage(commandContext,  "Set Category to a TaC Default | Scope |");
                            return 1;
                        }))
                /*.then(Commands.literal("tac_colorBench_category")
                        .executes(commandContext -> {
                            CommandsHandler.get().setCatCurrentIndex(2);
                            sendMessage(commandContext,  "Set Category to a TaC Default | ColorBench | Editing mode.");
                            return 1;
                }))*/
                .executes(commandContext -> sendMessage(commandContext, "The tdev command palette is for making on the fly adjustments within the current game instance, to as many pieces as developed, this REQUIRES code dev to utilize correctly. NEVER release with code listening to this class!"));  // blank: didn't match a literal or the custommessage argument

        dispatcher.register(tacCommander);
    }

    static int manageCat(CommandContext<CommandSource> commandContext, int cat) throws CommandSyntaxException
    {
        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                commandContext.getSource().getDisplayName(), new StringTextComponent("Added Cat: " + cat));

        if(CommandsHandler.get().catInGlobal(cat)) {
            CommandsHandler.get().setCatCurrentIndex(cat);
        }
        else
            finalText = new TranslationTextComponent("chat.type.announcement",
                    commandContext.getSource().getDisplayName(), new StringTextComponent("Cat: "+cat+" Doesn't exist yet."));

        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.CHAT, entity.getUniqueID());
            //func_232641_a_ is sendMessage()
        } else {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.SYSTEM, Util.DUMMY_UUID);
        }
        return 1;
    }

    static int sendMessage(CommandContext<CommandSource> commandContext, String message) throws CommandSyntaxException {
        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                commandContext.getSource().getDisplayName(), new StringTextComponent(message));

        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.CHAT, entity.getUniqueID());
            //func_232641_a_ is sendMessage()
        } else {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.SYSTEM, Util.DUMMY_UUID);
        }
        return 1;
    }
}
