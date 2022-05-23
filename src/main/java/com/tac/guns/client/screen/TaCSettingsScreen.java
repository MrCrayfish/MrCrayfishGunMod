package com.tac.guns.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.settings.GunOptions;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.stream.Stream;


@OnlyIn(Dist.CLIENT)
public class TaCSettingsScreen extends SettingsScreen {
    private OptionsRowList optionsList;
    private static final AbstractOption[] OPTIONS = new AbstractOption[]{};

    public TaCSettingsScreen(Screen p_i225929_1_, GameSettings p_i225929_2_) {
        super(p_i225929_1_, p_i225929_2_, new TranslationTextComponent("tac.options.screenFormat"));
    }

    protected void init() {
        this.optionsList = new OptionsRowList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        if (InputMappings.func_224790_a()) {
            this.optionsList.addOptions(Stream.concat(Arrays.stream(OPTIONS), Stream.of()).toArray((p_223702_0_) ->
            {
                return new AbstractOption[p_223702_0_];
            }));
            this.optionsList.addOption(GunOptions.SIZE_FIREMODE_POS);
            this.optionsList.addOption(GunOptions.FIREMODE_EXIST);
            this.optionsList.addOption(GunOptions.X_FIREMODE_POS);
            this.optionsList.addOption(GunOptions.Y_FIREMODE_POS);

            this.optionsList.addOption(GunOptions.AMMOCOUNTER_EXIST);
            this.optionsList.addOption(GunOptions.SIZE_AMMOCOUNTER_POS);
            this.optionsList.addOption(GunOptions.X_AMMOCOUNTER_POS);
            this.optionsList.addOption(GunOptions.Y_AMMOCOUNTER_POS);

            this.optionsList.addOption(GunOptions.WeaponIcon_EXIST);
            this.optionsList.addOption(GunOptions.SIZE_Icon_POS);
            this.optionsList.addOption(GunOptions.X_Icon_POS);
            this.optionsList.addOption(GunOptions.Y_Icon_POS);

            this.optionsList.addOption(GunOptions.ReloadBar_EXIST);
            this.optionsList.addOption(GunOptions.SIZE_ReloadBar_POS);
            this.optionsList.addOption(GunOptions.X_ReloadBar_POS);
            this.optionsList.addOption(GunOptions.Y_ReloadBar_POS);
        } else {
            this.optionsList.addOptions(OPTIONS);
        }

        this.children.add(this.optionsList);
        this.addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, DialogTexts.GUI_DONE, (p_223703_1_) -> {
            this.gameSettings.saveOptions();
            this.minecraft.displayGuiScreen(this.parentScreen);
        }));
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 5, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}

