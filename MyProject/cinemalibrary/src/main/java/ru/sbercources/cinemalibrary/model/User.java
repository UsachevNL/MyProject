package ru.sbercources.cinemalibrary.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "user_seq", allocationSize = 1)
public class User extends GenericModel{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "FK_USER_ROLES")
    )
    private Role role;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private  String lastName;
    @Column(name = "middle_name")
    private  String middleName;
    @Column(name = "birth_date")
    private  String birthDate;
    @Column(name = "email")
    private  String email;
    @Column(name = "phone")
    private  String phone;
    @Column(name = "addres")
    private  String addres;

    @OneToMany(mappedBy = "user")
    private Set<Orders> orders;

    @Builder
    public User(long id, LocalDateTime createdWhen, String createdBy, LocalDateTime updateWhen, String updateBy, boolean isDeleted, LocalDateTime deletedWhen, String deletedBy, Role role, String login, String password, String firstName, String lastName, String middleName, String birthDate, String email, String phone, String addres) {
        super(id, createdWhen, createdBy, updateWhen, updateBy, isDeleted, deletedWhen, deletedBy);
        this.role = role;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.addres = addres;
    }

    @Override
    public String toString() {
        return "User{" +
                "role=" + role.getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", addres='" + addres + '\'' +
                '}';
    }
}
