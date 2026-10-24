package net.minecraft.src;

public class GuiWorldSettingsOF extends GuiScreen {
   private GuiScreen prevScreen;
   protected String title;
   private GameSettings settings;
   private static EnumOptions[] enumOptions = new EnumOptions[]{EnumOptions.LOAD_FAR, EnumOptions.PRELOADED_CHUNKS, EnumOptions.CHUNK_UPDATES, EnumOptions.CHUNK_UPDATES_DYNAMIC, EnumOptions.WEATHER, EnumOptions.TIME, EnumOptions.FAR_VIEW};
   private int lastMouseX = 0;
   private int lastMouseY = 0;
   private long mouseStillTime = 0L;

   public GuiWorldSettingsOF(GuiScreen var1, GameSettings var2) {
      this.title = "World Settings";
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
      if (var1.equals("Load Far")) {
         return new String[]{"Loads the world chunks at distance Far.", "Switching the render distance does not cause all chunks ", "to be loaded again.", "  OFF - world chunks loaded up to render distance", "  ON - world chunks loaded at distance Far, allows", "       fast render distance switching"};
      } else if (var1.equals("Preloaded Chunks")) {
         return new String[]{"Defines an area in which no chunks will be loaded", "  OFF - after 5m new chunks will be loaded", "  2 - after 32m  new chunks will be loaded", "  8 - after 128m new chunks will be loaded", "Higher values need more time to load all the chunks"};
      } else if (var1.equals("Chunk Updates")) {
         return new String[]{"Chunk updates per frame", " 1 - (default) slower world loading, higher FPS", " 3 - faster world loading, lower FPS", " 5 - fastest world loading, lowest FPS"};
      } else if (var1.equals("Dynamic Updates")) {
         return new String[]{"Chunk updates per frame", " OFF - (default) standard chunk updates per frame", " ON - more updates while the player is standing still", "Dynamic updates force more chunk updates while", "the player is standing still to load the world faster."};
      } else if (var1.equals("Far View")) {
         return new String[]{"Far View", " OFF - (default) standard view distance", " ON - 3x view distance", "Far View is very resource demanding!", "3x view distance => 9x chunks to be loaded => FPS / 9", "Standard view distances: 32, 64, 128, 256", "Far view distances: 96, 192, 384, 512"};
      } else if (var1.equals("Time")) {
         return new String[]{"Time", " Default - normal day/night cycles", " Day Only - day only", " Night Only - night only"};
      } else {
         return var1.equals("Weather") ? new String[]{"Weather", "  ON - weather is active, slower", "  OFF  - weather is not active, faster", "The weather controls rain, snow and thunderstorms."} : null;
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
