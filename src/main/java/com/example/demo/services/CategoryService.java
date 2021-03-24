package com.example.demo.services;

import com.example.demo.entities.Category;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private String categoryApi = "http://api.sr.se/api/v2/programcategories/";

//_______________________________Hämta alla kategorier________________________________________________
    public List<Category> getAll(){
        RestTemplate template = new RestTemplate();
        Map response = template.getForObject(categoryApi + "?format=json", Map.class);

        List<Map> catMaps = (List<Map>) response.get("programcategories");

        List<Category> categories = new ArrayList<>();

        for(Map cat : catMaps){
            Long id = ((Number) cat.get("id")).longValue();
            String name = (String) cat.get("name");

            Category category = new Category(id, name);

            categories.add(category);
        }

        System.out.println(categories.toString());
        return categories;
    }

//______________________________Hämta alla kategorier i api'n genom id________________________________
    public List<Category> getById(Long catID){
        RestTemplate template = new RestTemplate();
        Map response = template.getForObject(categoryApi + catID + "?format=json", Map.class);

        String reply = (String)response.get("programcategory").toString();

        String[] shortAnswers = reply.split("=");
        List<String> finalAnswer = new ArrayList<>();
        for(String answer : shortAnswers){
            answer = answer.replace("{", "");
            answer = answer.replace("}", "");
            answer = answer.replace(", name", "");
            finalAnswer.add(answer);
        }

        Long newId = Long.parseLong(finalAnswer.get(1));
        String newName = finalAnswer.get(2);

        List<Category> categories = new ArrayList<>();

        Category category = new Category(newId, newName);
        categories.add(category);

        System.out.println(categories.toString());
        return categories;
    }
}