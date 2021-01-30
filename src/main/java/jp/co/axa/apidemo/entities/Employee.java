package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;


import javax.persistence.*;

@Entity
@Table(name="EMPLOYEE")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Employee extends VersionedEntity {

    @Getter
    @Setter
    @Version
    @Column(name="VERSION")
    private Integer version;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_NAME")
    private String name;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    private String department;



}
