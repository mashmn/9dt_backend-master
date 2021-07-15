package com._98point6.droptoken.dao;

import com._98point6.droptoken.entities.Games;

public interface GameDAOInterface {
    public abstract void addEmployee(Games games);
    public abstract Games fetchEmployeebyId(String gameId);
    public abstract void updateEmployeeById(String gameId, String newStatus);
    public abstract void  deleteEmployeeById(String gameId);
}
