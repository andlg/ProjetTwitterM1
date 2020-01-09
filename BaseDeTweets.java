
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class BaseDeTweets {

    ArrayList<Tweet> liste;

    //Instanciation de la liste de tweets
    public void initialise() {
        liste = new ArrayList<Tweet>();
    }

    //Recuperation des premiere ou derniere date du jeu de donnees
    public LocalDate date(int choix) {
        //Recuperer premiere date de la liste
        if (choix == 0) {
            return liste.get(0).getDate().toLocalDate();
        } //Recuperer dernière date de la liste 
        else {
            return liste.get(liste.size() - 1).getDate().toLocalDate();
        }
    }

    //Affichage du jeu de donnees entier 
    public String lire() {
        String sortie = "\n";
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext()) {
            Tweet t = it.next();
            String s = t.toStringPartiel();
            sortie = sortie + s + "\n";
        }
        return sortie;
    }
    
    //Affichage des tweets d'un jour donne
    public String lire(LocalDate d) {
        String sortie = "\n";
        boolean sup = false;
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext() & sup == false) {
            Tweet t = it.next();
            if (t.getDate().toLocalDate().equals(d)) {
                String s = t.toStringPartiel();
                sortie = sortie + s + "\n";
            }
            if (t.getDate().toLocalDate().isAfter(d)) {
                sup = true;
            }
        }
        return sortie;
    }

    //Affichage des tweets entre 2 dates 
    public String lire(LocalDateTime d1, LocalDateTime d2) {
        String sortie =  "\n";
        boolean sup = false;
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext() & sup == false) {
            Tweet t = it.next();
            if (t.getDate().isAfter(d1) & t.getDate().isBefore(d2)) {
                String s = t.toStringPartiel();
                sortie = sortie + s + "\n";
            }
            if (t.getDate().isAfter(d2)) {
                sup = true;
            }
        }
        return sortie;
    }

    //Affichage des nb premiers tweets
    //Fonction non utilise dans le programme final mais utile pour le debogage
   /* public String lire(int nb) {
        String sortie = "Liste : \n";
        int i = 1;
        Iterator<Tweet> it = liste.iterator();

        while (it.hasNext()
                & i <= nb) {
            Tweet t = it.next();
            String s = t.toString();
            sortie = sortie + s + "\n";
            i = i + 1;
        }
        sortie = sortie + "dernier" + liste.get(liste.size() - 1);
        return sortie;
    }
    */

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();

    }

    //Ajout d'un tweet
    public void ajoute(Tweet t) {
        liste.add(t);
    }

    //Top 5 des utilisateurs retweetes sur 1 jour 
    public String lireRetweet(LocalDate date) {

        //Compte des retweets par utilisateur 
        Iterator<Tweet> it = liste.iterator();
        TreeMap<String, Integer> tmap = new TreeMap<String, Integer>();
        while (it.hasNext()) {
            Tweet t = it.next();
            if (t.getDate().toLocalDate().equals(date)) {
                String s = t.getRetweet();
                //On exclu les tweets sans valeur dans le champs retweet
                if (s.length() > 0) {
                    //Si l'utilisateur est deja dans la liste: 
                    //incrementation du nombre d'occurence pour cet utilisateur 
                    if (tmap.containsKey(s)) {
                        int nb = tmap.get(s);
                        //hmap.remove(s);
                        tmap.put(s, nb + 1);
                    } //sinon inscription de l'utilisateur dans la liste 
                    else {
                        tmap.put(s, 1);
                    }
                }
            }
        }
        String sortie = "";

        //Creation de la liste pour le classement/TOP
        ArrayList al = new ArrayList();
        int max = 0;
        String ind = "null";
        
        //On veut le classement des 5 utilisateurs les plus retweetes
        for (int j = 1; j <= 5; j++) {
            //Parcours de la map precedemment creee
            //Recherche de l'utilisateur le plus retweete
            for (Map.Entry mapentry : tmap.entrySet()) {
                if (Integer.parseInt(mapentry.getValue().toString()) > max) {
                    max = Integer.parseInt(mapentry.getValue().toString());
                    ind = mapentry.getKey().toString();
                }
            }
            //Ajout de l'utilisateur au classement (+ nombre de retweets)
            al.add(ind + " - " + max);
            //On enleve cet utilisateur de la map pour pouvoir chercher le nouveau maximum
            tmap.remove(ind);
            max = 0;
        }

        //Parcours du TOP
        Iterator it2 = al.iterator();
        String lignetop;
        while (it2.hasNext()) {
            lignetop = it2.next().toString();
            sortie = sortie + lignetop + "\n\n";
        }
        return sortie;
    }

    //Top 5 des utilisateurs retweetes sur 1 jour    
    public String lireRetweet(LocalDate d1, LocalDate d2) {

        Iterator<Tweet> it = liste.iterator();
        TreeMap<String, Integer> tmap = new TreeMap<String, Integer>();
        while (it.hasNext()) {
            Tweet t = it.next();
            //Si la date du tweet est dans la periode selectionnee
            if (t.getDate().isAfter(d1.atStartOfDay())
            & t.getDate().isBefore(d2.plusDays(1).atStartOfDay())) {
                String s = t.getRetweet();
                //Si l'utilisateur est deja dans la liste: 
                //incrementation du nombre d'occurence pour cet utilisateur
                if (tmap.containsKey(s)) {
                    int nb = tmap.get(s);
                    tmap.put(s, nb + 1);
                } 
                //sinon inscription de l'utilisateur dans la liste 
                else {
                    tmap.put(s, 1);
                }
            }
        }

        String sortie = "";
        ArrayList al = new ArrayList();
        DecimalFormat nf = new DecimalFormat("000000");
        int max = 0;
        String ind = "null";
        for (int j = 1; j <= 5; j++) {
            for (Map.Entry mapentry : tmap.entrySet()) {
                //On enleve les retweets vides
                if (mapentry.getKey().toString().length() != 0) {
                    if (Integer.parseInt(mapentry.getValue().toString()) > max) {
                        max = Integer.parseInt(mapentry.getValue().toString());
                        ind = mapentry.getKey().toString();
                        
                    }
                }
            }
            al.add(ind + " - " + max);
            tmap.remove(ind);
            max = 0;
        }

        Iterator it2 = al.iterator();
        String srt;
        while (it2.hasNext()) {
            srt = it2.next().toString();
            sortie = sortie + srt + "\n";
        }
        return sortie;
    }

    //Popularite d'un utilisateur 
    public TreeMap lireRetweet(String stuser) {
    
        TreeMap<LocalDate, Integer> tmap = new TreeMap<LocalDate, Integer>();
        // Initialisation du TreeMap --> pour avoir axe des abscisses complet
        Iterator<Tweet> it2 = liste.iterator();
        while (it2.hasNext()) {
            Tweet t = it2.next();
            tmap.put(t.getDate().toLocalDate(), 0);
        }

        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext()) {
            Tweet t = it.next();
            //Recuperation du jour du tweet 
            LocalDate d = t.getDate().toLocalDate();
            //S'il s'agit d'un retweet de l'utilisateur choisi
            if (t.getRetweet().contains(stuser)) {
                //Augmentation du nombre de retweet su rla journee
                int nb = tmap.get(d);
                int nb2 = nb + 1;
                tmap.replace(d, nb + 1);
                
            }
        }
        return tmap;
    }

    //Popularite d'un utilisateur sur un jour precis
    public TreeMap lireRetweet(String stuser, LocalDate date) {

        TreeMap<Integer, Integer> tmap = new TreeMap<Integer, Integer>();
        // Initialisation du TreeMap --> pour avoir axe des abscisses complet
        Iterator<Tweet> it2 = liste.iterator();
        while (it2.hasNext()) {
            Tweet t = it2.next();
            tmap.put(t.getDate().getHour(), 0);
        }

        LocalDateTime dt = date.atStartOfDay();
        //On part du creneau 0H-1H
        int nb = 0;
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext() & (dt.toLocalDate().isBefore(date) || dt.toLocalDate().equals(date))) {
            Tweet t = it.next();
            //Recuperation de l'heure du tweet 
            int d = t.getDate().getHour();

            //S'il s'agit d'un retweet de l'utilisateur choisi le jour voulu
            if (t.getRetweet().contains(stuser) && dt.toLocalDate().equals(date)) {
                if (d == dt.getHour()) {
                    //Augmentation du nombre de retweet sur l'heure
                    nb = nb + 1;
                }
                if (d > dt.getHour()) {
                    //On associe le nombre de retweets au creneau
                    tmap.put(d, nb);
                    dt = t.getDate();
                    // On augmente le nombre de retweets dans le creneau suivant 
                    nb = 1;
                }
            }
        }
        for (Map.Entry mapentry : tmap.entrySet()) {
			System.out.println("clé: "+mapentry.getKey() 
			+ " | valeur: " + mapentry.getValue());
		}
        return tmap;
    }

    //Liste des utilisateurs retweetes
    public TreeSet listeRetweet() {
        Iterator<Tweet> it = liste.iterator();
        TreeSet hset = new TreeSet();
        while (it.hasNext()) {
            Tweet t = it.next();
            String s = t.getRetweet();
            //On verifie qu'il ne s'agit pas d'un tweet avec le champs retweet vide
            if (s.length()>0) {
                hset.add(s);
            }
        }
        return hset;
    }

    //Nombre de tweets entre 2 jours 
    public TreeMap nbTweets(LocalDate d1, LocalDate d2) {

        TreeMap<LocalDate, Integer> tmap = new TreeMap<LocalDate, Integer>();
        //Compteur du nombre de tweets 
        int nb = 0;
        //Nombre total de tweets sur la periode
        int tot = 0;
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext() & (d1.isBefore(d2) || d1.equals(d2))) {
            Tweet t = it.next();
            if (t.getDate().toLocalDate().equals(d1)) {
                nb = nb + 1;
            }
            if (t.getDate().toLocalDate().isAfter(d1)) {
                tot = tot + nb;
                //On associe le nombre de tweets au jour associe
                tmap.put(d1, nb);
                nb = 1;
                d1 = t.getDate().toLocalDate();
            }
        }
        return tmap;
    }

    //Nombre de tweets par heure
    public TreeMap nbTweets(LocalDate d) {

        TreeMap<Integer, Integer> tmap = new TreeMap<Integer, Integer>();
        int i;
        //Initialisation de la map
        for (i = 0; i <= 23; i++) {
            tmap.put(i, 0);
        }
        
        LocalDateTime dt = d.atStartOfDay();
        int nb = 0;
        int h = 0;
        int tot = 0;
        //Dernier creneau: creneau impossible 25 si pas passe dans la boucle 
        int lasth = 25;
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext() & (dt.toLocalDate().isBefore(d) || dt.toLocalDate().equals(d))) {
            Tweet t = it.next();
            if (t.getDate().toLocalDate().equals(d)) {

                if (t.getDate().getHour() == dt.getHour()) {
                    nb = nb + 1;
                    lasth = dt.getHour();
                }

                if (t.getDate().getHour() > dt.getHour()) {
                    tot = tot + nb;
                    //Ajout du couple heure-nombre de tweets a la map
                    tmap.replace(dt.getHour(), nb);
                    nb = 1;
                    dt = t.getDate();
                }
            }
        }
        //Ajout du dernier creneau a la map
        if (lasth != 25) {
            tmap.replace(lasth, nb);
        }
        return tmap;
    }

    public String[][] hashtagListe(LocalDate d1, LocalDate d2) {
        List<String> hash = new ArrayList<String>();
        int[] occurence = new int[1000000];
        for (int i = 0; i < 10000; i++) {
            occurence[i] = 0;
        }
        Iterator<Tweet> it = liste.iterator();
        while (it.hasNext() & (d1.isBefore(d2) || d1.equals(d2))) {
            Tweet t = it.next();
            List<String> tag = t.getHashtag();
            if ((t.getDate().toLocalDate().equals(d1)) || (t.getDate().toLocalDate().isAfter(d1))) {
                if (tag.isEmpty() == false) {
                    for (int i = 0; i < tag.size(); i++) {
                        if (hash.contains(tag.get(i)) == false) {
                            hash.add(tag.get(i));
                            occurence[hash.size()] = 1;
                        } else {
                            occurence[hash.lastIndexOf(tag.get(i))] += 1;
                        }
                    }
                }
            }
        }
        String[][] hashtag = new String[10][2];
        for (int j = 0; j < 10; j++) {
            int max = 0;
            int index = 0;
            for (int i = 0; i < hash.size(); i++) {
                if (occurence[i] > max) {
                    max = occurence[i];
                    index = i;
                }
            }
            String x = Integer.toString(occurence[index]);
            hashtag[j][0] = x;
            hashtag[j][1] = hash.get(index);
            occurence[index] = 0;
        }
        return hashtag;
    }
}
