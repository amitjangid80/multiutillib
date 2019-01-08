package com.amit.db;

/**
 * Created by AMIT JANGID on 08/01/2019.
 * <p>
 * this class is helper class for adding columns for the tables of the database
**/
@SuppressWarnings("unused")
public class DbColumn
{
    String columnName, columnDataType;

    /**
     * 2019 January 08 - Tuesday - 03:38 PM
     * constructor with parameters for adding columns for the table
     *
     * @param columnName     - Name of the column for the table
     * @param columnDataType - Data type of the column
    **/
    public DbColumn(String columnName, String columnDataType)
    {
        this.columnName = columnName;
        this.columnDataType = columnDataType;
    }

    /**
     * 2019 January 08 - Tuesday - 03:38 PM
     * constructor with parameters for adding columns for the table
     *
     * @param columnName      - Name of the column for the table
     * @param columnDataTypes - String array for data types and with constraints
     *                        Ex: not null, unique, etc
    **/
    public DbColumn(String columnName, String[] columnDataTypes)
    {
        StringBuilder finalDataTypes = new StringBuilder();

        for (int i = 0; i < columnDataTypes.length; i++)
        {
            if (!columnDataTypes[i].startsWith(" "))
            {
                columnDataTypes[i] = " " + columnDataTypes[i];
            }

            if (!columnDataTypes[i].endsWith(" "))
            {
                columnDataTypes[i] = columnDataTypes[i] + " ";
            }

            finalDataTypes.append(columnDataTypes[i].toUpperCase());
        }

        this.columnName = columnName;
        this.columnDataType = finalDataTypes.toString();
    }
}
