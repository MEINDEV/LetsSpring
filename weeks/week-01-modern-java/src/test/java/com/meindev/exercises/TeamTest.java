package com.meindev.exercises;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import java.util.List;


public class TeamTest{

    @Test
    public void checkTeamsFieldNotNull(){

        Team teamIT = new Team("IT-Support",List.of("abc","bcd"));
        assertThat(teamIT.name()).isNotNull();
        assertThat(teamIT.members()).isNotNull();
    }

    @Test
    public void changeTeamMembersMutability(){
        List teamMembers = List.of("abc","bcd");
        Team dev = new Team("Dev",teamMembers);

        assertThatThrownBy(()->{
            dev.members().add("name");
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void addNewMember(){
        List teamMembers = List.of("abc","bcd");
        Team dev = new Team("Dev",teamMembers);

        Team teamWithNewMember = dev.withMember("xyz");

        assertThat(teamWithNewMember.members()).containsExactly("abc","bcd","xyz");
    }


}