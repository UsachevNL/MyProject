package ru.sbercources.cinemalibrary.MVC.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.RememberPasswordDto;
import ru.sbercources.cinemalibrary.dto.UserDto;
import ru.sbercources.cinemalibrary.mapper.UserMapper;
import ru.sbercources.cinemalibrary.model.User;
import ru.sbercources.cinemalibrary.service.UserService;
import ru.sbercources.cinemalibrary.service.userDetails.CustomUserDetails;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Hidden
@Controller
@RequestMapping("/users")
public class MVCUserController {
    private final UserService service;
    private final UserMapper mapper;

    public MVCUserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UserDto userDto) {
        System.out.println(userDto);
        service.create(mapper.toEntity(userDto));
        return "redirect:login";
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Integer id, Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!id.equals(customUserDetails.getUserId())) {
            return HttpStatus.FORBIDDEN.toString();
        }

        model.addAttribute("user", mapper.toDto(service.getOne(Long.valueOf(id))));
        return "profile/viewProfile";
    }

    @GetMapping("/profile/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!id.equals(customUserDetails.getUserId())) {
            return HttpStatus.FORBIDDEN.toString();
        }
        model.addAttribute("user", mapper.toDto(service.getOne(Long.valueOf(id))));
        return "profile/updateProfile";
    }

    @PostMapping("/profile/update")
    public String update(@ModelAttribute("userForm") UserDto userDto) {
        User foundedUser = service.getOne(userDto.getId());
        foundedUser.setLogin(userDto.getLogin());
        foundedUser.setFirstName(userDto.getFirstName());
        foundedUser.setLastName(userDto.getLastName());
        foundedUser.setMiddleName(userDto.getMiddleName());
        foundedUser.setEmail(userDto.getEmail());
        foundedUser.setBirthDate(userDto.getBirthDate());
        foundedUser.setPhone(userDto.getPhone());
        foundedUser.setAddres(userDto.getAddres());
        userDto = mapper.toDto(foundedUser);
        service.update(mapper.toEntity(userDto));
        return "redirect:/users/profile/" + userDto.getId();
    }

    @GetMapping("/list")
    public String userFilms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<User> userPage = service.listAllPaginated(pageRequest);
        List<UserDto> userDtos = userPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("users", new PageImpl<>(userDtos, pageRequest, userPage.getTotalElements()));
        return "users/viewAllUsers";
    }
    @GetMapping("/ban/{userId}")
    public String banUser(@PathVariable Long userId) {
        service.banUser(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/unban/{userId}")
    public String unban(@PathVariable Long userId) {
        service.unbanUser(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/add-service_operator")
    public String createServiceOperator(@ModelAttribute("userForm") UserDto userDto) {
        return "users/addServiceOperator";
    }

    @PostMapping("add-service_operator")
    public String createServiceOperator(@ModelAttribute("userForm") @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/users/addServiceOperator";
        } else {
            service.createServiceOperator(mapper.toEntity(userDto));
            return "redirect:/users/list";
        }
    }

    @GetMapping("remember-password")
    public String rememberPassword(){
        return "rememberPassword";
    }

    @PostMapping("remember-password")
    public String rememberPassword(@ModelAttribute("email") RememberPasswordDto rememberPasswordDto) {
        UserDto userDto = mapper.toDto(service.getUserByEmail(rememberPasswordDto.getEmail()));
        if(Objects.isNull(userDto)) {
            return "redirect:/error-with-message?message=User not found";
        } else {
            service.sendChangePasswordEmail(userDto.getEmail(), "http://localhost:9090/users/change-password/" + userDto.getId());
            return "redirect:/login";
        }
    }

    @GetMapping("/change-password/{userId}")
    public String changePassword(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "changePassword";
    }

    @PostMapping("/change-password/{userId}")
    public String changePassword(@PathVariable Long userId, @ModelAttribute("changePasswordForm") UserDto userDto) {
        service.changePassword(userId, userDto.getPassword());
        return "redirect:/login";
    }
}

