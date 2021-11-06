package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import java.io.IOException;
import java.net.IDN;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen
{
    private final GuiScreen parentScreen;
    private final ServerData serverData;
    private GuiTextField textFieldServerAddress;
    private GuiTextField textFieldServerName;
    private GuiButton buttonResourcePack;
    private final Predicate<String> addressFilter = new Predicate<String>()
    {
        public boolean apply(@Nullable String p_apply_1_)
        {
            if (StringUtils.isNullOrEmpty(p_apply_1_))
            {
                return true;
            }
            else
            {
                String[] astring = p_apply_1_.split(":");

                if (astring.length == 0)
                {
                    return true;
                }
                else
                {
                    try
                    {
                        String s = IDN.toASCII(astring[0]);
                        return true;
                    }
                    catch (IllegalArgumentException var4)
                    {
                        return false;
                    }
                }
            }
        }
    };

    public GuiScreenAddServer(GuiScreen parentScreenIn, ServerData serverDataIn)
    {
        this.parentScreen = parentScreenIn;
        this.serverData = serverDataIn;
    }

    public void updateScreen()
    {
        this.textFieldServerName.tick();
        this.textFieldServerAddress.tick();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, I18n.format("addServer.add")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, I18n.format("gui.cancel")));
        this.buttonResourcePack = this.addButton(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, I18n.format("addServer.resourcePack") + ": " + this.serverData.getResourceMode().getMotd().getFormattedText()));
        this.textFieldServerName = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
        this.textFieldServerName.setFocused2(true);
        this.textFieldServerName.setText(this.serverData.serverName);
        this.textFieldServerAddress = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
        this.textFieldServerAddress.setMaxStringLength(128);
        this.textFieldServerAddress.setText(this.serverData.serverIP);
        this.textFieldServerAddress.setValidator(this.addressFilter);
        (this.buttonList.get(0)).enabled = !this.textFieldServerAddress.getText().isEmpty() && this.textFieldServerAddress.getText().split(":").length > 0 && !this.textFieldServerName.getText().isEmpty();
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {
                this.serverData.setResourceMode(ServerData.ServerResourceMode.values()[(this.serverData.getResourceMode().ordinal() + 1) % ServerData.ServerResourceMode.values().length]);
                this.buttonResourcePack.displayString = I18n.format("addServer.resourcePack") + ": " + this.serverData.getResourceMode().getMotd().getFormattedText();
            }
            else if (button.id == 1)
            {
                this.parentScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0)
            {
                this.serverData.serverName = this.textFieldServerName.getText();
                this.serverData.serverIP = this.textFieldServerAddress.getText();
                this.parentScreen.confirmClicked(true, 0);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.textFieldServerName.textboxKeyTyped(typedChar, keyCode);
        this.textFieldServerAddress.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15)
        {
            this.textFieldServerName.setFocused2(!this.textFieldServerName.isFocused());
            this.textFieldServerAddress.setFocused2(!this.textFieldServerAddress.isFocused());
        }

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }

        (this.buttonList.get(0)).enabled = !this.textFieldServerAddress.getText().isEmpty() && this.textFieldServerAddress.getText().split(":").length > 0 && !this.textFieldServerName.getText().isEmpty();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldServerAddress.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldServerName.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("addServer.title"), this.width / 2, 17, 16777215);
        this.drawString(this.fontRenderer, I18n.format("addServer.enterName"), this.width / 2 - 100, 53, 10526880);
        this.drawString(this.fontRenderer, I18n.format("addServer.enterIp"), this.width / 2 - 100, 94, 10526880);
        this.textFieldServerName.drawTextBox();
        this.textFieldServerAddress.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
