package mail;

import filedatareader.FileDataReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class represents emails which can be send by passing it to the MailManager.
 * @author Wessel Jongkind
 * @version 03-07-2017
 */
public class Mail
{
    /**
     * The recipients to which this email should be send.
     */
    private final ArrayList<String> recipients = new ArrayList<>();
    
    /**
     * The body of the email (a.k.a the main text)
     */
    private String body;
    
    /**
     * The title for this email.
     */
    private String title;
    
    /**
     * The attachment that should be send along with the email.
     */
    private File attachment;
    
    /**
     * The mail & password used to send the email
     */
    private final String from, password;
    
    /**
     * Email-address on which the owners of the application wish to receive
     * notifications.
     */
    private final String notificationAddress;
    
    /**
     * This object is used to keep track of the properties that should be used
     * to login with the given 'from' address and via which server and with what
     * settings it should send the email.
     */
    private Properties senderProperties;
    
    /**
     * Regex-code that is used to split property-lines in the configuration file
     * into a String array with index 0 being the key and index 1 being the value.
     */
    private static final String PROPERTIES_SPLITTER = "\\|\\|";
    
    /**
     * The path to the email configuration file.
     */
    private static final String MAIL_CONFIG_PATH =  "MailSettings.cfg";
    
    /**
     * Variables corresponding with the lines in the MailSettings.cfg file
     * that can be used to obtain data from the file.
     */
    private static final int FROM_INDEX = 0, PASSWORD_INDEX = 1, NOTIFICATION_INDEX = 2;
    
    /**
     * Instantiates a new  Mail object with the default sender settings.
     * @throws Exception When the email-settings could not be determined.
     */
    public Mail() throws Exception
    {
        FileDataReader reader = new FileDataReader();
        reader.setPath(new File(MAIL_CONFIG_PATH).getAbsolutePath());
        List<String> configLines = reader.getDataStringLines();
        
        //Set the send-data
        from = configLines.get(FROM_INDEX);
        password = configLines.get(PASSWORD_INDEX);
        notificationAddress = configLines.get(NOTIFICATION_INDEX);
        fillInProperties(configLines);
    }
    
    /**
     * Retrieves and fills in the properties that should be used to send this email.
     * @param configLines The lines of the configuration file
     */
    private void fillInProperties(List<String> configLines)
    {
        senderProperties = new Properties();
        
        for(int i = NOTIFICATION_INDEX + 1; i < configLines.size(); i++)
        {
            String[] property = configLines.get(i).split(PROPERTIES_SPLITTER);
            senderProperties.put(property[0], property[1]);
        }
    }
    
    /**
     * Sets the body of this email (Note: HTML is not supported)
     * @param body The body of the email.
     */
    public void setBody(String body)
    {
        this.body = body;
    }
    
    /**
     * Sets the title for this email.
     * @param title The title for this email.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * sets the attachment that should be send along with the email.
     * @param file The attachment that should be send along with the email.
     */
    public void setAttachment(File file)
    {
        this.attachment = file;
    }
    
    /**
     * Adds a recipient to which this email should be send.
     * @param email The address which should be added to the recipients-list.
     */
    public void addRecipient(String email)
    {
        recipients.add(email);
    }
    
    /**
     * Removes a recipient from the recipients-list.
     * @param email  The address which should be removed from the recipients-list.
     */
    public void removeRecipient(String email)
    {
        recipients.remove(email);
    }
    
    /**
     * Returns the email-address of the person that is set to send this message.
     * @return The email-address of the person that is set to send this message.
     */
    public String getFrom()
    {
        return from;
    }
    
    /**
     * Returns the password of the associated email-address that is set to send this message.
     * @return The password of the associated email-address that is set to send this message.
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * Returns the title of this email.
     * @return The title of this email.
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Returns the body of this email (plaintext as HTMl is not supported).
     * @return The body of this email.
     */
    public String getBody()
    {
        return body;
    }
    
    /**
     * Returns the recipients-list of this email. All items in this list are
     * the recipients their email-addresses.
     * @return The recipients-list.
     */
    public ArrayList<String> getRecipients()
    {
        return recipients;
    }
    
    /**
     * Obtains the notification email-address of the owners of the application.
     * @return The notification email-address of the owners of the application.
     */
    protected String getNotificationAddress()
    {
        return notificationAddress;
    }
    
    /**
     * Obtains the properties that determine how the email should be send.
     * @return The properties that determine how the email should be send.
     */
    public Properties getSenderProperties()
    {
        return senderProperties;
    }
    
    /**
     * Returns the file that is set to be send as an attachment with the email.
     * @return The file that is set to be send as an attachment with the email.
     */
    public File getAttachment()
    {
        return attachment;
    }
}
