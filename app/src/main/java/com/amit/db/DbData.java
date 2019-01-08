package com.amit.db;

import android.database.DatabaseUtils;

import java.sql.Blob;

/**
 * Created by AMIT JANGID on 08/01/2019.
 * <p>
 * this class is responsible for column name and data handling
**/
@SuppressWarnings("unused")
public class DbData
{
    Blob blobData;
    String columnName, columnData;

    public DbData(String columnName, int columnData)
    {
        this.columnName = columnName;
        this.columnData = DatabaseUtils.sqlEscapeString(String.valueOf(columnData));
    }

    public DbData(String columnName, float columnData)
    {
        this.columnName = columnName;
        this.columnData = DatabaseUtils.sqlEscapeString(String.valueOf(columnData));
    }

    public DbData(String columnName, double columnData)
    {
        this.columnName = columnName;
        this.columnData = DatabaseUtils.sqlEscapeString(String.valueOf(columnData));
    }

    public DbData(String columnName, String columnData)
    {
        this.columnName = columnName;
        this.columnData = DatabaseUtils.sqlEscapeString(columnData);
    }

    public DbData(String columnName, Blob columnData)
    {
        this.columnName = columnName;
        this.blobData = columnData;
    }
}
