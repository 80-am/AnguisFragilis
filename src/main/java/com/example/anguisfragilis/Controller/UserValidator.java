package com.example.anguisfragilis.Controller;


import com.example.anguisfragilis.Domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    public boolean supports(Class clazz){
        return User.class.equals(clazz);
    }

    public void validate(Object obj, Errors e){
        User u = (User) obj;
        if(u.getUserName().length() > 15){
            e.rejectValue("username", "Username must be less than 15 characters");
        }
    }
}
