package kuit.springbasic.controller;

import jakarta.servlet.http.HttpServletRequest;
import kuit.springbasic.db.MemoryUserRepository;
import kuit.springbasic.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

import static kuit.springbasic.util.UserSessionUtils.USER_SESSION_KEY;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class LoginController {

    private final MemoryUserRepository memoryUserRepository;

    /**
     * TODO: showLoginForm
     */
    @RequestMapping("/loginForm")
    public String forwardToUserLoginForm(){
        return "/user/login";
    }

    /**
     * TODO: showLoginFailed
     */
    @RequestMapping("/loginFailed")
    public String forwardToUserLoginFailed(){
        return "/user/loginFailed";
    }


    /**
     * TODO: login
     * loginV1 : @RequestParam("")
     * loginV2 : @RequestParam
     * loginV3 : @RequestParam 생략(비추천)
     * loginV4 : @ModelAttribute
     */
    @RequestMapping("/login")
    public String loginV1(@RequestParam("userId") String userId,
                          @RequestParam("password") String password,
                          HttpServletRequest request) throws SQLException {
        User loggedInUser = new User(userId, password);
        User user = memoryUserRepository.findByUserId(userId);

        if (user != null && user.isSameUser(loggedInUser)) {
            request.getSession().setAttribute(USER_SESSION_KEY, user);
            return "redirect:/";
        }
        return "redirect:/user/loginFailed";
    }

//    @RequestMapping("/login")
    public String loginV2(@ModelAttribute User loggedInUser, // 아무 필드가 선언되지 않은 기본 생성자 통해 객체 생성 후, @ModelAttribute 안의 parameter에 저장된 값들을 setter를 통해 그 생성된 객체에 넣는다.
                          HttpServletRequest request) throws SQLException {      // 따라서 Setter가 선언되어 있어야 함.
        User user = memoryUserRepository.findByUserId(loggedInUser.getUserId());

        if (user != null && user.isSameUser(loggedInUser)) {
            request.getSession().setAttribute(USER_SESSION_KEY, user);
            return "redirect:/";
        }
        return "redirect:/user/loginFailed";
    }


    /**
     * TODO: logout
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SESSION_KEY);
        return "redirect:/";
    }

}
