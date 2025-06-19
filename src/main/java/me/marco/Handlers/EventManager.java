package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Events.Clans.TimedEvents.TimedThirtySecondsEvent;
import me.marco.Events.Skills.ClassCheckEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EventManager {

    private Core instance;

    public EventManager(Core instance){
        this.instance = instance;
    }

    public void runTimers(){
        new BukkitRunnable(){
            public void run(){
                instance.getCooldownManager().handleCooldowns();
            }
        }.runTaskTimerAsynchronously(instance, 0, 2);
        run1TickTimer();
//        run1SecTimer();
        runHalfSecondTimer();
        run30SecTimer();
        runFiveMinTimer();
    }

//    public void run1SecTimer(){
//        new BukkitRunnable(){
//            public void run() {
//                instance.getWorldBossManager().handleTicks();
//            }
//        }.runTaskTimer(instance, 0, 20);
//    }

    public void run30SecTimer(){
        new BukkitRunnable(){
            public void run(){
                instance.getServer().getPluginManager().callEvent(new TimedThirtySecondsEvent());
                instance.getFieldsManager().handleTasks();
            }
        }.runTaskTimer(instance, 0, 600);
    }

    public void run1TickTimer(){
        new BukkitRunnable(){
            public void run(){
                instance.getCooldownManager().handleActionBars();
                instance.getManaManager().handleChannelBar();
                instance.getSkillManager().handleChannelSkills();
                instance.getTagManager().handleTags();
                instance.getPvPItemManager().handleItemTasks();
                instance.getBlockChangeManager().handleBlockChanges();
                instance.getItemManager().handleItems();
                instance.getServer().getPluginManager().callEvent(new ClassCheckEvent());
                instance.getWorldBossManager().handleTicks();
            }
        }.runTaskTimer(instance, 0, 1);
    }

    public void runHalfSecondTimer(){
        new BukkitRunnable(){
            public void run(){
                instance.getManaManager().handleMana();
            }
        }.runTaskTimer(instance, 0, 10);
    }

    public void runFiveMinTimer(){
        new BukkitRunnable(){
            public void run(){

            }
        }.runTaskTimer(instance, 0, 6000);
    }

}
