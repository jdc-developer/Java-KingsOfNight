package jdc.kings.state.options;

import java.lang.reflect.Method;
import java.util.Locale;

import jdc.kings.Game;
import jdc.kings.objects.Inventory;
import jdc.kings.objects.Item;
import jdc.kings.objects.Player;
import jdc.kings.state.GameState;
import jdc.kings.state.objects.Action;
import jdc.kings.state.objects.Option;
import jdc.kings.utils.BundleUtil;

public abstract class ItemActionManager {
	
	public static void createActionState(ItemState itemState, Option option) {
		try {
			Option[] actionOptions = itemState.getActionOptions();
			ActionState actionState = new ActionState(actionOptions, option.getX() + 22, option.getY() + 22);
			actionState.setParent(itemState);
			
			Item item = option.getInventoryItem().getItem();
			setDescriptionMethod(itemState, actionOptions[1], item);
			setDiscardMethod(actionState, option, actionOptions[3], item);
			
			itemState.setActionState(actionState);
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private static void setDescriptionMethod(ItemState parentState, Option actionOption, Item item) throws NoSuchMethodException, SecurityException {
		Method setDescriptionStateMethod = ItemState.class.getMethod("setDescriptionState", DescriptionState.class);
		DescriptionState descriptionState = new DescriptionState(parentState, item);
		Action<ItemState, DescriptionState> setDescriptionStateAction = new Action<>(setDescriptionStateMethod, parentState, descriptionState);
		actionOption.setAction(setDescriptionStateAction);
	}
	
	private static void setDiscardMethod(ActionState parentActionState, Option selectedOption, Option actionOption, Item item)
			throws NoSuchMethodException, SecurityException {
		Player player = GameState.getPlayer();
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String yes = BundleUtil.getMessageResourceString("yes", locale);
		String no = BundleUtil.getMessageResourceString("no", locale);
		String confirmation = BundleUtil.getMessageResourceString("confirmation", locale);
		
		Option[] promptOptions = new Option[2];
		promptOptions[0] = new Option(yes, 120, 18);
		promptOptions[1] = new Option(no, 120, 18);
		
		Method removeItemMethod = Inventory.class.getMethod("removeItem", Integer.class);
		Integer id = item.getId();
		Action<Inventory, Integer> removeItemAction = new Action<>(removeItemMethod, player.getInventory(), id);
		
		Method setSubActionMethod = ActionState.class.getMethod("setActionState", ActionState.class);
		Action<ActionState, Integer> setSubAction = new Action<>(setSubActionMethod, null, null);
		
		promptOptions[0].setAction(removeItemAction);
		promptOptions[1].setAction(setSubAction);
		ActionState subActionState = new ActionState(promptOptions, selectedOption.getX() + 52, selectedOption.getY() + 52);
		
		subActionState.setTitle(confirmation);
		actionOption.setPrompt(subActionState);
		setSubAction.setDeclaringClass(parentActionState);
		subActionState.setParent(parentActionState);
	}

}
