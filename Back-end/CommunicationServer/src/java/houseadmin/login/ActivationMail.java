package houseadmin.login;

import houseadmin.data.tables.Users;
import mail.Mail;

/**
 * Objects of this class can be used as predefined confirmation emails which
 * can be send to newly registered users to verify their account.
 * @author Wessel Jongkind
 * @version 27-08-2017
 */
public final class ActivationMail extends Mail
{
    /**
     * The standard title for this email.
     */
    public static final String TITLE = "Activate your HouseAdmin account";
    
    /**
     * Creates a new ActivationMail object.
     * @param email The email address to which the activation URL should be send.
     * @param activationCode The activationID corresponding to the account that is to be activated.
     * @param url The base-URL of the application.
     * @throws Exception If the email cannot be instantiated because no default sender can be determined.
     */
    public ActivationMail(String email, String activationCode, String url) throws Exception
    {
        super();
        this.addRecipient(email);
        this.setTitle(TITLE);
        this.setBody(
                      "Hello,\n\n"
                    + "With the link below you can activate your CowLite HouseAdmin account. "
                    + url + "/ActivationServlet?" + Users.ACTIVATION_ID + "=" + activationCode
                    + "\n\n"
                    + "Yours sincerely,\n"
                    + "CowLite"
        );
    }
    
}
