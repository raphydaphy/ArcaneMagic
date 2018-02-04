package com.raphydaphy.arcanemagic.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.client.particle.FountainRenderer;
import com.raphydaphy.arcanemagic.client.particle.ParticleFountain;
import com.raphydaphy.arcanemagic.client.particle.ParticlePos;
import com.raphydaphy.arcanemagic.client.particle.ParticleQueue;
import com.raphydaphy.arcanemagic.client.render.GLHelper;
import com.raphydaphy.arcanemagic.client.render.RenderEntityItemFancy;
import com.raphydaphy.arcanemagic.client.render.RenderEntityMagicCircles;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.common.entity.EntityMagicCircles;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.item.ItemParchment;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent ev) {
		World world = Minecraft.getMinecraft().world;

		if (world != null) {
			EntityPlayer player = Minecraft.getMinecraft().player;

			if (player != null) {
				ParticleQueue.getInstance().updateQueue(world, player);
			}
		}
	}

	@SubscribeEvent
	public static void onRenderHand(RenderSpecificHandEvent ev) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (!player.isSneaking() && ev.getItemStack().getItem() instanceof ItemParchment) {

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

			if (ev.getHand() == EnumHand.MAIN_HAND && player.getHeldItemOffhand().isEmpty()) {
				GLHelper.renderParchmentFirstPerson(f1, f5, f, ev.getItemStack());
			} else {
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
	public static void registerModels(ModelRegistryEvent event) {
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

	public static List<ParticleFountain> particles = new ArrayList<ParticleFountain>();
	private static FountainRenderer renderer = new FountainRenderer();
	
	@SubscribeEvent
	public static void renderWorldLastEvent(RenderWorldLastEvent ev) {
		World world = Minecraft.getMinecraft().world;
		EntityPlayer player = Minecraft.getMinecraft().player;

		if (world != null && player != null) {

			if (Keyboard.isKeyDown(Keyboard.KEY_Y))
			{
				new ParticleFountain(new Vector3f(player.getPosition().getX(),player.getPosition().getY(),player.getPosition().getZ()), new Vector3f(0,10,0), 0f, 1300, 0, 1);
				System.out.println(ClientEvents.particles.size());
			}
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.translate(-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).x,
					-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).y,
					-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).z);

			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GlStateManager.depthMask(false);
			GlStateManager.disableCull();
			
			Iterator<ParticleFountain> iterator = particles.iterator();
			while(iterator.hasNext())
			{
				ParticleFountain p = iterator.next();
				boolean stillAlive = p.update();
				if (!stillAlive)
				{
					iterator.remove();
				}
			}
			renderer.render(particles);

			GlStateManager.popAttrib();
			GlStateManager.popMatrix();

			if (world.getTotalWorldTime() % 38 == 0) {
				long oldNano = System.nanoTime();
				INotebookInfo info = player.getCapability(INotebookInfo.CAP, null);

				if (info != null) {
					int range = 20;
					if (player.getHeldItemMainhand().getItem().equals(ModRegistry.MYSTICAL_ILLUMINATOR)
							|| player.getHeldItemOffhand().getItem().equals(ModRegistry.MYSTICAL_ILLUMINATOR)) {
						float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

						double posX = player.posX;
						double posY = player.posY;
						double posZ = player.posZ;

						Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
						double cx = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
						double cy = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
						double cz = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;

						for (int x = -range; x < range; x++) {
							for (int y = -range; y < range; y++) {
								if (posY + y > 0 && posY + y < 256) {
									for (int z = -range; z < range; z++) {
										BlockPos first = new BlockPos(posX + x, posY + y, posZ + z);
										if (world.isBlockLoaded(first)) {
											IBlockState state = player.world.getBlockState(first);
											if (state.getBlock() != Blocks.AIR) {

												if (ClippingHelperImpl.getInstance().isBoxInFrustum(first.getX() - cx,
														first.getY() - cy, first.getZ() - cz, first.getX() + 1 - cx,
														first.getY() + 1 - cy, first.getZ() + 1 - cz)) {
													List<NotebookCategory> obtainable = ArcaneMagicAPI.getAnalyzer()
															.getAnalysisResults(state);

													if (!obtainable.isEmpty()) {

														boolean add = false;
														for (NotebookCategory couldGet : obtainable) {
															if (!info.isUnlocked(couldGet.getRequiredTag())
																	&& info.isUnlocked(couldGet.getPrerequisiteTag())) {
																add = true;
															}
														}
														if (add) {
															ParticleQueue.getInstance().addParticle(world,
																	new ParticlePos(first, EnumFacing.UP, 0, 0, 0));
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
					int diameter = range * 2;
					ArcaneMagic.LOGGER.log(Level.DEBUG, "Particle Calculations for " + diameter + "x" + diameter + "x"
							+ diameter + " area took " + (System.nanoTime() - oldNano) + " nanos");
				}

			}
		}
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {

		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.ALL) {
			EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;

			// a chance to do great things in the realm of huds
		}
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/ball"));
		ArcaneMagic.LOGGER.info("Stiched textures!");
	}
}
