package com.raphydaphy.arcanemagic.proxy;

import java.awt.Color;
import java.util.Collection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.client.model.SceptreModel;
import com.raphydaphy.arcanemagic.client.particle.ParticleEssence;
import com.raphydaphy.arcanemagic.client.render.EssenceConcentratorTESR;
import com.raphydaphy.arcanemagic.client.render.RenderEntityItemFancy;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceConcentrator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@SubscribeEvent
	public static void renderWorldLastEvent(RenderWorldLastEvent ev)
	{
		World world = Minecraft.getMinecraft().world;
		EntityPlayerSP player = Minecraft.getMinecraft().player;

		GlStateManager.translate(-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).x,
				-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).y,
				-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).z);
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.color(1, 1, 1,1);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.enableCull();
		GlStateManager.disableAlpha();
		// pre-alpha
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);

		GlStateManager.pushMatrix();

		for (int x = -10; x < 10; x++)
		{
			for (int y = -10; y < 10; y++)
			{
				for (int z = -10; z < 10; z++)
				{
					BlockPos first = new BlockPos(player.posX + x, player.posY + y, player.posZ + z);

					if (world.getBlockState(first).getBlock() == ModRegistry.CRYSTALLIZER)
					{
						for (int x2 = -10; x2 < 10; x2++)
						{
							for (int y2 = -10; y2 < 10; y2++)
							{
								for (int z2 = -10; z2 < 10; z2++)
								{
									BlockPos second = new BlockPos(first.getX() + x2, first.getY() + y2,
											first.getZ() + z2);

									if (world.getBlockState(second).getBlock() == ModRegistry.ESSENCE_CONCENTRATOR)
									{
										Vec3d from = new Vec3d(first.getX() + 0.5, first.getY() + 2.3, first.getZ() + 0.5);
										Vec3d to = new Vec3d(second.getX() + 0.5, second.getY() + 2.4, second.getZ() + 0.5);
										Vec3d aim = new Vec3d(to.x - from.x, to.y - from.y, to.z - from.z);
										Vec3d aimPerp = new Vec3d(aim.x, aim.y, aim.z);
										
										if (aimPerp.z == 0.0D)
										{
											double d = aimPerp.y;
											double d1 = -aimPerp.z;
											
											aimPerp = new Vec3d(d, d1, 0.0D);
										}
										else
										{
											double d = aimPerp.z;
											double d1 = -aimPerp.y;
											
											aimPerp = new Vec3d(0.0D, d, d1);
										}
										
										double fromSize = 10;
										double toSize = 10;
										
										int deg = 0;
										
										for (int i = 0; i < 3; i++)
										{
											// Angle goes up by 120 degrees each iteration
											double angle = Math.toRadians(deg);
											
											// Building a quaternion from Vec3d
											double sinAngle = Math.sin(angle);
											Quaternion plzDieAllQuats = new Quaternion((float)(aim.x * sinAngle), (float)(aim.y * sinAngle), (float)(aim.z * sinAngle), (float)(Math.cos(angle)));
											
											// Rotating the new quaternion based on aim with the magnitude from aimPerp
											double d = - plzDieAllQuats.x * aimPerp.x - plzDieAllQuats.y * aimPerp.y - plzDieAllQuats.z * aimPerp.z;
											double d1 = plzDieAllQuats.w * aimPerp.x - plzDieAllQuats.y * aimPerp.z - plzDieAllQuats.z * aimPerp.y;
											double d2 = plzDieAllQuats.w * aimPerp.y - plzDieAllQuats.x * aimPerp.z - plzDieAllQuats.z * aimPerp.x;
											double d3 = plzDieAllQuats.w * aimPerp.z - plzDieAllQuats.x * aimPerp.y - plzDieAllQuats.y * aimPerp.x;
											Vec3d perp = new Vec3d(d1 * plzDieAllQuats.w - d * plzDieAllQuats.x - d2 * plzDieAllQuats.z + d3 * plzDieAllQuats.y, 
																   d2 * plzDieAllQuats.w - d * plzDieAllQuats.y + d1 * plzDieAllQuats.z - d3 * plzDieAllQuats.x, 
																   d3 * plzDieAllQuats.w - d * plzDieAllQuats.z - d1 * plzDieAllQuats.y + d2 * plzDieAllQuats.x);
											
											// Normalize the vector perspective
											double perpLen = Math.sqrt(perp.x * perp.x + perp.y * perp.y + perp.z * perp.z);
											perp = new Vec3d(perp.x / perpLen, perp.y / perpLen, perp.z / perpLen);
											
											// Get a seperate vector for the to and from positions
											Vec3d perpFrom = new Vec3d(perp.x * fromSize, perp.y * fromSize, perp.z * fromSize);
											Vec3d perpTo = new Vec3d(perp.x * toSize, perp.y * toSize, perp.z * toSize);
											
											Tessellator tes = Tessellator.getInstance();
											BufferBuilder vb = tes.getBuffer();
	
											RenderHelper.disableStandardItemLighting();
											vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
											Color effectColor = Essence.getFromBiome(world.getBiome(new BlockPos(x, y, z)))
													.getColor();
											
											Vec3d vec = new Vec3d((from.x + perpFrom.x) * -1, (from.y + perpFrom.y) * -1, (from.z + perpFrom.z) * -1);
											vb.pos(vec.x, vec.y, vec.z).color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
									        vec = new Vec3d(from.x + perpFrom.x, from.y + perpFrom.y, from.z + perpFrom.z);
									        vb.pos(vec.x, vec.y, vec.z).color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
									        vec = new Vec3d(to.x + perpTo.x, to.y + perpTo.y, to.z + perpTo.z);
									        vb.pos(vec.x, vec.y, vec.z).color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
									        vec = new Vec3d((to.x + perpTo.x) * -1, (to.y + perpTo.y) * -1, (to.z + perpTo.z) * -1);
									        vb.pos(vec.x, vec.y, vec.z).color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
											tes.draw();
											
											deg+= 120;
										}

									}
								}
							}
						}
					}
				}
			}
		}
		

        if(lighting) {
            GL11.glEnable(GL11.GL_LIGHTING);
        }
        
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();

		RenderHelper.enableStandardItemLighting();
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();

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

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
		ModelLoaderRegistry.registerLoader(SceptreModel.Loader.INSTANCE);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		registerColors();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEssenceConcentrator.class,
				new EssenceConcentratorTESR());
	}

	public static void registerColors()
	{
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityItemFancy.class, new RenderEntityItemFancy.Factory());
		for (Item i : ModRegistry.ITEMS)
			if (i instanceof IHasModel)
				((IHasModel) i).initModels(event);
		for (Block b : ModRegistry.BLOCKS)
			if (b instanceof IHasModel)
				((IHasModel) b).initModels(event);
	}

	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event)
	{
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/particle_star"));
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/particles"));
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/orb"));
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/ball"));
		for (int essenceParticle = 1; essenceParticle < 15; essenceParticle++)
		{
			event.getMap()
					.registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/essence/essence" + essenceParticle));
		}
		ScepterRegistry.getValues().forEach(part ->
		{
			event.getMap().registerSprite(part.getTexture());
		});
		ArcaneMagic.LOGGER.info("Stiched textures!");
	}

	@Override
	public void spawnEssenceParticles(World world, Vec3d pos, Vec3d speed, Essence essence, Vec3d travelPos,
			boolean isCosmetic)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleEssence(world, pos.x, pos.y, pos.z, speed.x,
				speed.y, speed.z, essence, travelPos, isCosmetic));
	}
	
	@Override
	public String translate(String key, Object... args) {
		return I18n.format(key, args);
	}
}
