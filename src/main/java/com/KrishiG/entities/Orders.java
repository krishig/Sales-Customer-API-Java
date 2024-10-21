package com.KrishiG.entities;

import com.KrishiG.util.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ORDERS")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customerId;

    @Column(name = "total_price")
    private float totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @JoinColumn(name = "payment_method")
    private Long paymentMethod;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private Date modifiedDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "closed_at")
    private Date closedDate;

    @Column(name = "out_of_delivered_at")
    private Date outOfDeliveryDate;

}
