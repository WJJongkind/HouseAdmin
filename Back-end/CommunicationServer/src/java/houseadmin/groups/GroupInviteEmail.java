/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.groups;

import houseadmin.data.Group;
import houseadmin.data.User;
import houseadmin.data.tables.Users;
import mail.Mail;

/**
 *
 * @author Wessel
 */
public final class GroupInviteEmail extends Mail {
    /**
     * The standard title for this email.
     */
    public static final String TITLE = "You Have Been Invited To Join A HouseAdmin Group!";
    

    public GroupInviteEmail(String email, User user, Group group) throws Exception
    {
        super();
        super.addRecipient(email);
        super.setTitle(TITLE);
        super.setBody(
                      "Hey there!\n\n"
                    + user.getName() + " has invited you to join a new HouseAdmin group called \"" + group.getName() + "\"."
                    + "\n\n"
                    + "Yours sincerely,\n"
                    + "CowLite"
        );
    }
}
