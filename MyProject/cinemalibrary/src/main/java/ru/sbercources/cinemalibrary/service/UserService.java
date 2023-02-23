package ru.sbercources.cinemalibrary.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.sbercources.cinemalibrary.dto.LoginDto;
import ru.sbercources.cinemalibrary.model.User;
import ru.sbercources.cinemalibrary.repository.UserRepository;
@Slf4j
@Service
public class UserService extends GenericService<User> { ///СКОПИРОВАНО
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final JavaMailSender javaMailSender;

    private UserService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, JavaMailSender javaMailSender) {
        super(repository);
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public User create(User user) {
        user.setCreatedBy("REGISTRATION");
        user.setRole(roleService.getOne(1L));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setDeleted(false);
        return repository.save(user);
    }

    public User createServiceOperator(User user) {
        user.setCreatedBy("ADMIN");
        user.setRole(roleService.getOne(2L));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setDeleted(false);
        return repository.save(user);
    }

    public User getByLogin(String login) {
        return repository.findUserByLoginAndDeletedFalse(login);
    }

    public boolean checkPassword(LoginDto loginDto) {
        return bCryptPasswordEncoder.matches(loginDto.getPassword(), getByLogin(loginDto.getLogin()).getPassword());
    }

    public Page<User> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void banUser(Long id) {
        User user = getOne(id);
        user.setDeleted(true);
        update(user);
    }

    public void unbanUser(Long id) {
        User user = getOne(id);
        user.setDeleted(false);
        update(user);
    }

    public void sendChangePasswordEmail(String email, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Восстановление пароля на сайте Онлайн Библиотека");
        message.setText("Добрый день. Вы получили это письмо, так как с вашего аккаунта была отправлена заявка <br> на восстановление пароля.\n"
                + "Для восстановления пароля перейдите по ссылке: " + verificationLink);
        javaMailSender.send(message);
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void changePassword(Long userId, String password) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setPassword(bCryptPasswordEncoder.encode(password));
        update(user);
    }
}

