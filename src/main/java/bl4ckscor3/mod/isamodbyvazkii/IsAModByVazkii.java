package bl4ckscor3.mod.isamodbyvazkii;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod(modid=IsAModByVazkii.MOD_ID, name="...is a mod by Vazkii", version="v1.2", acceptedMinecraftVersions="[1.12]")
@EventBusSubscriber
public class IsAModByVazkii
{
	protected static final String MOD_ID = "isamodbyvazkii";
	private static final HashMap<String,String> MODS = new HashMap<>();
	private static final HashMap<String,String> CACHE = new HashMap<>();
	public static final String MOD = "isamodbyvazkii:mod";
	private static final String BLOCK = "isamodbyvazkii:block";
	private static final String ITEM = "isamodbyvazkii:item";

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Map<String, ModContainer> modList = Loader.instance().getIndexedModList();

		for(String modid : modList.keySet())
		{
			String authorList = modList.get(modid).getMetadata().getAuthorList();

			if(authorList.toLowerCase().contains("vazkii"))
				MODS.put(modid, modList.get(modid).getName());

			if(modList.get(modid) instanceof FMLModContainer) 
			{
				if(authorList.toLowerCase().contains("vazkii")) 
				{
					FMLModContainer container = (FMLModContainer) modList.get(modid);
					ModMetadata metadata = ReflectionHelper.getPrivateValue(FMLModContainer.class, container, "modMetadata");

					metadata.name = I18n.format(MOD, metadata.name);
					ReflectionHelper.setPrivateValue(FMLModContainer.class, container, metadata, "modMetadata");
				}
			}
		}
	}

  @SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if(event.getToolTip().size() < 1)
			return;

		List<String> tooltips = event.getToolTip();
		String name = tooltips.get(0);
		ItemStack stack = event.getItemStack();

		if(!stack.isEmpty() && MODS.containsKey(stack.getItem().getRegistryName().getNamespace()))
		{
			String newName;
			String modName = MODS.get(stack.getItem().getRegistryName().getNamespace());

			if(!CACHE.containsKey(name))
			{
				String addition = "";
				String cachedName = name;

				if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips && name.contains(" "))
				{
					addition = name.substring(name.lastIndexOf(" "), name.length());
					name = name.replace(addition, "");
				}

				newName = I18n.format(stack.getItem() instanceof ItemBlock ? BLOCK : ITEM, name) + addition;
				CACHE.put(cachedName, newName);
			}
			else
				newName = CACHE.get(name);

			tooltips.set(0, newName);
		}
	}
}
