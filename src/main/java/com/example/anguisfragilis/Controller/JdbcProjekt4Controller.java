package com.example.anguisfragilis.Controller;

import com.example.anguisfragilis.Domain.HighScore;
import com.example.anguisfragilis.Domain.User;
import com.example.anguisfragilis.Repository.Projekt4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class JdbcProjekt4Controller {

    @Autowired
    private Projekt4Repository repo;

   @PostMapping("/postLogin")
    public String addUsers (@RequestParam String username, @RequestParam String password) {
        String registrationInfo = repo.addUser(new User(username, password));
        //return "index"; // open index.html, but do not update current URL
        return "redirect:login"; //go to GetMapping("")
    }

    @GetMapping("/login")
    public String loginHTML(){
       return "login";
    }

    @GetMapping("/signUp")
    public String signUpHTML(){
        return "signUp";
    }

    @GetMapping("/game")
    public String gameHTML(HttpSession session){
        return "game";
    }

    @GetMapping("/highScore")
    public ModelAndView highScoreHTML(){
        List<HighScore> highScores = repo.getHighScores();
        return new ModelAndView("highScore").addObject("highScoresList", highScores);
    }

    @GetMapping("/loadingScreen")
    public String loadingScreenHTML(){
        return "loadingScreen";
    }

    @PostMapping("/verification")
    public String checkUser (@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = repo.addUserIdIfUserExists(new User(username, password));
        if(user.getUserId() != 0){
            session.setAttribute("user", user);
            return "redirect:loadingScreen";
        }
        return "redirect:login";
    }
    @ResponseBody
    @GetMapping("/userScore/{score}")
    public void addUserScore(@PathVariable int score, HttpSession session){
        System.out.println("Controller addUser");
        User user = (User)session.getAttribute("user");
        repo.addScore(score, user);
    }



}
