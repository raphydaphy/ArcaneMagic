package com.raphydaphy.arcanemagic.client;

import java.util.Collection;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.client.render.GLHelper;
import com.raphydaphy.arcanemagic.client.render.RenderEntityItemFancy;
import com.raphydaphy.arcanemagic.client.render.RenderEntityMagicCircles;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.common.entity.EntityMagicCircles;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.item.ItemParchment;
import com.raphydaphy.arcanemagic.common.item.ItemScepter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEvents
{
	@SubscribeEvent
	public static void onRenderHand(RenderSpecificHandEvent ev)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (!player.isSneaking() && ev.getItemStack().getItem() instanceof ItemParchment)
		{

			float f = player.getSwingProgress(ev.getPartialTicks());
			float f1 = player.prevRotationPitch
					+ (player.rotationPitch - player.prevRotationPitch) * ev.getPartialTicks();

			ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer();

			float prevEquipProgress = ev.getHand() == EnumHand.MAIN_HAND ? itemrenderer.prevEquippedProgressMainHand
					: itemrenderer.prevEquippedProgressOffHand;
			float equipProgress = ev.getHand() == EnumHand.MAIN_HAND ? itemrenderer.equippedProgressMainHand
					: itemrenderer.equippedProgressOffHand;
			float f5 = 1.0F - (prevEquipProgress + (equipProgress - prevEquipProgress) * ev.getPartialTicks());

			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();

			if (ev.getHand() == EnumHand.MAIN_HAND && player.getHeldItemOffhand().isEmpty())
			{
				GLHelper.renderParchmentFirstPerson(f1, f5, f, ev.getItemStack());
			} else
			{
				EnumHandSide enumhandside = ev.getHand() == EnumHand.MAIN_HAND ? player.getPrimaryHand()
						: player.getPrimaryHand().opposite();
				GLHelper.renderParchmentFirstPersonSide(f5, enumhandside, f, ev.getItemStack());
			}

			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
			ev.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityItemFancy.class, new RenderEntityItemFancy.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicCircles.class,
				new RenderEntityMagicCircles.Factory());
		for (Item i : ModRegistry.ITEMS)
			if (i instanceof IHasModel)
				((IHasModel) i).initModels(event);
		for (Block b : ModRegistry.BLOCKS)
			if (b instanceof IHasModel)
				((IHasModel) b).initModels(event);
	}

	//private static boolean[][][] = new boolean[100][100][100];

	@SubscribeEvent
	public static void clientTick(ClientTickEvent ev)
	{
		World world = Minecraft.getMinecraft().world;
		EntityPlayer player = Minecraft.getMinecraft().player;

		if (world != null && player != null)
		{
			if (player.getHeldItemMainhand().getItem().equals(ModRegistry.MYSTICAL_ILLUMINATOR)
					|| player.getHeldItemOffhand().getItem().equals(ModRegistry.MYSTICAL_ILLUMINATOR))
			{
				ICamera icamera = new Frustum();
				float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
				icamera.setPosition(d0, d1, d2);
				ViewFrustum vf = Minecraft.getMinecraft().renderGlobal.viewFrustum;

				double posX = player.posX;
				double posY = player.posY;
				double posZ = player.posZ;

				for (int x = -50; x < 50; x++)
				{
					for (int y = -50; y < 50; y++)
					{
						if (posY + y > 0 && posY + y < 256)
						{
							for (int z = -50; z < 50; z++)
							{
								if (world.rand.nextInt(10) == 1)
								{
									BlockPos first = new BlockPos(posX + x, posY + y, posZ + z);
									if (icamera.isBoundingBoxInFrustum(vf.getRenderChunk(first).boundingBox))
									{
										if (world.isBlockLoaded(first))
										{
											Block firstBlock = player.world.getBlockState(first).getBlock();
											if (firstBlock != Blocks.AIR)
											{
												if (ArcaneMagicAPI.canAnalyseBlock(firstBlock))
												{
													System.out.println("can see and analyze: " + firstBlock.toString());
													world.spawnParticle(EnumParticleTypes.PORTAL,
															first.getX() + 0.4 + (world.rand.nextFloat() / 4),
															first.getY() + 1,
															first.getZ() + 0.4 + (world.rand.nextFloat() / 4), 0, 0, 0);

												}
											}
										}
									}

								}
							}
						}
					}
				}
			}
		}
	}

	/*
	@SubscribeEvent
	public static void renderWorldLastEvent(RenderWorldLastEvent ev)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;

		GlStateManager.translate(-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).x,
				-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).y,
				-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).z);
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();

		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}*/

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event)
	{

		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.ALL)
		{
			EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;

			if ((!player.getHeldItemMainhand().isEmpty()
					&& player.getHeldItemMainhand().getItem().equals(ModRegistry.SCEPTER)))
			{
				ItemScepter.renderHUD(mc, event.getResolution(), player.getHeldItemMainhand());
			}

			else if (!player.getHeldItemOffhand().isEmpty()
					&& player.getHeldItemOffhand().getItem().equals(ModRegistry.SCEPTER))
			{
				ItemScepter.renderHUD(mc, event.getResolution(), player.getHeldItemOffhand());
			}
		}
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/ball"));
		ScepterRegistry.getValues().forEach(part ->
		{
			event.getMap().registerSprite(part.getTexture());
		});
		ArcaneMagic.LOGGER.info("Stiched textures!");
	}

	@SubscribeEvent
	public static void renderTooltipPostBackground(RenderTooltipEvent.PostBackground ev)
	{
		if (ev.getStack().getItem() == ModRegistry.SCEPTER)
		{

			ItemStack stack = ev.getStack();
			IEssenceStorage handler = stack.getCapability(IEssenceStorage.CAP, null);

			int y = ev.getY();
			for (int line = 0; line < ev.getLines().size(); line++)
			{
				if (ev.getLines().get(line).equals("\u00A77"))
				{

					break;
				}
				y += 11;
			}
			if (handler != null)
			{
				Collection<EssenceStack> storedEssence = handler.getStored().values();

				if (storedEssence.size() > 0)
				{
					int x = ev.getX();
					int curYCounter = 0;

					for (EssenceStack essence : storedEssence)
					{

						String thisString = essence.getCount() + " "
								+ I18n.format(essence.getEssence().getTranslationName()) + " ";
						ev.getFontRenderer().drawStringWithShadow(thisString, x, y, essence.getEssence().getColorInt());

						x += 70;
						curYCounter++;

						if (curYCounter % 2 == 0)
						{
							y += 10;
							x = ev.getX();
						}
					}

				}
			}
		}
	}
}
