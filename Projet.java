
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import static javafx.scene.input.KeyCode.X;
import static javafx.scene.input.KeyCode.Y;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Anthony
 */
public class Projet extends Application {

    //Onglet d'accueil
    Stage myStage;
    //Onglet Menu
    Stage StageMenu;
    //Indicateur pour savoir si des donnees ont deja ete chargees
    String data = "null";

    static BaseDeTweets tab = new BaseDeTweets();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    Font fontbut = new Font("Dialog", 16);
    String stylebg = "-fx-background-color: white;";
    String stylebg2 = "-fx-background-color: #B0C4DE;";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        Stage myStage = primaryStage;
        myStage.setResizable(false);
        primaryStage.setTitle("Accueil");
        primaryStage.setScene(construitScene());
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    //Accueil: choix du fichier
    public Scene construitScene() {

        Font fontacc = new Font("Arial", 16);
        BorderPane bpane = new BorderPane();
        bpane.setStyle(stylebg);
        Text txtintro = new Text("\n Outil de reporting sur les données de Twitter \n");
        txtintro.setFont(new Font("Arial", 24));

        Label ldata = new Label("Choisissez le fichier à  utiliser : ");
        ldata.setFont(fontacc);
        ToggleGroup tog = new ToggleGroup();
        RadioButton rbfoot = new RadioButton("Foot");
        rbfoot.setToggleGroup(tog);
        rbfoot.setSelected(true);
        rbfoot.setFont(fontacc);
        RadioButton rbclim = new RadioButton("Climat");
        rbclim.setToggleGroup(tog);
        rbclim.setFont(fontacc);

        //Choix du jeu de donnees
        Button bcharge = new Button("Charger");
        bcharge.setFont(fontbut);
        bcharge.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                //Chargement des donnees foot
                //Si des donnees sont deja chargees et correspondent au choix
                //alors pas besoin de charger a nouveau
                if (rbfoot.isSelected() && data != "foot") {
                    charger("Foot.txt");
                    data = "foot";
                } //Chargement des donnees climat
                else if (rbclim.isSelected() && data != "climat") {
                    charger("Climat.txt");
                    data = "climat";
                }

                //Creation de la page de "menu"
                StageMenu = new Stage();
                StageMenu.setResizable(false);
                StageMenu.setTitle("Menu");
                StageMenu.setScene(construitMenu());
                StageMenu.sizeToScene();
                StageMenu.show();
            }
        });

        Button bquit = new Button("Quitter");
        bquit.setFont(fontbut);

        VBox vbox = new VBox(2);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);
        vbox.getChildren().addAll(txtintro, ldata, rbfoot, rbclim, bcharge, bquit);

        bpane.setCenter(vbox);

        StackPane root = new StackPane();
        root.getChildren().addAll(bpane);
        Scene scene = new Scene(root, 500, 300);
        return scene;

    }

    //Onglet Menu 
    public Scene construitMenu() {
        
        BorderPane bpane = new BorderPane();
        bpane.setStyle(stylebg);
        Text txtintro = new Text("Liste des fonctionnalités de l'outil");
        txtintro.setFont(new Font("Arial", 20));
        Button baff = new Button("Lire les données");
        baff.setFont(fontbut);
        baff.setOnAction((ActionEvent t) -> {
            affichage();
        });

        Button bnb = new Button("Nombre de tweets");
        bnb.setFont(fontbut);
        bnb.setOnAction((ActionEvent t) -> {
            nbTweets();
        });

        Button brt = new Button("Retweets");
        brt.setFont(fontbut);
        brt.setOnAction((ActionEvent t) -> {
            Retweets();
        });
        
        Button bhash = new Button("Hashtag");
        bhash.setFont(fontbut);
        bhash.setOnAction((ActionEvent t) -> {
            HashtagStage();
        });

        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(baff, bnb, brt, bhash);
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(txtintro, hbox);

        MenuBar menu = menuB(StageMenu);
        //Agencement des elements    
        bpane.setTop(menu);
        bpane.setCenter(vbox);

        StackPane root = new StackPane();
        root.getChildren().addAll(bpane);
        Scene scene = new Scene(root, 600, 200);
        return scene;

    }

    //Chargement des donnees depuis les fichiers
    public static void charger(String fichier) {
        
        tab.initialise();
        Tweet t;
        try {
            FileReader r = new FileReader(fichier);
            BufferedReader br = new BufferedReader(r);
            String ligne;
            ligne = br.readLine();
            do {
                //On decoupe les lignes du fichier pour associer 
                //chaque element a un champs de tweet
                try {
                    String[] sp = ligne.split("	", 5);
                    if (Character.isDigit(sp[0].charAt(0)) && (sp[2].length() > 0)
                            && Character.isDigit(sp[2].charAt(0))) {
                        try {
                            LocalDateTime date = LocalDateTime.parse(sp[2], formatter);
                            t = new Tweet(sp[0], sp[1], date, sp[3], sp[4]);
                            //Ajout du tweet a la liste 
                            tab.ajoute(t);
                        } catch (DateTimeParseException e) {
                            //  e.printStackTrace();
                            System.out.println("Erreur lors du formatage de la date ");
                        }
                    }
                } catch (Exception e) {
                    //Certaines lignes ne contiennent pas 5 elements et ne sont donc pas traitees
                    System.out.println("Tweet non utilisable");
                }
                ligne = br.readLine();
                //Tant qu'il y a une ligne a lire dans le fichier 
            } while (ligne != null);
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Chargement");
            alert.setHeaderText("Chargement des données terminé");
            alert.showAndWait();
        } catch (Exception e) {
            //  e.printStackTrace();
            System.out.println("Erreur lors du chargement des données");
        }
    }

    //Page pour les traitements sur le nombre de tweets 
    public void nbTweets() {

        Stage StageNb = new Stage();
        StageNb.setTitle("Nombre de tweets");
        ScrollPane scrollPane = new ScrollPane();

        //Abscisse graphique 1
        CategoryAxis x = new CategoryAxis();
        x.setTickMarkVisible(true);
        x.setTickLabelRotation(90);
        //Ordonee graphique 1
        NumberAxis y = new NumberAxis();
        y.setLabel("Nombre de tweets");
        //Graphique 1
        BarChart bchart = new BarChart<String, Number>(x, y);
        bchart.setLegendVisible(false);
        bchart.setTitle("Nombre de tweets sur la période");

        //Abscisse graphique 2
        CategoryAxis x2 = new CategoryAxis();
        x2.setTickLabelRotation(90);
        //Ordonee graphique 2
        NumberAxis y2 = new NumberAxis();
        y2.setLabel("Nombre de tweets");
        //Graphique 2
        BarChart bchart2 = new BarChart<String, Number>(x2, y2);
        bchart2.setLegendVisible(false);
        bchart2.setTitle("Nombre de tweets par heure");

        String st = "Nombre moyen de tweets par jour sur la période : ";
        Label l = new Label(st);
        GridPane grid = new GridPane();
        grid.setStyle(stylebg);
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        //Calendrier depart
        DatePicker d1 = new DatePicker();
        d1.setValue(tab.date(0));
        //Calendrier arrivee
        DatePicker d2 = new DatePicker();
        d2.setValue(tab.date(0));

        //Definition des labels en abscisse pour les 2 graphes 
        ObservableList<String> xlabels = FXCollections.observableArrayList();
        ObservableList<String> x2labels = FXCollections.observableArrayList();
        x2labels.addAll("0h-1h","1h-2h", "2h-3h", "3h-4h", "4h-5h", "5h-6h", "6h-7h", "7h-8h",
                 "8h-9h", "9h-10h", "10h-11h", "11h-12h", "12h-13h", "13h-14h", "14h-15h", "15h-16h", "16h-17h",
                 "17h-18h", "18h-19h", "19h-20h", "20h-21h", "21h-22h", "22h-23h", "23h-24h");
        x2.setCategories(x2labels);

        //Affichage du nombre de tweets entre 2 jours 
        Button but = new Button("Afficher");
        but.setFont(fontbut);
        but.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                xlabels.clear();
                //Recuperation du nombre de tweets sur la periode selectionnee
                TreeMap<LocalDate, Integer> hmap = tab.nbTweets(d1.getValue(), d2.getValue());

                XYChart.Series series = new XYChart.Series();
                series.setName("Nombre de tweets");
                int tot = 0;

                //Definition des valeurs de la serie a afficher 
                for (Map.Entry mapentry : hmap.entrySet()) {
                    series.getData().add(new XYChart.Data(mapentry.getKey().toString(), mapentry.getValue()));
                    //Somme des tweets
                    tot = tot + hmap.get(mapentry.getKey());
                    xlabels.add(mapentry.getKey().toString());
                }
                bchart.getData().clear();
                x.setCategories(xlabels);

                //Ajout de la serie au graphique 
                bchart.getData().add(series);

                //Affichage du nombre moyen de tweets par jour sur la periode 
                String st = "Nombre moyen de tweets par jour sur la période : ";
                if(hmap.size()!=0){
                    st=st+ Integer.toString(tot / hmap.size());
                }
                l.setText(st);
            }
        });

        DatePicker d3 = new DatePicker();
        d3.setValue(tab.date(0));
        //Affichage du nombre de tweets sur une journee
        Button but2 = new Button("Afficher");
        but2.setFont(fontbut);
        but2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //Recuperation du nombre de tweets sur la periode selectionnee
                TreeMap<Integer, Integer> hmap = tab.nbTweets(d3.getValue());

                XYChart.Series series = new XYChart.Series();
                series.setName("Nombre de tweets");
                int tot = 0;

                //Definition des valeurs de la serie a afficher 
                for (Map.Entry mapentry : hmap.entrySet()) {
                    series.getData().add(new XYChart.Data(mapentry.getKey().toString() + "h-" + (Integer.parseInt(mapentry.getKey().toString()) + 1) + "h", mapentry.getValue()));
                    //Somme des tweets
                    tot = tot + hmap.get(mapentry.getKey());
                }
                bchart2.getData().clear();
                //Ajout de la serie au graphique 
                bchart2.getData().add(series);

                //Affichage du nombre moyen de tweets par jour sur la periode 
                String st = "Nombre moyen de tweets par heure sur la periode : " + Integer.toString(tot / 24);
                l.setText(st);
            }
        });
        vbox.setAlignment(Pos.CENTER);
        VBox vbox2 = new VBox();
        vbox2.setSpacing(15);
        vbox2.setAlignment(Pos.CENTER);
        VBox vbox3 = new VBox();
        vbox3.setSpacing(15);
        vbox3.setAlignment(Pos.CENTER);

        MenuBar menu = menuB(StageNb);

        vbox.getChildren().addAll(d1, d2, but);
        vbox.setStyle(stylebg2);
        vbox2.getChildren().addAll(d3, but2);
        vbox2.setStyle(stylebg2);
        vbox3.getChildren().addAll(bchart, l);
        grid.setConstraints(menu, 0, 0, 2, 1);
        grid.getChildren().add(menu);
        grid.setConstraints(vbox, 0, 1);
        grid.getChildren().add(vbox);
        grid.setConstraints(vbox3, 1, 1);
        grid.getChildren().add(vbox3);
        grid.setConstraints(vbox2, 0, 2);
        grid.getChildren().add(vbox2);
        grid.setConstraints(bchart2, 1, 2);
        grid.getChildren().add(bchart2);
        
        StackPane root = new StackPane();
        grid.autosize();
        root.getChildren().addAll(grid);
        Scene scene = new Scene(root);
        StageNb.setScene(scene);
        StageNb.sizeToScene();
        StageNb.show();
        StageNb.setResizable(false);
    }

    public void affichage() {
        
        TextFlow txtflow = new TextFlow();
        Text tf = new Text();
        Stage StageAff = new Stage();
        StageAff.setTitle("Lecture des données");

        BorderPane bp = new BorderPane();
        bp.setStyle(stylebg);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(bp);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);

        VBox vbox = new VBox();
        vbox.setSpacing(25);
        
        //Tableau de couleurs pour affichage differencie des elements du tweets
         Color[] colors = {Color.BLACK, Color.TEAL, Color.NAVY, Color.BLACK, Color.BLUE}; 
        Button baff = new Button("Afficher tout");
        baff.setFont(fontbut);
        baff.setOnAction((ActionEvent t) -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setResizable(true);
            alert.setWidth(800);
            alert.setHeight(400);
            alert.setTitle("Affichage complet");
            alert.setHeaderText("L'affichage de l'ensemble des données peut etre long."
                    + " Etes-vous sûrs de vouloir tout afficher ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //On reinitialise l'affichage
                txtflow.getChildren().clear();
                String resall = tab.lire();
                tf.setText(resall);
                txtflow.getChildren().add(tf);
            }
        });

        DatePicker d1 = new DatePicker();
        //Valeur initiale du calendrier = date du premier tweet de la liste 
        d1.setValue(tab.date(0));

        Button but = new Button("Afficher");
        but.setFont(fontbut);
        //Affichage des tweets d'un certain jour 
        but.setOnAction((ActionEvent t) -> {
            //On reinitialise l'affichage
            txtflow.getChildren().clear();
            String resd = tab.lire(d1.getValue());
            tf.setText(resd);
            txtflow.getChildren().add(tf);
        });

        HBox hbox = new HBox();
        hbox.setSpacing(15);
        HBox hbox2 = new HBox();
        hbox2.setSpacing(15);
        DatePicker d2 = new DatePicker();
        d2.setValue(tab.date(0));

        //Choix des heure de depart et d'arrivee
        Spinner<Integer> spinh = new Spinner<Integer>(0, 23, 0);
        spinh.setPrefWidth(60);
        Label lheures1 = new Label("h");
        Spinner<Integer> spinm = new Spinner<Integer>(0, 59, 0);
        spinm.setPrefWidth(60);
        Label lheures2 = new Label("à ");
        Spinner<Integer> spinh2 = new Spinner<Integer>(0, 23, 0);
        spinh2.setPrefWidth(60);
        Label lheures3 = new Label("h");
        Spinner<Integer> spinm2 = new Spinner<Integer>(0, 59, 0);
        spinm2.setPrefWidth(60);
        Button but2 = new Button("Afficher");
        but2.setFont(fontbut);
        but2.setOnAction((ActionEvent t) -> {
            //On reinitialise l'affichage
            txtflow.getChildren().clear();
            DecimalFormat df = new DecimalFormat("00");
            String j = d2.getValue().toString();
            String s;
            s = j + " " + df.format(spinh.getValue()) + ":" + df.format(spinm.getValue());
            String s2;
            s2 = j + " " + df.format(spinh2.getValue()) + ":" + df.format(spinm2.getValue());
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            try {
                LocalDateTime date1 = LocalDateTime.parse(s, formatter2);
                LocalDateTime date2 = LocalDateTime.parse(s2, formatter2);
                String resd1d2 = tab.lire(date1, date2);
               // tf.setText(resd1d2);
                String[] decoupe = resd1d2.split("\n");
                int stl = 0;
                for (String ligne : decoupe) {
                    if (stl == 5) {
                        stl = 0;
                    }
                    Text text = new Text(ligne + "\n");
                    text.setFill(colors[stl]);
                    txtflow.getChildren().add(text);
                    stl = stl + 1;
                }
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        });

        MenuBar menu = menuB(StageAff);
        vbox.setStyle(stylebg2);
        hbox.getChildren().addAll(baff, d1, but);
        hbox2.getChildren().addAll(d2, spinh, lheures1, spinm, lheures2, spinh2, lheures3, spinm2, but2);
        Label lperiode = new Label("Lecture des tweets sur une plage horaire d'une journée: ");
        vbox.getChildren().addAll(menu, hbox, lperiode, hbox2);
        bp.setTop(vbox);
        bp.setCenter(txtflow);
        StackPane root = new StackPane();
        Scene scene = new Scene(scrollPane, 800, 500);
        root.getChildren().addAll(bp);
        StageAff.setScene(scene);
        StageAff.sizeToScene();
        StageAff.setResizable(false);
        StageAff.show();

    }

    public void Retweets() {

        Font fontrt = new Font("Arial", 16);
        Stage StageRT = new Stage();
        StageRT.setTitle("Retweets");

        //Abscisse
        CategoryAxis x = new CategoryAxis();
        //Ordonee
        NumberAxis y = new NumberAxis();
        y.setLabel("Nombre de retweets");
        x.setTickLabelRotation(90);
        //Graphique
        BarChart bchart = new BarChart<String, Number>(x, y);
        bchart.setLegendVisible(false);
        Label l = new Label();
        BorderPane bp = new BorderPane();
        bp.setStyle(stylebg);
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        Label ltop = new Label("TOP Retweets entre 2 jours");
        ltop.setFont(fontrt);
        //Calendrier depart
        DatePicker d1 = new DatePicker();
        d1.setValue(tab.date(0));
        //Calendrier arrivee
        DatePicker d2 = new DatePicker();
        d2.setValue(tab.date(1));

        //Affichage du TOP 5
        String sttop = "";
        Label ltop5 = new Label("TOP 5 \n  ");
        ltop5.setFont(fontrt);
        Text ttop = new Text(" -- \n -- \n -- \n -- \n --");
        ttop.setFont(fontrt);
        ttop.setFill(Color.NAVY);
        ttop.setTextAlignment(TextAlignment.CENTER);

        //TOP 5 Retweets entre 2 jours
        Button but = new Button("Afficher");
        but.setFont(fontbut);
        but.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String sortie = tab.lireRetweet(d1.getValue(), d2.getValue());
                ttop.setText(sttop + sortie);
            }
        });

        //TOP 5 Retweets sur une journee
        Label ltop2 = new Label("TOP Retweets sur une journée");
        ltop2.setFont(fontrt);
        DatePicker d3 = new DatePicker();
        d3.setValue(tab.date(0));

        Button but2 = new Button("Afficher");
        but2.setFont(fontbut);
        but2.setOnAction((ActionEvent t) -> {
            String res = tab.lireRetweet(d3.getValue());
            ttop.setText(sttop + res);
        });

        //Suivi de la popularite des utilisateurs sur toute la periode
        //Recuperation de la liste des utilisateurs retweetes
        ComboBox comb = new ComboBox();
        Label luser = new Label("Liste des utilisateurs retweetés : ");
        TreeSet tset = tab.listeRetweet();
        Iterator it = tset.iterator();
        while (it.hasNext()) {
            comb.getItems().add(it.next());
        }

        ObservableList<String> xlabels = FXCollections.observableArrayList();
        LocalDate currentdate = tab.date(0);
        Period dif = Period.between(currentdate, tab.date(1));
        for (int i = 0; i <= dif.getDays(); i++) {
            xlabels.add(currentdate.toString());
            currentdate = currentdate.plusDays(1);
        }
        x.setCategories(xlabels);
        Button but3 = new Button("Afficher");
        but3.setFont(fontbut);
        but3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TreeMap<LocalDate, Integer> tmap = tab.lireRetweet(comb.getValue().toString());
                XYChart.Series series = new XYChart.Series();
                int tot = 0;
                //Definition des valeurs de la serie a afficher 
                for (Map.Entry mapentry : tmap.entrySet()) {
                    series.getData().add(new XYChart.Data(mapentry.getKey().toString(), mapentry.getValue()));
                    //Somme des tweets
                    tot = tot + tmap.get(mapentry.getKey());
                }
                bchart.getData().clear();
                y.setUpperBound(tot);
                y.setTickUnit(1);

                //Ajout de la serie au graphique 
                bchart.getData().add(series);

                //Affichage du nombre moyen de tweets par jour sur la periode 
                String st = "Nombre moyen de retweets par jour sur la periode : "+ Integer.toString(tot / tmap.size());
                l.setText(st);
            }
        });

        DatePicker d4 = new DatePicker();
        d4.setValue(tab.date(0));

        //Abscisse
        CategoryAxis x2 = new CategoryAxis();
        x2.setTickLabelRotation(90);
        ObservableList<String> x2labels = FXCollections.observableArrayList();
        x2labels.addAll("0h-1h","1h-2h", "2h-3h", "3h-4h", "4h-5h", "5h-6h", "6h-7h", "7h-8h",
                 "8h-9h", "9h-10h", "10h-11h", "11h-12h", "12h-13h", "13h-14h", "14h-15h", "15h-16h", "16h-17h",
                 "17h-18h", "18h-19h", "19h-20h", "20h-21h", "21h-22h", "22h-23h", "23h-24h");
        x2.setCategories(x2labels);
        //Ordonee
        NumberAxis y2 = new NumberAxis();
        y2.setLabel("Nombre de retweets");
        //Graphique
        BarChart bchart2 = new BarChart<String, Number>(x2, y2);
        bchart2.setLegendVisible(false);

        Button butjour = new Button("Retweets sur la journée");
        butjour.setFont(fontbut);
        butjour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TreeMap<Integer, Integer> tmap = tab.lireRetweet(comb.getValue().toString(), d4.getValue());
                XYChart.Series series = new XYChart.Series();
                int tot = 0;
                //Definition des valeurs de la serie a afficher 
                for (Map.Entry mapentry : tmap.entrySet()) {
                    series.getData().add(new XYChart.Data(mapentry.getKey().toString() + "h-" + (Integer.parseInt(mapentry.getKey().toString()) + 1) + "h", mapentry.getValue()));
                    //Somme des tweets
                    tot = tot + tmap.get(mapentry.getKey());
                }
                bchart2.getData().clear();
                y2.setUpperBound(tot);
                //Ajout de la serie au graphique 
                bchart2.getData().add(series);

                //Affichage du nombre moyen de tweets par jour sur la periode 
                String st = "Nombre moyen de retweets par jour sur la journée : " + Integer.toString(tot / tmap.size());
                l.setText(st);
            }
        });

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(ltop, d1, d2, but, ltop2, d3, but2, ltop5, ttop);
        vbox.setStyle(stylebg2);
        bp.setLeft(vbox);
        MenuBar menu = menuB(StageRT);
        VBox vboxtop = new VBox();
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.getChildren().addAll(luser, comb, but3);
        hbox.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox();
        hbox2.setSpacing(15);
        hbox2.getChildren().addAll(d4, butjour);
        hbox2.setAlignment(Pos.CENTER);
        vboxtop.setSpacing(15);
        vboxtop.getChildren().addAll(menu);
        bp.setTop(vboxtop);
        VBox vbox2 = new VBox();
        vbox2.setSpacing(15);
        vbox2.setAlignment(Pos.CENTER);
        vbox2.getChildren().addAll(hbox, bchart, l, hbox2, bchart2);
        bp.setCenter(vbox2);

        StackPane root = new StackPane();
        root.getChildren().addAll(bp);
        Scene scene = new Scene(root, 1000, 800);
        StageRT.setScene(scene);
        StageRT.sizeToScene();
        StageRT.show();
    }

    public void HashtagStage() {

        Stage Stagehash = new Stage();
        Stagehash.setTitle("Nombre de hashtag");

        //Abscisse
        CategoryAxis x = new CategoryAxis();
        x.setTickLabelRotation(90);
        //Ordonee
        NumberAxis y = new NumberAxis();
        y.setLabel("Nombre d'occurences");
        //Graphique
        BarChart lc = new BarChart<String, Number>(x, y);
        lc.setTitle("TOP 10 des hastags");
        ObservableList<String> xlabels = FXCollections.observableArrayList();

        BorderPane bp = new BorderPane();
        bp.setStyle(stylebg);
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        //Calendrier depart
        DatePicker d1 = new DatePicker();
        d1.setValue(tab.date(0));
        //Calendrier arrivee
        DatePicker d2 = new DatePicker();
        d2.setValue(tab.date(1));

        Button but = new Button("Afficher");
        but.setFont(fontbut);
        but.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //Recuperation du nombre de hashtag sur la periode selectionnee
                String[][] hmap = tab.hashtagListe(d1.getValue(), d2.getValue());

                XYChart.Series<String, Number> series = new XYChart.Series();
                series.setName("Nombre de hashtag");

                //Definition des valeurs de la serie a afficher 
                for (int i = 0; i < hmap.length; i++) {
                    int a = Integer.parseInt(hmap[i][0]);
                    series.getData().add(new XYChart.Data(hmap[i][1], a));
                    xlabels.add(hmap[i][1]);
                }
                lc.getData().clear();
                x.setCategories(xlabels);
                //Ajout de la serie au graphique 
                lc.getData().add(series);
            }
        });
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(d1, d2, but);
        vbox.setStyle(stylebg2);
        bp.setLeft(vbox);
        VBox vbox2 = new VBox();
        vbox2.setSpacing(15);
        vbox2.setAlignment(Pos.CENTER);
        vbox2.getChildren().addAll(lc);
        bp.setCenter(vbox2);
        MenuBar menu = menuB(Stagehash);
        bp.setTop(menu);
        StackPane root = new StackPane();
        root.getChildren().addAll(bp);
        Scene scene = new Scene(root, 800, 400);
        Stagehash.setScene(scene);
        Stagehash.sizeToScene();
        Stagehash.show();
    }

    public MenuBar menuB(Stage CurrentStage) {
        MenuBar menu = new MenuBar();
        Menu menu1 = new Menu("Application");
        menu.getMenus().addAll(menu1);
        
        MenuItem itemacc = new MenuItem("Accueil");
        itemacc.setOnAction((ActionEvent t) -> {
            CurrentStage.close();
            if (!(CurrentStage.equals(StageMenu))) {
                StageMenu.close();
            }
        });
        //racocurci ne fonctionne pas
        itemacc.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        
        MenuItem itemmenu = new MenuItem("Menu");
        //Fermeture de l'onglet courant et retour a l'onglet menu
        itemmenu.setOnAction((ActionEvent t) -> {
            CurrentStage.close();
            StageMenu.isFocused();
        });
        //raccourci ne fonctionne pas 
        itemmenu.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
        
        MenuItem itemquit = new MenuItem("Quitter");
        itemquit.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        itemquit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        //Si l'onglet courant est l'onglet menu --> pas d'item menu
        if (CurrentStage.equals(StageMenu)) {
            menu1.getItems().addAll(itemacc, itemquit);
        } else {
            menu1.getItems().addAll(itemacc, itemmenu, itemquit);
        }
        
        return menu;
    }
}
