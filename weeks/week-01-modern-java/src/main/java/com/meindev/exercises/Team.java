package com.meindev.exercises;
import java.util.List;
import java.util.ArrayList;

public record Team(
        String name,
        List<String> members
){
    Team withMember(String newMember){
        List mutableList = new ArrayList<>(members);
        mutableList.add(newMember);
        Team newTeam = new Team(name,List.copyOf(mutableList));

        return newTeam;
    }
}