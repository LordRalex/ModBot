package us.lordralex.modbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import org.pircbotx.PircBotX;

public class Mail extends Thread {

    List<String> threads = new ArrayList<>();
    Session session;
    Store store;

    public Mail() throws NoSuchProviderException {
        this.setName("Mail_Thread");
        session = Session.getDefaultInstance(System.getProperties(), null);
        store = session.getStore("imap");
    }

    @Override
    public void run() {
        PircBotX driver = Main.getIrc().getDriver();
        while (driver.isConnected()) {
            synchronized (this) {
                Folder inbox = null;
                try {
                    String tempV;
                    store.connect(
                            ((tempV = Config.getStringFromFile("config", "hostname")) == null) ? "localhost" : tempV,
                            ((tempV = Config.getStringFromFile("config", "username")) == null) ? "user" : tempV, 
                            ((tempV = Config.getStringFromFile("config", "password")) == null) ? "pass" : tempV);
                    inbox = store.getFolder("Inbox");
                    inbox.open(Folder.READ_WRITE);
                    Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                    for (Message message : messages) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()))) {
                            List<String> temp = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                temp.add(line);
                            }
                            threads.add(temp.get(3)
                                    .replace("has just posted a new topic entitled", "posted")
                                    .replace("\" in forum \"", "\" in \"")
                                    .replace("has just posted a reply to a topic that you have subscribed to titled \"", "posted a reply to ")
                                    .replace("The topic can be found here: ", "")
                                    + " "
                                    + temp.get(temp.size() - 8).split("-")[0]
                                    + "-");
                            FileExamine examine = new FileExamine();
                            examine.start(temp.get(temp.size() - 8).split("-")[0] + "-");
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                        message.setFlag(Flags.Flag.SEEN, true);
                    }
                    Thread thread = Main.getIrcThread();
                    synchronized (thread) {
                        thread.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                } finally {
                    if (inbox != null) {
                        try {
                            inbox.close(true);
                        } catch (MessagingException ex) {
                            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        store.close();
                    } catch (MessagingException ex) {
                        Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    wait(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<String> getThreads() {
        List<String> old = new ArrayList<>();
        old.addAll(threads);
        threads.clear();
        return old;
    }
}
