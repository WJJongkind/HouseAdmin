package mail;

import java.util.ArrayList;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



/**
 * This class is used to send an email that has been given to it. The class
 * has been made protected (rather than public) as it is unwanted that this class
 * is used by anything else than the MailManager class. The reason for this is
 * to prevent accidental mistakes in the usage of the mail-system which may
 * lead to potential performance issues on the server.
 * @author Wessel Jongkind
 * @version 03-07-2017
 */
class MailSender extends Thread
{
    /**
     * The email that the sender has been tasked to send.
     */
    private final Mail mail;
    
    /**
     * Whether or not the sender has completed it's task.
     */
    private boolean completed = false;
    
    /**
     * Error that should be thrown when no email has been set to send. This can happen
     * when a null value is passed to the constructor.
     */
    private static final String NO_MAIL_ERROR = "Cannot send email because no email has been set to send.";
    
    /**
     * Constructs a new MailSender thread which will attempt to send an email.
     * @param mail The mail to be send.
     */
    public MailSender(Mail mail)
    {
        this.mail = mail;
    }
    
    /**
     * Don't call run() to start this sender. Rather than that; call start()
     * so that this sender will start on a new Thread.
     * @see #start() 
     */
    @Override
    public void run()
    {
        sendMail();
    }
    
    /**
     * Attempts to send an email to the recipients provided with it.
     */
    private void sendMail()
    {
        if(mail == null)
            new Exception(NO_MAIL_ERROR).printStackTrace();
        
        //Set up the correct properties to connect to the smtp server.
        Session session = getSession();
        
        // Construct the message
        Message msg = new MimeMessage(session);
        try {
            //Gets all addresses.
            Address[] tofull = getAdresses(mail.getRecipients());

            //Sets all the data for the email
            msg.setFrom(new InternetAddress(mail.getFrom()));
            msg.setRecipients(Message.RecipientType.TO, tofull);
            msg.setSubject(mail.getTitle());
            
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(mail.getBody());

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            if(mail.getAttachment() != null)
            {             // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(mail.getAttachment());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(mail.getAttachment().getName());
                multipart.addBodyPart(messageBodyPart);
            }
             
            msg.setContent(multipart);
            
            // Send the message.
            System.out.println("Transporting...");
            Transport.send(msg);
            System.out.println("Transported...");
            completed = true;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Tells whether or not this thread has finished sending the email assigned
     * to it.
     * @return True if it has send the email, false if it hasn't.
     */
    public boolean getCompleted()
    {
        return completed;
    }
    
    /**
     * Creates a new email-session which is used to send an email.
     * @return The associated email-session.
     */
    private Session getSession()
    {
        Session session = Session.getInstance(mail.getSenderProperties(), new SmtpAuthenticator(mail.getFrom(), mail.getPassword()));
        
        return session;
    }
    
    /**
     * Converts an ArrayList of email-addresses to a String array.
     * @param mails The ArrayList to be converted.
     * @return The array reflecting the ArrayList.
     * @see java.util.ArrayList
     */
    private InternetAddress[] getAdresses(ArrayList<String> mails)
    {
        InternetAddress[] ads = new InternetAddress[mails.size()];
        try{
            int i = 0;
            for(String mail : mails)
            {
                ads[i] = new InternetAddress(mails.get(i));
                i++;
            }
        }catch(Exception e){e.printStackTrace();}
            return ads;
    } 
    
    /**
     * This class handles authentication required to send email.
     * @author Wessel Jongkind
     * @version 01-06-2017
     */
    private static class SmtpAuthenticator extends Authenticator
    {
        /**
         * The sender's email address.
         */
        private String from;
        
        /**
         * The sender's password associated with it's email address.
         */
        private String pw;
        
        /**
         * Creates a new authenticator used to authenticate an email-sender.
         * @param from The sender's email-address
         * @param password The password associated with the sender's email-address.
         */
        public SmtpAuthenticator(String from, String password)
        {
            super();
            this.from = from;
            this.pw = password;
        }

        /**
         * Authenticates the sender so that it's account can be used to send an email.
         * @return 
         */
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
         String username = from;
         String password = pw;
            if ((username != null) && (username.length() > 0) && (password != null) && (password.length   () > 0))
                return new PasswordAuthentication(username, password);
            
            return null;
        }
    }
}