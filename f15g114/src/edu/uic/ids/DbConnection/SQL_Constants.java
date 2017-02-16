/**
 * 
 */
package edu.uic.ids.DbConnection;

/**
 * @author Nitheen
 *
 */
public class SQL_Constants {
	
	//Statements to create new tables and add the constraints
	
	public static final String query_0 = "CREATE TABLE IF NOT EXISTS F15g114_Login_Details (Net_Id varchar(10)  NOT NULL,User_Type char(1)  NOT NULL,Login_Timestamp timestamp  NOT NULL,IP varchar(20)  NOT NULL);";
	public static final String query_1 = "CREATE TABLE IF NOT EXISTS F15g114_Assessment_Details (Assessment_No varchar(50)  NOT NULL,Qn_No int  NOT NULL,Qn_Type varchar(30)  NOT NULL,Actual_Qn text  NOT NULL,Answer text  NOT NULL,Tolerance decimal(10,2)  NULL,Course_Id varchar(10)  NOT NULL);";
	public static final String query_2 = "CREATE TABLE IF NOT EXISTS F15g114_Assessments (Assessment_No varchar(50)  NOT NULL,Assessment_Table_Name varchar(30)  NOT NULL,Course_Id varchar(10)  NOT NULL,Net_Id varchar(10)  NOT NULL,CONSTRAINT F15g114_Assessments_pk PRIMARY KEY (Assessment_No,Net_Id));";
	public static final String query_3 = "CREATE TABLE IF NOT EXISTS F15g114_Course_Details (Course_Id varchar(10)  NOT NULL,Course_Name varchar(100)  NOT NULL,CONSTRAINT Course_Details_pk PRIMARY KEY (Course_Id));";
	public static final String query_4 = "CREATE TABLE IF NOT EXISTS F15g114_Professor_Details (Net_Id varchar(10)  NOT NULL,First_Name varchar(100)  NOT NULL,Last_Name varchar(100)  NOT NULL,Course_Id varchar(10)  NOT NULL,CONSTRAINT Professor_Details_pk PRIMARY KEY (Net_Id,Course_Id));";
	public static final String query_5 = "CREATE TABLE IF NOT EXISTS F15g114_Response (R_Id int  NOT NULL  AUTO_INCREMENT,Net_Id varchar(10)  NOT NULL,Response text  NOT NULL,Course_Id varchar(10)  NOT NULL,Assessment_No varchar(50)  NOT NULL,Qn_No int  NOT NULL,Response_Correct varchar(5)  NOT NULL,CONSTRAINT Response_pk PRIMARY KEY (R_Id));";
	public static final String query_6 = "CREATE TABLE IF NOT EXISTS F15g114_Student_Details (Net_Id varchar(10)  NOT NULL,First_Name varchar(100)  NOT NULL,Last_Name varchar(100)  NOT NULL,Course_Id varchar(10)  NOT NULL,CONSTRAINT Student_Details_pk PRIMARY KEY (Net_Id,Course_Id));";
	public static final String query_7 = "CREATE TABLE IF NOT EXISTS F15g114_Student_Grade_Details (Grade_Id int  NOT NULL  AUTO_INCREMENT,Total_Points int  NOT NULL,Scored_Points int  NOT NULL,Net_Id varchar(10)  NOT NULL,Course_Id varchar(10)  NOT NULL,Assessment_No varchar(50)  NOT NULL,Comments text NULL,CONSTRAINT Student_Grade_Details_pk PRIMARY KEY (Grade_Id));";
	public static final String query_8 = "CREATE TABLE IF NOT EXISTS F15g114_TA_Details (Net_Id varchar(10)  NOT NULL,First_Name varchar(100)  NOT NULL,Last_Name varchar(100)  NOT NULL,Course_Id varchar(10)  NOT NULL,CONSTRAINT TA_Details_pk PRIMARY KEY (Net_Id,Course_Id));";
	public static final String query_9 = "CREATE TABLE IF NOT EXISTS F15g114_User_Details (Net_Id varchar(10)  NOT NULL,Password varchar(20)  NOT NULL,User_Type char(1)  NOT NULL,Login_Timestamp timestamp  NOT NULL,IP varchar(20)  NOT NULL,CONSTRAINT User_Details_pk PRIMARY KEY (Net_Id));";
	public static final String query_10 = "ALTER TABLE F15g114_Assessment_Details ADD CONSTRAINT Assessment_Course_Details FOREIGN KEY Assessment_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_11 = "ALTER TABLE F15g114_Assessment_Details ADD CONSTRAINT Assessment_Details_Assessments FOREIGN KEY Assessment_Details_Assessments (Assessment_No) REFERENCES F15g114_Assessments (Assessment_No);";
	public static final String query_12 = "ALTER TABLE F15g114_Assessments ADD CONSTRAINT Assessments_Course_Details FOREIGN KEY Assessments_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_13 = "ALTER TABLE F15g114_Assessments ADD CONSTRAINT Assessments_User_Details FOREIGN KEY Assessments_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";
	public static final String query_14 = "ALTER TABLE F15g114_Professor_Details ADD CONSTRAINT Professor_Details_Course_Details FOREIGN KEY Professor_Details_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_15 = "ALTER TABLE F15g114_Professor_Details ADD CONSTRAINT Professor_Details_User_Details FOREIGN KEY Professor_Details_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";
	public static final String query_16 = "ALTER TABLE F15g114_Response ADD CONSTRAINT Response_Assessments FOREIGN KEY Response_Assessments (Assessment_No) REFERENCES F15g114_Assessments (Assessment_No);";
	public static final String query_17 = "ALTER TABLE F15g114_Response ADD CONSTRAINT Response_Course_Details FOREIGN KEY Response_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_18 = "ALTER TABLE F15g114_Response ADD CONSTRAINT Response_User_Details FOREIGN KEY Response_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";
	public static final String query_19 = "ALTER TABLE F15g114_Student_Details ADD CONSTRAINT Student_Details_Course_Details FOREIGN KEY Student_Details_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_20 = "ALTER TABLE F15g114_Student_Details ADD CONSTRAINT Student_Details_User_Details FOREIGN KEY Student_Details_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";
	public static final String query_21 = "ALTER TABLE F15g114_Student_Grade_Details ADD CONSTRAINT Student_Grade_Details_Assessments FOREIGN KEY Student_Grade_Details_Assessments (Assessment_No) REFERENCES F15g114_Assessments (Assessment_No);";
	public static final String query_22 = "ALTER TABLE F15g114_Student_Grade_Details ADD CONSTRAINT Student_Grade_Details_Course_Details FOREIGN KEY Student_Grade_Details_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_23 = "ALTER TABLE F15g114_Student_Grade_Details ADD CONSTRAINT Student_Grade_Details_User_Details FOREIGN KEY Student_Grade_Details_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";
	public static final String query_24 = "ALTER TABLE F15g114_TA_Details ADD CONSTRAINT TA_Details_Course_Details FOREIGN KEY TA_Details_Course_Details (Course_Id) REFERENCES F15g114_Course_Details (Course_Id);";
	public static final String query_25 = "ALTER TABLE F15g114_TA_Details ADD CONSTRAINT TA_Details_User_Details FOREIGN KEY TA_Details_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";
	public static final String query_26 = "ALTER TABLE F15g114_Login_Details ADD CONSTRAINT F15g114_Login_Details_F15g114_User_Details FOREIGN KEY F15g114_Login_Details_F15g114_User_Details (Net_Id) REFERENCES F15g114_User_Details (Net_Id);";

	//Statements to drop the tables in the database
	
	public static final String query_27 = "DROP TABLE IF EXISTS F15g114_Student_Grade_Details;";
	public static final String query_28 = "DROP TABLE IF EXISTS F15g114_TA_Details;";
	public static final String query_29 = "DROP TABLE IF EXISTS F15g114_Student_Details;";
	public static final String query_30 = "DROP TABLE IF EXISTS F15g114_Response;";
	public static final String query_31 = "DROP TABLE IF EXISTS F15g114_Professor_Details;";
	public static final String query_32 = "DROP TABLE IF EXISTS F15g114_Assessment_Details;";
	public static final String query_33 = "DROP TABLE IF EXISTS F15g114_Assessments;";
	public static final String query_34 = "DROP TABLE IF EXISTS F15g114_Course_Details;";
	public static final String query_35 = "DROP TABLE IF EXISTS F15g114_Login_Details;";
	public static final String query_36 = "DROP TABLE IF EXISTS F15g114_User_Details;";


}
