# OpenFine

Deobfuscated and decompiled client-side source code for OptiFine for Minecraft Beta 1.7.3. Designed to aid the community in studying internal architecture and performance optimizations.

## Overview

* **Core Codebase:** Fully restored and deobfuscated [OptiFine HD_S_G](https://raw.githubusercontent.com/SynArchive/OptiFine-Archive/main/b1.7.3/beta_OptiFine_1.7.3_HD_S_G.zip) [(by sp614x)](https://github.com/sp614x) source.
* **Documentation:** Detailed [Technical Architecture Guide](./ARCHITECTURE.md) covering optimization logic.
* **Integrations:** 
  * [Long Distance Patch](https://github.com/FarnGitHub/Farn_Minecraft_Mod_Collection/raw/refs/heads/old/OptifineLongDistancePatchSmoothV2.zip) by [Farn](https://github.com/FarnGitHub).
  * [Basic FOV Slider](https://download2266.mediafire.com/xgdv4gztu05g3_oCc0rnSQRcgZpOfotQ4C8BbafrHlAZRGG33bSad-arCZHCxhw1qxOZa0fk459RHYFRm3jZGCtiyujurhp__TeWRBYiZXLMQQzQ-ig5tW2WvL8IwquSH6LgbBoD7oMEsTtnNcwEHrE2AH9H5XMLcgmLSgEoHQkU/odydweoy5e4sy7s/FOVMod-v1.zip) by [vfjekMC](https://www.minecraftforum.net/members/vfjekMC).
* **Maintenance:** Includes minor bug fixes and stability adjustments.

## Build

This project was restored using a custom automated mod decompiler (currently unreleased).

* **Environment:** [1.7.3-LTS](https://github.com/ModificationStation/1.7.3-LTS)
* **Compiler / IDE:** [Java 8 (Temurin)](https://adoptium.net/es/temurin/releases?version=8&os=any&arch=any) and [Eclipse IDE](https://www.eclipse.org/downloads/)

> **Note:** Due to automated restoration processes, minor edge cases or residual issues may exist. Please report any bugs or inconsistencies via GitHub issues.

## Mappings

| Class | Exported as |
| :--- | :--- |
| `ClippingHelper.java` | `dr.class` |
| `Config.java` | `Config.class` |
| `EntityRenderer.java` | `px.class` |
| `EnumOptions.java` | `ht.class` |
| `FontRenderer.java` | `sj.class` |
| `Frustrum.java` | `sr.class` |
| `GameSettings.java` | `kv.class` |
| `GuiAnimationSettingsOF.java` | `GuiAnimationSettingsOF.class` |
| `GuiDetailSettingsOF.java` | `GuiDetailSettingsOF.class` |
| `GuiOptions.java` | `co.class` |
| `GuiOtherSettingsOF.java` | `GuiOtherSettingsOF.class` |
| `GuiVideoSettings.java` | `nj.class` |
| `GuiWorldSettingsOF.java` | `GuiWorldSettingsOF.class` |
| `ICamera.java` | `yn.class` |
| `ItemRendererHD.java` | `ItemRendererHD.class` |
| `RenderBlocks.java` | `cv.class` |
| `RenderEngine.java` | `ji.class` |
| `RenderGlobal.java` | `n.class` |
| `Tessellator.java` | `nw.class` |
| `TextureHDCompassFX.java` | `TextureHDCompassFX.class` |
| `TextureHDFlamesFX.java` | `TextureHDFlamesFX.class` |
| `TextureHDFX.java` | `TextureHDFX.class` |
| `TextureHDLavaFlowFX.java` | `TextureHDLavaFlowFX.class` |
| `TextureHDLavaFX.java` | `TextureHDLavaFX.class` |
| `TextureHDPortalFX.java` | `TextureHDPortalFX.class` |
| `TextureHDWatchFX.java` | `TextureHDWatchFX.class` |
| `TextureHDWaterFlowFX.java` | `TextureHDWaterFlowFX.class` |
| `TextureHDWaterFX.java` | `TextureHDWaterFX.class` |
| `WorldRenderer.java` | `dk.class` |
| `WrUpdateState.java` | `WrUpdateState.class` |
