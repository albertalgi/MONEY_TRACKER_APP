package com.manage_money.money_tracker.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.manage_money.money_tracker.database.entities.GroupByTuple;
import com.manage_money.money_tracker.database.entities.MovimentEntity;

import java.util.List;

@Dao
public interface MovimentsDao {
    @Query("SELECT * FROM moviments")
    List<MovimentEntity> getAllMovements();

    @Query("SELECT * FROM moviments WHERE tipus = 'DESPESA'")
    List<MovimentEntity> getDespeses();

    @Query("SELECT * FROM moviments WHERE tipus = 'INGRES'")
    List<MovimentEntity> getIngressos();

    @Query("SELECT SUM(quantitat) FROM moviments WHERE tipus = 'DESPESA'")
    double getSumDespeses();

    @Query("SELECT SUM(quantitat) FROM moviments WHERE tipus = 'INGRES'")
    double getSumIngressos();

    @Query("SELECT * FROM moviments WHERE id =:movimentId")
    MovimentEntity getMovimentById(int movimentId);

    @Query("UPDATE moviments SET tipus =:newTipus, quantitat =:newQuantity, titol =:newTitol, data =:newData, notes =:newObs, classification =:newClassif, picturePath =:newPicturePath WHERE id =:movimentId")
    int updateMoviment(int movimentId, String newTipus, double newQuantity, String newTitol, String newData, String newObs, String newClassif, String newPicturePath);

    @Query("SELECT SUM(quantitat) as total, classification, titol FROM moviments WHERE tipus = 'DESPESA' GROUP BY classification")
    List<GroupByTuple> getGroupByDespeses();

    @Query("SELECT SUM(quantitat) as total, classification, titol FROM moviments WHERE tipus = 'INGRES' GROUP BY classification")
    List<GroupByTuple> getGroupByIngressos();

    /*

    --------------      BY DATE     ----------------------

    */

    @Query("SELECT * FROM moviments WHERE Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate)")
    List<MovimentEntity> getMovimentsByDate(String initDate, String endDate);

    @Query("SELECT * FROM moviments WHERE tipus = 'DESPESA' AND Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate)")
    List<MovimentEntity> getDespesesByDate(String initDate, String endDate);

    @Query("SELECT * FROM moviments WHERE tipus = 'INGRES' AND Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate)")
    List<MovimentEntity> getIngressosByDate(String initDate, String endDate);

    @Query("SELECT SUM(quantitat) FROM moviments WHERE tipus = 'DESPESA' AND Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate)")
    double getSumDespesesByDate(String initDate, String endDate);

    @Query("SELECT SUM(quantitat) FROM moviments WHERE tipus = 'INGRES' AND Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate)")
    double getSumIngressosByDate(String initDate, String endDate);

    @Query("SELECT SUM(quantitat) as total, classification, titol FROM moviments WHERE tipus = 'DESPESA' AND Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate) GROUP BY classification")
    List<GroupByTuple> getGroupByDespesesByDate(String initDate, String endDate);

    @Query("SELECT SUM(quantitat) as total, classification, titol FROM moviments WHERE tipus = 'INGRES' AND Date(data) >= Date(:initDate) AND Date(data) <= Date(:endDate) GROUP BY classification")
    List<GroupByTuple> getGroupByIngressosByDate(String initDate, String endDate);

    /*------------------------------------------------------------*/

    @Insert
    public void insertMoviment(MovimentEntity moviment);

    @Update
    public void updateMoviment(MovimentEntity moviment);

    @Delete
    public void deleteMoviment(MovimentEntity moviment);
}
