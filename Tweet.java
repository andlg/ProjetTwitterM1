
import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tweet implements Serializable {

    private String id_tweet;
    private String id_user;
    private LocalDateTime date;
    private String mess;
    private String id_uretweet;
    private List<String> hashtag;

    //Constructeur
    public Tweet(String p_tweet, String p_user, LocalDateTime p_date, String p_mess, String p_retweet) {
        id_tweet = p_tweet;
        id_user = p_user;
        date = p_date;
        mess = p_mess;
        id_uretweet = p_retweet;
        setHashtag(getHashtagliste(mess));
    }
    
    //Affichage complet du tweet(En UTF 8)
    public String toString() {
        String string_iso = "ID: " + id_tweet + " - User: " + id_user + " - date: " + date + " \n mess: " + mess + " \n retweet: " + id_uretweet + "\n ------";
        String string_utf = new String(string_iso.getBytes(), Charset.forName("UTF-8"));
        return string_utf;
    }
    
    //Affichage des caracteristiques principales du tweet pour l'utilisateur 
    public String toStringPartiel() {
        String string_iso = date + "\n " + id_user + " a tweeté :  \n" + mess + " \n Retweet de: " + id_uretweet + "\n -------------";
        String string_utf = new String(string_iso.getBytes(), Charset.forName("UTF-8"));
        return string_utf;
    }
    
    //Accesseur date
    public LocalDateTime getDate() {
        return date;
    }
    
    //Accesseur retweet
    public String getRetweet() {
        return id_uretweet;
    }
    
    //Accesseur message
    public String getMess() {
        return mess;
    }
    
    //Accesseur hashtag
    public List<String> getHashtag() {
        return hashtag;
    }
    
    //Mutateur
    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }
    
    //Recupere dans une liste les chaines de characteres identifiees comme des hashtags
    public List<String> getHashtagliste(String message) {
        List<String> hash = new ArrayList<String>();
        //on met le message dans un tableau de characteres
        char[] Mess_Array = message.toCharArray();
        int i = 0;
        String s = "";
        for (char c : Mess_Array) {
            if (i == 0) {
                if (c == '#') {
                    s = "#";
                    i = 1;
                }
            } else {
                //Si le charactere est non alphabetique on considere que le hashtag est terminï¿½
                if (Character.isAlphabetic(c) == false) {
                    i = 0;
                    String sutf = new String(s.getBytes(), Charset.forName("UTF-8"));
                    hash.add(sutf.toLowerCase());
                } else {
                    s = s + c;
                }
            }
        }
        return hash;
    }
}
