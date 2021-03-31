package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
public class UserRole {

    private Integer id;
    private String roleName;

    @OneToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> roleUsers;

    public UserRole(Integer id, String roleName) {

        this.id = id;
        this.roleName = roleName;
    }
}
