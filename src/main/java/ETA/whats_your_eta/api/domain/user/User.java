package ETA.whats_your_eta.api.domain.user;

import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@EntityListeners({AuditingEntityListener.class})
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // W or M
    @Column
    private Character sex;

    @Column(length = 3)
    private Integer height;

    @Column(length = 3)
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column
    private GoalLevel goalLevel;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    // 현재 사용자가 가진 role에 따른 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.getKey()));
    }

    // Spring Security가 사용자 인증을 위해 필수로 요구하는 메서드들
    @Override
    public String getPassword() {
        return "";
    } // 사용하지 않음

    @Override
    public String getUsername() {
        return this.email; // 이메일을 username으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.role == Role.USER || this.role == Role.ADMIN;
    }

    public void registerProfile(UserRequestDto.Register data) {
        // 이름이 입력된 경우만 업데이트
        if (data.getName() != null && !data.getName().isEmpty()) {
            this.name = data.getName();
        }

        // 필수 필드 업데이트
        this.sex = data.getSex();
        this.height = data.getHeight();
        this.weight = data.getWeight();
        this.goalLevel = data.getGoalLevel();
    }

    // 사용자 정보 업데이트
    public void updateProfile(UserRequestDto.Update data) {
        if (data.getName() != null && !data.getName().isEmpty()) {
            this.name = data.getName();
        }
        if (data.getSex() != null) {
            this.sex = data.getSex();
        }
        if (data.getHeight() != null) {
            this.height = data.getHeight();
        }
        if (data.getWeight() != null) {
            this.weight = data.getWeight();
        }
        if (data.getGoalLevel() != null) {
            this.goalLevel = data.getGoalLevel();
        }
    }
}