package net.minecraft.src;

public class GuiOtherSettingsOF extends GuiScreen {
   private GuiScreen prevScreen;
   protected String title;
   private GameSettings settings;
   private static EnumOptions[] enumOptions = new EnumOptions[]{EnumOptions.SMOOTH_FPS, EnumOptions.SMOOTH_INPUT, EnumOptions.AUTOSAVE_TICKS, EnumOptions.FAST_DEBUG_INFO};
   private int lastMouseX = 0;
   private int lastMouseY = 0;
   private long mouseStillTime = 0L;

   public GuiOtherSettingsOF(GuiScreen var1, GameSettings var2) {
      this.title = "Other Settings";
      this.prevScreen = var1;
      this.settings = var2;
   }

   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      int var2 = 0;

      for (EnumOptions var6 : enumOptions) {
         int var7 = this.width / 2 - 155 + var2 % 2 * 160;
         int var8 = this.height / 6 + 21 * (var2 / 2) - 10;
         if (!var6.getEnumFloat()) {
            this.controlList.add(new GuiSmallButton(var6.returnEnumOrdinal(), var7, var8, var6, this.settings.getKeyBinding(var6)));
         } else {
            this.controlList.add(new GuiSlider(var6.returnEnumOrdinal(), var7, var8, var6, this.settings.getKeyBinding(var6), this.settings.getOptionFloatValue(var6)));
         }

         var2++;
      }

      this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, var1.translateKey("gui.done")));
   }

   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id < 100 && var1 instanceof GuiSmallButton) {
            this.settings.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
            var1.displayString = this.settings.getKeyBinding(EnumOptions.getEnumOptions(var1.id));
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.prevScreen);
         }

         if (var1.id != EnumOptions.CLOUD_HEIGHT.ordinal()) {
            ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int var3 = var2.getScaledWidth();
            int var4 = var2.getScaledHeight();
            this.setWorldAndResolution(this.mc, var3, var4);
         }
      }
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
      if (Math.abs(var1 - this.lastMouseX) <= 5 && Math.abs(var2 - this.lastMouseY) <= 5) {
         int var4 = 700;
         if (System.currentTimeMillis() >= this.mouseStillTime + var4) {
            int var5 = this.width / 2 - 150;
            int var6 = this.height / 6 - 5;
            if (var2 <= var6 + 98) {
               var6 += 105;
            }

            int var7 = var5 + 150 + 150;
            int var8 = var6 + 84 + 10;
            GuiButton var9 = this.getSelectedButton(var1, var2);
            if (var9 != null) {
               String var10 = this.getButtonName(var9.displayString);
               String[] var11 = this.getTooltipLines(var10);
               if (var11 == null) {
                  return;
               }

               this.drawGradientRect(var5, var6, var7, var8, -536870912, -536870912);

               for (int var12 = 0; var12 < var11.length; var12++) {
                  String var13 = var11[var12];
                  this.fontRenderer.drawStringWithShadow(var13, var5 + 5, var6 + 5 + var12 * 11, 14540253);
               }
            }
         }
      } else {
         this.lastMouseX = var1;
         this.lastMouseY = var2;
         this.mouseStillTime = System.currentTimeMillis();
      }
   }

   private String[] getTooltipLines(String var1) {
      if (var1.equals("Smooth FPS")) {
         return new String[]{"Stabilizes FPS by flushing the graphic driver buffers", "  OFF - no stabilization, FPS may fluctuate", "  ON - FPS stabilization", "This option is graphic driver dependant and its effect", "is not always visible"};
      } else if (var1.equals("Smooth Input")) {
         return new String[]{"Fixes stuck keys, slow input response and sound lag", "  OFF - no fix for stuck keys", "  ON - fixes stuck keys", "This option adds a small delay (1ms) to the game loop", "which fixes the stuck keys, slow input and sound lag."};
      } else if (var1.equals("Autosave")) {
         return new String[]{"Autosave interval", "Default autosave interval (2s) is NOT RECOMMENDED.", "Autosave causes the famous Lag Spike of Death."};
      } else {
         return var1.equals("Fast Debug Info") ? new String[]{"Fast Debug Info", " OFF - default debug info screen, slower", " ON - debug info screen without lagometer, faster", "Removes the lagometer from the debug screen (F3)."} : null;
      }
   }

   private String getButtonName(String var1) {
      int var2 = var1.indexOf(58);
      return var2 < 0 ? var1 : var1.substring(0, var2);
   }

   private GuiButton getSelectedButton(int var1, int var2) {
      for (int var3 = 0; var3 < this.controlList.size(); var3++) {
         GuiButton var4 = (GuiButton)this.controlList.get(var3);
         boolean var5 = var1 >= var4.xPosition && var2 >= var4.yPosition && var1 < var4.xPosition + var4.width && var2 < var4.yPosition + var4.height;
         if (var5) {
            return var4;
         }
      }

      return null;
   }
}
