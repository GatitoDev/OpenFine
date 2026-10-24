package net.minecraft.src;

public class GuiDetailSettingsOF extends GuiScreen {
   private GuiScreen prevScreen;
   protected String title;
   private GameSettings settings;
   private static EnumOptions[] enumOptions = new EnumOptions[]{EnumOptions.CLOUDS, EnumOptions.CLOUD_HEIGHT, EnumOptions.TREES, EnumOptions.GRASS, EnumOptions.WATER, EnumOptions.RAIN, EnumOptions.SKY, EnumOptions.STARS, EnumOptions.CLEAR_WATER};
   private int lastMouseX = 0;
   private int lastMouseY = 0;
   private long mouseStillTime = 0L;

   public GuiDetailSettingsOF(GuiScreen var1, GameSettings var2) {
      this.title = "Detail Settings";
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
      if (var1.equals("Clouds")) {
         return new String[]{"Clouds", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "  OFF - no clouds, fastest", "Fast clouds are rendered 2D.", "Fancy clouds are rendered 3D."};
      } else if (var1.equals("Cloud Height")) {
         return new String[]{"Cloud Height", "  OFF - default height", "  100% - above world height limit"};
      } else if (var1.equals("Trees")) {
         return new String[]{"Trees", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast trees have opaque leaves.", "Fancy trees have transparent leaves."};
      } else if (var1.equals("Grass")) {
         return new String[]{"Grass", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast grass uses default side texture.", "Fancy grass uses biome side texture."};
      } else if (var1.equals("Water")) {
         return new String[]{"Water", "  Default - as set by setting Graphics", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Fast water (1 pass) has some visual artifacts", "Fancy water (2 pass) has no visual artifacts"};
      } else if (var1.equals("Rain & Snow")) {
         return new String[]{"Rain & Snow", "  Default - as set by setting Graphics", "  Fast  - light rain/snow, faster", "  Fancy - heavy rain/snow, slower", "  OFF - no rain/snow, fastest", "When rain is OFF the splashes and rain sounds", "are still active."};
      } else if (var1.equals("Sky")) {
         return new String[]{"Sky", "  ON - sky is visible, slower", "  OFF  - sky is not visible, faster", "When sky is OFF the moon and sun are still visible."};
      } else if (var1.equals("Stars")) {
         return new String[]{"Stars", "  ON - stars are visible, slower", "  OFF  - stars are not visible, faster"};
      } else if (var1.equals("Autosave")) {
         return new String[]{"Autosave interval", "Default autosave interval (2s) is NOT RECOMMENDED.", "Autosave causes the famous Lag Spike of Death."};
      } else if (var1.equals("Fast Debug Info")) {
         return new String[]{"Fast Debug Info", " OFF - default debug info screen, slower", " ON - debug info screen without lagometer, faster", "Removes the lagometer from the debug screen (F3)."};
      } else if (var1.equals("Chunk Updates")) {
         return new String[]{"Chunk updates per frame", " 1 - (default) slower world loading, higher FPS", " 3 - faster world loading, lower FPS", " 5 - fastest world loading, lowest FPS"};
      } else {
         return var1.equals("Dynamic Updates") ? new String[]{"Chunk updates per frame", " OFF - (default) standard chunk updates per frame", " ON - more updates while the player is standing still", "Dynamic updates force more chunk updates while", "the player is standing still to load the world faster."} : null;
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
