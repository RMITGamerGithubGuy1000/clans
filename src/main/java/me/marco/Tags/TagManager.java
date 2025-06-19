package me.marco.Tags;

import me.marco.Base.Core;
import me.marco.Client.Client;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private Core instance;

    public TagManager(Core instance){
        this.instance = instance;
    }

    private List<Tag> tagList = new ArrayList<Tag>();

    public void addTag(Client owner, Tag tag){
        owner.addTag(tag);
        tagList.add(tag);
    }

    public void addTagRemoveOld(Client owner, Tag tag){
        owner.addTagRemoveOld(tag);
        tagList.add(tag);
    }

    public void removeTags(List<Tag> toRemove){
        toRemove.forEach(tag -> {
            tag.getOwner().removeTag(tag);
            this.tagList.remove(tag);
        });
    }

//    public void handleTags() {
//        List<Tag> toRemove = new ArrayList<Tag>();
//        for(Tag tag : this.tagList){
//            if(tag.runTick()){
//                tag.onExpiry();
//                toRemove.add(tag);
//            }
//        }
//        toRemove.forEach(tag -> this.tagList.remove(tag));
//    }

    public void handleTags(){
        List<Tag> toRemove = new ArrayList<Tag>();
        for(Tag tag : getTagList()){
            if(tag.runTick()){
                toRemove.add(tag);
            }
        }
        removeTags(toRemove);
//        for(Player player : getInstance().getServer().getOnlinePlayers()){
//            Client client = getInstance().getClientManager().getClient(player);
//            client.handleTags();
//        }
    }

    public void handleDamageTagging(Player damager, Player target, String reason){
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if(!damagerClient.hasCombatTag()){
            getInstance().getChat().sendModule(damager, "You are now " + getInstance().getChat().highlightText + "Combat Tagged", "Combat");
        }
        damagerClient.removeCombatTag();
        CombatTag combatTag = new CombatTag(damagerClient, target, damager, reason, getInstance());
        damagerClient.setCombatTag(combatTag);

        Client targetClient = getInstance().getClientManager().getClient(target);
        PvPTag pvptag = new PvPTag(targetClient, target, damager, reason, getInstance());
        targetClient.removePvPTag();
        getInstance().getClientManager().getClient(target).setPvPTag(pvptag);
    }

    public Core getInstance() {
        return instance;
    }

    public List<Tag> getTagList() {
        return tagList;
    }
}
