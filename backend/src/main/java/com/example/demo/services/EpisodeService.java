package com.example.demo.services;


import com.example.demo.entities.Episode;
import com.example.demo.repositories.EpisodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EpisodeService {

    private String episodeApi = "http://api.sr.se/api/v2/episodes/index";
//http://api.sr.se/api/v2/episodes/index?programid=3718&fromdate=2012-08-27&todate=2012-08-31

    // Skapa en episode repo för att få ut info från databasen.
    @Autowired
    private EpisodeRepo episodeRepo;

    //___________Hämta alla episode från ett program i api'n genom id (ENDAST EN SIDA, &page=(sida) för nästa)___________

    // 1. Tar emot ett programid
    public List<Episode> getbyProgramid(long id) {

        RestTemplate restTemplate = new RestTemplate();
        //2. Skapar en url och hämtar genom resttemplate och sparar i en map
        Map response = restTemplate.getForObject(episodeApi + "?programid=" + id + "&format=Json", Map.class);

        //3. Tar ut episodedeln från mappen och sparar i en lista
        List<Map> episodeMaps = (List<Map>) response.get("episodes");

        //4. Skapar en arraylist som vi sedan ska spara de delar vi vill ha från episodes
        List<Episode> episodes = new ArrayList<>();
        for (Map episode : episodeMaps) {

            //    5. skapar en publishdateutc och tar ut det från episode. Sedan tar vi bort texten från  strängen och behåller
            //       endast siffrorna genom substring och sparar i epoch. Sedan parsar vi epoch och får en long med airtime. Sedan
            //       måste vi omvanlda utc tiden till vanlig tid genom:
            //       "new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (airtime));"
            //       och sparar resultatet i broadcasttime och får då ut allt som ett datum!


            String publishdateutc = (String) episode.get("publishdateutc");
            String epoch = publishdateutc.substring(6, 19);
            long airtime = Long.parseLong(epoch);
            String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(airtime));

            //     6.Skapar en Episode med alla delar vi vill ha från episode delen i urlen. Sedan sparar vi det i vår episode lista
            //      och returnerar den!
            Episode EP = new Episode(
                    (Integer) episode.get("id"),
                    (String) episode.get("title"),
                    (String) episode.get("description"),
                    date
            );
            episodes.add(EP);
        }
        System.out.println(episodes);
        return episodes;
    }


//--------------------------------------Get episode by date-------------------------------------------------------------


    public List<Episode> getByDate(long id, String date) {

//        1.Skapas för att kunna göra en get på en specifik url

        RestTemplate restTemplate = new RestTemplate();

//        2. Ta date och parsa den till local date för att sedan kunna addera en dag på localdate 2. Detta gör att vi kan få
//        ut en specifik dag genom apit då man måste jämföra mellan 2 datum för att få ett datum. Exemepel:
//        2012-02-03 = From: 2012-02-03 To: 2012-02-04  = Alla episodes för den 03 februari 2012!
        LocalDate localDate = LocalDate.parse(date);
        LocalDate localDate1 = localDate.plusDays(1);


        //        3. Skapa Url'en för att hämta från api

        Map response = restTemplate.getForObject(episodeApi + "?programid=" + id + "&fromdate=" + localDate
                + "&todate=" + localDate1 + "&format=Json", Map.class);


        //        4. Skapar en map för att hämta allt från episodes delen i url'en
        List<Map> episodeMaps = (List<Map>) response.get("episodes");

        //        5. skapar en lista där vi kommer ta in de delar vi vill ha från episodes delen.
        List<Episode> episodes = new ArrayList<>();
        for (Map episode : episodeMaps) {


            //    6. skapar en publishdateutc och tar ut det från episode. Sedan tar vi bort texten från  strängen och behåller
            //       endast siffrorna genom substring och sparar i epoch. Sedan parsar vi epoch och får en long med airtime. Sedan
            //       måste vi omvanlda utc tiden till vanlig tid genom:
            //       "new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (airtime));"
            //       och sparar resultatet i broadcasttime och får då ut allt som ett datum!

            String publishdateutc = (String) episode.get("publishdateutc");
            String epoch = publishdateutc.substring(6, 19);
            long airtime = Long.parseLong(epoch);
            String broadcasttime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(airtime));

        //     7. Skapar en Episode med alla delar vi vill ha från episode delen i urlen. Sedan sparar vi det i vår episode lista
        //      och returnerar den!

            Episode EP = new Episode(
                    (Integer) episode.get("id"),
                    (Integer) ((Map) episode.get("program")).get("id"),
                    (String) episode.get("title"),
                    (String) episode.get("description"),
                    broadcasttime,
                    (String) ((Map) episode.get("program")).get("name")
            );
            episodes.add(EP);
        }
        System.out.println(episodes);
        return episodes;
    }


    //_______________________________Hämta allt från Episodes i databsen(EJ AKTUELL LÄNGRE)_____________________________
    // Skapa en lista som tar emot allt från databasen genom episoderepos inbyggda metod från jpa.
    // Koppla sedan till controller för att kunna kalla på och få ut värdena.
    public List<Episode> getAllEpisodesFromDb() {
        return episodeRepo.findAll();
    }


    //--------------------------Hämta alla episodes i databasen baserad på en viss titel(EJ AKTUELL LÄNGRE)-------------
    public List<Episode> getWithTitle(String title) {
        return episodeRepo.getWithTitle(title);
    }


    //------------------------------Post en episode till databasen(EJ AKTUELL LÄNGRE)-----------------------------------
    public Episode addEpisode(Episode episode) {
        return episodeRepo.save(episode);
    }














///////////////////////////////////////////////// GAMMAL KOD ///////////////////////////////////////////////////////////

//    private String parseDate(String date, int index){
////Insert "-" into string to form a date, using index to describe where to split
//        date = date.substring(0, index) + "-" +
//                date.substring(index, index + 2) + "-" + date.substring(index + 2, 8);
//        return date;
//    }


//        Integer date1 = Integer.parseInt(date);
//        Integer date2 = Integer.parseInt(date);
//        date2++;
//        String newDate1 = ""+ date1;
//        String newDate2 = ""+ date2;
//        newDate1 = parseDate(newDate1, 4);
//        newDate2 = parseDate(newDate2, 4);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//formatter.format(localDate1);

}