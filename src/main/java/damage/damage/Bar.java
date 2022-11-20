package damage.damage;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class Bar extends onDamegeEvent{
    private BossBar bar;

    public Bar() {
    }

    public BossBar getBar() {
        return bar;
    }

    public void addPlayer(Player player){
        bar.addPlayer(player);
    }

    public void removePlayer(Player player){
        bar.removePlayer(player);
    }

    public void createBar(){
        this.bar = Bukkit.createBossBar("残り体力", BarColor.BLUE, BarStyle.SOLID);
        bar.setVisible(true);
    }
}
