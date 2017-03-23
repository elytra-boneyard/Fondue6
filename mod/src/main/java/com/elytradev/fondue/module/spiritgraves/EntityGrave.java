package com.elytradev.fondue.module.spiritgraves;

import java.util.List;
import java.util.UUID;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.spiritgraves.client.ModuleSpiritGravesClient;
import com.elytradev.fruitphone.FruitPhone;
import com.elytradev.fruitphone.capability.FruitEquipmentCapability;
import com.elytradev.fruitphone.network.EquipmentDataPacket;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class EntityGrave extends Entity {

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(EntityGrave.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<String> OWNER_NAME = EntityDataManager.createKey(EntityGrave.class, DataSerializers.STRING);
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
			 1   // Fruit Glass
			;
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
	private NonNullList<ItemStack> extras = NonNullList.create();
	
	private GameProfile profile;
	
	public EntityGrave(World worldIn) {
		super(worldIn);
		setSize(0.5f, 0.5f);
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
	public boolean canBeCollidedWith() {
		return true;
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
		return true;
	}
	
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return null;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source instanceof EntityDamageSource) {
			EntityDamageSource eds = (EntityDamageSource)source;
			if (eds.getEntity() instanceof EntityPlayer) {
				if (isOwner((EntityPlayer)eds.getEntity())) {
					giveItems((EntityPlayer)eds.getEntity(), true);
					playSound(ModuleSpiritGraves.DISPEL, 1, 1);
					new GraveDispelMessage(eds.getEntity(), this).sendToAllWatching(this);
					setDead();
				}
			}
		}
		return false;
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
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand stack) {
		if (!world.isRemote && (player.isSneaking() || isOwner(player))) {
			giveItems(player, false);
			new GraveDispelMessage(player, this).sendToAllWatching(this);
			playSound(ModuleSpiritGraves.DISPEL, 1, 1);
			setDead();
		}
		return EnumActionResult.SUCCESS;
	}
	
	private void giveItems(EntityPlayer player, boolean displace) {
		Iterable<ItemStack> drops;
		if (displace) {
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
				player.inventory.mainInventory.set(i, inventory.get(i));
			}
			for (int i = 0; i < 4; i++) {
				player.inventory.armorInventory.set(i, inventory.get(36+i));
			}
			player.inventory.offHandInventory.set(0, inventory.get(40));
			for (int i = 0; i < 7; i++) {
				ibih.setStackInSlot(i, inventory.get(41+i));
			}
			if (fec != null) {
				fec.glasses = inventory.get(48);
				EquipmentDataPacket.forEntity(player).ifPresent(p -> {
					p.sendToAllWatching(player);
					p.sendTo(player);
				});
			} else {
				extras.add(inventory.get(48));
			}
			drops = extras;
		} else {
			drops = Iterables.concat(inventory, extras);
		}
		
		
		for (ItemStack is : drops) {
			if (is.isEmpty()) continue;
			if (!player.inventory.addItemStackToInventory(is)) {
				entityDropItem(is, 0.25f);
			}
		}
	}

	public GameProfile getOwner() {
		if (dataManager.get(OWNER).isPresent()) {
			if (profile == null) {
				profile = new GameProfile(dataManager.get(OWNER).get(), Strings.emptyToNull(dataManager.get(OWNER_NAME)));
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
			dataManager.set(OWNER_NAME, "");
		} else {
			dataManager.set(OWNER, Optional.fromNullable(owner.getId()));
			dataManager.set(OWNER_NAME, Strings.nullToEmpty(owner.getName()));
		}
		this.profile = owner;
	}
	
	public boolean isOwner(EntityPlayer ep) {
		UUID u = ep.getGameProfile().getId();
		if (u == null) return false;
		Optional<UUID> opt = dataManager.get(OWNER);
		return opt.isPresent() && opt.get().equals(u);
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
	
	public void populateFrom(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		inventory.clear();
		int i = 0;
		for (ItemStack is : inv.mainInventory) {
			inventory.set(i, is);
			i++;
		}
		for (ItemStack is : inv.armorInventory) {
			inventory.set(i, is);
			i++;
		}
		for (ItemStack is : inv.offHandInventory) {
			inventory.set(i, is);
			i++;
		}
		IBaublesItemHandler ibih = BaublesApi.getBaublesHandler(player);
		for (int j = 0; j < ibih.getSlots(); j++) {
			inventory.set(i, ibih.getStackInSlot(j));
			i++;
		}
		if (player.hasCapability(FruitPhone.CAPABILITY_EQUIPMENT, null)) {
			FruitEquipmentCapability fec = player.getCapability(FruitPhone.CAPABILITY_EQUIPMENT, null);
			inventory.set(i, fec.glasses);
			i++;
		}
		
		
		dataManager.set(SELECTED_SLOT, inv.currentItem);
		
		buildClientData();
		
		dataManager.set(RIGHT_HANDED, player.getPrimaryHand() == EnumHandSide.RIGHT);
		setOwner(player.getGameProfile());
	}
	
	public void clear(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		IBaublesItemHandler ibih = BaublesApi.getBaublesHandler(player);
		for (int j = 0; j < ibih.getSlots(); j++) {
			ibih.setStackInSlot(j, ItemStack.EMPTY);
		}
		
		if (player.hasCapability(FruitPhone.CAPABILITY_EQUIPMENT, null)) {
			FruitEquipmentCapability fec = player.getCapability(FruitPhone.CAPABILITY_EQUIPMENT, null);
			fec.glasses = ItemStack.EMPTY;
		}
		
		inv.mainInventory.clear();
		inv.armorInventory.clear();
		inv.offHandInventory.clear();
	}
	
	public boolean isEmpty() {
		if (!extras.isEmpty()) return false;
		for (ItemStack is : inventory) {
			if (is != null && !is.isEmpty()) return false;
		}
		return true;
	}
	
	private void buildClientData() {
		for (int j = 0; j < 9; j++) {
			setHotbarItem(j, inventory.get(j));
		}
		
		dataManager.set(OFFHAND, inventory.get(40));
		
		setItemStackToSlot(EntityEquipmentSlot.HEAD, inventory.get(39));
		setItemStackToSlot(EntityEquipmentSlot.CHEST,inventory.get(38));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, inventory.get(37));
		setItemStackToSlot(EntityEquipmentSlot.FEET, inventory.get(36));
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (Fondue.isModuleLoaded(ModuleSpiritGravesClient.class)) {
			Fondue.getModule(ModuleSpiritGravesClient.class).onUpdate(this);
		}
		if (posY < 5) {
			// Float out of the void
			motionY = 0.5;
		}
		motionX *= 0.5;
		motionY *= 0.5;
		motionZ *= 0.5;
		for (EntityPlayer ep : world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(16, 128, 16))) {
			double dist = new Vec3d(ep.posX, posY, ep.posZ).squareDistanceTo(posX, posY, posZ);
			if (!ep.isDead
					&& dist < 256
					&& ep.canEntityBeSeen(this)) {
				dist = ep.getDistanceSqToEntity(this);
				Vec3d look = ep.getLook(1).normalize();
				Vec3d diff = new Vec3d(posX - ep.posX, getEntityBoundingBox().minY + 0.25 - (ep.posY + ep.getEyeHeight()), posZ - ep.posZ);
				double len = diff.lengthVector();
				diff = diff.normalize();
				double dot = look.dotProduct(diff);
				if (dot > 1 - 0.025 / len) {
					if (dist > 3.7 || dist < 3.3) {
						double speed;
						if (dist < 3.5) {
							speed = 0.15;
						} else if (dist > 128) {
							speed = -0.5;
						} else {
							speed = -0.15;
						}
						double targetX = diff.xCoord*speed;
						double targetY = diff.yCoord*speed;
						double targetZ = diff.zCoord*speed;
						double diffX = targetX-motionX;
						double diffY = targetY-motionY;
						double diffZ = targetZ-motionZ;
						motionX += (diffX/8);
						motionY += (diffY/8);
						motionZ += (diffZ/8);
					}
				}
			}
		}
		
		move(MoverType.SELF, motionX, motionY, motionZ);
	}
	
	@Override
	protected void entityInit() {
		dataManager.register(OWNER, Optional.absent());
		dataManager.register(OWNER_NAME, "");
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
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setBoolean("RightHanded", dataManager.get(RIGHT_HANDED));
		compound.setByte("SelectedSlot", dataManager.get(SELECTED_SLOT).byteValue());
		
		Optional<UUID> owner = dataManager.get(OWNER);
		if (owner.isPresent()) {
			compound.setUniqueId("Owner", owner.get());
		}
		compound.setString("OwnerName", dataManager.get(OWNER_NAME));
		
		NBTTagList inv = new NBTTagList();
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack is = inventory.get(i);
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
		dataManager.set(RIGHT_HANDED, compound.getBoolean("RightHanded"));
		dataManager.set(SELECTED_SLOT, compound.getInteger("SelectedSlot"));
		
		if (compound.hasUniqueId("Owner")) {
			dataManager.set(OWNER, Optional.of(compound.getUniqueId("Owner")));
		} else {
			dataManager.set(OWNER, Optional.absent());
		}
		dataManager.set(OWNER_NAME, compound.getString("OwnerName"));
		profile = null;
		
		NBTTagList inv = compound.getTagList("Inventory", NBT.TAG_COMPOUND);
		inventory.clear();
		for (int i = 0; i < inv.tagCount(); i++) {
			NBTTagCompound tag = inv.getCompoundTagAt(i);
			ItemStack is = new ItemStack(tag);
			inventory.set(tag.getInteger("Slot"), is);
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

