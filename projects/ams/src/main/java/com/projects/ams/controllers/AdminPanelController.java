package com.projects.ams.controllers;

import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private AdvertRepository advertRepository;

    /*

    1. Lista wszystkich użytkowników
    2. Usunięcia dowolnego użytkownika (poza sobą, czyli zalogowanym)
    3. Zablokowanie dowolnego użytkownika (poza sobą, czyli zalogowanym)
    4. Odblokowanie dowolnego użytkownika (poza sobą, czyli zalogowanym

    */

    private UserRepository userRepository;

    public AdminPanelController(AdvertRepository advertRepository, UserRepository userRepository) {
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users-page";
    }

    @PostMapping("/delete-users")
    public String deleteUser(@RequestParam Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        if (!currentUser.getId().equals(userId)) {
            Long count = advertRepository.countByUserId(userId);
            if (count.equals(0L)) {
                userRepository.deleteById(userId);
            } else {
                return "redirect:/admin/users?deleteNotAllowed";
            }
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/block")
    public String blockUser(@RequestParam Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        if (!currentUser.getId().equals(userId)) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User userToBlock = optionalUser.get();
                userToBlock.setActive(false);
                userRepository.save(userToBlock);
            }
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/unblock")
    public String unblockUser(@RequestParam Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        if (!currentUser.getId().equals(userId)) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User userToUnblock = optionalUser.get();
                userToUnblock.setActive(true);
                userRepository.save(userToUnblock);
            }
        }
        return "redirect:/admin/users";
    }
}
