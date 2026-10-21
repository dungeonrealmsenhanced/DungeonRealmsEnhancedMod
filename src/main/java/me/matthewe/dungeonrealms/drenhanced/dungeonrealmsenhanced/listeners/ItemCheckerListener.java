package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import com.google.gson.Gson;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Drop;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemChecker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.mob.MobChecker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.mob.MobTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

/** Created by Matthew E on 12/31/2018 at 7:39 PM for the project DungeonRealmsDREnhanced */
public class ItemCheckerListener {
  private ItemChecker itemChecker;
  private MobChecker<Zombie> zombieMobChecker;
  private MobChecker<Skeleton> skeletonMobChecker;
  private MobChecker<IronGolem> ironGolemMobChecker;
  private MobChecker<Wolf> wolfMobChecker;
  private Map<UUID, MobTracker> mobTrackerMap;

  public ItemCheckerListener() {
    this.itemChecker = new ItemChecker(this::onItemFind);
    mobTrackerMap = new ConcurrentHashMap<>();

    this.zombieMobChecker =
        new MobChecker<>(
            entityZombie -> {
              MobTracker mobTracker = new MobTracker();
              mobTracker.update(entityZombie);

              mobTrackerMap.put(entityZombie.getUUID(), mobTracker);
            },
            Zombie.class);
    this.ironGolemMobChecker =
        new MobChecker<>(
            entityIronGolem -> {
              MobTracker mobTracker = new MobTracker();
              mobTracker.update(entityIronGolem);

              mobTrackerMap.put(entityIronGolem.getUUID(), mobTracker);
            },
            IronGolem.class);

    this.wolfMobChecker =
        new MobChecker<>(
            entityIronGolem -> {
              MobTracker mobTracker = new MobTracker();
              mobTracker.update(entityIronGolem);

              mobTrackerMap.put(entityIronGolem.getUUID(), mobTracker);
            },
            Wolf.class);
    this.skeletonMobChecker =
        new MobChecker<>(
            entityZombie -> {
              MobTracker mobTracker = new MobTracker();
              mobTracker.update(entityZombie);

              mobTrackerMap.put(entityZombie.getUUID(), mobTracker);
            },
            Skeleton.class);
  }

  @SubscribeEvent
  public void onPlayerDrops(LivingDropsEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    if (!(event.getEntity() instanceof Player)) return;
    for (ItemEntity entityItem : event.getDrops()) {
      entityItem.addTag("M_CHECKED");
    }
  }

  @SubscribeEvent
  public void onRenderGameOverlayText(ClientTickEvent.Post event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    if (!ServerContext.active()) return;
    if (net.minecraft.client.Minecraft.getInstance().player.tickCount % 5 != 0) return;
    if (mobTrackerMap.size() > 2000) mobTrackerMap.clear();
    this.zombieMobChecker.update();
    this.skeletonMobChecker.update();
    this.wolfMobChecker.update();
    this.ironGolemMobChecker.update();
  }

  private void onItemFind(ItemEntity entityItem) {
    ItemStack itemStack = entityItem.getItem();

    if (ItemUtils.isWeapon(itemStack.getItem())) {
      ItemRarity itemRarity = ItemUtils.getRarity(itemStack);
      if (itemRarity == null) {
        return;
      }
      findItem(itemStack);

    } else if (ItemUtils.isArmor(itemStack.getItem())) {
      ItemRarity itemRarity = ItemUtils.getRarity(itemStack);
      if (itemRarity == null) {
        return;
      }
      findItem(itemStack);
    }
  }

  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    if (this.mobTrackerMap.containsKey(event.getEntity().getUUID())) {
      Entity target = event.getEntity();
      itemChecker.update();
      MobTracker mobTracker = this.mobTrackerMap.get(target.getUUID());
      mobTracker.death(target);
      mobTrackerMap.remove(target.getUUID());
      mobTrackerMap.put(target.getUUID(), mobTracker);
    }
  }

  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;

    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      Entity target = event.getTarget();
      if (target instanceof Player) {
        return;
      }
      if (this.mobTrackerMap.containsKey(target.getUUID())) {
        MobTracker mobTracker = this.mobTrackerMap.get(target.getUUID());
        mobTracker.update(target);
        if (mobTracker.getTier() > -1) {
          DRPlayer.drPlayer.setLastTier(Tier.getByNumber(mobTracker.getTier()));
        }
        mobTrackerMap.remove(target.getUUID());
        mobTrackerMap.put(target.getUUID(), mobTracker);
      }
    }
  }

  private void findItem(ItemStack itemStack) {
    Tier tier = ItemUtils.getTier(itemStack);
    if (tier.getNumber() > 0) {

      Drop drop =
          new Drop(
              tier,
              ItemUtils.getRarity(itemStack),
              (int) DRPlayer.drPlayer.getStatistics().getMobKillCount(tier),
              DRPlayer.drPlayer.getStatistics().getDryStreak(tier),
              new Gson().toJson(ItemData.tag(itemStack)));
      DRPlayer.drPlayer.addDrop(drop);
      DRPlayer.drPlayer.getStatistics().setDryStreak(tier, -1);
    }
  }
}
