package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
@Table(name="episodes")
public class Episode {
    @Id // Berättar att det är en primary key
    private long id;
    private long program_id;
    String title;
    String description;
    String publishdateutc;
    String name;


    public Episode() {
    }



    public Episode(long id,  String title, String description, String publishdateutc) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publishdateutc = publishdateutc;
    }

    public Episode(long id, long program_id, String title, String description, String publishdateutc) {
        this.id = id;
        this.program_id = program_id;
        this.title = title;
        this.description = description;
        this.publishdateutc = publishdateutc;
    }

    public Episode(long id, long program_id, String title, String description, String publishdateutc, String name) {
        this.id = id;
        this.program_id = program_id;
        this.title = title;
        this.description = description;
        this.publishdateutc = publishdateutc;
        this.name = name;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProgram_id() {
        return program_id;
    }

    public void setProgram_id(long program_id) {
        this.program_id = program_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @JsonProperty("Airtime")
    public String getPublishdateutc() {
        return publishdateutc;
    }

    public void setPublishdateutc(String publishdateutc) {
        this.publishdateutc = publishdateutc;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", program_id=" + program_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publishdateutc='" + publishdateutc + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
