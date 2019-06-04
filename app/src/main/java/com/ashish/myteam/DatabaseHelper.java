package com.ashish.myteam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "MyTeam.db";
    private static final String TABLE_NAME2 = "table_user";
    private static final String T2COL_1 = "ID";
    private static final String T2COL_2 = "email";
    private static final String T2COL_3 = "aadhaar";
    private static final String T2COL_4 = "name";
    private static final String T2COL_5 = "nick_name";
    private static final String T2COL_6 = "team";
    private static final String T2COL_7 = "image";
    private static final String T2COL_8 = "dob";
    private static final String T2COL_9 = "food";
    private static final String T2COL_10 = "mobile";
    private static final String T2COL_11 = "bl_grp";
    private static final String T2COL_12 = "pan_no";
    public static final String T2COL_13 = "events";
    public static final String T2COL_14 = "user_transaction";
    public static final String T2COL_15 = "user_about";
    public static final String T2COL_16 = "user_designation";
    public static final String T2COL_17 = "user_hobbies";


    public static final String TABLE_NAME0 = "table_login_user";
    public static final String T0COL_1 = "ID";
    public static final String T0COL_2 = "email";
    public static final String T0COL_3 = "password";


    public static final String TABLE_NAME1 = "table_team";
    public static final String T1COL_1 = "ID";
    public static final String T1COL_2 = "team_name";
    public static final String T1COL_3 = "registering_email";
    public static final String T1COL_4 = "team_pin";

    public static final String TABLE_NAME3 = "table_event";
    public static final String T3COL_1 = "event_ID";
    public static final String T3COL_2 = "event_name";
    public static final String T3COL_3 = "event_owner";
    public static final String T3COL_4 = "event_desc";
    public static final String T3COL_5 = "event_date";
    public static final String T3COL_6 = "event_members";
    public static final String T3COL_7 = "contribution";
    public static final String T3COL_8 = "contri_amount";
    public static final String T3COL_9 = "event_contri";
    public static final String T3COL_10 = "spent_amount";
    public static final String T3COL_11 = "remaining_contri";
    public static final String T3COL_12 = "credit_transactions";
    public static final String T3COL_13 = "debit_transactions";
    public static final String T3COL_14 = "credit_members";


    public static final String TABLE_NAME4 = "table_transaction";
    public static final String T4COL_1 = "transaction_id";
    public static final String T4COL_2 = "user_id";
    public static final String T4COL_3 = "amount";
    public static final String T4COL_4 = "transaction_date";
    public static final String T4COL_5 = "transaction_type";
    public static final String T4COL_6 = "transaction_desc";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE table_user (ID INTEGER PRIMARY  KEY AUTOINCREMENT, email TEXT, aadhaar TEXT, name TEXT, nick_name TEXT, team TEXT,image blob, dob TEXT, food TEXT,mobile TEXT,bl_grp TEXT,pan_no TEXT,events TEXT,user_transaction TEXT, user_about TEXT, user_designation TEXT, user_hobbies TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE table_team (ID INTEGER PRIMARY  KEY AUTOINCREMENT, team_name TEXT, registering_email TEXT,team_pin TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE table_login_user (ID INTEGER PRIMARY  KEY AUTOINCREMENT, email TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE table_event (event_ID INTEGER PRIMARY  KEY AUTOINCREMENT, event_name TEXT, event_owner TEXT,event_desc TEXT, event_date TEXT,event_members TEXT, contribution TEXT, contri_amount INTEGER, event_contri INTEGER, spent_amount INTEGER, remaining_contri INTEGER, credit_transactions TEXT, debit_transactions TEXT, credit_members TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE table_transaction (transaction_id INTEGER PRIMARY  KEY AUTOINCREMENT, user_id TEXT, amount INTEGER, transaction_date TEXT, transaction_type TEXT, transaction_desc TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME0);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(sqLiteDatabase);
    }


    public long addTeam(String teamName, String email, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO table_team (team_name,registering_email,team_pin) VALUES (?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, teamName);
        statement.bindString(2, email);
        statement.bindString(3, pin);

        long res1 = statement.executeInsert();
        return res1;
    }


    public boolean checkteamname(String team) {
        String[] columns = {T1COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = T1COL_2 + "=?";
        String[] selectionArgs = {team};
        Cursor cursor = db.query(TABLE_NAME1, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean checkPin(String team, String pin) {
        String[] columns = {T1COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = T1COL_2 + "=?" + " and " + T1COL_4 + "=?";
        String[] selectionArgs = {team, pin};
        Cursor cursor = db.query(TABLE_NAME1, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean checkregisteringEmail(String email) {
        String[] columns = {T1COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = T1COL_3 + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME1, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }


    public long addUser(String email, String password, String name, String nickname, String team, byte[] image) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO table_user VALUES (NULL, ?, ?, ?, ?, ?, ?, date('now'), ?,?,?,?,?,?,?,?,?)";
        String sql1 = "INSERT INTO table_login_user VALUES (NULL, ?, ?)";
        SQLiteStatement statement1 = db.compileStatement(sql1);
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, email);
        statement.bindString(2, "12xxxxxxxxxx");
        statement.bindString(3, name);
        statement.bindString(4, nickname);
        statement.bindString(5, team);
        statement.bindBlob(6, image);
        statement.bindString(7, "veg");
        statement.bindString(8, "10xxxxxxxx");
        statement.bindString(9, "B+");
        statement.bindString(10, "10xxxxxxxx");
        statement.bindString(11, "");
        statement.bindString(12, "");
        statement.bindString(13, "Describe Yourself, Your experiance, skills, passion");
        statement.bindString(14, "Designation");
        statement.bindString(15, "Write your hobbies");
        long res2 = statement.executeInsert();

        statement1.clearBindings();
        statement1.bindString(1, email);
        statement1.bindString(2, password);
        statement1.executeInsert();
        return res2;
    }

    public boolean checkLogin(String email, String password) {
        String[] columns = {T0COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = T0COL_2 + "=?" + " and " + T0COL_3 + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME0, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean checkUser(String email) {
        String[] columns = {T2COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = T2COL_2 + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public long updateImg(String mail, byte[] image) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE table_user SET image = ? WHERE email = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindBlob(1, image);
        statement.bindString(2, mail);

        long res = statement.executeInsert();
        return res;
    }

    public Cursor getuserdetails(String mail) {
        String[] columns = {T2COL_5, T2COL_6, T2COL_7};
        String selection = T2COL_2 + "=?";
        String[] selectionArgs = {mail};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.query(TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        return data;
    }

    public Cursor getuserAlldetails(String mail) {
        String[] columns = {T2COL_2, T2COL_3, T2COL_4, T2COL_5, T2COL_6, T2COL_7, T2COL_8, T2COL_9, T2COL_10, T2COL_11, T2COL_12};
        String selection = T2COL_2 + "=?";
        String[] selectionArgs = {mail};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.query(TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        return data;
    }

    public Cursor getUserAllDetailsWithId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name from team_user where id=" + id, null);
        return res;
    }

    public Cursor getteamMembers(String team_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {T2COL_2,T2COL_4, T2COL_7,T2COL_16};
        String selection = T2COL_6 + "=?";
        String[] selectionArgs = {team_name};
        Cursor data = db.query(TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        return data;
    }


    public Cursor getdata(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(sql, null);
    }

    public Cursor getMailsOfTeam(String team) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {T2COL_2};
        String selection = T2COL_6 + "=?";
        String[] selectionArgs = {team};
        Cursor data = db.query(TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        return data;
    }

    public long updateUserDetails(String email, String niknm, String dob, String mob, String bgrp, String food, String pan, String aadhar,String desig) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_user SET nick_name = ?, dob = ?, mobile = ?, bl_grp = ?,food = ?, pan_no = ?, aadhaar = ?, user_designation = ? WHERE email = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, niknm);
        statement.bindString(2, dob);
        statement.bindString(3, mob);
        statement.bindString(4, bgrp);
        statement.bindString(5, food);
        statement.bindString(6, pan);
        statement.bindString(7, aadhar);
        statement.bindString(8, desig);
        statement.bindString(9, email);
        long res2 = statement.executeInsert();
        return res2;
    }

    public long updateAboutDetails(String email, String abt, String hobby) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_user SET user_about = ?, user_hobbies = ? WHERE email = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, abt);
        statement.bindString(2, hobby);
        statement.bindString(3, email);
        long res2 = statement.executeInsert();
        return res2;
    }

    public long newEvent(String name, String mail, String desc, String date, String members, String contri, Long amount) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO table_event VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, mail);
        statement.bindString(3, desc);
        statement.bindString(4, date);
        statement.bindString(5, members);
        statement.bindString(6, contri);
        statement.bindLong(7, amount);
        statement.bindLong(8, 0);
        statement.bindLong(9, 0);
        statement.bindLong(10, 0);
        statement.bindString(11, "");
        statement.bindString(12, "");
        statement.bindString(13, "");
        long res3 = statement.executeInsert();
        return res3;
    }

    public Long addEvent(String[] names, String eventId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_user SET events = events || " + eventId + "|| ',' WHERE ID IN (" + makePlaceholders(names.length) + ")";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindAllArgsAsStrings(names);
        long res3 = statement.executeInsert();
        return res3;

    }

    public Long addTransaction(String[] names, String transId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_user SET user_transaction = user_transaction || " + transId + "|| ',' WHERE ID IN (" + makePlaceholders(names.length) + ")";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindAllArgsAsStrings(names);
        long res3 = statement.executeInsert();
        return res3;

    }

    public long addCreditTransaction(Long eventId, String transId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_event SET credit_transactions = credit_transactions || ',' || ?  WHERE event_ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, transId);
        statement.bindLong(2, eventId);
        long res2 = statement.executeInsert();
        return res2;
    }

    public long addCreditMember(Long eventId, String memId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_event SET credit_members = credit_members || ',' || ?  WHERE event_ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, memId);
        statement.bindLong(2, eventId);
        long res2 = statement.executeInsert();
        return res2;
    }

    public long addDebitTransaction(Long eventId, String trasId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_event SET debit_transactions = debit_transactions || ',' || ? WHERE event_ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, trasId);
        statement.bindLong(2, eventId);
        long res2 = statement.executeInsert();
        return res2;
    }

    public long updateContriAmounts(long evtContri, long spent, long remain, long id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE table_event SET event_contri = ?, spent_amount = ?, remaining_contri = ? WHERE event_ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindLong(1, evtContri);
        statement.bindLong(2, spent);
        statement.bindLong(3, remain);
        statement.bindLong(4, id);
        long res2 = statement.executeInsert();
        return res2;
    }


    public Cursor getEvents(String query, String[] names) {
        SQLiteDatabase db = getWritableDatabase();
         return db.rawQuery(query, names);
    }

    public long newTransaction(String userId, Integer amount, String date, String type,String desc) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO table_transaction VALUES (NULL, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, userId);
        statement.bindLong(2, amount);
        statement.bindString(3, date);
        statement.bindString(4, type);
        statement.bindString(5, desc);
        long res3 = statement.executeInsert();
        return res3;
    }

    public static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
