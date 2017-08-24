package com.example.anguisfragilis.Controller;

import com.example.anguisfragilis.Domain.HighScore;
import com.example.anguisfragilis.Domain.User;
import com.example.anguisfragilis.Repository.Projekt4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class JdbcProjekt4Controller {

    @Autowired
    private Projekt4Repository repo;

   @PostMapping("/postLogin")
    public ModelAndView addUsers (@RequestParam String username, @RequestParam String password, HttpSession session) {
       ModelAndView setWarning = new ModelAndView();
        boolean existingUser = repo.addUser(new User(username, password));
        if(existingUser) {
            String warning = "The username is already taken!";

            return new ModelAndView("signUp").addObject("warning", warning);
        }else {
            User user = repo.checkForLogin(new User(username, password));
            session.setAttribute("user", user);
            return new ModelAndView("loadingScreen");
        }
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
    public ModelAndView checkUser (@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = repo.checkForLogin(new User(username, password));
        if(user.getUserId() != 0){
            session.setAttribute("user", user);
            return new ModelAndView("loadingScreen");
        }
        return new ModelAndView("login").addObject("warning", "Invalid username or password");
    }
    @ResponseBody
    @GetMapping("/userScore/{score}")
    public void addUserScore(@PathVariable int score, HttpSession session){
        User user = (User)session.getAttribute("user");
        session.setAttribute("lastScore", score);
        repo.addScore(score, user);
    }



}
