package com.panda.mojobs.web.controller;

import com.panda.mojobs.domain.User;
import com.panda.mojobs.repository.UserRepository;
import com.panda.mojobs.service.MailService;
import com.panda.mojobs.service.UserService;
import com.panda.mojobs.web.controller.form.RegisterForm;
import com.panda.mojobs.web.controller.util.GlobalMessageUtil;
import com.panda.mojobs.web.rest.errors.InternalServerErrorException;
import com.panda.mojobs.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/common")
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public CommonController(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public String activateAccount(@RequestParam(value = "key") String key,RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
        redirectAttributes.addFlashAttribute(GlobalMessageUtil.GLOBAL_SUCCESS_MESSAGE,"email.activation.success");
        return "redirect:/login";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(Model model, HttpServletRequest request) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerPost(@Valid RegisterForm registerForm, final BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerForm", registerForm);
            return "register/register";
        }
        userRepository.findOneByLogin(registerForm.getLogin().toLowerCase()).ifPresent(u -> {
            bindingResult.addError(new ObjectError("userName", "UserName already in use"));
        });
        userRepository.findOneByEmailIgnoreCase(registerForm.getEmail()).ifPresent(u -> {
            bindingResult.addError(new ObjectError("email", "Email already in use"));

        });
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerForm", registerForm);
            return "register/register";
        }
        User user = userService.registerUser(registerForm);
        mailService.sendActivationEmail(user);
        redirectAttributes.addFlashAttribute(GlobalMessageUtil.GLOBAL_SUCCESS_MESSAGE,"email.creation.send");
        return "redirect:/login";
    }


    @RequestMapping("forgotPassword")
    public String forgotPassword(Model model, HttpServletRequest request) {
        return "register/forgotPassword";
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
