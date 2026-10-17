package com.meindev.exercises;
import java.util.List;
import java.util.ArrayList;

public record Team(
        String name,
        List<String> members
){
    public Team(String name, List<String> members){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("name can't be blank or null.");
        }
        if (members == null){
            throw new IllegalArgumentException("members can't be null.");
        }

        this.name = name;
        this.members = List.copyOf(members); 
    }

    Team withMember(String newMember){
        List<String> mutableList = new ArrayList<>(members);
        mutableList.add(newMember);
        Team newTeam = new Team(name,List.copyOf(mutableList));

        return newTeam;
    }
}