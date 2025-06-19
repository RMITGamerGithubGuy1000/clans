package me.marco.Clans.Objects.Invites;

public class Invite {

    private Inviteable invited;
    private Inviteable inviter;
    private InviteType inviteType;

    public Invite(Inviteable invited, Inviteable inviter, InviteType inviteType){
        this.invited = invited;
        this.inviter = inviter;
        this.inviteType = inviteType;
    }

    public InviteType getInviteType() {
        return inviteType;
    }

    public void setInviteType(InviteType inviteType) {
        this.inviteType = inviteType;
    }

    public Inviteable getInviter() {
        return inviter;
    }

    public void setInviter(Inviteable inviter) {
        this.inviter = inviter;
    }

    public Inviteable getInvited() {
        return invited;
    }

    public void setInvited(Inviteable invited) {
        this.invited = invited;
    }

}
