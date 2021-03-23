package com.example.demo.controllers;


import com.example.demo.entities.Episode;
import com.example.demo.services.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/rest")
public class EpisodesController {
    @Autowired
    private EpisodeService episodeService;


    //---------------------------Hämta alla episodes från ett program från api'n----------------------------------------
    @GetMapping("/rest/episodes/{id}")
    public List<Episode> getById(@PathVariable long id) {
        return episodeService.getbyProgramid(id);
    }





    //________Hämta alla episodes från databasen genom att kalla på servicen som är kopplad till repot__________________

    @GetMapping("/rest/episodes/all")
    public List<Episode> getAllEpisodes(){
        return  episodeService.getAllEpisodesFromDb();
    }

//---------------------------------------Hämta alla episodes baserad på en viss titel-----------------------------------
    @GetMapping("/rest/episodes/all/{title}")
    public List <Episode> getByTitle(@PathVariable String title){
        return episodeService.getWithTitle(title);
    }

//--------------------------------------Post en episode till db---------------------------------------------------------
    @PostMapping("/rest/episodes")
    private Episode createEpisode (@RequestBody Episode episode){
        System.out.println(episode);
        return episodeService.addEpisode(episode);
    }
 //----------------------------------Hitta en episode beroende på dag---------------------------------------------------

    @GetMapping("/rest/episodes/day/{id}/{date}")
    private List<Episode> getEpisodesByDay(@PathVariable long id, @PathVariable String date){
        return episodeService.getByDate(id,date);
    }
}