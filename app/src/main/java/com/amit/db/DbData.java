package com.amit.db;

/**
 * Created by AMIT JANGID on 08/01/2019.
 * <p>
 * this class is responsible for column name and data handling
**/
@SuppressWarnings("unused")
public class DbData
{
    String columnName;
    Object columnData;

    public DbData(String columnName, int columnData)
    {
        this.columnName = columnName;
        this.columnData = columnData;
    }

    public DbData(String columnName, float columnData)
    {
        this.columnName = columnName;
        this.columnData = columnData;
    }

    public DbData(String columnName, double columnData)
    {
        this.columnName = columnName;
        this.columnData = columnData;
    }

    public DbData(String columnName, String columnData)
    {
        this.columnName = columnName;
        this.columnData = columnData;
    }

    /*public DbData(String columnName, byte[] columnData)
    {
        this.columnName = columnName;
        this.bytesData = columnData;
    }*/
}
