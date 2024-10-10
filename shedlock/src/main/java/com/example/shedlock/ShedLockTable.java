package com.example.shedlock;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shed_lock_t")
public class ShedLockTable {

    @Id
    private String taskName;
    private String lockUntilTmstamp;
    private String lockedAtTmstamp;
    private String lockedBy;


}
