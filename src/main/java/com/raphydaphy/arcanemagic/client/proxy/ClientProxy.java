package com.raphydaphy.arcanemagic.client.proxy;

import java.awt.Color;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.client.particle.ParticleAnima;
import com.raphydaphy.arcanemagic.client.particle.ParticleAnimaTransfer;
import com.raphydaphy.arcanemagic.client.particle.ParticlePos;
import com.raphydaphy.arcanemagic.client.particle.ParticleQueue;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.AnalyzerTESR;
import com.raphydaphy.arcanemagic.client.render.AnimaConjurerTESR;
import com.raphydaphy.arcanemagic.client.render.AnimusMaterializerTESR;
import com.raphydaphy.arcanemagic.client.render.ArcaneForgeTESR;
import com.raphydaphy.arcanemagic.client.render.ArcaneTransfigurationTableTESR;
import com.raphydaphy.arcanemagic.client.render.InfernalSmelterTESR;
import com.raphydaphy.arcanemagic.client.render.PedestalTESR;
import com.raphydaphy.arcanemagic.client.toast.CategoryUnlockedToast;
import com.raphydaphy.arcanemagic.common.item.ItemIlluminator;
import com.raphydaphy.arcanemagic.common.proxy.CommonProxy;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnalyzer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimaConjurer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneForge;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneTransfigurationTable;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityInfernalSmelter;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityPedestal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public static ParticleRenderer particleRenderer = new ParticleRenderer();

	@Override
	public void sendAnimaSafe(AnimaStack anima, Vec3d from, Vec3d to, Vec3d toCosmetic, boolean spawnParticles)
	{
		Anima.sendAnima(Minecraft.getMinecraft().world, anima, from, to, toCosmetic, false, spawnParticles);
	}

	@Override
	public void addCategoryUnlockToast(NotebookCategory category, boolean expanded)
	{
		Minecraft.getMinecraft().getToastGui().add(new CategoryUnlockedToast(category, expanded));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		registerColors();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnimaConjurer.class, new AnimaConjurerTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArcaneTransfigurationTable.class,
				new ArcaneTransfigurationTableTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnalyzer.class, new AnalyzerTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnimusMaterializer.class, new AnimusMaterializerTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfernalSmelter.class, new InfernalSmelterTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArcaneForge.class, new ArcaneForgeTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new PedestalTESR());
	}

	public static void registerColors()
	{
	}

	@Override
	public void spawnAnimaParticles(World world, Vec3d pos, Vec3d speed, Anima anima, Vec3d travelPos,
			boolean isCosmetic)
	{

		Minecraft.getMinecraft().effectRenderer.addEffect(
				new ParticleAnima(world, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z, anima, travelPos, isCosmetic));
	}

	@Override
	public void addIlluminatorParticle(ItemIlluminator item, World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			ParticleQueue.getInstance().addParticle(world, new ParticlePos(pos, facing, hitX, hitY, hitZ));
		}
	}

	@Override
	public String translate(String key, Object... args)
	{
		return I18n.format(key, args);
	}

	@Override
	public void magicParticle(Color color, BlockPos from, BlockPos to)
	{
		World world = Minecraft.getMinecraft().world;

		if (world != null)
		{

			float magic = 0.01625f;

			float distX = from.getX() - to.getX();
			float vx = magic * distX;

			float distZ = from.getZ() - to.getZ();
			float vz = magic * distZ;

			float distY = from.getY() - to.getY();
			float vy = 0.053f + 0.017f * distY;

			int life = 111 + (0 * (int) Math.abs(distY));

			float alpha = Math.min(Math.max(world.rand.nextFloat(), 0.25f), 0.30f);

			int dist = Math.abs((int) distX) + Math.abs((int) distZ) + Math.abs((int) distZ);
			float size;

			if (dist < 4)
			{
				size = Math.min(Math.max(world.rand.nextFloat() * 6, 1.5f), 2);
			} else if (dist < 6)
			{
				size = Math.min(Math.max(world.rand.nextFloat() * 10, 2.5f), 3);
			} else
			{
				size = Math.min(Math.max(world.rand.nextFloat() * 14, 3f), 3.5f);
			}

			ParticleRenderer.particleCounter += world.rand.nextInt(3);
			if (ParticleRenderer.particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1
					: 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0)
			{
				ClientProxy.particleRenderer.addParticle(new ParticleAnimaTransfer(world, to.getX() + .5f,
						to.getY() + .8f, to.getZ() + 0.5f, vx, vy, vz, color.getRed() / 256f, color.getGreen() / 256f,
						color.getBlue() / 256f, alpha, size, (int) (life), 0.1f));
			}

		}
	}
}
