package net.minecraft.src;

public class GuiVideoSettings extends GuiScreen {
   private GuiScreen parentGuiScreen;
   protected String screenTitle;
   private GameSettings guiGameSettings;
   private static EnumOptions[] videoOptions = new EnumOptions[]{EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AO_LEVEL, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.FOG_FANCY, EnumOptions.FOG_START, EnumOptions.MIPMAP_LEVEL, EnumOptions.MIPMAP_TYPE, EnumOptions.BETTER_GRASS, EnumOptions.BRIGHTNESS};
   private int lastMouseX = 0;
   private int lastMouseY = 0;
   private long mouseStillTime = 0L;

   public GuiVideoSettings(GuiScreen var1, GameSettings var2) {
      this.screenTitle = "Video Settings";
      this.parentGuiScreen = var1;
      this.guiGameSettings = var2;
   }

   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.screenTitle = var1.translateKey("options.videoTitle");
      int var2 = 0;

      for (EnumOptions var6 : videoOptions) {
         int var7 = this.width / 2 - 155 + var2 % 2 * 160;
         int var8 = this.height / 6 + 21 * (var2 / 2) - 10;
         if (!var6.getEnumFloat()) {
            this.controlList.add(new GuiSmallButton(var6.returnEnumOrdinal(), var7, var8, var6, this.guiGameSettings.getKeyBinding(var6)));
         } else {
            this.controlList.add(new GuiSlider(var6.returnEnumOrdinal(), var7, var8, var6, this.guiGameSettings.getKeyBinding(var6), this.guiGameSettings.getOptionFloatValue(var6)));
         }

         var2++;
      }

      int var9 = this.height / 6 + 21 * (var2 / 2) - 10;
      int var11 = 0;
      var11 = this.width / 2 - 155 + 0;
      this.controlList.add(new GuiSmallButton(100, var11, var9, "Animations..."));
      var11 = this.width / 2 - 155 + 160;
      this.controlList.add(new GuiSmallButton(101, var11, var9, "Details..."));
      var9 += 21;
      var11 = this.width / 2 - 155 + 0;
      this.controlList.add(new GuiSmallButton(102, var11, var9, "World..."));
      var11 = this.width / 2 - 155 + 160;
      this.controlList.add(new GuiSmallButton(103, var11, var9, "Other..."));
      this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, var1.translateKey("gui.done")));
   }

   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id < 100 && var1 instanceof GuiSmallButton) {
            this.guiGameSettings.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
            var1.displayString = this.guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(var1.id));
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentGuiScreen);
         }

         if (var1.id == 100) {
            this.mc.gameSettings.saveOptions();
            GuiAnimationSettingsOF var2 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var2);
         }

         if (var1.id == 101) {
            this.mc.gameSettings.saveOptions();
            GuiDetailSettingsOF var5 = new GuiDetailSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var5);
         }

         if (var1.id == 102) {
            this.mc.gameSettings.saveOptions();
            GuiWorldSettingsOF var6 = new GuiWorldSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var6);
         }

         if (var1.id == 103) {
            this.mc.gameSettings.saveOptions();
            GuiOtherSettingsOF var7 = new GuiOtherSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var7);
         }

         if (var1.id != EnumOptions.BRIGHTNESS.ordinal() && var1.id != EnumOptions.AO_LEVEL.ordinal()) {
            ScaledResolution var8 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int var3 = var8.getScaledWidth();
            int var4 = var8.getScaledHeight();
            this.setWorldAndResolution(this.mc, var3, var4);
         }
      }
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
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
      if (var1.equals("Graphics")) {
         return new String[]{"Visual quality", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,", "shadows and grass sides."};
      } else if (var1.equals("Render Distance")) {
         return new String[]{"Visible distance", "  Far - 256m (slower)", "  Normal - 128m", "  Short - 64m (faster)", "  Tiny - 32m (fastest)"};
      } else if (var1.equals("Smooth Lighting")) {
         return new String[]{"Smooth lighting", "  OFF - no smooth lighting (faster)", "  1% - light smooth lighting (slower)", "  100% - dark smooth lighting (slower)"};
      } else if (var1.equals("Performance")) {
         return new String[]{"FPS Limit", "  Max FPS - no limit (fastest)", "  Balanced - limit 120 FPS (slower)", "  Power saver - limit 40 FPS (slowest)", "  VSync - limit to monitor framerate (60, 30, 20)", "Balanced and Power saver decrease the FPS even if", "the limit value is not reached."};
      } else if (var1.equals("3D Anaglyph")) {
         return new String[]{"3D mode used with red-cyan 3D glasses."};
      } else if (var1.equals("View Bobbing")) {
         return new String[]{"More realistic movement.", "When using mipmaps set it to OFF for best results."};
      } else if (var1.equals("GUI Scale")) {
         return new String[]{"GUI Scale", "Smaller GUI might be faster"};
      } else if (var1.equals("Advanced OpenGL")) {
         return new String[]{"Detect and render only visible geometry", "  OFF - all geometry is rendered (slower)", "  Fast - ony visible geometry is rendered (fastest)", "  Fancy - conservative, avoids visual artifacts (faster)", "The option is available only if it is supported by the ", "graphic card."};
      } else if (var1.equals("Fog")) {
         return new String[]{"Fog type", "  Fast - faster fog", "  Fancy - slower fog, looks better", "The fancy fog is available only if it is supported by the ", "graphic card."};
      } else if (var1.equals("Fog Start")) {
         return new String[]{"Fog start", "  0.2 - the fog starts near the player", "  0.8 - the fog starts far from the player", "This option usually does not affect the performance."};
      } else if (var1.equals("Mipmap Level")) {
         return new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  OFF - no smoothing", "  1 - minimum smoothing", "  4 - maximum smoothing", "This option usually does not affect the performance."};
      } else if (var1.equals("Mipmap Type")) {
         return new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  Nearest - rough smoothing", "  Linear - fine smoothing", "This option usually does not affect the performance."};
      } else if (var1.equals("Better Grass")) {
         return new String[]{"Better Grass", "  OFF - default side grass texture, fastest", "  Fast - full side grass texture, slower", "  Fancy - dynamic side grass texture, slowest"};
      } else {
         return var1.equals("Brightness") ? new String[]{"Increases the brightness of darker objects", "  OFF - standard brightness", "  100% - maximum brightness for darker objects", "This options does not change the brightness of ", "fully black objects"} : null;
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
