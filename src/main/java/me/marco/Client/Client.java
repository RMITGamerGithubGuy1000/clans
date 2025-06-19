package me.marco.Client;

import me.marco.Clans.Objects.Invites.Inviteable;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Cosmetics.CosmeticProfile;
import me.marco.Quests.QuestProgression;
import me.marco.Quests.Questable;
import me.marco.Tags.CombatTag;
import me.marco.Tags.PvPTag;
import me.marco.Tags.Tag;
import me.marco.Scoreboard.uScoreboard;
import me.marco.Skills.Builders.BuildsContainer;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client implements Inviteable {

    private String name;
    private UUID uuid;
    private double money;
    private Clan clan;
    private ClientRank clientRank;
    private ClanRank clanRank;

    private QuestProgression questProgression;

    private String previousName;
    private String ip;
    private double startTime;

    private boolean adminMode = false;

    private ClassBuild activeClass;
    private BuildsContainer buildsContainer;

    private uScoreboard scoreboard;

    private PvPTag pvptag = null;
    private CombatTag combatTag = null;

    private ChannelSkill isChanneling = null;
    private boolean isInvulnerable = false;

    private List<Tag> tagList = new ArrayList<Tag>();

    private CosmeticProfile cosmeticProfile = null;

    public Client(Player player){
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.money = 5000;
        this.clientRank = ClientRank.DEFAULT;
        this.clanRank = ClanRank.NOMAD;
        this.previousName = player.getName();
        this.ip = player.getAddress().getHostName().toString();
        this.startTime = System.currentTimeMillis();
        this.questProgression = new QuestProgression();
        this.cosmeticProfile = new CosmeticProfile(this);
    }

    public Client (String name, UUID uuid, double money, Clan clan, ClientRank clientRank, ClanRank clanRank, String previousName, String ip, double startTime){
        this.name = name;
        this.uuid = uuid;
        this.money = money;
        this.clan = clan;
        this.clientRank = clientRank;
        this.clanRank = clanRank;
        this.previousName = previousName;
        this.ip = ip;
        this.startTime = startTime;
        this.questProgression = new QuestProgression();
        this.cosmeticProfile = new CosmeticProfile(this);
    }

    public String getName(){
        return this.name;
    }

    public UUID getUUID(){
        return this.uuid;
    }

    public double getMoney() { return this.money; }

    public void addMoney(double amount) { this.money += amount; }

    public void removeMoney(double amount) { this.money -= amount; }

    public void setClan(Clan clan){
        this.clan = clan;
    }

    public Clan getClan(){
        return this.clan;
    }

    public boolean hasClan(){
        return this.clan != null;
    }

    public ClientRank getClientRank() { return this.clientRank; }

    public ClanRank getClanRank(){
        return this.clanRank;
    }

    public void setClanRank(ClanRank clanRank){
        this.clanRank = clanRank;
    }

    public String getPreviousName() { return this.previousName; }

    public void setPreviousName(String name) { this.previousName = previousName; }

    public String getIP(){ return this.ip; }

    public void setIP(String ip) { this.ip = ip; }

    public double getStartTime() { return this.startTime; }

    public Player getPlayer(){
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean isOnline(){
        return getPlayer() != null;
    }

    public boolean isClanAdmin(){
        return this.clan != null && (this.clanRank == ClanRank.ADMIN || this.isClanOwner());
    }

    public boolean isClanOwner(){
        return this.clan != null && this.clan.getOwner().equals(this.getUUID());
    }

    public boolean isAdminMode() { return this.adminMode; }

    public void toggleAdminMode() { this.adminMode = !this.adminMode; }

    public ClassBuild getActiveBuild(){
        return this.activeClass;
    }

    public ClassBuild getActiveClassBuild(eClassType classType){
        return this.buildsContainer.getActiveBuild(classType);
    }

    public void setActiveBuild(ClassBuild activeClass){
        this.activeClass = activeClass;
    }

    public boolean hasActiveBuild(){ return this.activeClass != null; }

    public void setBuildsContainer(BuildsContainer buildsContainer){
        this.buildsContainer = buildsContainer;
    }

    public BuildsContainer getBuildsContainer() { return this.buildsContainer; }

//    public void addPotionEffect(PotionEffectType potionEffectType, int duration, int level){
//        getPlayer().addPotionEffect(new PotionEffect(potionEffectType, duration, level));
//    }

    public void removePotionEffect(PotionEffectType potionEffectType){
        Player player = getPlayer();
        for(PotionEffect potionEffect :player.getActivePotionEffects()){
            if(potionEffect.getType() == potionEffectType) player.removePotionEffect(potionEffectType);
        }
    }

    public boolean hasSkill(Skill skill){
        if(this.getActiveBuild() == null) return false;
        return this.getActiveBuild().hasSkill(skill);
    }

    public boolean isFriendly(Client check){
        if(!this.hasClan()) return false;
        if(!check.hasClan()) return false;
        Clan checkClan = check.getClan();
        return this.clan == checkClan || this.clan.isAllied(checkClan);
    }

    public uScoreboard getScoreboard(){
        return this.scoreboard;
    }

    public void setScoreboard(uScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    public boolean hasScoreboard(){
        return this.scoreboard != null;
    }

    public void updateScoreboard(){
        getScoreboard().refresh();
    }

    public PvPTag getPvPTag(){
        return this.pvptag;
    }

    public void setPvPTag(PvPTag pvptag){
        this.pvptag = pvptag;
        this.tagList.add(pvptag);
    }

    public boolean hasPvPTag(){
        return this.pvptag != null && !this.pvptag.isExpired();
    }

    public void removePvPTag(){
        this.tagList.remove(this.pvptag);
        this.pvptag = null;
    }

    public void setCombatTag(CombatTag combatTag){
        this.combatTag = combatTag;
        this.tagList.add(combatTag);
    }

    public boolean hasCombatTag() {
        return this.combatTag != null && !this.combatTag.isExpired();
    }

    public void removeCombatTag(){
        this.tagList.remove(this.combatTag);
        this.combatTag = null;
    }

    public boolean isChanneling() {
        return isChanneling != null;
    }

    public ChannelSkill getIsChanneling(){ return this.isChanneling; }

    public void setChanneling(ChannelSkill channeling) {
        isChanneling = channeling;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void setInvulnerable(boolean isInvulnerable) {
        this.isInvulnerable = isInvulnerable;
    }

    public CombatTag getCombatTag() {
        return combatTag;
    }

    public Tag getTag(String tagName){
        return this.tagList.stream().filter(tag -> tag.getName().equalsIgnoreCase(tagName)).findFirst().orElse(null);
    }

    public Tag getTag(Tag tagToCheck){
        return this.tagList.stream().filter(tag -> tag.equals(tagToCheck)).findFirst().orElse(null);
    }

    public boolean hasTag(String tagName){
        return getTag(tagName) != null;
    }

    public boolean hasTag(Tag tag){
        return getTag(tag) != null;
    }

    public void removeTag(String tagName) {
        Tag tag = getTag(tagName);
        getTag(tagName).setForceExpired();
        removeTag(tag);
    }

    public void removeTag(Tag tag) {
        this.tagList.remove(tag);
    }

    public void addTag(Tag tag){
        if(hasTag(tag.getName())) return;
        this.tagList.add(tag);
    }

    public void addTagRemoveOld(Tag tag){
        if(hasTag(tag.getName())) removeTag(tag.getName());
        this.tagList.add(tag);
    }

    public boolean hasQuest(Questable quest) {
        return this.questProgression.hasQuest(quest) && !this.questProgression.getWrapper(quest).isComplete();
    }

    public QuestProgression getQuestProgression() {
        return this.questProgression;
    }

    public void setQuestProgression(QuestProgression questProgression) {
        this.questProgression = questProgression;
    }

    public boolean hasAdminLevel(ClientRank clientRank) {
        return this.clientRank.getPower() >= clientRank.getPower();
    }

    public void setCosmeticProfile(CosmeticProfile cosmeticProfile){
        this.cosmeticProfile = cosmeticProfile;
    }

    public CosmeticProfile getCosmeticProfile(){
        return this.cosmeticProfile;
    }

}
