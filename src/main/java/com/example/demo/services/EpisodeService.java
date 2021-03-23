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

    //______________________________Hämta alla episode från ett program i api'n genom id________________________________
    public List<Episode> getbyProgramid(long id){

        RestTemplate restTemplate= new RestTemplate();

        Map response = restTemplate.getForObject(episodeApi + "?programid=" + id + "&format=Json" , Map.class);

        List<Map> episodeMaps = (List<Map>) response.get("episodes");

        List<Episode> episodes = new ArrayList<>();
        for (Map episode : episodeMaps){

           String publishdateutc = (String)episode.get("publishdateutc");
           String epoch = publishdateutc.substring(6,19);
           long airtime = Long.parseLong(epoch);
            String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (airtime));

            Episode EP = new Episode(
                    (Integer)episode.get("id"),
                    (String)episode.get("title"),
                    (String)episode.get("description"),
                    date
            );
            episodes.add(EP);
        }
        System.out.println(episodes);
        return episodes;
    }



    //_______________________________Hämta allt från Episodes i databsen________________________________________________
    // Skapa en lista som tar emot allt från databasen genom episoderepos inbyggda metod från jpa.
    // Koppla sedan till controller för att kunna kalla på och få ut värdena.
    public List<Episode> getAllEpisodesFromDb(){
        return episodeRepo.findAll();
}


    //-------------------------------Hämta alla episodes i databasen baserad på en viss titel---------------------------
    public List <Episode> getWithTitle(String title){
       return episodeRepo.getWithTitle(title);
}


//-----------------------------------Post en episode till databasen-----------------------------------------------------
public Episode addEpisode(Episode episode){
        return episodeRepo.save(episode);
}



//--------------------------------------Get episode by date-------------------------------------------------------------

    //http://api.sr.se/api/v2/episodes/index?programid=3718&fromdate=2012-08-27&todate=2012-08-31
    public List<Episode> getByDate(long id, String date){
        RestTemplate restTemplate= new RestTemplate();

        Integer date1 = Integer.parseInt(date);
        Integer date2 = Integer.parseInt(date);
        date2++;
        String newDate1 = ""+ date1;
        String newDate2 = ""+ date2;
        newDate1 = newDate1.substring(0, 4) + "-" +
                newDate1.substring(4, 6) + "-" + newDate1.substring(6, 8);
        newDate2 = newDate2.substring(0, 4) + "-" +
                newDate2.substring(4, 6) + "-" + newDate2.substring(6, 8);
        Map response = restTemplate.getForObject(episodeApi + "?programid=" + id + "&fromdate=" + newDate1
                + "&todate=" + newDate2 + "&format=Json" , Map.class);

        List<Map> episodeMaps = (List<Map>) response.get("episodes");
        List<Episode> episodes = new ArrayList<>();
        for (Map episode : episodeMaps){
            String publishdateutc = (String)episode.get("publishdateutc");
            String epoch = publishdateutc.substring(6,19);
            long airtime = Long.parseLong(epoch);
            String broadcasttime = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (airtime));

            Episode EP = new Episode(
                    (Integer)episode.get("id"),
                    (Integer)((Map)episode.get("program")).get("id"),
                    (String)episode.get("title"),
                    (String)episode.get("description"),
                    broadcasttime,
                    (String)((Map)episode.get("program")).get("name")
            );
            episodes.add(EP);
        }
        System.out.println(episodes);
        return episodes;
    }
}
