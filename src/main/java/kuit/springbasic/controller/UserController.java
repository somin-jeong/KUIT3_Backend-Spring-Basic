package kuit.springbasic.controller;

import jakarta.servlet.http.HttpServletRequest;
import kuit.springbasic.db.MemoryUserRepository;
import kuit.springbasic.domain.User;
import kuit.springbasic.util.UserSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final MemoryUserRepository memoryUserRepository;

    /**
     * TODO: showUserForm
     */
    @RequestMapping("/form")
    public String forwardToUserLoginForm(){
        return "/user/form";
    }


    /**
     * TODO: createUser
     * createUserV1 : @RequestParam
     * createUserV2 : @ModelAttribute
     */
    @RequestMapping("/signup")
    public String signUp(@RequestParam("userId") String userId,
                         @RequestParam("password") String password,
                         @RequestParam("name") String name,
                         @RequestParam("email") String emai) throws SQLException {
        User user = new User(userId, password, name, emai);
        memoryUserRepository.insert(user);
        return "redirect:/";
    }

//    @RequestMapping("/signup")
    public String signUp(@ModelAttribute User user) throws SQLException {
        memoryUserRepository.insert(user);
        return "redirect:/";
    }


    /**
     * TODO: showUserList
     */
    @RequestMapping("/list")
    public String userList(HttpServletRequest request, Model model) {
        if(UserSessionUtils.isLoggedIn(request.getSession())){
            model.addAttribute("users", memoryUserRepository.findAll());
            return "/user/list";
        }
        return "redirect:/user/loginForm";
    }


    /**
     * TODO: showUserUpdateForm
     */
    @RequestMapping("/updateForm")
    public String userUpdateForm(@RequestParam("userId") String userId, Model model) {
        User user = memoryUserRepository.findByUserId(userId);

        if (user != null) {
            model.addAttribute("user", user);
            return "/user/updateForm";
        }
        return "redirect:/";
    }


    /**
     * TODO: updateUser
     * updateUserV1 : @RequestParam
     * updateUserV2 : @ModelAttribute
     */
    @RequestMapping("/update")
    public String userUpdate(@RequestParam("userId") String userId,
                             @RequestParam("password") String password,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email){
        User user = new User(userId, password, name, email);
        memoryUserRepository.update(user);
        return "redirect:/user/list";
    }

//    @RequestMapping("/update")
    public String userUpdate(@ModelAttribute User user){
        memoryUserRepository.update(user);
        return "redirect:/user/list";
    }

}
