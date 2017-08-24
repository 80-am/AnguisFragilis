package com.example.anguisfragilis.Controller;

import com.example.anguisfragilis.Domain.HighScore;
import com.example.anguisfragilis.Domain.User;
import com.example.anguisfragilis.Repository.Projekt4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class JdbcProjekt4Controller {

    @Autowired
    private Projekt4Repository repo;

   @PostMapping("/postLogin")
    public ModelAndView addUsers (@RequestParam String username, @RequestParam String password, HttpSession session) {
       ModelAndView setWarning = new ModelAndView();
       if(username.length()>15){
           return new ModelAndView("signUp").addObject("warning", "Username must be less than 15 characters");
       }
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
    public String loginHTML(HttpSession session){
       if(session.getAttribute("user") != null){
           return "loadingScreen";
       }
       return "login";
    }

    @GetMapping("/signUp")
    public String signUpHTML(){
        return "signUp";
    }

    @GetMapping("/game")
    public ModelAndView gameHTML(HttpSession session){
        User user = (User)session.getAttribute("user");
        int userHighScore = repo.getUserHighScore(user);
        return new ModelAndView("game").addObject("userHighScore", new Integer(userHighScore));
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
        repo.addScore(score, user);
        session.setAttribute("latestScore", score);
    }

    @GetMapping("/logOut")
    public String logOut (HttpSession session, HttpServletResponse res){
        session.invalidate();
        Cookie cookie = new Cookie("jessionid", "");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        return "login";
    }


}
