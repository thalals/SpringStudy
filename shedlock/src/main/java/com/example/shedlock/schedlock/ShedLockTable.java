package com.example.shedlock.schedlock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shed_lock_t")
public class ShedLockTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String taskName;
    private String lockUntilTmstamp;
    private String lockedAtTmstamp;
    private String lockedBy;


}
