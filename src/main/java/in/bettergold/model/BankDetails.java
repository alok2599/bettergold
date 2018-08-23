package in.bettergold.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "BANK_DETAIL")
public class BankDetails {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 100)
    @NotNull
    private String username;
    
    @Column(name = "CUSOTMER_ACCOUNT_BANK", length = 100)
    @NotNull
    private String customerAccountBank;
    
    @Column(name = "CUSTOMER_ACCOUNT_NUMBER", length = 100)
    @NotNull
    private String customerAccountNumber;
    
    @Column(name = "CUSTOMER_ACCOUNT_IFSC", length = 100)
    @NotNull
    private String customerAccountIfsc;
    
    @Column(name = "ASSIGNED_ACCOUNT_BANKNAME", length = 100)
    @NotNull
    private String assignedAccountBankname;
    
    @Column(name = "ASSIGNED_ACCOUNT_NUMBER", length = 100)
    @NotNull
    private String assignedAccountNumber;
    
    @Column(name = "ASSIGNED_ACCOUNT_HOLDER_NAME", length = 100)
    @NotNull
    private String assignedAccountHolderName;
    
    @Column(name = "ASSIGNED_ACCOUNT_IFSC", length = 100)
    @NotNull
    private String assignedAccountIfsc; 

    @Column(name = "LAST_MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastModifiedTimestamp;

   
}