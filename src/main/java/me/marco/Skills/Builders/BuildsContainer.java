package me.marco.Skills.Builders;

import java.util.HashMap;

public class BuildsContainer {

    private HashMap<eClassType, ClassBuild> builds;

    public BuildsContainer(){
        builds = new HashMap<eClassType, ClassBuild>();
    }

    public ClassBuild getClassBuilds(eClassType eClassType){
        return builds.get(eClassType);
    }

    public void addBuild(eClassType classType, ClassBuild classBuild){
        this.builds.put(classType, classBuild);
    }

    public ClassBuild getActiveBuild(eClassType classType){
        return builds.containsKey(classType) ? builds.get(classType) : null;
    }

}
