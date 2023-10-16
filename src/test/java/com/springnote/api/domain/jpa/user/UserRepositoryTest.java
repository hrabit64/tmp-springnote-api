package com.springnote.api.domain.jpa.user;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("existsUserByName - 주어진 이름(어드민) 이 존재할 때 true를 반환")
    @Test
    void existsUserByName() {
        //given
        var targetName = "어드민";

        //when
        var result = userRepository.existsUserByName(targetName);

        //then
        assertTrue(result);
    }

    @DisplayName("existsUserByName - 존재하지 않는 이름(sigong)이 주어지면 false를 반환")
    @Test
    void existsUserByName2() {
        //given
        var targetName = "sigong";

        //when
        var result = userRepository.existsUserByName(targetName);

        //then
        assertFalse(result);
    }

    @DisplayName("save - 정상적인 정보가 주어지면 , user를 저장한다.")
    @Test
    void save() {
        //given
        var targetUser = User.builder()
                .id("thisIsNotRealUidILikeHos1234")
                .name("테스트")
                .isAdmin(false)
                .build();
        //when
        var savedUser = userRepository.save(targetUser);

        //then
        assertNotNull(savedUser);
        assertEquals(targetUser.getId(),savedUser.getId());
        assertEquals(targetUser.getName(),savedUser.getName());
        assertEquals(targetUser.getIsAdmin(),savedUser.getIsAdmin());
    }

    @DisplayName("delete - 존재하는 유저가 주어지면, 유저를 삭제한다.")
    @Test
    void delete() {
        //given
        var targetUser = User.builder()
                .id("dOiQ2SIn2wLDbMHF87YvSBSV5ylS")
                .name("일반회원")
                .isAdmin(false)
                .build();
        //when
        userRepository.delete(targetUser);

        //then

    }

    @DisplayName("save(update) - 존재하는 유저의 이름을 변경한다.")
    @Test
    void save_update() {
        //given
        var targetUser = User.builder()
                .id("dOiQ2SIn2wLDbMHF87YvSBSV5ylS")
                .name("일반회원")
                .isAdmin(false)
                .build();
        var newName = "일반회원2";
        targetUser.setName(newName);
        //when
        var savedUser = userRepository.save(targetUser);

        //then
        assertEquals(newName,savedUser.getName());
    }
}