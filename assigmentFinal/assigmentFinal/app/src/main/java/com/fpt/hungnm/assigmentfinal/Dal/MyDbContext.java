//package com.fpt.hungnm.assigmentfinal.Dal;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.nfc.Tag;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//
//import com.fpt.hungnm.assigmentfinal.Model.Budget;
//import com.fpt.hungnm.assigmentfinal.Model.Category;
//import com.fpt.hungnm.assigmentfinal.Model.Transaction;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class MyDbContext extends SQLiteOpenHelper {
//    private static final String TAG = "HungnmError";
//    private static final String DATABASE_NAME = "MoneyManage.db";
//    private static final String TABLE_TRANSACTIONS = "transactions";
//    private static final String TABLE_CATEGORY = "categories";
//    private static final String TABLE_BUDGET = "budgets";
//    private static final String TABLE_USER = "users";
//    private static final String COLUMN_ID = "id";
//    private static final String COLUMN_USERNAME = "Username";
//    private static final String COLUMN_EMAIL = "Email";
//    private static final String COLUMN_PASSWORD = "Password";
//    private static final String PATTERN = "yyyy-MM-dd";
//    private static final String PATTERN_ORIGIN = "yyyy-MM-dd HH:mm:ss";
//    private static int DATABASE_VERSION = 2;
//
//    public MyDbContext(@Nullable Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String sqlCreateTransition = "CREATE TABLE " +
//                TABLE_TRANSACTIONS +
//                "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                "Title TEXT,"+
//                "CategoryId TEXT,"+
//                "Price TEXT,"+
//                "IsIncome TEXT,"+
//                "CreateDate DATETIME)";
//        db.execSQL(sqlCreateTransition);
//
//        String sqlCreateCategory = "CREATE TABLE " +
//                TABLE_CATEGORY +
//                "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                "Title TEXT,"+
//                "IsIncome TEXT,"+
//                "CreateDate DATETIME)";
//        db.execSQL(sqlCreateCategory);
//
//        String sqlCreateBudget = "CREATE TABLE "+
//                TABLE_BUDGET+
//                "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                "Title TEXT,"+
//                "TypeOfBudget TEXT,"+
//                "Time TEXT,"+
//                "CategoryId TEXT,"+
//                "CreateDate DATETIME)";
//        db.execSQL(sqlCreateBudget);
//
//        String sqlCreateUser = "CREATE TABLE users (" +
//                "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "Username TEXT UNIQUE, " +
//                "Password TEXT, " +
//                "Email TEXT, " +
//                "CreateDate DATETIME)";
//        db.execSQL(sqlCreateUser);
//
//        String insertDefaultUser = "INSERT INTO users (Username, Password, Email, CreateDate) " +
//                "VALUES ('admin', '123', 'admin@example.com', datetime('now'))";
//        db.execSQL(insertDefaultUser);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
//        db.execSQL("DROP TABLE IF EXISTS users");
//        onCreate(db);
//    }
//
//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//    }
//
//
//    public boolean registerUser(String username, String email, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USERNAME, username);
//        values.put(COLUMN_EMAIL, email);
//        values.put(COLUMN_PASSWORD, password);
//
//        long result = db.insert(TABLE_USER, null, values);
//        db.close();
//        return result != -1;  // Trả về true nếu lưu thành công
//    }
//
//
//    public boolean login(String username, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{username, password});
//
//        boolean isValid = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//        return isValid;
//    }
//
//
//    public List<Transaction> getAllTransition(){
//        List<Transaction> list = new ArrayList<>();
//        try{
//            SQLiteDatabase sql = getReadableDatabase();
//            String orderBy = "CreateDate DESC";
//            Cursor cs = sql.query(TABLE_TRANSACTIONS,null,null,null,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String categoryId = cs.getString(2);
//                String price = cs.getString(3);
//                String isIncome = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Transaction(id,title,price,categoryId,isIncome,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getAllTransition - " + ex.getMessage());
//        }
//        return list;
//    }
//
//
//    public boolean getCategoryById(int _id ){
//        try{
//
//            SQLiteDatabase sql = getReadableDatabase();
//            String orderBy = "CreateDate DESC";
//            Cursor cs = sql.rawQuery("Select * from categories where id = " + _id,null);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String isIncome = cs.getString(2);
//                String createDateStr = cs.getString(3);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                return true;
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getAllCategory - " + ex.getMessage());
//        }
//        return false;
//    }
//
//    public List<Category> getAllCategory(){
//        List<Category> list = new ArrayList<>();
//        try{
//            SQLiteDatabase sql = getReadableDatabase();
//            String orderBy = "CreateDate DESC";
//            Cursor cs = sql.query(TABLE_CATEGORY,null,null,null,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String isIncome = cs.getString(2);
//                String createDateStr = cs.getString(3);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Category(id,title,isIncome,date,Long.valueOf(0)));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getAllCategory - " + ex.getMessage());
//        }
//        return list;
//    }
//
//    public List<Category> getAllCategoryByType(String type){
//        List<Category> list = new ArrayList<>();
//        try{
//            SQLiteDatabase sql = getReadableDatabase();
//            String whereClause = "IsIncome = ? ";
//            String[] whereArgs = {type};
//            String orderBy = "CreateDate DESC";
//            Cursor cs = sql.query(TABLE_CATEGORY,null,whereClause,whereArgs,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String isIncome = cs.getString(2);
//
//                String createDateStr = cs.getString(3);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Category(id,title,isIncome,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getAllCategoryByType - " + ex.getMessage());
//        }
//        return list;
//    }
//
//    public List<Budget> getAllBudget(){
//        List<Budget> list = new ArrayList<>();
//        try{
//            SQLiteDatabase sql = getReadableDatabase();
//            String orderBy = "CreateDate DESC";
//            Cursor cs = sql.query(TABLE_BUDGET,null,null,null,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String typeOfBudget = cs.getString(2);
//                String time = cs.getString(3);
//                String categoryId = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Budget(id,title,typeOfBudget,time,categoryId,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getAllBudget - " + ex.getMessage());
//        }
//        return list;
//    }
//
//    public int getTransactionCount() {
//        int count = 0;
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_TRANSACTIONS, null);
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0);
//        }
//        cursor.close();
//        return count;
//    }
//
//    public long addTransaction(Transaction transaction){
//        long result = -1;
//        try{
//            ContentValues values = new ContentValues();
//            values.put("title", transaction.getTitle());
//            values.put("categoryId", transaction.getCategory());
//            values.put("price", transaction.getPrice());
//            values.put("isIncome", transaction.getIsIncome());
//            values.put("createDate", transaction.getCreateDate());
//            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//            result = sqLiteDatabase.insert(TABLE_TRANSACTIONS,null,values);
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - addTransactions - " + ex.getMessage());
//        }
//        return result;
//    }
//
//    public long updateTransaction(Transaction transaction){
//        long result = -1;
//        try{
//            ContentValues values = new ContentValues();
//            values.put("title", transaction.getTitle());
//            values.put("categoryId", transaction.getCategory());
//            values.put("price", transaction.getPrice());
//            values.put("isIncome", transaction.getIsIncome());
//            values.put("createDate", transaction.getCreateDate());
//            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//
//            String whereClause = "id = ?";
//            String[] whereArgs = {Integer.toString(transaction.getId())};
//            result = sqLiteDatabase.update(TABLE_TRANSACTIONS,values,whereClause,whereArgs);
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - updateTransaction - " + ex.getMessage());
//        }
//        return result;
//    }
//
//    public boolean deleteTransaction(int transactionId) {
//        try {
//            SQLiteDatabase db = getWritableDatabase();
//            String whereClause = "id = ?";
//            String[] whereArgs = {String.valueOf(transactionId)};
//            int rowsDeleted = db.delete(TABLE_TRANSACTIONS, whereClause, whereArgs);
//            return rowsDeleted > 0;
//        } catch (Exception ex) {
//            Log.e(TAG, "MyDbContext - deleteTransaction - " + ex.getMessage());
//            return false;
//        }
//    }
//
//    public long addCategory(Category category){
//        long result = -1;
//        try{
//            ContentValues values = new ContentValues();
//            values.put("title", category.getTitle());
//            values.put("isIncome", category.getIsIncome());
//            values.put("createDate", category.getCreateDate());
//            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//            result = sqLiteDatabase.insert(TABLE_CATEGORY,null,values);
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - addCategory - " + ex.getMessage());
//        }
//        return  result;
//    }
//
//    public long updateCategory(Category item){
//        long result = -1;
//        try{
//            ContentValues values = new ContentValues();
//            values.put("title", item.getTitle());
//            values.put("isIncome", item.getIsIncome());
//            values.put("createDate", item.getCreateDate());
//            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//
//            String whereClause = "id = ?";
//            String[] whereArgs = {Integer.toString(item.getId())};
//            result = sqLiteDatabase.update(TABLE_CATEGORY,values,whereClause,whereArgs);
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - updateCategory - " + ex.getMessage());
//        }
//        return result;
//    }
//
//    public boolean deleteCategory(int categoryId) {
//        try {
//            SQLiteDatabase db = getWritableDatabase();
//            String whereClause = "id = ?";
//            String[] whereArgs = {String.valueOf(categoryId)};
//            int rowsDeleted = db.delete(TABLE_CATEGORY, whereClause, whereArgs);
//            return rowsDeleted > 0;
//        } catch (Exception ex) {
//            Log.e(TAG, "MyDbContext - deleteCategory - " + ex.getMessage());
//            return false;
//        }
//    }
//
//    public long addBudget(Budget budget){
//        long result = -1;
//        try{
//            ContentValues values = new ContentValues();
//            values.put("title", budget.getTitle());
//            values.put("typeOfBudget", budget.getTypeOfBudget());
//            values.put("time", budget.getTime());
//            values.put("categoryId", budget.getCategotyId());
//            values.put("createDate", budget.getCreateDate());
//            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//            result = sqLiteDatabase.insert(TABLE_BUDGET,null,values);
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - addCategory - " + ex.getMessage());
//        }
//        return  result;
//    }
//    public long updateBudget(Budget item){
//        long result = -1;
//        try{
//            ContentValues values = new ContentValues();
//            values.put("title", item.getTitle());
//            values.put("typeofbudget", item.getTypeOfBudget());
//            values.put("time", item.getTime());
//            values.put("categoryId", item.getCategotyId());
//            values.put("createDate", item.getCreateDate());
//            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//
//            String whereClause = "id = ?";
//            String[] whereArgs = {Integer.toString(item.getId())};
//            result = sqLiteDatabase.update(TABLE_BUDGET,values,whereClause,whereArgs);
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - updateCategory - " + ex.getMessage());
//        }
//        return result;
//    }
//
//    public boolean deleteBudget(int budgetId) {
//        try {
//            SQLiteDatabase db = getWritableDatabase();
//            String whereClause = "id = ?";
//            String[] whereArgs = {String.valueOf(budgetId)};
//            int rowsDeleted = db.delete(TABLE_BUDGET, whereClause, whereArgs);
//            return rowsDeleted > 0;
//        } catch (Exception ex) {
//            Log.e(TAG, "MyDbContext - deleteBudget - " + ex.getMessage());
//            return false;
//        }
//    }
//
//    public List<Transaction> getTransactionByMonth(int month){
//        List<Transaction> list = new ArrayList<>();
//        try{
//            String selection = "strftime('%m', CreateDate) = ?";
//            String[] selectionArgs = {String.format("%02d", month)};
//            String orderBy = "CreateDate DESC";
//            SQLiteDatabase st = getReadableDatabase();
//            Cursor cs = st.query(TABLE_TRANSACTIONS,null,selection,selectionArgs,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String categoryId = cs.getString(2);
//                String price = cs.getString(3);
//                String isIncome = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Transaction(id,title,price,categoryId,isIncome,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getTransactionByDate - " + ex.getMessage());
//        }
//        return list;
//    }
//
//    public List<Transaction> getTransactionByFilter(int month,String type,String _categoryId){
//        List<Transaction> list = new ArrayList<>();
//        try{
//            String selection = "strftime('%m', CreateDate) = ?";
//            List<String> selectionArgsList = new ArrayList<>();
//            selectionArgsList.add(String.format("%02d", month));
//            String orderBy = "CreateDate DESC";
//            if(type != null){
//                selection = selection + " AND IsIncome = ?";
//                selectionArgsList.add(type);
//            }
//            SQLiteDatabase st = getReadableDatabase();
//            String[] selectionArgs = selectionArgsList.toArray(new String[0]);
//            Cursor cs = st.query(TABLE_TRANSACTIONS,null,selection,selectionArgs,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String categoryId = cs.getString(2);
//                String price = cs.getString(3);
//                String isIncome = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Transaction(id,title,price,categoryId,isIncome,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getTransactionByDate - " + ex.getMessage());
//        }
//        return list;
//    }
//
//    //    public List<Transaction> getTransactionByFilter(Date startDate, Date endDate, Boolean isIncomeFilter, String categoryIdFilter){
////        List<Transaction> list = new ArrayList<>();
////        String type = null;
////        if(isIncomeFilter){
////            type = "y";
////        }else if(!isIncomeFilter){
////            type = "N";
////        }
////        try{
////            String selection = null;
////            List<String> selectionArgsList = new ArrayList<>();
////            if(startDate != null){
////                selection = "CreateDate >= ?";
////                selectionArgsList.add(String.valueOf(startDate.getTime()));
////            }
////            if(endDate != null){
////                if(selection != null){
////                    selection = "CreateDate <= ?";
////                    selectionArgsList.add(String.valueOf(endDate.getTime()));
////                }else{
////                    selection = "AND CreateDate <= ?";
////                    selectionArgsList.add(String.valueOf(endDate.getTime()));
////                }
////            }
////            if(type != null){
////                if(selection != null){
////                    selection = "IsIncome = ?";
////                    selectionArgsList.add(type);
////                }else{
////                    selection = "AND IsIncome = ?";
////                    selectionArgsList.add(type);
////                }
////            }
////            if(categoryIdFilter != null){
////                if(selection != null){
////                    selection = "CategoryId = ?";
////                    selectionArgsList.add(categoryIdFilter);
////                }else{
////                    selection = "AND CategoryId = ?";
////                    selectionArgsList.add(categoryIdFilter);
////                }
////            }
////            String[] selectionArgs = new String[selectionArgsList.size()];
////            selectionArgs = selectionArgsList.toArray(selectionArgs);
////
////            String orderBy = "CreateDate DESC";
////            SQLiteDatabase st = getReadableDatabase();
////            Cursor cs = st.query(TABLE_TRANSACTIONS,null,selection,selectionArgs,null,null,orderBy);
////            while (cs != null && cs.moveToNext()){
////                int id = cs.getInt(0);
////                String title = cs.getString(1);
////                String categoryId = cs.getString(2);
////                String price = cs.getString(3);
////                String isIncome = cs.getString(4);
////                String createDateStr = cs.getString(5);
////                Date date = new Date(createDateStr);
////                SimpleDateFormat outputFormat = new SimpleDateFormat(PATTERN, Locale.ENGLISH);
////                String formattedDate = outputFormat.format(date);
////                list.add(new Transaction(id,title,categoryId,price,isIncome,formattedDate));
////            }
////        }catch (Exception ex){
////            Log.e(TAG,"MyDbContext - getTransactionByFilter - " + ex.getMessage());
////        }
////        return list;
////    }
//    private String convertDateFormat(String inputDate) {
//        // Định dạng ngày gốc (người dùng nhập)
//        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
//        // Định dạng ngày cần chuyển sang (Firebase sử dụng)
//        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        try {
//            // Chuyển đổi từ chuỗi thành đối tượng Date
//            Date date = inputFormat.parse(inputDate);
//            // Định dạng lại ngày thành chuỗi theo yêu cầu
//            return outputFormat.format(date);
//        } catch (ParseException e) {
//            Log.e(TAG, "convertDateFormat - Lỗi chuyển đổi ngày: " + e.getMessage());
//            // Trả về chuỗi rỗng nếu có lỗi
//            return "";
//        }
//    }
//
//    public Long getBalance(int month, String type, String categoryID) {
//        try {
//            List<Transaction> list = getTransactionByFilter(month,type,categoryID);
//            if(list.size() != 0 ){
//                Long result = new Long(0);
//                for (Transaction item :
//                        list) {
//                    if(item.getIsIncome().toString().equals("INCOME")){
//                        result = result + Long.valueOf(item.getPrice());
//                    }else{
//                        result = result - Long.valueOf(item.getPrice());
//                    }
//                }
//                if(type!= null && type.equals("EXPENSE")){
//                    return Math.abs(result);
//                }
//                return result;
//            }
//        } catch (Exception ex) {
//            Log.e(TAG, "MyDbContext - getBalance - " + ex.getMessage());
//        }
//        return Long.valueOf(0);
//    }
//
//    public Transaction getTransactionById(int _id) {
//        try{
//            String selection = "Id = ?";
//            String[] selectionArgs = {String.valueOf(_id)};
//            String orderBy = "CreateDate DESC";
//            SQLiteDatabase st = getReadableDatabase();
//            Cursor cs = st.query(TABLE_TRANSACTIONS,null,selection,selectionArgs,null,null,orderBy);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String categoryId = cs.getString(2);
//                String price = cs.getString(3);
//                String isIncome = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                return (new Transaction(id,title,price,categoryId,isIncome,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getTransactionByDate - " + ex.getMessage());
//        }
//        return null;
//    }
//
//    public List<Transaction> getTransactionSearch(String title, String categoryId, String fromDate, String toDate) {
//        List<Transaction> list = new ArrayList<>();
//        try{
//            String selection = null;
//            List<String> selectionArgsList = new ArrayList<>();
//            if(!fromDate.equals("")){
//                selection = "CreateDate >= " + convertDateFormat(fromDate);
//                selectionArgsList.add(convertDateFormat(fromDate));
//            }
//            if(!toDate.equals("")){
//                if(selection == null){
//                    selection = " CreateDate <= " + convertDateFormat(toDate);
//                    selectionArgsList.add(convertDateFormat(toDate));
//                }else{
//                    selection = selection + " AND CreateDate <= " + convertDateFormat(toDate);
//                    selectionArgsList.add(convertDateFormat(toDate));
//                }
//            }
//
//            if(!categoryId.equals("ALL")){
//                if(selection == null){
//                    selection = "CategoryId = " + getStringSQL(categoryId);
//                    selectionArgsList.add(categoryId);
//                }else{
//                    selection =selection + " AND CategoryId = " + getStringSQL(categoryId);
//                    selectionArgsList.add(categoryId);
//                }
//            }
//
//            if(!title.equals("")){
//                if(selection == null){
//                    selection = "Title = " + getStringSQL(title);
//                    selectionArgsList.add(title);
//                }else{
//                    selection = selection + " AND Title = " + getStringSQL(title);
//                    selectionArgsList.add(title);
//                }
//            }
//            String[] selectionArgs = new String[selectionArgsList.size()];
//            selectionArgs = selectionArgsList.toArray(selectionArgs);
//
//            String orderBy = "CreateDate DESC";
//            SQLiteDatabase st = getReadableDatabase();
//            String query ="";
//            if(selection == null){
//                query = "SELECT * FROM transactions ORDER BY CreateDate DESC";
//            }else{
//                query = "SELECT * FROM transactions where "+ selection + " ORDER BY CreateDate DESC";
//            }
//
//            //Cursor cs = st.query(TABLE_TRANSACTIONS,null,selection,selectionArgs,null,null,orderBy);
//            Cursor cs = st.rawQuery(query,null);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String _title = cs.getString(1);
//                String _categoryId = cs.getString(2);
//                String price = cs.getString(3);
//                String isIncome = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//                list.add(new Transaction(id,_title,price,_categoryId,isIncome,date));
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getTransactionSearch - " + ex.getMessage());
//        }
//        return list;
//    }
//
//    //    public Long getBalanceIntenal(int currentMonth, String type) {
////        try {
////            List<Transaction> list = getTransactionByType(currentMonth,type);
////            if(list.size() != 0 ){
////                Long result = new Long(0);
////                for (Transaction item :
////                        list) {
////                    if(item.getIsIncome().toString().equals("INCOME")){
////                        result = result + Long.valueOf(item.getPrice());
////                    }else{
////                        result = result - Long.valueOf(item.getPrice());
////                    }
////                }
////                return result;
////            }
////        } catch (Exception ex) {
////            Log.e(TAG, "MyDbContext - getBalance - " + ex.getMessage());
////        }
////        return Long.valueOf(0);
////    }
//    public String getStringSQL(String s){
//        return "'" + s + "' ";
//    }
//    public void Test(){
//        try{
//            String sqlQuery = " SELECT * FROM transactions where CreateDate >= 2024-03-12 ";
//            SQLiteDatabase st = getReadableDatabase();
//            Cursor cs = st.rawQuery(sqlQuery, null);
//            while (cs != null && cs.moveToNext()){
//                int id = cs.getInt(0);
//                String title = cs.getString(1);
//                String categoryId = cs.getString(2);
//                String price = cs.getString(3);
//                String isIncome = cs.getString(4);
//                String createDateStr = cs.getString(5);
//                String[] parts = createDateStr.split(" ");
//                String date = parts[0];
//            }
//        }catch (Exception ex){
//            Log.e(TAG,"MyDbContext - getTransactionByDate - " + ex.getMessage());
//        }
//    }
//}
