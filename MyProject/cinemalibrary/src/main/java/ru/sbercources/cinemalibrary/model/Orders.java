package ru.sbercources.cinemalibrary.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "orders_seq", allocationSize = 1)
public class Orders extends GenericModel{

    @Column(name = "rent_date", nullable = false)
    private LocalDateTime rentDate;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDate;

    @Column(name = "returned", nullable = false)
    private boolean returned;

    @Column(name = "rent_period", nullable = false)
    private Integer rentPeriod;

    @Column(name = "purchase")
    private boolean purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_ORDERS_USER"))
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "films_id", foreignKey = @ForeignKey(name = "FK_ORDERS_FILMS"))
    private Films films;

    @Builder
    public Orders(Long id, LocalDateTime createdWhen, String createdBy, LocalDateTime updateWhen, String updateBy, boolean isDeleted, LocalDateTime deletedWhen, String deletedBy, LocalDateTime rentDate, LocalDateTime returnDate, boolean returned, Integer rentPeriod, boolean purchase, Integer amount, User user, Films films) {
        super(id, createdWhen, createdBy, updateWhen, updateBy, isDeleted, deletedWhen, deletedBy);
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.rentPeriod = rentPeriod;
        this.purchase = purchase;
        this.user = user;
        this.films = films;
    }
}
