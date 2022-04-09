package com.manage_money.money_tracker.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.manage_money.money_tracker.database.entities.Report;

import java.util.List;

@Dao
public interface ReportsDao {
    @Query("SELECT * FROM reports ORDER BY initData")
    List<Report> getAllReports();

    @Query("SELECT * FROM reports WHERE id =:reportId")
    Report getReportById(int reportId);

    @Query("UPDATE reports SET initData =:newInitDate, endDate =:newEndDate, name =:newName WHERE id =:reportId")
    int updateMoviment(int reportId, String newInitDate, double newEndDate, String newName);

    @Query("SELECT * FROM reports WHERE initData =:initData")
    List<Report> getReportsByDate(String initData);

    @Insert
    public void insertReport(Report report);

    @Update
    public void updateReport(Report report);

    @Delete
    public void deleteReport(Report report);
}
