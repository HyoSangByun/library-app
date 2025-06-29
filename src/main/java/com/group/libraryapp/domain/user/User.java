package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id = null;

  @Column(nullable = false, length = 20)
  private String name;

  private Integer age;

  /*
  * 연관관계의 주인 효과
  * 1) 상대 테이블을 참조하고 있으면 연관관계 주인
  * 2) 연관관계 주인이 아니면 mappedBy 사용
  * 3) 연관관계의 주인의 setter가 사용되어야만 테이블 연결
  * */

/*   관계의 주도권을 UserLoanHistory가 갖고 있다.
   mappedBy = "user"를 통해 UserLoanHistory의 user필드를 JPA가 연관관계의 주인으로 인식하게 된다.
   cascade : user가 삭제되면 UserLoanHistory도 삭제
   orphanRemoval : 객체간의  관계가 끊어진 데이터를 자동으로 삭제하는 옵션*/
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

  public User(String name, Integer age) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
    }
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

  public Long getId() {
    return id;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void loanBook(String bookName) {
    this.userLoanHistories.add(new UserLoanHistory(this, bookName));
  }

  public void returnBook(String bookName) {
    UserLoanHistory targetHistory = this.userLoanHistories.stream()
        .filter(history -> history.getBookName().equals(bookName))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
    targetHistory.doReturn();
  }

}
