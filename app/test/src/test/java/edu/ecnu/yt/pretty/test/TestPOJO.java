package edu.ecnu.yt.pretty.test;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TestPOJO implements Serializable{
	
	private static final long serialVersionUID = -6152094564256348669L;
	
	private String name;
    private int age;

    public TestPOJO(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
