package com.mrcrayfish.guns.debug;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public interface IEditorMenu
{
    Component getLabel();

    void getWidgets(List<Pair<Component, Supplier<AbstractWidget>>> widgets);
}
