package com.Api.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Data
public class UserProfile implements Serializable {
    @Id
    private String id;
    private String name;
    private String email;
    @Embedded
    private Phone phone;

    @CreatedDate
    private LocalDateTime  createdAt  ;
    @LastModifiedDate
    private LocalDateTime  updatedAt ;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;


}
