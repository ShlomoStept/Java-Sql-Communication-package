/*  Written by: Shlomo Stept
    Purpose: This project is a java class/package that greatly simplifies the communication between
    any Java program and a SQL Database. It was built with MySql in mind but does work with other databases

    MIT License
    
    -links to documentation used to build this package:
     https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-connect-drivermanager.html
     https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
     https://docs.oracle.com/javase/7/docs/api/java/sql/CallableStatement.html
     https://docs.oracle.com/javase/7/docs/api/java/sql/Statement.html#execute(java.lang.String)
     https://docs.oracle.com/javase/6/docs/api/java/sql/PreparedStatement.html#

   - JavaDoc comments are present by every function and should explain what information each function requires
        and how to use it
*/
import com.mysql.cj.xdevapi.JsonValue;
import java.sql.*;

import java.util.Scanner;
import java.util.regex.*;
import static java.util.Calendar.DATE;


public class JavaSqlCommunication {

    //---------------------------------------------------------------------------------------------------------------------
        // 0 -> Data Fields:
    //---------------------------------------------------------------------------------------------------------------------
    private String url; // url of database
    private String userName; // username to database
    private String password; // password to database
    private String databaseName; // database name
    private String tableName; // table-name in database
    // to do :: --> :: private Boolean create = false; // is there a table currently in the database

    //---------------------------------------------------------------------------------------------------------------------
        // 1 -> Constructors: JavaSqlCommunication
    //---------------------------------------------------------------------------------------------------------------------

