package com.example.anguisfragilis.Controller;

import com.example.anguisfragilis.Domain.HighScore;
import com.example.anguisfragilis.Domain.User;
import com.example.anguisfragilis.Repository.Projekt4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/")
    public String indexHTML(){
        return "index";
    }

   @PostMapping("/postLogin")
    public String addUsers (@RequestParam String username, @RequestParam String password, HttpSession session , Model model/*, BindingResult result*/) {
       /* User user = new User(username, password);  //Using Validator
        UserValidator userval = new UserValidator();
        if(userval.supports(user.getClass())){
            userval.validate(user, result);
        }
        if(result.hasErrors()){
            return new ModelAndView("signUp").addObject("result", result);
        }*/
       if(username.length()>15){
           model.addAttribute("warning", "Username must be less than 15 characters");
           return "signUp";
       }
        if(checkIfUserExists(username)) {
           model.addAttribute("warning", "User already exists!");
            return "signUp";
        }else {
            User user = repo.checkForLogin(new User(username, password));
            session.setAttribute("user", user);
            return "loadingScreen";
        }
    }

    boolean checkIfUserExists (String username){
       if(repo.getUserByUserName(username) != null){
           return true;
       }
       return false;
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
    public String gameHTML(){
        return "game";
    }

    @GetMapping("/passwordChange")
    public String passwordChangeHTML(){
        return "passwordChange";
    }

    @PostMapping("/passwordVerification")
    public String passwordVerification(@RequestParam String newPassword, @RequestParam String oldPassword, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getPassword().equals(oldPassword)) {
            changePassword(user.getUserName(), newPassword);
            return "index";
        } else {
            model.addAttribute("warning","Wrong password!");
            return "passwordChange";
        }
    }

    public void changePassword(String username, String password){
        repo.setNewPassword(username, password);
    }

    @GetMapping("/highScore")
    public String highScoreHTML(HttpSession session){
        List<HighScore> highScores = repo.getHighScores();
        session.setAttribute("highScoreList", highScores);
        return "highScore";
    }

    @GetMapping("/loadingScreen")
    public String loadingScreenHTML(){
        return "loadingScreen";
    }

    @PostMapping("/verification")
    public String checkUser (@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User user = repo.checkForLogin(new User(username, password));
        if(user.getUserId() != 0){
            int userHighScore = repo.getUserHighScore(user);
            session.setAttribute("userHighScore", userHighScore);
            session.setAttribute("user", user);
            return "index";
        }
        model.addAttribute("warning", "Invalid username or password");
        return "login";
    }
    @ResponseBody
    @GetMapping("/userScore/{score}")
    public void addUserScore(@PathVariable int score, HttpSession session){
        User user = (User)session.getAttribute("user");
        repo.addScore(score-1, user);
        session.setAttribute("latestScore", score-1);
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
