package mail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Timer;

/**
 * This class manages all the emails that should be send and detects potential
 * problems that occur during the (attempted) transport of an email. If an EmailSender
 * takes too long to finish it's job it is interrupted and removed from the manager. This is
 * done in order to maintain adequate CPU usage and RAM control on the server. There
 * will be no new automatic attempts to send an email.
 * attempt to send that email. 
 * @author Wessel Jongkind
 * @version 01-06-2017
 */
public class MailManager implements ActionListener
{
    /**
     * This map contains all the MailSenders used to send emails, and the timestamp
     * in Unix in milliseconds at which the attempted transport has been started.
     */
    private static final HashMap<MailSender, Long> SENDERS = new HashMap<>();
    
    /**
     * The queue of MailSenders that still have to be activated.
     */
    private static final ArrayList<MailSender> QUEUE = new ArrayList<>();
    
    /**
     * The maximum amount of time an EmailSender is allowed to take for sending an email.
     */
    private static final int TIMEOUT_TRESHOLD = 10000;
    
    /**
     * The maximum amount of active MailSenders per CPU-core.
     */
    private static final int THREADS_PER_CORE = 3;
    
    /**
     * Timer-object used to detect potential problems with the sending of emails.
     */
    private static Timer timer;
    
    /**
     * The amount of MailSenders that are currently active.
     */
    private static int beingSend = 0;
    private static final int TIMER_INTERVAL = 1000;
    
    /**
     * Constructs a new MailManager which can be used to send emails.
     */
    public MailManager()
    {
        if(timer == null) {
            timer = new Timer(TIMER_INTERVAL, this);
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    /**
     * Adds a new email to the manager which it will attempt to send.
     * @param mail The email that should be send.
     */
    public void sendMail(Mail mail)
    {
        MailSender sender = new MailSender(mail);
        QUEUE.add(sender);
        
        timer.restart();
    }

    /**
     * This method checks wether an email has been send. If so it will automatically
     * remove the EmailSender and it's associated email from the MailManager. It
     * also checks wether or not a EmailSender thread takes too long to send an email.
     * If it takes too long then it will automatically be interrupted and destroyed. 
     * The program will not do another attempt at sending the email.
     * @param e The ActionEvent.
     * @see #TIMEOUT_TRESHOLD
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        try{
            checkSendMails();
            refreshQueue();
            
            //Will stop the manager to reduce CPU/RAM usage if it has nothing to monitor.
            if(SENDERS.size() == 0 && QUEUE.size() == 0) {
                timer = null;
            }
                timer.stop();
        }catch(Exception f){
            f.printStackTrace();
        }
    }
    
    private void checkSendMails() throws Exception
    {
        /*
            Iterates through all MailSender threads to see if they have 
            completed their task, failed their task or are still working.
        */
        Iterator<Map.Entry<MailSender, Long>> it = SENDERS.entrySet().iterator();
        while(it.hasNext())
        {
            /*
                Obtaining the new entry (MailSender with it's associated 
                starting-time) to be checked.
            */
            Map.Entry<MailSender, Long> entry = it.next();

            /*
                If an EmailSender took too long to send an email, it and the
                email that it was tasked to send will be removed from the
                manager.
            */
            if(entry.getValue() + TIMEOUT_TRESHOLD < System.currentTimeMillis())
            {
                beingSend--;
                ((MailSender) entry.getKey()).interrupt();
                it.remove();
                continue;
            }

            /*
                If a mail has succesfully been send, it's sender will be
                removed from the manager.
            */
            if(((MailSender) entry.getKey()).getCompleted())
            {
                beingSend--;
                SENDERS.remove(entry.getKey());
            }
        }
    }
    
    /**
     * Refreshes the queue of MailSenders.
     */
    private void refreshQueue()
    {
        Iterator<MailSender> it = QUEUE.iterator();
        int coreCount = Runtime.getRuntime().availableProcessors();
        
        while(it.hasNext() && beingSend <= THREADS_PER_CORE * coreCount)
        {
            MailSender sender = it.next();
            sender.start();
            beingSend++;
            QUEUE.remove(sender);
            SENDERS.put(sender, System.currentTimeMillis());
        }
    }
}
