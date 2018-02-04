package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import akka.japi.Pair;
//TODO: No scala pair pls
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class NotebookCategory extends IForgeRegistryEntry.Impl<NotebookCategory> {

	public static final CategoryRegistry REGISTRY = new CategoryRegistry();
	public static final CategoryRegistry SUB_REGISTRY = new CategoryRegistry();

	private String requiredTag = "usedNotebook";
	private String prerequisiteTag = "usedNotebook";
	private ItemStack icon = ItemStack.EMPTY;
	private String unlocalizedName = "arcanemagic.notebook.category";
	private Pair<String, Integer> unlocParchmentInfo = new Pair<String, Integer>(
			"arcanemagic.notebook.category.unknown_realms", 2);

	public NotebookCategory setRequiredTag(String requiredTag) {
		this.requiredTag = requiredTag;
		return this;
	}

	public NotebookCategory setPrerequisiteTag(String prerequisiteTag) {
		this.prerequisiteTag = prerequisiteTag;
		return this;
	}

	public NotebookCategory setIcon(ItemStack icon) {
		this.icon = icon;
		return this;
	}

	public NotebookCategory setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		return this;
	}

	public NotebookCategory setUnlocParchmentInfo(Pair<String, Integer> unlocParchmentInfo) {
		this.unlocParchmentInfo = unlocParchmentInfo;
		return this;
	}

	public String getRequiredTag() {
		return requiredTag;
	}

	public String getPrerequisiteTag() {
		return prerequisiteTag;
	}

	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(GuiScreen notebook) {
		return notebook.mc.fontRenderer;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getUnlocalizedTitle(INotebookInfo info, int page) {
		return page == 0 ? getUnlocalizedName() : null;
	}

	@SideOnly(Side.CLIENT)
	public boolean matchesSearchKey(String key, INotebookInfo info) {
		if (I18n.format(getUnlocalizedName()).toLowerCase().contains(key)) {
			return true;
		}
		for (NotebookPage page : getPages(info)) {
			if (page.matchesText(key)) {
				return true;
			}
		}
		return false;
	}

	public abstract List<NotebookPage> getPages(INotebookInfo info);

	public Pair<String, Integer> getUnlocParchmentInfo() {
		return unlocParchmentInfo;
	}

}
