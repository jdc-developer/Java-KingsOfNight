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
			setUseMethod(itemState, actionOptions[0], item);
			setDescriptionMethod(itemState, actionOptions[1], item);
			setEquipMethod(actionOptions[2], item);
			setDiscardMethod(actionState, option, actionOptions[3], item);
			
			itemState.setActionState(actionState);
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void createEquipActionState(EquipmentState equipmentState, Option option, String equip) {
		try {
			Option[] actionOptions = new Option[1];
			ActionState actionState = new ActionState(actionOptions, option.getX() + 22, option.getY() + 22);
			actionState.setParent(equipmentState);
			
			actionOptions[0] = new Option(null, 120, 18);
			actionOptions[0].setDescription(equip);
			
			Method setSubStateMethod = OptionsState.class.getMethod("setSubState", GameState.class);
			ItemState itemState = new ItemState(option.getType());
			Action<OptionsState, GameState> setSubStateAction = new Action<>(setSubStateMethod, OptionsState.getInstance(), itemState);
			actionOptions[0].setAction(setSubStateAction);
			
			equipmentState.setActionState(actionState);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createUnequipActionState(EquipmentState equipmentState, Option option, String description,
			String unequip, Integer slot) {
		try {
			Option[] actionOptions = new Option[2];
			ActionState actionState = new ActionState(actionOptions, option.getX() + 22, option.getY() + 22);
			actionState.setParent(equipmentState);
			
			actionOptions[0] = new Option(description, 120, 18);
			actionOptions[1] = new Option(unequip, 120, 18);
			
			Item item = option.getItem();
			setDescriptionMethod(equipmentState, actionOptions[0], item);
			
			Method unequipMethod = null;
			if (slot == 1) {
				unequipMethod = EquipmentState.class.getMethod("unequipSlotOne", Item.class);
			} else if (slot == 2) {
				unequipMethod = EquipmentState.class.getMethod("unequipSlotTwo", Item.class);
			} else {
				unequipMethod = EquipmentState.class.getMethod("unequip", Item.class);
			}
			
			Action<EquipmentState, Item> unequipAction = new Action<>(unequipMethod, equipmentState, option.getItem());
			actionOptions[1].setAction(unequipAction);
			
			equipmentState.setActionState(actionState);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createSlotEquipActionState(EquipmentState equipmentState, Item item, String slotDescription) {
		try {
			Option[] actionOptions = new Option[2];
			ActionState actionState = new ActionState(actionOptions, 447, 142);
			actionState.setParent(equipmentState);
			actionState.setTitle(slotDescription);
			
			actionOptions[0] = new Option("Slot 1", 120, 18);
			actionOptions[1] = new Option("Slot 2", 120, 18);
			
			Method equipSlotOneMethod = EquipmentState.class.getMethod("equipSlotOne", Item.class);
			Action<EquipmentState, Item> equipSlotOneAction = new Action<>(equipSlotOneMethod, equipmentState, item);
			actionOptions[0].setAction(equipSlotOneAction);
			
			Method equipSlotTwoMethod = EquipmentState.class.getMethod("equipSlotTwo", Item.class);
			Action<EquipmentState, Item> equipSlotTwoAction = new Action<>(equipSlotTwoMethod, equipmentState, item);
			actionOptions[1].setAction(equipSlotTwoAction);
			
			equipmentState.setActionState(actionState);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void setUseMethod(ItemState itemState, Option actionOption, Item item) throws NoSuchMethodException, SecurityException {
		Method useMethod = Inventory.class.getMethod("useItem", Integer.class);
		Action<Inventory, Integer> useAction = new Action<>(useMethod, GameState.getPlayer().getInventory(), item.getId());
		actionOption.setAction(useAction);
	}
	
	private static void setDescriptionMethod(GameState parentState, Option actionOption, Item item) throws NoSuchMethodException, SecurityException {
		Method setDescriptionStateMethod = parentState.getClass().getMethod("setDescriptionState", DescriptionState.class);
		DescriptionState descriptionState = new DescriptionState(parentState, item);
		Action<GameState, DescriptionState> setDescriptionStateAction = new Action<>(setDescriptionStateMethod, parentState, descriptionState);
		actionOption.setAction(setDescriptionStateAction);
	}
	
	private static void setEquipMethod(Option actionOption, Item item) throws NoSuchMethodException, SecurityException {
		Method setSubStateMethod = OptionsState.class.getMethod("setSubState", GameState.class);
		EquipmentState equipmentState = new EquipmentState(item);
		Action<OptionsState, GameState> setSubStateAction = new Action<>(setSubStateMethod, OptionsState.getInstance(), equipmentState);
		actionOption.setAction(setSubStateAction);
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
