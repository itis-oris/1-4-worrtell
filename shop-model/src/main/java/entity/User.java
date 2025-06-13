package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import static jakarta.persistence.EnumType.STRING;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "account")
public class User implements Serializable {

    private static final long serialVersionUID = 5718266650651034373L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String hashPassword;

    private String firstName;

    private String lastName;

    @Enumerated(STRING)
    private UserRole role;

    @Enumerated(STRING)
    private UserStatus status;

    public boolean isActive() {
        return true;
        //return status == UserStatus.ACTIVE;
    }
}