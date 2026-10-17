package com.meindev.exercises;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;


public class TeamTest{

    @Test
    public void checkTeamsFieldNotNull(){

        Team teamIT = new Team("IT-Support",List.of("abc","bcd"));
        assertThat(teamIT.name()).isNotNull();
        assertThat(teamIT.members()).isNotNull();
    }

    @Test
    public void changeTeamMembersMutability(){
        List<String> teamMembers = new ArrayList<>(List.of("abc","bcd"));
        Team dev = new Team("Dev",teamMembers);

        teamMembers.add("xyz");
        
        assertThat(dev.members()).containsExactly("abc","bcd");

        // assertThatThrownBy(()->{
        //     teamMembers.add("somehting");
        // }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void blankOrNullNameThrowException(){
        String name = null;
        List<String> members = new ArrayList<>(List.of("abc","bcd"));

        assertThatThrownBy(()-> new Team(name, members))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name can't be blank or null.");

    }

    @Test
    public void nullMembersThrowException(){
        String name = "Dev";
        List<String> members = null;

        assertThatThrownBy(() -> new Team(name, members))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("members can't be null.");
    }

    @Test
    public void addNewMember(){
        List<String> teamMembers = List.of("abc","bcd");
        Team dev = new Team("Dev",teamMembers);

        Team teamWithNewMember = dev.withMember("xyz");

        assertThat(teamWithNewMember.members()).containsExactly("abc","bcd","xyz");
    }


}