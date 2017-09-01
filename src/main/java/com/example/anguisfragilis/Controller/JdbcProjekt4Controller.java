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
import javax.xml.ws.http.HTTPBinding;
import java.util.List;

@Controller
public class JdbcProjekt4Controller {

    @Autowired
    private Projekt4Repository repo;
    //private HttpServletResponse res;

    @GetMapping("/")
    public String indexHTML(HttpSession session){
        setGlobalHighScoreList(session);
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
            repo.addUser(username, password);
            User user = repo.checkForLogin(new User(username, password));
            setCookiesForUser(session, user);
            int userHighScore = getUserHighScore(user);
            session.setAttribute("userHighScore", userHighScore);
            session.setAttribute("user", user);
            setGlobalHighScoreList(session);
            //clearCookies(session, user);
            return "index";
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

    @GetMapping("/highScore")
    public String highScoreHTML(){
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
            setCookiesForUser(session, user);
            int userHighScore = getUserHighScore(user);
            session.setAttribute("userHighScore", userHighScore);
            session.setAttribute("user", user);
            setGlobalHighScoreList(session);
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
        setGlobalHighScoreList(session);
        int userHighScore = repo.getUserHighScore(user);
        if(userHighScore > (int)session.getAttribute("userHighScore")){
            session.setAttribute("userHighScore", userHighScore);
        }
    }

    @GetMapping("/logOut")
    public String logOut (HttpSession session, HttpServletResponse res){
        session.invalidate();
        Cookie cookie = new Cookie("jessionid", "");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        return "login";
    }


    boolean checkIfUserExists (String username){
        if(repo.getUserByUserName(username) != null){
            return true;
        }
        return false;
    }

    public void setCookiesForUser(HttpSession session, User user){
        session.setAttribute("user", user);
    }


    public void setGlobalHighScoreList(HttpSession session){
        List<HighScore> highScores = repo.getHighScores();
        session.setAttribute("highScoreList", highScores);
    }

    public void changePassword(String username, String password){
        repo.setNewPassword(username, password);
    }

    public int getUserHighScore (User user){
        int userHighScore = repo.getUserHighScore(user);
        return userHighScore;
    }

   /* public void clearCookies (HttpSession session, User user){
        if(user.getUserName() != null) {
            session.invalidate();
            Cookie cookie = new Cookie("jessionid", "");
            cookie.setMaxAge(0);
            res.addCookie(cookie);
        }
    }*/
}
