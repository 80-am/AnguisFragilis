package com.example.anguisfragilis.Controller;

import com.example.anguisfragilis.Domain.User;
import com.example.anguisfragilis.Repository.Projekt4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JdbcProjekt4Controller {

    @Autowired
    private Projekt4Repository repo;

   @PostMapping("/postLogin")
    public String addUsers (@RequestParam String username, @RequestParam String password) {
        repo.addUser(new User(username, password));
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
    public String gameHTML(){
        return "game";
    }

    @GetMapping("/highScore")
    public String highScoreHTML(){
        return "highScore";
    }

   /*@GetMapping("/static/index")
    public String indexHTML(){
        return "redirect:index";
    }*/

    @PostMapping("/verification")
    public String checkUser (@RequestParam String username, @RequestParam String password) {
        User user = repo.checkUser(new User(username, password));
        System.out.println("checkUser");
        if(user.getUserId() != 0){
            return "redirect:game";
        }
        return "redirect:login";
    }

}
