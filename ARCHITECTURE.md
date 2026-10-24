# OptiFine

OptiFine makes Minecraft Beta 1.7.3 run faster and smoother. This repository contains a decompiled version of it.

**The idea in one sentence:** do less work, do it smarter, and only when needed.

---

## Problem

Minecraft draws thousands of blocks, entities, particles, and lights every frame. Done naively, the game would be very slow.

## Principle

- Do not draw what the player cannot see.
- Do not repeat expensive work.
- Reuse what was already calculated.
- Update only what changed.

## Chunks

The world is split into chunks, which are small sections of blocks.

- Only nearby chunks are drawn.
- Chunks outside the camera view are skipped.
- Chunk data is reused instead of rebuilt.
- Hidden block faces are not drawn, because most blocks are covered by their neighbors.

This is one of the biggest improvements: less geometry means less work for both the processor and the graphics card.

## Batching

Sending one draw command per block is slow. Instead, many blocks are grouped and drawn together.

- Fewer draw commands.
- Fewer texture and state changes.
- Buffers are reused.

## Culling

Before drawing, the game checks what is actually visible and removes the rest.

- Objects outside the camera view.
- Chunks behind the player or too far away.
- Entities hidden by terrain.
- Faces blocked by adjacent blocks.

Removing work before it starts is often better than making the work faster.

## Lighting

Lighting is expensive in large worlds, so it is calculated carefully.

- Reuse light values already calculated.
- Recalculate only the blocks affected by a change.
- Avoid heavy per-pixel lighting when possible.

## Memory

Java can stutter when it creates and discards too many temporary objects.

- Reuse buffers and arrays.
- Create fewer short-lived objects while playing.
- Keep data structures simple and compact.

## Updates

Only what changed is updated.

| Change | What is updated |
| --- | --- |
| A block is placed or removed | Only nearby chunks |
| A light source changes | Only the affected sections |
| An entity moves slightly | Only its own position and state |

## Summary

| Area | Goal |
| --- | --- |
| Chunks | Draw only what is needed |
| Batching | Fewer draw commands |
| Culling | Skip what is hidden |
| Lighting | Avoid recalculation |
| Memory | Avoid stutters |
| Updates | Change only what changed |

Together, these keep the processor, the graphics card, and memory balanced, so the game feels smooth for the player.
