package com.toiec.toiec.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CriteriaPointUtils {
    private Map<Integer,Integer> criteriaPointListening;
    private Map<Integer,Integer> criteriaPointReading;

    private static  CriteriaPointUtils instance ;
    public CriteriaPointUtils() {
        criteriaPointListening= new HashMap<Integer,Integer>();
        criteriaPointReading=  new HashMap<Integer,Integer>();
        for(int i=0;i<=100;i++)
        {
            if(i<=6)
            {
                criteriaPointListening.put(i,5);
                criteriaPointReading.put(i,5);
            }else if(i<=9){
                criteriaPointListening.put(i,criteriaPointListening.get(i-1)+5);
                criteriaPointReading.put(i,5);
            }else if(i==39) {
                criteriaPointListening.put(i,criteriaPointListening.get(i-1)+10);
                criteriaPointReading.put(i,criteriaPointReading.get(i-1)+10);
            }else if(i==25||i==28||i==43||i==47||i==52||i==55||i==64||i==89||i==92) {
                criteriaPointListening.put(i,criteriaPointListening.get(i-1)+5);
                criteriaPointReading.put(i,criteriaPointReading.get(i-1)+10);
            }else if(i==31||i==44||i==45||i==54||i==59||i==70||i==75||i==80||i==85||i==88) {
                criteriaPointListening.put(i,criteriaPointListening.get(i-1)+10);
                criteriaPointReading.put(i,criteriaPointReading.get(i-1)+5);
            } else if (i==82) {
                criteriaPointReading.put(i,405);
                criteriaPointListening.put(i,criteriaPointListening.get(i-1)+5);
            } else if (i==94) {
                criteriaPointListening.put(i,495);
                criteriaPointReading.put(i,criteriaPointReading.get(i-1)+10);
            } else if(i<93){
                criteriaPointListening.put(i,criteriaPointListening.get(i-1)+5);
                criteriaPointReading.put(i,criteriaPointReading.get(i-1)+5);
            }else if(i<=96){
                criteriaPointListening.put(i,495);
                criteriaPointReading.put(i,criteriaPointReading.get(i-1)+5);
            }else{
                criteriaPointListening.put(i,495);
                criteriaPointReading.put(i,495);
            }
        }
    }
    public static synchronized CriteriaPointUtils getInstance() {
        if(instance == null) {
            instance = new CriteriaPointUtils();
        }
        return instance;
    }

    public Integer getPointListeningByCount(Integer integer)
    {
        return criteriaPointListening.get(integer);
    }
    public Integer getPointRedingByCount(Integer integer)
    {
        return criteriaPointReading.get(integer);
    }
}
