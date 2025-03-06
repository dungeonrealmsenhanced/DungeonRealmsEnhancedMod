package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BuffListener implements Listener {



    private static List<Buff> activeBuffs = new ArrayList<>();


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player==null)return;

        final String unformattedText = event.getMessage().getUnformattedText();

        try {
            if (unformattedText.isEmpty()){
                if (System.currentTimeMillis()<delay1){
                    event.setCanceled(true);
                    return;
                }
            }
            if (unformattedText.toLowerCase().equalsIgnoreCase("there are no active buffs.")){

                activeBuffs =new ArrayList<>();

                if (System.currentTimeMillis()<delay){

                    event.setCanceled(true);
                }
                return;
            }

            if (!unformattedText.toLowerCase().contains("the event for") && (unformattedText.toLowerCase().contains("is active for")) && unformattedText.startsWith(">> ")) {
                String[] s = unformattedText.split(">> ")[1].trim().split(" ");
                String name = s[0].trim().replaceAll("'s", "").trim();
                String percent = s[1].trim();

                BuffType buffType = null;
                if (s[2].equalsIgnoreCase("Global") && s[3].equalsIgnoreCase("Experience") && s[4].equalsIgnoreCase("Buff")) {
                    buffType = BuffType.EXPERIENCE;
                } else if (s[2].equalsIgnoreCase("Global") && s[3].equalsIgnoreCase("Loot") && s[4].equalsIgnoreCase("Buff")) {
                    buffType = BuffType.LOOT;
                }

                String time = s[8];
                if (buffType != null) {
                    if (System.currentTimeMillis()<delay1){
                        event.setCanceled(true);
                        return;
                    }
                    removeActiveBuff(buffType);
                    try{
                        Buff buff = new Buff(Integer.parseInt(time.trim()), name.trim(), buffType, Integer.parseInt(percent.trim().replaceAll("\\+", "").replaceAll("%", "")));
//                        System.out.println(buff.toString());
                        activeBuffs.add(buff);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(new TextComponentString("(1) Failed to update buff time"));
        }
    }

    private void removeActiveBuff(BuffType buffType) {
        Buff toRemove = null;
        for (Buff activeBuff : activeBuffs) {
            if (activeBuff.getBuffType()==buffType){
                toRemove = activeBuff;
                break;
            }
        }
        if (toRemove!=null){
            activeBuffs.remove(toRemove);
        }
    }

    private static long delay = 0;
    private static long delay1 = 0;

    public static List<Buff> getActiveBuffs() {
        return activeBuffs;
    }


    public static void requestActiveBuffs() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player==null)return;

       try {
           if (System.currentTimeMillis()<delay1){
               return;
           }
           if (System.currentTimeMillis()<delay){
               return;
           }

           delay = System.currentTimeMillis()+ TimeUnit.SECONDS.toMillis(3);
           delay1 = System.currentTimeMillis()+ TimeUnit.SECONDS.toMillis(1);

           Minecraft.getMinecraft().player.sendChatMessage("/bufftime");
       } catch ( Exception e) {
           e.printStackTrace();
           player.sendMessage(new TextComponentString("(2) Failed to update buff time"));
       }


    }

}
