package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Client.Client;
import me.marco.Client.ClientRank;
import me.marco.Quests.QuestProgression;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.DoubleStatementValue;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.SQLRepo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ClientRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE players (id INT NOT NULL AUTO_INCREMENT, " +
            "UUID VARCHAR(255), " +
            "Name VARCHAR(255), " +
            "Money DOUBLE," +
            "Clan VARCHAR(255), " +
            "ClientRank VARCHAR(255), " +
            "ClanRank VARCHAR(255), " +
            "PreviousName VARCHAR(255), " +
            "IP VARCHAR(255), " +
            "StartTime DOUBLE, " +
            "PRIMARY KEY (id))";

    public ClientRepo(Core instance) {
        super(instance);
        this.name = "players";
        this.createRepo();
    }

    public String getName() {
        return this.name;
    }

    public void loadData() {
        String query = "SELECT * FROM players";
        try {
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("Name");
                String uuid = rs.getString("UUID");
                double money = rs.getDouble("Money");
                String clan = rs.getString("Clan");
                String clientRank = rs.getString("ClientRank");
                String clanRank = rs.getString("ClanRank");
                String previousName = rs.getString("PreviousName");
                String ip = rs.getString("IP");
                double startTime = rs.getDouble("StartTime");
                System.out.println(name + " name | " + ClientRank.valueOf(clientRank) + " | rank");
                Client client = new Client(
                        name,
                        UUID.fromString(uuid),
                        money,
                        getInstance().getClanManager().getClan(clan),
                        ClientRank.valueOf(clientRank),
                        ClanRank.valueOf(clanRank),
                        previousName,
                        ip,
                        startTime
                );
                if (client.hasClan()) {
                    Clan clanObj = client.getClan();
                    clanObj.addMember(client);
                }
                System.out.println("Adding " + client.getName());
                getInstance().getClientManager().addClient(client);
//                if(client.getBuildsContainer() == null){
//                    getInstance().getSkillManager().generateDefaults(client);
//                }
//                getInstance().getSqlRepoManager().getClassBuildRepo().createClientBuild(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createRepo() {
        try {
            if (!this.getSQL().tableExists(this.name)) {
                System.out.println("Table " + this.name + " doesn't exist... Creating now!");
                System.out.println(createString);
                this.getSQL().executePreparedStatement(createString);
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createClient(Client client) {
        String query = "INSERT INTO players (UUID, Name, Money, Clan, ClientRank, ClanRank, PreviousName, IP, StartTime) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Statement statement = new Statement(query,
                new StringStatementValue(client.getUUID().toString()),
                new StringStatementValue(client.getName()),
                new DoubleStatementValue(client.getMoney()),
                new StringStatementValue(client.hasClan() ? client.getClan().getName() : null),
                new StringStatementValue(client.getClientRank().toString()),
                new StringStatementValue(client.getClanRank().toString()),
                new StringStatementValue(client.getPreviousName()),
                new StringStatementValue(client.getIP()),
                new DoubleStatementValue(client.getStartTime()));
        getSQL().addQuery(new Query(statement));
    }

    public void setClan(Client joining, ClanRank clanRank, Clan clan) {
        String query = "UPDATE players SET Clan=?, ClanRank=? WHERE uuid=?";
        Statement statement = new Statement(query,
                new StringStatementValue(clan.getName()),
                new StringStatementValue(clanRank.toString()),
                new StringStatementValue(joining.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void setClanLeave(Client client) {
        String query = "UPDATE players SET Clan=?, ClanRank=? WHERE uuid=?";
        Statement statement = new Statement(query,
                new StringStatementValue(null),
                new StringStatementValue(ClanRank.NOMAD.toString()),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void setClanRank(Client client, ClanRank clanRank) {
        String query = "UPDATE players SET ClanRank=? WHERE uuid=?";
        Statement statement = new Statement(query,
                new StringStatementValue(clanRank.toString()),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateMoney(Client client) {
        String query = "UPDATE players SET Money=? WHERE uuid=?";
        Statement statement = new Statement(query,
                new DoubleStatementValue(client.getMoney()),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

}
