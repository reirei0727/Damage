package damage.damage;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class onDamegeEvent implements Listener {
    private boolean s = true;
    private double progress = 1.0;
    private Bar bar;

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        EntityType type = e.getEntityType();
        Player p = (Player)e.getEntity();

        final int count = 10;
        if(type == EntityType.PLAYER){
            ItemStack h = p.getInventory().getItemInMainHand();
            if (h.getType() == Material.STICK) {
                Bukkit.getOnlinePlayers().forEach((player) -> {
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1,1);
                    player.sendTitle(ChatColor.AQUA + "GAME CLEAR!!!", "ダメージ受けるの禁止世界", 40, 110, 40);
                    return;
                });

            } else{

                World w = p.getWorld();
                if (!s) return;
                s = false;
                // 0.1ずつ引いていくと0.7になった時点で表示が0.700000000001..みたいな感じになったので対策です
                // https://www.delftstack.com/ja/howto/java/how-to-round-a-double-to-two-decimal-places-in-java/#math.rounddouble100.0%2f100.0-%25E3%2582%2592%25E7%2594%25A8%25E3%2581%2584%25E3%2581%259F-double-%25E3%2581%25AE%25E5%25B0%258F%25E6%2595%25B0%25E7%2582%25B9%25E4%25BB%25A5%25E4%25B8%258B-2-%25E6%25A1%2581%25E3%2581%25B8%25E3%2581%25AE%25E4%25B8%25B8%25E3%2582%2581
                new BukkitRunnable() {
                    int i = count;

                    @Override
                    public void run() {
                        if (i == 0) {
                            if (bar == null) {
                                bar = new Bar();
                                bar.createBar();
                                Bukkit.getOnlinePlayers().forEach((player) -> {
                                    bar.addPlayer(player);
                                });
                                // progressの監視をしなきゃいけないので0以下になるまでRunnableを走らせます
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (progress <= 0) {
                                            Bukkit.getOnlinePlayers().forEach((player) -> {
                                                bar.removePlayer(player);
                                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH,1,1);
                                                player.sendTitle(ChatColor.DARK_PURPLE + "GAME OVER", "ダメージを受けるの禁止世界", 40, 250, 40);
                                            });
                                            bar = null;
                                            progress = 1.0;
                                            this.cancel();
                                            return;
                                        }
                                        bar.getBar().setProgress(progress);
                                    }
                                }.runTaskTimer(Damage.plugin, 0, 2);
                            }
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            if (hand.getType() == Material.DIAMOND) {
                                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() -1);
                                Bukkit.broadcastMessage("ダイアで防ぎました");
                                p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE,1,1);
                                s = true;
                                cancel();
                                return;
                            }
                            Location l = p.getLocation();
                            w.createExplosion(l, 100, false, false);
                            s = true;
                            progress = Math.round((progress - 0.1) * 10.0) / 10.0;
                            Bukkit.broadcastMessage("現在の値 : " + progress );
                            this.cancel();
                            return;
                        }
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            Location loc = player.getLocation();
                            loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
                            player.sendMessage(ChatColor.RED + p.getName() + "の禁止行為を検知しました。 爆発まで : " + i);
                        }
                        i--;
                    }
                }.runTaskTimer(Damage.plugin, 600L, 20L);
            }

        }

    }



    @EventHandler
    public void onFakeEvent(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();
        final int count = 10;
        if(b.getType() == Material.CRAFTING_TABLE){
            if(s==true){
                s = false;
                BukkitRunnable task = new BukkitRunnable() {
                    int i = count;
                    @Override
                    public void run() {
                        if(i==0){
                            Location l = p.getLocation();
                            p.sendTitle("うっそぴょ～ん", "", 20, 100, 20);
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_PIGLIN_JEALOUS,1,1);
                            s = true;
                            cancel();
                            return;
                        }
                        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                            onlinePlayer.getLocation().getWorld().playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP,1,10);
                            onlinePlayer.sendMessage(ChatColor.RED + p.getName() + " の禁止行為を検知しました。爆発まで："+i);
                        }
                        i--;
                    }
                };
                task.runTaskTimer(Damage.plugin,200L,20L);
            }

        }
    }
}
