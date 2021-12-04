package com.codegym.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name not empty")
    @Pattern(regexp = "(^([AÀẢÃÁẠĂẰẮẲẴẶÂẤẦẨẪẬBCDĐEÈÉẺẼẸÊỀẾỂỄỆFGHIÍÌỈĨỊJKLMNOÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢPQRSTUÙÚỦŨỤƯỪỨỬỮỰVWXYÝỲỶỸỴZ]+[aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*?[ ]?)+$)", message = "Name format not true, Ex example : Nguyễn Văn A ")
    @Size(min=1 , max=45 ,message = "Full name description within 255 characters ! ")
    private String fullName;

    @Pattern(regexp = "(^[a-z][a-z0-9_\\.]{3,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,7}){1,7}$)" , message = "Mail not true, Ex: codegymhue2021@codegym.com ")
    @Column(unique = true)
    private String email;

    @Pattern(regexp ="(^$|[0][0-9]{9,10}$)",message = "Formatter not true, phone number is have 10-11 character ! " )
    private String phone;

    @Size(min=1 , max=255 , message = "Address description within 255 characters ! ")
    private String address;

    @Min(0)
    private long balance = 0 ;

    private boolean isDelete = false ;
    private LocalDateTime created_at = LocalDateTime.now();

    @OneToMany(targetEntity = Deposit.class, mappedBy = "customer")
    private List<Deposit> deposits;

    @OneToMany(targetEntity = Withdraw.class, mappedBy = "customer")
    private List<Withdraw> withdraws;

    @OneToMany(targetEntity = Transfer.class, mappedBy = "sender")
    private List<Transfer> senders;

    @OneToMany(targetEntity = Transfer.class, mappedBy = "recipient")
    private List<Transfer> recipients;

    public Customer( @NotEmpty(message = "Name not empty")
                     @Pattern(regexp = "(^([AÀẢÃÁẠĂẰẮẲẴẶÂẤẦẨẪẬBCDĐEÈÉẺẼẸÊỀẾỂỄỆFGHIÍÌỈĨỊJKLMNOÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢPQRSTUÙÚỦŨỤƯỪỨỬỮỰVWXYÝỲỶỸỴZ]+[aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*?[ ]?)+$)", message = "Name format not true, Ex example : Nguyễn Văn A")
                     @Size(min=1 , max=45 ,message = "Full name description within 255 characters ! ")
                    String fullName,
                     @Pattern(regexp = "^[a-z][a-z0-9_\\\\.]{3,32}@[a-z0-9]{2,}(\\\\.[a-z0-9]{2,7}){1,7}$" , message = "Mail not true, Ex: codegymhue2021@codegym.com")
                    String email,
                     @Pattern(regexp ="(^$|[0][0-9]{9,10}$)",message = "Formatter not true, phone number is have 10-11 character !" )
                    String phone,
                     @Size(min=1 , max=255 , message = "Address description within 255 characters ! ")
                    String address) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

}

