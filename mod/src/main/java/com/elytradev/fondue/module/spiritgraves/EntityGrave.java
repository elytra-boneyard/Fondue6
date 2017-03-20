package com.elytradev.fondue.module.spiritgraves;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.spiritgraves.ai.EntityAIMagnetize;
import com.elytradev.fondue.module.spiritgraves.ai.EntityAISeekSun;
import com.elytradev.fondue.module.spiritgraves.ai.FloatMoveHelper;
import com.elytradev.fondue.module.spiritgraves.ai.PathNavigateFloater;
import com.elytradev.fondue.module.spiritgraves.client.ModuleSpiritGravesClient;
import com.elytradev.fruitphone.FruitPhone;
import com.elytradev.fruitphone.capability.FruitEquipmentCapability;
import com.elytradev.fruitphone.network.EquipmentDataPacket;
import com.google.common.base.Optional;
import com.mojang.authlib.GameProfile;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class EntityGrave extends EntityCreature {

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Integer> SELECTED_SLOT = EntityDataManager.createKey(EntityGrave.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> RIGHT_HANDED = EntityDataManager.createKey(EntityGrave.class, DataSerializers.BOOLEAN);
	
	private static final DataParameter<ItemStack> HOTBAR1 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR2 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR3 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR4 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR5 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR6 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR7 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR8 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> HOTBAR9 = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	
	private static final DataParameter<ItemStack> OFFHAND = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	
	private static final DataParameter<ItemStack> HELMET = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> CHESTPLATE = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> LEGGINGS = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<ItemStack> BOOTS = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_ITEM_STACK);
	
	
	public static final int INVENTORY_SIZE =
			36 + // Main
			4 + // Armor
			1 + // Offhand
			7 + // Baubles
			1 // Fruit Glass
			;
	
	private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
	private NonNullList<ItemStack> extras = NonNullList.create();
	
	private GameProfile profile;
	
	public EntityGrave(World worldIn) {
		super(worldIn);
		setSize(0.5f, 0.5f);
		this.moveHelper = new FloatMoveHelper(this);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}
	
	@Override
	protected SoundEvent getFallSound(int heightIn) {
		return null;
	}
	
	@Override
	protected SoundEvent getHurtSound() {
		return null;
	}
	
	@Override
	protected SoundEvent getSplashSound() {
		return null;
	}
	
	@Override
	protected SoundEvent getSwimSound() {
		return null;
	}
	
	@Override
	public void onKillCommand() {
		setDead();
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		
	}
	
	@Override
	protected void collideWithNearbyEntities() {
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return false;
	}
	
	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISeekSun(this, 0.5f));
		tasks.addTask(1, new EntityAIMagnetize(this));
	}
	
	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateFloater(this);
	}
	
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		// overriding this method means we still get knockback, but no damage
		if (damageSrc instanceof EntityDamageSource) {
			EntityDamageSource eds = (EntityDamageSource)damageSrc;
			if (eds.getEntity() instanceof EntityPlayer) {
				giveItems((EntityPlayer)eds.getEntity());
				setDead();
			}
		}
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		// We're lying to the engine and saying we're translucent to render in
		// front of particles
		return pass == 1;
	}
	
	@Override
	public boolean hasNoGravity() {
		return true;
	}
	
	@Override
	public void setHomePosAndDistance(BlockPos pos, int distance) {
		if ("TJ \"Henry\" Yoshi".equals(getName())) {
			// Update our home to the death barrier
			super.setHomePosAndDistance(pos.offset(EnumFacing.DOWN, pos.getY()+64), distance);
		} else {
			super.setHomePosAndDistance(pos, distance);
		}
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand stack) {
		if (!world.isRemote) {
			giveItems(player);
			setDead();
		}
		return EnumActionResult.SUCCESS;
	}
	
	private void giveItems(EntityPlayer player) {
		IBaublesItemHandler ibih = BaublesApi.getBaublesHandler(player);
		
		FruitEquipmentCapability fec = null;
		if (player.hasCapability(FruitPhone.CAPABILITY_EQUIPMENT, null)) {
			fec = player.getCapability(FruitPhone.CAPABILITY_EQUIPMENT, null);
		}
		
		for (ItemStack is : player.inventory.armorInventory) {
			extras.add(is);
		}
		for (ItemStack is : player.inventory.mainInventory) {
			extras.add(is);
		}
		for (ItemStack is : player.inventory.offHandInventory) {
			extras.add(is);
		}
		for (int i = 0; i < ibih.getSlots(); i++) {
			extras.add(ibih.getStackInSlot(i));
			ibih.setStackInSlot(i, ItemStack.EMPTY);
		}
		if (fec != null) {
			extras.add(fec.glasses);
			fec.glasses = ItemStack.EMPTY;
		}
		player.inventory.armorInventory.clear();
		player.inventory.mainInventory.clear();
		player.inventory.offHandInventory.clear();
		
		player.heal(player.getMaxHealth()/2);
		
		for (int i = 0; i < 36; i++) {
			player.inventory.mainInventory.set(i, inventory[i]);
		}
		for (int i = 0; i < 4; i++) {
			player.inventory.armorInventory.set(i, inventory[36+i]);
		}
		player.inventory.offHandInventory.set(0, inventory[40]);
		for (int i = 0; i < 7; i++) {
			ibih.setStackInSlot(i, inventory[41+i]);
		}
		if (fec != null) {
			fec.glasses = inventory[48];
			EquipmentDataPacket.forEntity(player).ifPresent(p -> {
				p.sendToAllWatching(player);
				p.sendTo(player);
			});
		} else {
			extras.add(inventory[48]);
		}
		
		
		for (ItemStack is : extras) {
			if (is.isEmpty()) continue;
			if (!player.inventory.addItemStackToInventory(is)) {
				entityDropItem(is, 0.15f);
			}
		}
	}

	public GameProfile getOwner() {
		if (dataManager.get(OWNER).isPresent()) {
			if (profile == null) {
				profile = new GameProfile(dataManager.get(OWNER).get(), null);
				try {
					Minecraft.getMinecraft().getSessionService().fillProfileProperties(profile, true);
				} catch (Exception e) {}
			}
			return profile;
		} else {
			return null;
		}
	}
	
	public void setOwner(GameProfile owner) {
		if (owner == null) {
			dataManager.set(OWNER, Optional.absent());
		} else {
			dataManager.set(OWNER, Optional.fromNullable(owner.getId()));
		}
		this.profile = owner;
	}
	
	private DataParameter<ItemStack> getHotbarParam(int i) {
		switch (i) {
			case 0: return HOTBAR1;
			case 1: return HOTBAR2;
			case 2: return HOTBAR3;
			case 3: return HOTBAR4;
			case 4: return HOTBAR5;
			case 5: return HOTBAR6;
			case 6: return HOTBAR7;
			case 7: return HOTBAR8;
			case 8: return HOTBAR9;
			default: return null;
		}
	}
	
	public ItemStack getHotbarItem(int i) {
		return (i >= 0 && i <= 8) ? dataManager.get(getHotbarParam(i)) : ItemStack.EMPTY;
	}
	
	public void setHotbarItem(int i, ItemStack is) {
		if (i >= 0 && i <= 8) {
			dataManager.set(getHotbarParam(i), is);
		}
	}
	
	public void setExtras(NonNullList<ItemStack> extras) {
		this.extras = extras;
	}
	
	public void addExtras(List<ItemStack> li) {
		for (ItemStack is : li) {
			addExtra(is);
		}
	}
	
	public void addExtra(ItemStack is) {
		if (is != null) {
			extras.add(is);
		}
	}
	
	public NonNullList<ItemStack> getExtras() {
		return extras;
	}
	
	public void populateFrom(EntityPlayer player, boolean clear) {
		InventoryPlayer inv = player.inventory;
		inventory = new ItemStack[INVENTORY_SIZE];
		Arrays.fill(inventory, ItemStack.EMPTY);
		int i = 0;
		for (ItemStack is : inv.mainInventory) {
			inventory[i] = is;
			i++;
		}
		for (ItemStack is : inv.armorInventory) {
			inventory[i] = is;
			i++;
		}
		for (ItemStack is : inv.offHandInventory) {
			inventory[i] = is;
			i++;
		}
		IBaublesItemHandler ibih = BaublesApi.getBaublesHandler(player);
		for (int j = 0; j < ibih.getSlots(); j++) {
			inventory[i] = ibih.getStackInSlot(j);
			ibih.setStackInSlot(j, ItemStack.EMPTY);
			i++;
		}
		if (player.hasCapability(FruitPhone.CAPABILITY_EQUIPMENT, null)) {
			FruitEquipmentCapability fec = player.getCapability(FruitPhone.CAPABILITY_EQUIPMENT, null);
			inventory[i] = fec.glasses;
			fec.glasses = ItemStack.EMPTY;
			i++;
		}
		
		
		dataManager.set(SELECTED_SLOT, inv.currentItem);
		
		buildClientData();
		
		dataManager.set(RIGHT_HANDED, player.getPrimaryHand() == EnumHandSide.RIGHT);
		setOwner(player.getGameProfile());
		
		if (clear) {
			inv.mainInventory.clear();
			inv.armorInventory.clear();
			inv.offHandInventory.clear();
		}
	}
	
	private void buildClientData() {
		for (int j = 0; j < 9; j++) {
			setHotbarItem(j, inventory[j]);
		}
		
		dataManager.set(OFFHAND, inventory[40]);
		
		setItemStackToSlot(EntityEquipmentSlot.HEAD, inventory[39]);
		setItemStackToSlot(EntityEquipmentSlot.CHEST, inventory[38]);
		setItemStackToSlot(EntityEquipmentSlot.LEGS, inventory[37]);
		setItemStackToSlot(EntityEquipmentSlot.FEET, inventory[36]);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (Fondue.isModuleLoaded(ModuleSpiritGravesClient.class)) {
			Fondue.getModule(ModuleSpiritGravesClient.class).onUpdate(this);
		}
		if (posY < 50 && world.getHeight((int)posX, (int)posZ) <= 0) {
			// Float out of the void
			motionY = 0.5;
		}
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(OWNER, Optional.absent());
		dataManager.register(SELECTED_SLOT, 0);
		dataManager.register(RIGHT_HANDED, true);
		
		dataManager.register(HOTBAR1, ItemStack.EMPTY);
		dataManager.register(HOTBAR2, ItemStack.EMPTY);
		dataManager.register(HOTBAR3, ItemStack.EMPTY);
		dataManager.register(HOTBAR4, ItemStack.EMPTY);
		dataManager.register(HOTBAR5, ItemStack.EMPTY);
		dataManager.register(HOTBAR6, ItemStack.EMPTY);
		dataManager.register(HOTBAR7, ItemStack.EMPTY);
		dataManager.register(HOTBAR8, ItemStack.EMPTY);
		dataManager.register(HOTBAR9, ItemStack.EMPTY);
		
		dataManager.register(OFFHAND, ItemStack.EMPTY);
		
		dataManager.register(HELMET, ItemStack.EMPTY);
		dataManager.register(CHESTPLATE, ItemStack.EMPTY);
		dataManager.register(LEGGINGS, ItemStack.EMPTY);
		dataManager.register(BOOTS, ItemStack.EMPTY);
	}
	
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Arrays.asList(
				getItemStackFromSlot(EntityEquipmentSlot.HEAD),
				getItemStackFromSlot(EntityEquipmentSlot.CHEST),
				getItemStackFromSlot(EntityEquipmentSlot.LEGS),
				getItemStackFromSlot(EntityEquipmentSlot.FEET),
				
				getItemStackFromSlot(EntityEquipmentSlot.MAINHAND),
				getItemStackFromSlot(EntityEquipmentSlot.OFFHAND)
				);
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		switch (slotIn) {
			case HEAD: return dataManager.get(HELMET);
			case CHEST: return dataManager.get(CHESTPLATE);
			case LEGS: return dataManager.get(LEGGINGS);
			case FEET: return dataManager.get(BOOTS);
			case MAINHAND: return getHotbarItem(dataManager.get(SELECTED_SLOT));
			case OFFHAND: return dataManager.get(OFFHAND);
			default: return ItemStack.EMPTY;
		}
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		switch (slotIn) {
			case HEAD: dataManager.set(HELMET, stack); break;
			case CHEST: dataManager.set(CHESTPLATE, stack); break;
			case LEGS: dataManager.set(LEGGINGS, stack); break;
			case FEET: dataManager.set(BOOTS, stack); break;
			case MAINHAND: setHotbarItem(dataManager.get(SELECTED_SLOT), stack); break;
			case OFFHAND: dataManager.set(OFFHAND, stack); break;
		}
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return dataManager.get(RIGHT_HANDED) ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		
		compound.setBoolean("RightHanded", dataManager.get(RIGHT_HANDED));
		compound.setByte("SelectedSlot", dataManager.get(SELECTED_SLOT).byteValue());
		
		Optional<UUID> owner = dataManager.get(OWNER);
		if (owner.isPresent()) {
			compound.setUniqueId("Owner", owner.get());
		}
		
		NBTTagList inv = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack is = inventory[i];
			NBTTagCompound tag = is.serializeNBT();
			tag.setInteger("Slot", i);
			inv.appendTag(tag);
		}
		compound.setTag("Inventory", inv);
		
		NBTTagList ext = new NBTTagList();
		for (ItemStack is : extras) {
			ext.appendTag(is.serializeNBT());
		}
		compound.setTag("Extras", ext);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
		dataManager.set(RIGHT_HANDED, compound.getBoolean("RightHanded"));
		dataManager.set(SELECTED_SLOT, compound.getInteger("SelectedSlot"));
		
		if (compound.hasUniqueId("Owner")) {
			dataManager.set(OWNER, Optional.of(compound.getUniqueId("Owner")));
		} else {
			dataManager.set(OWNER, Optional.absent());
		}
		profile = null;
		
		NBTTagList inv = compound.getTagList("Inventory", NBT.TAG_COMPOUND);
		inventory = new ItemStack[INVENTORY_SIZE];
		Arrays.fill(inventory, ItemStack.EMPTY);
		for (int i = 0; i < inv.tagCount(); i++) {
			NBTTagCompound tag = inv.getCompoundTagAt(i);
			ItemStack is = new ItemStack(tag);
			inventory[tag.getInteger("Slot")] = is;
		}
		buildClientData();
		
		NBTTagList ext = compound.getTagList("Extras", NBT.TAG_COMPOUND);
		extras = NonNullList.create();
		for (int i = 0; i < ext.tagCount(); i++) {
			NBTTagCompound tag = inv.getCompoundTagAt(i);
			ItemStack is = new ItemStack(tag);
			extras.add(is);
		}
	}
	
}
