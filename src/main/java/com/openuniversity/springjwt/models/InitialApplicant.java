package com.openuniversity.springjwt.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "INITIAL_APPLICANT")
public class InitialApplicant {
    @Id
    Long id;
    Long programstartedid;
    Long streamid;
    Long specializationid;

    Long academiccenterid;
    Long admincenterid;
    Long med_mediumId;

    Long programentryqualificationid;
    String nic;
    String nicFake;
    String correspondanceaddress;

    String namewithinitials;
    String mobileno;
    String faxno;

    String email;
    String mobileverifieid;
    String emailverifiedid;
    Long applicantype_id;
}
