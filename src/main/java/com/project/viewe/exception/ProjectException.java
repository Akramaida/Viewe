package com.project.viewe.exception;

public class ProjectException extends RuntimeException{

    public ProjectException(String eMessage, Exception e){
        super(eMessage, e);
    }

    public ProjectException(String eMessage){
        super(eMessage);
    }

}