        //  --> 1.1 JavaSqlCommunication
    //-------------------------------------------------------------------------------------
    /**
     * JavaSqlCommunication is a Constructor that instantiates a new instance of the JavaSqlCommunication class
     * @param url The url to the database.
     * @param username The username for the database.
     * @param password The password to the database.
     * @param databasename The database name.
     *  --> NOTES: <--  1. The JavaSqlCommunication, allows for a more user-friendly connection interface between java and a sql server
     *          --> 2. this package was constructed with MYSQL as the primary database, however most if not all the methods are compatible with all Databases
     *
     * ----> IMPORTANT <----  (1) the Url is obtained from the database workspace (ask google depending on the database in question how to obtain)
     *       (2) the Url must be in one of the two forms --> jdbc:"database software name"://localhost:3306 or jdbc:"database software name"://127.0.0.1:3306, Both will work
     *       IMPORTANT --> you MUST get the JDBC Connection url !!!  NOT the regular connection which will typically be in the form of root@127.0.0.1:3306
     */
    public  JavaSqlCommunication(String url, String username, String password, String databasename)
    {
        if(url == null )
            throw new IllegalArgumentException("JavaSqlCommunication: url is null");
        if(username == null )
            throw new IllegalArgumentException("JavaSqlCommunication: username is null");
        if(password == null )
            throw new IllegalArgumentException("JavaSqlCommunication: password is null");
        if(databasename == null )
            throw new IllegalArgumentException("JavaSqlCommunication: databasename is null");
        this.url = url;
        this.userName = username;
        this.password = password;
        this.databaseName = databasename;

        //create = true;
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 1.2 JavaSqlCommunication
        //-------------------------------------------------------------------------------------
    /**
     * JavaSqlCommunication is a Constructor that instantiates a new instance of the JavaSqlCommunication class
     * @param url The url to the database.
     * @param username The username for the database.
     * @param password The password to the database.
     * @param databasename The database name.
     * @param tableName The tablename to this javasqlcommunication instance will work with
     * --> Note: if the user instantiates a JavaSqlCommunication object, then the methods allow for the user to leave out the tablename parameter
     */
    public JavaSqlCommunication(String url, String username, String password, String databasename, String tableName)
    {
        if(url == null )
            throw new IllegalArgumentException("JavaSqlCommunication: url is null");
        if(username == null )
            throw new IllegalArgumentException("JavaSqlCommunication: username is null");
        if(password == null )
            throw new IllegalArgumentException("JavaSqlCommunication: password is null");
        if(databasename == null )
            throw new IllegalArgumentException("JavaSqlCommunication: databasename is null");
        if(tableName == null )
            throw new IllegalArgumentException("JavaSqlCommunication: tableName is null");
        this.url = url;
        this.userName = username;
        this.password = password;
        this.databaseName = databasename;
        this.tableName = tableName;

    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 1.3. toString
        //-------------------------------------------------------------------------------------
    /**
     * toString is a To string method that returns a String with all the vales for the data-fields in the JavaSqlCommunication package/class
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("url = "+ url +"\n");
        sb.append("username = "+ userName +"\n");
        //sb.append("password = "+ password +"\n");  // turned off due too security concerns
        sb.append("databaseName = "+ databaseName +"\n");
        sb.append("tableName = "+ tableName +"\n");
        //sb.append("Create = "+ create +"\n");

        return sb.toString();
    }


    //---------------------------------------------------------------------------------------------------------------------
            // End ----> Code for Constructors  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


        //********************************************************************************************************************
            //********************************************************************************************************************
                //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
        // Methods: Part 1 : JavaSqlCommunication  --> Connection <-- operations
    //---------------------------------------------------------------------------------------------------------------------

        //  --> 1.1 getConnection
    //-------------------------------------------------------------------------------------
    /**
     * getConnection is a function that obtains a connection to the database requested and returns that as a Connection object
     * @return Returns the Connection object with a connection to the database specified in the respective JavaSqlCommunication Instance
     */
    public Connection getConnection() throws Exception {
        return getConnection(this.databaseName+"");
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 1.2 getConnection
        //-------------------------------------------------------------------------------------
    /**
     * getConnection is a function that obtains a connection to the database requested and returns that as a Connection object
     * @return Returns the Connection object with a connection to the database specified in the accompanied JavaSqlCommunication Instance
     *
     * USE TO CONNECT TO A DIFFERENT TABLE FROM THE ONE ENTERED WHEN CONSTRUCTING THE RESPECTIVE  INSTANCE of JavaSqlCommunication
     */
    public Connection getConnection(String databasename) throws Exception {
        return getConnection(this.url+"",this.userName+"", this.password+"",databasename+"");
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 1.3 getConnection
        //-------------------------------------------------------------------------------------

    /**
     * getConnection is a function that obtains a connection to the database requested and returns that as a Connection object
     * @return Returns the Connection object with a connection to the database specified in the accompanied JavaSqlCommunication Instance
     * --> NOTE --> CAN BE USED TO CONNECT TO ANY DATABASE, ITS NOT LIMITEd TO THE ONE SPECIFIED WHEN INSTANTIATING THE RESPECTIVE INSTANCE of JavaSqlCommunication
     */
    public Connection getConnection(String url, String username, String password, String databasename) throws Exception {
        if(url == null )
            throw new IllegalArgumentException("JavaSqlCommunication: url is null");
        if(username == null )
            throw new IllegalArgumentException("JavaSqlCommunication: username is null");
        if(password == null )
            throw new IllegalArgumentException("JavaSqlCommunication: password is null");
        if(databasename == null )
            throw new IllegalArgumentException("JavaSqlCommunication: databasename is null");

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&dbname="+databasename,
                    username+"", password+"");
            Statement stmt = conn.createStatement();
            stmt.executeQuery("USE " + databasename);
            stmt.close();
            System.out.println("Connection Successful");


        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    }


    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for getConnection  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


        //********************************************************************************************************************
            //********************************************************************************************************************
                //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
    // Methods: Part 2 : JavaSqlCommunication  --> Create <-- operations
    //---------------------------------------------------------------------------------------------------------------------

        //  --> 2.1 createTable
    //-------------------------------------------------------------------------------------
    /**
     * createTable is a void function that creates a new table with the given tablename in the database specified when instantiating the respective instance of JavaSqlCommunication
     * -->>>>>>>> ** FURTHER WORK MUST BE DONE TO CREATE AN INSTANCE OF THIS METHOD THT ALLOWS FOR THE USER TO CREATE 1 SPECIFIED ROW WITH A RESPECTIVE COLUMN NAME
     * --> by default since a table must have at least one column if no column is specified the column id - with datatype int will be initialized
     */
    public void createTable(String tablename) throws Exception {
        if(tablename == null)
            throw new IllegalArgumentException("JavaSqlCommunication: tablename is null");
        try (Connection conn = getConnection();
             PreparedStatement create = conn.prepareStatement("CREATE TABLE "+tablename+"(id int)")){
            create.execute();
            System.out.println("Create_table Successfully executed");
            try(PreparedStatement alter = conn.prepareStatement("ALTER TABLE "+tablename+ " CHANGE COLUMN  id"+ "  id"+" INT(11) NOT NULL AUTO_INCREMENT" + " , ADD PRIMARY KEY ( id );")){
                alter.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.2 createTable
        //-------------------------------------------------------------------------------------
    /**
     * createTable is a void function that creates a new table with the given tablename in the database specified by current JavaSqlCommunication instance
     * --> this method allows the user to specify 1 columnname and its associated datatype, when creating the neww table
     */
   /* public void create_table(String tablename, String columnName, String columnDataType) throws Exception {
        try {
            Connection conn = getConnection(url + "", userName + "", password + "", tablename + "");
            //PreparedStatement stmt = conn.prepareStatement( DECLARE @myvar = int; SET @myvar =1)
            //in the future use a switch ( ) to -->
            //  (A) utalize the methods created for each individual type to create a specific column name and datatype,
            //   (B)and then to delete the id int column
            PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tablename(columnName columnDataType) ");
            create.execute();
            create.close();
            System.out.println("Create_table Successfully executed");
        } catch (SQLException ex) {
            // handle any errors
            // ?? should i keep the below printout ?
            //System.out.println("ERROR :: Connection  Failed! -> Error in getConnection");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }*/

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.3.1 createTextColumn
        //-------------------------------------------------------------------------------------
    /**
     * createTextColumn is a function that creates a new Text column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createTextColumn(String columnname) throws Exception {
        createTextColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.3.2 createTextColumn
        //-------------------------------------------------------------------------------------
    /**
     * createTextColumn is a function that creates a new Text column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
      */
    public void createTextColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createTextColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createTextColumn: columnname is null");
        if(columnname.equalsIgnoreCase("text"))
            throw new IllegalArgumentException("createTextColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+" TEXT");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.4.1 createIntColumn
        //-------------------------------------------------------------------------------------
    /**
     * createIntColumn is a function that creates a new Integer column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createIntColumn(String columnname) throws Exception {
        createIntColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.4.2 createIntColumn
        //-------------------------------------------------------------------------------------
    /**
     * createIntColumn is a function that creates a new Integer column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createIntColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createIntColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createIntColumn: columnname is null");
        if(columnname.equalsIgnoreCase("int")|| columnname.equalsIgnoreCase("in")|| columnname.equalsIgnoreCase("integer"))
            throw new IllegalArgumentException("createIntColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");

        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+" INTEGER");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.5.1 createBooleanColumn
        //-------------------------------------------------------------------------------------
    /**
     * createBooleanColumn is a function that creates a new Boolean column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createBooleanColumn(String columnname) throws Exception {
        createBooleanColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.5.2 createBooleanColumn
        //-------------------------------------------------------------------------------------
    /**
     * createBooleanColumn is a function that creates a new Boolean column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createBooleanColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createBooleanColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createBooleanColumn: columnname is null");
        if(columnname.equalsIgnoreCase("boolean"))
            throw new IllegalArgumentException("createBooleanColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+" BOOLEAN");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.6.1 createFloatColumn
        //-------------------------------------------------------------------------------------
    /**
     * createFloatColumn is a function that creates a new Float column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createFloatColumn(String columnname) throws Exception {
        createFloatColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.6.2 createFloatColumn
        //-------------------------------------------------------------------------------------
    /**
     * ccreateFloatColumn is a function that creates a new Float column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createFloatColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createFloatColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createFloatColumn: columnname is null");
        if(columnname.equalsIgnoreCase("float"))
            throw new IllegalArgumentException("createFloatColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+" FLOAT");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.7.1 createRealColumn
        //-------------------------------------------------------------------------------------
    /**
     * createRealColumn is a function that creates a new Real column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createRealColumn(String columnname) throws Exception {
        createRealColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.7.2 createRealColumn
        //-------------------------------------------------------------------------------------
    /**
     * createRealColumn is a function that creates a new Real column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createRealColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createRealColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createRealColumn: columnname is null");
        if(columnname.equalsIgnoreCase("real"))
            throw new IllegalArgumentException("createRealColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+" REAL");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.8.1 createImageColumn
        //-------------------------------------------------------------------------------------
    /**
     * createImageColumn is a function that creates a new Image column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createImageColumn(String columnname) throws Exception {
        createRealColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.8.2 createImageColumn
        //-------------------------------------------------------------------------------------
    /**
     * createImageColumn is a function that creates a new Image column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createImageColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createImageColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createImageColumn: columnname is null");
        if(columnname.equalsIgnoreCase("blob"))
            throw new IllegalArgumentException("createImageColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+"  BLOB");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.9.1 createDateColumn
        //-------------------------------------------------------------------------------------
    /**
     * createDateColumn is a function that creates a new Date column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createDateColumn(String columnname) throws Exception {
        createDateColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.9.2 createDateColumn
        //-------------------------------------------------------------------------------------
    /**
     * createDateColumn is a function that creates a new Date column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createDateColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createDateColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createDateColumn: columnname is null");
        if(columnname.equalsIgnoreCase("date"))
            throw new IllegalArgumentException("createDateColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+"  DATE");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.10.1 createDateTimeColumn
        //-------------------------------------------------------------------------------------
    /**
     * createDateTimeColumn is a function that creates a new DateTime column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createDateTimeColumn(String columnname) throws Exception {
        createDateColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.10.2 createDateTimeColumn
        //-------------------------------------------------------------------------------------
    /**
     * createDateTimeColumn is a function that creates a new DateTime column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createDateTimeColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createDateTimeColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createDateTimeColumn: columnname is null");
        if(columnname.equalsIgnoreCase("datetime"))
            throw new IllegalArgumentException("createDateTimeColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+"  DATETIME");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.11.1 createBlobColumn
        //-------------------------------------------------------------------------------------
    /**
     * createBlobColumn is a function that creates a new Blob column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createBlobColumn(String columnname) throws Exception {
        createBlobColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.11.2 createBlobColumn
        //-------------------------------------------------------------------------------------
    /**
     * createBlobColumn is a function that creates a new Blob column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column should be created
     */
    public void createBlobColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createBlobColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createBlobColumn: columnname is null");
        if(columnname.equalsIgnoreCase("blob"))
            throw new IllegalArgumentException("createBlobColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+"  BLOB");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.12.1 createJSONColumn
        //-------------------------------------------------------------------------------------
    /**
     * createJSONColumn is a function that creates a new JSON column in the database, and specific table specified by current JavaSqlCommunication instance
     * @param columnname The Column-name for the column being created
     */
    public void createJSONColumn(String columnname) throws Exception {
        createJSONColumn(this.tableName, columnname);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 2.12.2 createJSONColumn
     //-------------------------------------------------------------------------------------
    /**
     * createJSONColumn is a function that creates a new JSON column in the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the new column should be created in
     * @param columnname The Column-name for the column being created
     * @param tablename The name of the Table where the column will be created
     */
    public void createJSONColumn(String columnname, String tablename) throws Exception {
        if(tablename ==null)
            throw new IllegalStateException("createJSONColumn: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("createJSONColumn: columnname is null");
        if(columnname.equalsIgnoreCase("json"))
            throw new IllegalArgumentException("createJSONColumn: SQL SYNTAX ERROR columnname cannot be the same as the datatype ");
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement create = conn.prepareStatement("ALTER TABLE " +tablename + " ADD "+columnname+"  JSON");
            create.executeUpdate();
            create.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }



    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for Part 2: Create  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


    //********************************************************************************************************************
    //********************************************************************************************************************
    //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
    // Methods: Part 3 : JavaSqlCommunication  --> Insert <-- operations
    //---------------------------------------------------------------------------------------------------------------------


        //  --> 3.1.1 insertText
    //-------------------------------------------------------------------------------------
    /**
     * insertText is a function that inserts the specified text into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param text The Text to be entered into the column
     * @param columnname The Column-name where the Text will be inserted
     */
    public void  insertText(String text, String columnname) throws Exception{
        insertText(text+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.1.2 insertText
        //-------------------------------------------------------------------------------------
    /**
     * insertText is a function that inserts the specified text into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param text The Text to be entered into the column
     * @param columnname The Column-name where the Text will be inserted
     * @param tablename The name of the Table where the Text will be inserted
     */
    public void insertText(String text, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertText: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertText: columnname is null");
        if(text ==null)
            throw new IllegalStateException("insertText: text is null");
        String var1 =   "\'" + text+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }

    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.2.1 insertInt
        //-------------------------------------------------------------------------------------
    /**
     * insertInt is a function that inserts the specified Integer value into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param integer The Text to be entered into the column
     * @param columnname The Column-name where the Integer value will be inserted
     */
    public void  insertInt(String integer, String columnname) throws Exception{
        insertInt(integer+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
    //  --> 3.2.2 insertInt
    //-------------------------------------------------------------------------------------
    /**
     * insertInt is a function that inserts the specified Integer value into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param integer The Text to be entered into the column
     * @param columnname The Column-name where the Integer value will be inserted
     * @param tablename The name of the Table where the Integer will be inserted
     */
    public void insertInt(String integer, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertInt: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertInt: columnname is null");
        if(integer ==null)
            throw new IllegalStateException("insertInt: integer is null");
        String var1 =   "\'" + integer+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.3.1 insertBoolean
        //-------------------------------------------------------------------------------------
    /**
     * insertBoolean is a function that inserts the specified Boolean value into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param bool The Text to be entered into the column
     * @param columnname The Column-name where the Boolean value will be inserted
     */
    public void  insertBoolean(Boolean bool, String columnname) throws Exception{
        insertBoolean(bool,columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.3.2 insertBoolean
        //-------------------------------------------------------------------------------------
    /**
     * insertBoolean is a function that inserts the specified Boolean value into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param bool The Text to be entered into the column
     * @param columnname The Column-name where the Boolean value will be inserted
     * @param tablename The name of the Table where the Boolean value will be inserted
     */
    public void insertBoolean(Boolean bool, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertBoolean: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertBoolean: columnname is null");
        if(bool ==null)
            throw new IllegalStateException("insertBoolean: bool is null");
        //String var1 =   "\'" + bool+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+bool+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.4.1 insertFloat
        //-------------------------------------------------------------------------------------
    /**
     * insertFloat is a function that inserts the specified Float value into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param flt The Text to be entered into the column
     * @param columnname The Column-name where the Float value will be inserted
     */
    public void  insertFloat(String flt, String columnname) throws Exception{
        insertFloat(flt+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.4.2 insertFloat
        //-------------------------------------------------------------------------------------
    /**
     * insertFloat is a function that inserts the specified Float value into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param flt The Text to be entered into the column
     * @param columnname The Column-name where the Float value will be inserted
     * @param tablename The name of the Table where the Float will be inserted
     */
    public void insertFloat(String flt, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertFloat: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertFloat: columnname is null");
        if(flt ==null)
            throw new IllegalStateException("insertFloat: flt is null");
        String var1 =   "\'" + flt+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.5.1 insertReal
        //-------------------------------------------------------------------------------------
    /**
     * insertReal is a function that inserts the specified Real value into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param real The Text to be entered into the column
     * @param columnname The Column-name where the Real value will be inserted
     */
    public void  insertReal(String real, String columnname) throws Exception{
        insertReal(real+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.5.2 insertReal
        //-------------------------------------------------------------------------------------
    /**
     * insertReal is a function that inserts the specified Real value into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param real The Text to be entered into the column
     * @param columnname The Column-name where the Real value will be inserted
     * @param tablename The name of the Table where the Real value will be inserted
     */
    public void insertReal(String real, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertReal: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertReal: columnname is null");
        if(real ==null)
            throw new IllegalStateException("insertReal: real is null");
        String var1 =   "\'" + real+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.6.1 insertImage
    //** --> i think this needs to change to accept image Objects (as a binary string that is) **
        //-------------------------------------------------------------------------------------
    /**
     * insertImage is a function that inserts the specified image into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param imageUrl The Text to be entered into the column
     * @param columnname The Column-name where the Image will be inserted
     */
    public void  insertImage(String imageUrl, String columnname) throws Exception{
        insertImage(imageUrl+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.6.2 insertImage
        //-------------------------------------------------------------------------------------
    /**
     * insertImage is a function that inserts the specified image into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param imageUrl The Text to be entered into the column
     * @param columnname The Column-name where the Image will be inserted
     * @param tablename The name of the Table where the Image will be inserted
     */
    public void insertImage(String imageUrl, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertImage: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertImage: columnname is null");
        if(imageUrl ==null)
            throw new IllegalStateException("insertImage: imageUrl is null");
        String var1 =   "\'" + imageUrl+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.7.1 insertDate
        //-------------------------------------------------------------------------------------
    /**
     * insertDate is a function that inserts the specified Date into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param date The Text to be entered into the column
     * @param columnname The Column-name where the Date will be inserted
     */
    public void  insertDate(String date, String columnname) throws Exception{
        insertDate(date+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.7.2 insertDate
        //-------------------------------------------------------------------------------------
    /**
     * insertDate is a function that inserts the specified Date into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param date The Text to be entered into the column
     * @param columnname The Column-name where the Date will be inserted
     * @param tablename The name of the Table where the Date will be inserted
     */
    public void insertDate(String date, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertDate: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertDate: columnname is null");
        if(date ==null)
            throw new IllegalStateException("insertDate: date is null");
        String var1 =   "\'" + date+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }

    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.8.1 insertDateTime
        //-------------------------------------------------------------------------------------
    /**
     * insertDateTime is a function that inserts the specified datetime into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param datetime The Text to be entered into the column
     * @param columnname The Column-name where the datetime will be inserted
     */
    public void  insertDateTime(String datetime, String columnname) throws Exception{
        insertDateTime(datetime+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.8.2 insertDateTime
        //-------------------------------------------------------------------------------------
    /**
     * insertDateTime is a function that inserts the specified datetime into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param datetime The Text to be entered into the column
     * @param columnname The Column-name where the datetime will be inserted
     * @param tablename The name of the Table where the datetime will be inserted
     */
    public void insertDateTime(String datetime, String columnname, String tablename) throws Exception{
       if(tablename==null)
            throw new IllegalStateException("insertDateTime: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertDateTime: columnname is null");
        if(datetime ==null)
            throw new IllegalStateException("insertDateTime: datetime is null");
        String var1 =   "\'" + datetime+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.9.1 insertBlob
        //-------------------------------------------------------------------------------------
    /**
     * insertBlob is a function that inserts the specified Blob value into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param blob The Text to be entered into the column
     * @param columnname The Column-name where the blob will be inserted
     */
    public void  insertBlob(String blob, String columnname) throws Exception{
        insertBlob(blob+"",columnname+"",this.tableName+"" );
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.9.2 insertBlob
        //-------------------------------------------------------------------------------------
    /**
     * insertBlob is a function that inserts the specified Blob value into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param blob The Text to be entered into the column
     * @param columnname The Column-name where the blob will be inserted
     * @param tablename The name of the Table where the blob will be inserted
     */
    public void insertBlob(String blob, String columnname, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insertBlob: tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertBlob: columnname is null");
        if(blob ==null)
            throw new IllegalStateException("insertBlob: blob is null");
        String var1 =   "\'" + blob+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


/*
        TO DO: ::---> This does not work properly yet

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.10.1 insertJSON
    //   ** --> i think this needs to change to accept json (objects..) **
        //-------------------------------------------------------------------------------------
    */
/**
     * insertJSON is a function that inserts the specified JSON object into the specified column
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param json The JSON to be entered into the column
     * @param columnname The Column-name where the JSON will be inserted
     *//*

    public void  insertJSON(String json, String columnname) throws Exception{
        insertJSON(json+"",columnname+"",this.tableName+"" );
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.10.2 insertJSON
        //-------------------------------------------------------------------------------------
    */
/**
     * insertJSON is a function that inserts the specified JSON object into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param json The JSON to be entered into the column
     * @param columnname The Column-name where the JSON will be inserted
     * @param tablename The name of the Table where the JSON will be inserted
     *//*

    public void insertJSON(String json, String columnname, String tablename) throws Exception{
       if(tablename==null)
            throw new IllegalStateException(" insertJSON::  tablename is null");
        if(columnname ==null)
            throw new IllegalStateException("insertJSON:: columnname is null");
        if(json ==null)
            throw new IllegalStateException("insertJSON:: json is null");
        String var1 =   "\'" + json+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +columnname+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
*/

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 3.11.2 insert  ---> inserts any value ito any column,  it must be able to get the datatype for the column????? does it ???
    //  **  i have to finnish this later ---> come back and update **
        //-------------------------------------------------------------------------------------
    /**
     * insert is a function that inserts the specified value into the specified column
     * :: --> ::NOTE 0: This method allows the user to specify the table where the new column should be created in
     * --> Note 1: The column must already exist in the table specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will create a new row, and will not update/add the value to an existing row, for that to occur the user must use the update method/function
     * @param value1 The Value to be entered into the column
     * @param column1name The Column-name where the value will be inserted
     * @param tablename The name of the Table where the value will be inserted
     */
    public void insert(String value1, String column1name, String tablename) throws Exception{
        if(tablename==null)
            throw new IllegalStateException("insert: Currently the tablename is null, so insert has no location to send the insert");
        if(column1name ==null)
            throw new IllegalStateException("insert: columnname is null");
        if(value1 ==null)
            throw new IllegalStateException("insert: value1 is null");
        String var1 =   "\'" + value1+ "\'" ;
        try {
            Connection conn = getConnection(this.databaseName+"");
            PreparedStatement posted = conn.prepareStatement("INSERT INTO " + tablename + "(" +column1name+ ") " + "VALUES" +"("+var1+")");
            posted.executeUpdate();
            posted.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }



    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for Part 3 :  Insert  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


    //********************************************************************************************************************
    //********************************************************************************************************************
    //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
    // Methods: Part 4 : JavaSqlCommunication  --> Update, where  <-- operations
    //---------------------------------------------------------------------------------------------------------------------

        //  --> 4.1.1 updateText
    //-------------------------------------------------------------------------------------
    /**
     * update is a function that Updates all rows in the specified column with the specified value, --> (A) where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_value The Value to be entered into the column
     * @param column_Name The Column-name where the Text will be inserted
     * @param tablename The name of the Table where the Text will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void update(String input_value, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_value == null)
            throw new IllegalStateException(" update: input_value is null");
        if(column_Name == null)
            throw new IllegalStateException(" update: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" update: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" update: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_value+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.1.1 updateText
        //-------------------------------------------------------------------------------------
    /**
     * updateText is a function that Updates all rows in the specified column with the specified text, --> (A) where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_text The Text to be entered into the column
     * @param column_Name The Column-name where the Text will be inserted
     * @param tablename The name of the Table where the Text will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateText(String input_text, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_text == null)
            throw new IllegalStateException(" updateText: input_text is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateText: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateText: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateText: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_text+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.2.1 updateInt
        //-------------------------------------------------------------------------------------
    /**
     * updateInt is a function that Updates all rows in the specified column with the specified integer, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_int The integer to be entered into the column
     * @param column_Name The Column-name where the integer will be inserted
     * @param tablename The name of the Table where the integer will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateInt(String input_int, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_int == null)
            throw new IllegalStateException(" updateInt: input_int is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateInt: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateInt: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateInt: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_int+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.3.1 updateBoolean
        //-------------------------------------------------------------------------------------
    /**
     * updateBoolean is a function that Updates all rows in the specified column with the specified Boolean value, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_bool The Text to be entered into the column
     * @param column_Name The Column-name where the Boolean value will be inserted
     * @param tablename The name of the Table where the Boolean value will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateBoolean(Boolean input_bool, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_bool == null)
            throw new IllegalStateException(" updateBoolean: input_bool is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateBoolean: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateBoolean: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateBoolean: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            //String var1 =   "\'" + input_bool+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + input_bool  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.4.1 updateFloat
        //-------------------------------------------------------------------------------------
    /**
     * updateFloat is a function that Updates all rows in the specified column with the specified Float value, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_float The Float value to be entered into the column
     * @param column_Name The Column-name where the Float value will be inserted
     * @param tablename The name of the Table where the Float value will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateFloat(String input_float, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_float == null)
            throw new IllegalStateException(" updateFloat: input_float is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateFloat: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateFloat: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateFloat: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_float+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.5.1 updateReal
        //-------------------------------------------------------------------------------------
    /**
     * updateReal is a function that Updates all rows in the specified column with the specified Real value, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_real The Real value to be entered into the column
     * @param column_Name The Column-name where the Real value will be inserted
     * @param tablename The name of the Table where the Real value will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateReal(String input_real, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_real == null)
            throw new IllegalStateException(" updateReal: input_real is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateReal: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateReal: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateReal: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_real+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.6.1 updateImage
    // --> work to be done
        //-------------------------------------------------------------------------------------
    /**
     * updateImage is a function that Updates all rows in the specified column with the specified Image, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_imageURL The Image to be entered into the column
     * @param column_Name The Column-name where the Image will be inserted
     * @param tablename The name of the Table where the Image will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateImage(String input_imageURL, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_imageURL == null)
            throw new IllegalStateException(" updateImage: input_value is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateImage: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateImage: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateImage: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_imageURL+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.7.1 updateDate
        //-------------------------------------------------------------------------------------
    /**
     * updateDate is a function that Updates all rows in the specified column with the specified Date, --> (A) ONly where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_date The Date to be entered into the column
     * @param column_Name The Column-name where the Date will be inserted
     * @param tablename The name of the Table where the Date will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateDate(String input_date, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_date == null)
            throw new IllegalStateException(" updateDate: input_date is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateDate: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateDate: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateDate: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_date+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.8.1 updateDateTime
        //-------------------------------------------------------------------------------------
    /**
     * updateDateTime is a function that Updates all rows in the specified column with the specified datetime value, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_datetime The datetime value to be entered into the column
     * @param column_Name The Column-name where the datetime value will be inserted
     * @param tablename The name of the Table where the datetime value will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateDateTime(String input_datetime, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_datetime == null)
            throw new IllegalStateException(" updateDateTime: input_datetime is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateDateTime: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateDateTime: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateDateTime: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_datetime+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.9.1 updateBlob
        //-------------------------------------------------------------------------------------
    /**
     * updateBlob is a function that Updates all rows in the specified column with the specified Blob, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_blob The Text to be entered into the column
     * @param column_Name The Column-name where the Blob will be inserted
     * @param tablename The name of the Table where the Blob will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void updateBlob(String input_blob, String column_Name, String tablename, String whereCondition ) throws Exception{
       if(input_blob == null)
            throw new IllegalStateException(" updateBlob: input_blob is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateBlob: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateBlob: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateBlob: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_blob+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

/*
            TO DO
    //-----------------------------------------------------------------------------------------------------------------
            //  --> 4.10.1 updateJSON
        //-------------------------------------------------------------------------------------
    *//**
     * updateJSON is a function that Updates all rows in the specified column with the specified JSON Value/Object, --> (A) ONLY where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_json The JSON Value/Object to be entered into the column
     * @param column_Name The Column-name where the JSON Value/Object will be inserted
     * @param tablename The name of the Table where the JSON Value/Object will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     *//*
    public void updateJSON(String input_json, String column_Name, String tablename, String whereCondition ) throws Exception{
        if(input_json == null)
            throw new IllegalStateException(" updateJSON: input_json is null");
        if(column_Name == null)
            throw new IllegalStateException(" updateJSON: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" updateJSON: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" updateJSON: whereCondition is null");
        try{
            Connection con = getConnection(this.databaseName+"");
            String var1 =   "\'" + input_json+ "\'" ;

            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            con.close()

        }catch (SQLException ex) {

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }*/

    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for Part 4 :  Update, where  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


    //********************************************************************************************************************
    //********************************************************************************************************************
    //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
    // Methods: Part 5 : JavaSqlCommunication  --> Select  <-- operations, returns the datatype that asked for or object if requested a full row of data
    //---------------------------------------------------------------------------------------------------------------------

        //  --> 5.1.1 selectText
    //-------------------------------------------------------------------------------------
    /**
     * selectText is a function that returns a single row of text from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name  The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single row (table-entry) of text
     */
        public  String selectText(String column_name, String whereCondition ) throws Exception
    {
        return selectText(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.1.2 selectText
        //-------------------------------------------------------------------------------------
    /**
     * selectText is a function that returns a single row of text from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the text should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     */
    public  String selectText(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectText: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectText: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectText: table_name is null");
        // --> must code in a unique method to make this possible
        // --> i.e to actually return all the data values tht are in a row of seperate columns ,
        // we could also ust print it as a string or return it all as a string
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        String text = null;
        ResultSet temp = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            temp = posted.executeQuery();
            temp.next();
            text =temp.getString(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return text;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.2.1 selectInt
        //-------------------------------------------------------------------------------------
    /**
     * selectInt is a function that returns a single integer value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) integer value
     */
    public  Integer selectInt(String column_name, String whereCondition ) throws Exception
    {
        return selectInt(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.2.2 selectInt
        //-------------------------------------------------------------------------------------
    /**
     * selectInt is a function that returns a single integer value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) integer value
     */
    public Integer selectInt(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectInt: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectInt: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectInt: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        Integer integer = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            integer = temp.getInt(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return integer;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.3.1 selectBoolean
        //-------------------------------------------------------------------------------------
    /**
     * selectBoolean is a function that returns a single boolean value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) boolean value
     */
    public  Boolean selectBoolean(String column_name, String whereCondition ) throws Exception
    {
        return selectBoolean(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.3.2 updateInt
        //-------------------------------------------------------------------------------------
    /**
     * selectBoolean is a function that returns a single boolean value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) boolean value
     */
    public  Boolean selectBoolean(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectBoolean: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectBoolean: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectBoolean: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        Boolean bool = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            bool = temp.getBoolean(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return bool;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.4.1 selectFloat
        //-------------------------------------------------------------------------------------
    /**
     * selectFloat is a function that returns a single Float value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Float value
     */
    public  Float selectFloat(String column_name, String whereCondition ) throws Exception
    {
        return selectFloat(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.4.2 selectFloat
        //-------------------------------------------------------------------------------------
    /**
     * selectFloat is a function that returns a single Float value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Float value
     */
    public  Float selectFloat(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectFloat: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectFloat: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectFloat: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        Float aFloat = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            aFloat =temp.getFloat(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return aFloat;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.5.1 selectReal
        //-------------------------------------------------------------------------------------
    /**
     * selectReal is a function that returns a single Real value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Real value
     */
    public Double selectReal(String column_name, String whereCondition ) throws Exception
    {
        return selectReal(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.5.2 selectReal
        //-------------------------------------------------------------------------------------
    /**
     * selectReal is a function that returns a single Real value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Real value
     */
    public  Double selectReal(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectReal: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectReal: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectReal: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        Double real = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            real =temp.getDouble(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return real;
    }



    /////////************
    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.6.1 selectImage
    // ??????????? DO THIS / rework this!!!!!
        //-------------------------------------------------------------------------------------
    /**
     * selectImage is a function that returns a single Image value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Image value
     */
    public  String selectImage(String column_name, String whereCondition ) throws Exception
    {
        return selectImage(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.6.2 selectImage
        //-------------------------------------------------------------------------------------
    /**
     * selectImage is a function that returns a single Image value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Image value
     */
    public  String selectImage(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectImage: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectImage: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectImage: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        String image = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            image =temp.getString(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return image;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.7.1  selectDate
        //-------------------------------------------------------------------------------------
    /**
     * selectDate is a function that returns a single Date value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Date value
     */
    public  String selectDate(String column_name, String whereCondition ) throws Exception
    {
        return selectDate(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.7.2 selectDate
        //-------------------------------------------------------------------------------------
    /**
     * selectDate is a function that returns a single Date value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Date value
     */
    public  String selectDate(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectDate: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectDate: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectDate: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        String date = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            date =temp.getString(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return date;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.8.1 selectDateTime
        //-------------------------------------------------------------------------------------
    /**
     * selectDateTime is a function that returns a single DateTime value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) DateTime value
     */
    public  String selectDateTime(String column_name, String whereCondition ) throws Exception
    {
        return selectDateTime(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.8.2 selectDateTime
        //-------------------------------------------------------------------------------------
    /**
     * selectDateTime is a function that returns a single DateTime value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) DateTime value
     */
    public  String selectDateTime(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(column_name == null)
            throw new IllegalStateException("selectDateTime: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectDateTime: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectDateTime: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        String datetime = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            datetime =temp.getString(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return datetime;
    }



    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.9.1 selectBlob
        //-------------------------------------------------------------------------------------
    /**
     * selectBlob is a function that returns a single Blob value from the database, based specific table specified by current JavaSqlCommunication instance
     * @param column_name The Column-name from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Blob value
     */
    public  String selectBlob(String column_name, String whereCondition ) throws Exception
    {
        return selectBlob(column_name, whereCondition, this.tableName);
    }

    //-----------------------------------------------------------------------------------------------------------------
            //  --> 5.9.2 selectBlob
        //-------------------------------------------------------------------------------------
    /**
     * selectBlob is a function that returns a single Blob value from the database specified by current JavaSqlCommunication instance,
     * NOTE: This method allows the user to specify the table where the value should be obtained from
     * @param column_name The Column-name from where the value is selected
     * @param table_name The name of the Table from where the value is selected
     * @param whereCondition The Condition that must be met for the select to occur in a specific row
     * @return Returns a single (table-entry) Blob value
     */
    public  String selectBlob(String column_name, String whereCondition, String table_name ) throws Exception {
        if(column_name == null)
            throw new IllegalStateException("selectBlob: column_name is null");
        if(whereCondition == null)
            throw new IllegalStateException("selectBlob: whereCondition is null");
        if(table_name == null)
            throw new IllegalStateException("selectBlob: table_name is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        String blob = null;
        try{
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();
            blob =temp.getString(1);
            temp.close();
            posted.close();
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return blob;
    }



    /*//  ****    ------- >  TO DO!!!!
    // --> the implementation depends on the datatype and the structure of the json file itself as such
    //        it requires unique implementation
    //-----------------------------------------------------------------------------------------------------------------
    //  --> 5.10.1 selectJSON
    //-------------------------------------------------------------------------------------
    public JsonValue selectJSON(String column_name, String whereCondition ) throws Exception
    {
        return selectJSON(column_name, whereCondition, this.tableName)
    }

    //-----------------------------------------------------------------------------------------------------------------
    //  --> 5.10.2 selectJSON
    //-------------------------------------------------------------------------------------
    public  JsonValue selectJSON(String column_name, String whereCondition, String table_name ) throws Exception
    {
        if(table_name==null)
            throw new IllegalStateException(" selectJSON: tablename is null");
        if(column_name.compareToIgnoreCase("all") == 0 )
            column_name = "*";

        JsonValue json = null;
        try{
            Connection con = getConnection(this.databaseName+"");

            PreparedStatement posted = con.prepareStatement("SELECT " + column_name + " FROM " + table_name + " WHERE " + whereCondition);
            ResultSet temp = posted.executeQuery();
            temp.next();

            json =temp.get(1);
            posted.close();
            conn.close()

        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }finally{System.out.println("Select Text Successfully executed");}
        return json;
    }
*/

    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for Part 5 :  Select  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


    //********************************************************************************************************************
    //********************************************************************************************************************
    //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
    // Methods: Part 6 : JavaSqlCommunication  --> Delete <-- operations
    //---------------------------------------------------------------------------------------------------------------------




    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for Part 6 :  delete  <----- End.
    //---------------------------------------------------------------------------------------------------------------------


    //********************************************************************************************************************
    //********************************************************************************************************************
    //********************************************************************************************************************


    //---------------------------------------------------------------------------------------------------------------------
    // Methods: Part 7 : JavaSqlCommunication  --> useful functions  <-- operations
    //---------------------------------------------------------------------------------------------------------------------
    //  --> 7.1.1 addText
    //-------------------------------------------------------------------------------------
    /**
     * addText is a function that adds tet to an existing entry of text contianedd in a  in the specified column with the specified value, --> (A) where the specified condition is true
     * :: --> ::NOTE 0: This method allows the user to specify the table where the update should occur in
     *      * --> Note 1: The column must already exist in the database specified by current JavaSqlCommunication instance.
     *      * --> Note 2: This method will NOT create a new row, HOWEVER it will update the value to an existing row
     * @param input_value The Value to be entered into the column
     * @param column_Name The Column-name where the Text will be inserted
     * @param tablename The name of the Table where the Text will be inserted
     * @param whereCondition The Condition that must be met for the update to occur in a specific row
     */
    public void addText(String input_value, String column_Name, String whereCondition, String tablename ) throws Exception{
        if(input_value == null)
            throw new IllegalStateException(" update: input_value is null");
        if(column_Name == null)
            throw new IllegalStateException(" update: column_Name is null");
        if(tablename == null)
            throw new IllegalStateException(" update: tablename is null");
        if(whereCondition == null)
            throw new IllegalStateException(" update: whereCondition is null");
        try{
            // part 1 --> get the value already in the box/location and add the new addition to the end
            StringBuilder text = new StringBuilder();
            ResultSet temp = null;
            Connection con = getConnection(this.databaseName+"");
            PreparedStatement get = con.prepareStatement("SELECT " + column_Name + " FROM " + tablename + " WHERE " + whereCondition);
            temp = get.executeQuery();
            temp.next();
            text.append(temp.getString(1));
            get.close();
            temp.close();
            text.append(", " + input_value);

            // part 2 --> update the var/box
            String var1 =   "\'" + text.toString()+ "\'" ;
            PreparedStatement posted = con.prepareStatement("UPDATE " + tablename + " SET " + column_Name + " = " + var1  +" WHERE " + whereCondition);
            posted.executeUpdate();
            posted.close();
            
            con.close();
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }




    //---------------------------------------------------------------------------------------------------------------------
    // End ----> Code for Part 7 :  useful functions  <----- End.
    //---------------------------------------------------------------------------------------------------------------------





    //---------------------------------------------------------------------------------------------------------------------
    //  ::   MAIN   ::
    //---------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) throws Exception {
    }


}
