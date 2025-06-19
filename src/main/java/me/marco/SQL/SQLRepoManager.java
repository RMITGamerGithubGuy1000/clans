package me.marco.SQL;

import me.marco.Base.Core;
import me.marco.SQL.SQLRepos.*;

import java.util.HashMap;

public class SQLRepoManager {

    private Core instance;

    public SQLRepoManager(Core instance){
        this.instance = instance;
    }

    private ClanRepo clanRepo;
    private ClientRepo clientRepo;
    private AllianceRepo allianceRepo;
    private EnemyRepo enemyRepo;
    private PillageRepo pillageRepo;
    private AdminRepo adminRepo;
    private ClassBuildRepo classBuildRepo;
    private FieldsRepo fieldsRepo;
    private QuestRepo questRepo;

    public void initialise(){
        clanRepo = new ClanRepo(instance);
        clientRepo = new ClientRepo(instance);
        allianceRepo = new AllianceRepo(instance);
        enemyRepo = new EnemyRepo(instance);
        pillageRepo = new PillageRepo(instance);
        adminRepo = new AdminRepo(instance);
        classBuildRepo = new ClassBuildRepo(instance);
        fieldsRepo = new FieldsRepo(instance);
        questRepo = new QuestRepo(instance);
    }

    public void loadRepos(){
        clanRepo.loadData();
        clientRepo.loadData();
        allianceRepo.loadData();
        enemyRepo.loadData();
        pillageRepo.loadData();
        adminRepo.loadData();
        classBuildRepo.loadData();
        fieldsRepo.loadData();
        questRepo.loadData();
    }

    public ClanRepo getClanRepo() {
        return clanRepo;
    }

    public ClientRepo getClientRepo() {
        return clientRepo;
    }

    public AllianceRepo getAllianceRepo() { return allianceRepo; }

    public EnemyRepo getEnemyRepo() { return enemyRepo; }

    public PillageRepo getPillageRepo() { return pillageRepo; }

    public AdminRepo getAdminRepo() { return adminRepo; }

    public ClassBuildRepo getClassBuildRepo() { return classBuildRepo; }

    public FieldsRepo getFieldsRepo() { return fieldsRepo; }

    public QuestRepo getQuestRepo() { return questRepo; }

}
