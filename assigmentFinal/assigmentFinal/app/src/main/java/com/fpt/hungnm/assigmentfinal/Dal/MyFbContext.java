package com.fpt.hungnm.assigmentfinal.Dal;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fpt.hungnm.assigmentfinal.Model.Category;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;
import com.fpt.hungnm.assigmentfinal.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class MyFbContext {
    private static final String TAG = "MyFbContext";
    private DatabaseReference userRef;
    private final DatabaseReference transactionRef;
    private final DatabaseReference categoryRef;

    public MyFbContext() {
        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        transactionRef = database.getReference("transactions");
        categoryRef = database.getReference("categories");
        // Kiểm tra kết nối Firebase
        DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d(TAG, "Connected to Firebase Realtime Database.");
                } else {
                    Log.d(TAG, "Not connected to Firebase Realtime Database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Listener was cancelled", error.toException());
            }
        });
    }

    // Đăng ký người dùng
    public void register(String username, String email, String password, RegisterCallback callback) {
        Log.d(TAG, "Attempting to register user: " + username);

        // Kiểm tra xem tên người dùng đã tồn tại chưa
        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Người dùng đã tồn tại
                    Log.d(TAG, "Username already exists: " + username);
                    callback.onRegisterFailed("Tên người dùng đã tồn tại.");
                } else {
                    // Thêm người dùng mới vào Database
                    User user = new User(username, email, password);
                    userRef.child(username).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "User registered successfully: " + username);
                                callback.onRegisterSuccess();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to register user: ", e);
                                callback.onRegisterFailed("Đăng ký người dùng không thành công.");
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: ", error.toException());
                callback.onRegisterFailed("Lỗi cơ sở dữ liệu.");
            }
        });
    }


    // Interface callback cho kết quả đăng ký
    public interface RegisterCallback {
        void onRegisterSuccess();

        void onRegisterFailed(String message);
    }

    // Đăng nhập người dùng
    public void login(String username, String password, LoginCallback callback) {
        Log.d(TAG, "Attempting to login user: " + username);

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Log.d(TAG, "User found: " + username);
                        if (user.getPassword().equals(password)) {
                            // Đăng nhập thành công
                            Log.d(TAG, "Login successful for user: " + username);
                            callback.onLoginSuccess(user);
                        } else {
                            // Sai mật khẩu
                            Log.d(TAG, "Incorrect password for user: " + username);
                            callback.onLoginFailed("Incorrect password.");
                        }
                    } else {
                        Log.d(TAG, "User data is null for: " + username);
                        callback.onLoginFailed("User does not exist.");
                    }
                } else {
                    // Tài khoản không tồn tại
                    Log.d(TAG, "User does not exist: " + username);
                    callback.onLoginFailed("User does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: ", databaseError.toException());
                callback.onLoginFailed("Database error.");
            }
        });
    }

    // Interface callback cho kết quả đăng nhập
    public interface LoginCallback {
        void onLoginSuccess(User user);

        void onLoginFailed(String message);
    }

    public interface OnTransactionFetchListener {
        void onSuccess(List<Transaction> transactions);

        void onFailure(Exception e);
    }

    public void getAllTransaction(FirebaseCallback callback) {
        List<Transaction> list = new ArrayList<>();

        // Truy cập vào nhánh "transactions" trên Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("transactions");

        // Lấy dữ liệu bất đồng bộ
        databaseRef.orderByChild("createDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Duyệt qua các phần tử con trong "transactions"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        // Lấy từng trường từ Firebase và tạo Transaction
                        int id = snapshot.child("id").getValue(Integer.class);
                        String title = snapshot.child("title").getValue(String.class);
                        String categoryId = snapshot.child("categoryId").getValue(String.class);
                        String price = snapshot.child("price").getValue(String.class);
                        String isIncome = snapshot.child("isIncome").getValue(String.class);
                        String createDate = snapshot.child("createDate").getValue(String.class);

                        // Thêm vào danh sách
                        list.add(new Transaction(id, title, price, categoryId, isIncome, createDate));
                    } catch (Exception ex) {
                        Log.e("Firebase", "getAllTransactions - Error: " + ex.getMessage());
                    }
                }
                // Trả kết quả qua callback
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Database error: " + databaseError.getMessage());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    public interface FirebaseCallback {
        void onSuccess(List<Transaction> transactions);
        void onFailure(Exception e);
    }


    public void getCategoriesByType(String type, Consumer<List<Category>> callback) {
        categoryRef.orderByChild("type").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> categories = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Category category = child.getValue(Category.class);
                    if (category != null) {
                        categories.add(category);
                    }
                }
                callback.accept(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch categories.", error.toException());
            }
        });
    }

    public void getTransactionCount(Consumer<Integer> callback) {
        transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                callback.accept(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch transaction count.", error.toException());
            }
        });
    }

    public void getTransactionById(int id, final TransactionCallback callback) {
        transactionRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);
                    callback.onSuccess(transaction);
                } else {
                    Log.e(TAG, "Transaction not found");
                    callback.onFailure("Transaction not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        });
    }

    public interface TransactionCallback {
        void onSuccess(Transaction transaction);

        void onFailure(String errorMessage);
    }

    public void getAllCategoryByType(String type, final CategoryCallback callback) {
        categoryRef.orderByChild("isIncome").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    if (category != null) {
                        list.add(category);
                    }
                }
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        });
    }

    public interface CategoryCallback {
        void onSuccess(List<Category> categories);

        void onFailure(String errorMessage);
    }

    public void addTransaction(Transaction transaction, final AddTransactionCallback callback) {
        // Tạo ID ngẫu nhiên cho giao dịch (sử dụng int)
        int transactionId = (int) (System.currentTimeMillis() / 1000); // ID có thể dựa vào thời gian
        transaction.setId(transactionId); // Cập nhật ID vào đối tượng Transaction

        // Lưu giao dịch vào Firebase
        transactionRef.child(String.valueOf(transactionId)).setValue(transaction)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Giao dịch đã được thêm thành công!");
                    callback.onSuccess(String.valueOf(transactionId));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi thêm giao dịch: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public interface AddTransactionCallback {
        void onSuccess(String transactionId);

        void onFailure(String errorMessage);
    }


    /// main category
    public void getAllCategory(final CategoryCallback callback) {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        list.add(category);
                    }
                }
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "MyFbContext - getAllCategory - Database error: " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        });
    }

    public void deleteCategory(int categoryId, final DeleteCategoryCallback callback) {
        try {
            // Tạo DatabaseReference cho danh mục cần xóa
            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child(String.valueOf(categoryId));

            // Xóa danh mục khỏi Firebase
            categoryRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Danh mục đã được xóa thành công!");
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Lỗi khi xóa danh mục: " + e.getMessage());
                        callback.onFailure(e.getMessage());
                    });
        } catch (Exception ex) {
            Log.e(TAG, "MyFbContext - deleteCategory - " + ex.getMessage());
            callback.onFailure(ex.getMessage());
        }
    }

    // Giao diện callback để xử lý kết quả xóa
    public interface DeleteCategoryCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }


    public void updateCategory(Category item, final UpdateCategoryCallback callback) {
        // Lấy ID danh mục
        String categoryId = String.valueOf(item.getId());

        // Lưu danh mục vào Firebase Realtime Database
        categoryRef.child(categoryId).setValue(item)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Danh mục đã được cập nhật thành công!");
                    callback.onSuccess(); // Gọi callback thành công
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi cập nhật danh mục: " + e.getMessage());
                    callback.onFailure(e.getMessage()); // Gọi callback thất bại
                });
    }

    // Định nghĩa interface callback cho việc cập nhật danh mục
    public interface UpdateCategoryCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void getAllCategories(FirebaseCategoryCallback callback) {
        List<Category> list = new ArrayList<>();

        // Truy cập nhánh "categories" trong Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("categories");

        // Lấy dữ liệu bất đồng bộ và sắp xếp theo "createDate"
        databaseRef.orderByChild("createDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        // Lấy từng trường dữ liệu từ Firebase
                        int id = snapshot.child("id").getValue(Integer.class);
                        String title = snapshot.child("title").getValue(String.class);
                        String isIncome = snapshot.child("isIncome").getValue(String.class);
                        String createDate = snapshot.child("createDate").getValue(String.class);

                        // Thêm đối tượng Category vào danh sách
                        list.add(new Category(id, title, isIncome, createDate, 0L));
                    } catch (Exception ex) {
                        Log.e("Firebase", "getAllCategories - Error: " + ex.getMessage());
                    }
                }
                // Gọi callback khi dữ liệu đã tải xong
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Database error: " + databaseError.getMessage());
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public interface FirebaseCategoryCallback {
        void onSuccess(List<Category> categories);
        void onFailure(Exception e);
    }


    public interface GetAllCategoriesCallback {
        void onSuccess(List<Category> categories);

        void onFailure(String errorMessage);
    }

    public void getAllTransactions(OnTransactionFetchListener listener) {
        List<Transaction> transactions = new ArrayList<>();
        transactionRef.orderByChild("createDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);
                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                }
                listener.onSuccess(transactions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch transactions", error.toException());
                listener.onFailure(error.toException());
            }
        });
    }

    public void addCategory(Category category, AddCategoryCallback callback) {
        // Tạo ID ngẫu nhiên cho danh mục (sử dụng int)
        int categoryId = (int) (System.currentTimeMillis() / 1000); // ID có thể dựa vào thời gian
        category.setId(categoryId); // Cập nhật ID vào đối tượng Category

        categoryRef.child(String.valueOf(categoryId)).setValue(category)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Danh mục đã được thêm thành công!");
                    callback.onSuccess(categoryId); // Trả về ID của danh mục
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi thêm danh mục: " + e.getMessage());
                    callback.onFailure(e.getMessage()); // Trả về thông báo lỗi
                });
    }

    public interface AddCategoryCallback {
        void onSuccess(int categoryId);

        void onFailure(String errorMessage);
    }

    public void updateTransaction(Transaction transaction, final UpdateTransactionCallback callback) {
        try {
            // Lấy tham chiếu đến vị trí của giao dịch trong Firebase
            DatabaseReference transactionRefAtId = transactionRef.child(String.valueOf(transaction.getId()));

            // Tạo một HashMap để lưu trữ các giá trị cập nhật
            HashMap<String, Object> values = new HashMap<>();
            values.put("title", transaction.getTitle());
            values.put("categoryId", transaction.getCategory());
            values.put("price", transaction.getPrice());
            values.put("isIncome", transaction.getIsIncome());
            values.put("createDate", transaction.getCreateDate());

            // Cập nhật dữ liệu trong Firebase
            transactionRefAtId.updateChildren(values)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Giao dịch đã được cập nhật thành công!");
                        callback.onSuccess(); // Thông báo thành công
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Lỗi khi cập nhật giao dịch: " + e.getMessage());
                        callback.onFailure(e.getMessage()); // Thông báo lỗi
                    });
        } catch (Exception ex) {
            Log.e(TAG, "MyFbContext - updateTransaction - " + ex.getMessage());
            callback.onFailure(ex.getMessage()); // Thông báo lỗi nếu có ngoại lệ
        }
    }


    // Định nghĩa callback interface cho việc cập nhật
    public interface UpdateTransactionCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }
    public void getTransactionByFilter(int month, String type, String categoryID, OnDataFetchedListener<List<Transaction>> listener) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("transactions");
            Query query = ref.orderByChild("CreateDate").startAt(String.format("%02d", month)).endAt(String.format("%02d", month) + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Transaction> list = new ArrayList<>();
                    for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                        Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                        if (transaction != null && (type == null || transaction.getIsIncome().equals(type))) {
                            list.add(transaction);
                        }
                    }
                    listener.onDataFetched(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "getTransactionByFilter - onCancelled: " + databaseError.getMessage());
                    listener.onDataFetchFailed(databaseError.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MyDbContext - getTransactionByFilter - " + ex.getMessage());
            listener.onDataFetchFailed(ex.getMessage());
        }
    }

    public void getBalance(int month, String type, String categoryID, OnDataFetchedListener<Long> listener) {
        getTransactionByFilter(month, type, categoryID, new OnDataFetchedListener<List<Transaction>>() {
            @Override
            public void onDataFetched(List<Transaction> transactions) {
                Long result = 0L;
                for (Transaction item : transactions) {
                    if ("INCOME".equals(item.getIsIncome())) {
                        result += Long.valueOf(item.getPrice());
                    } else {
                        result -= Long.valueOf(item.getPrice());
                    }
                }
                if ("EXPENSE".equals(type)) {
                    listener.onDataFetched(Math.abs(result));
                } else {
                    listener.onDataFetched(result);
                }
            }

            @Override
            public void onDataFetchFailed(String errorMessage) {
                Log.e(TAG, "MyDbContext - getBalance - " + errorMessage);
                listener.onDataFetchFailed(errorMessage);
            }
        });
    }

    // Define the listener interface
    public interface OnDataFetchedListener<T> {
        void onDataFetched(T data);
        void onDataFetchFailed(String errorMessage);
    }


    public void getAllTransactions(final TransactionListener callback) {
        try {
            // Truy vấn đến Firebase để lấy tất cả giao dịch
            transactionRef.orderByChild("createDate").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Transaction> list = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Transaction transaction = data.getValue(Transaction.class);
                        if (transaction != null) {
                            list.add(transaction);
                        }
                    }
                    // Gọi callback với danh sách giao dịch đã lấy
                    callback.onSuccess(list);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "MyDbContext - getAllTransactions - " + error.getMessage());
                    callback.onFailure(error.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MyDbContext - getAllTransactions - " + ex.getMessage());
            callback.onFailure(ex.getMessage());
        }
    }


    // Interface mới
    public interface TransactionListener {
        void onSuccess(List<Transaction> transactions);
        void onFailure(String errorMessage);
    }

    // Interface để xử lý kết quả cho số dư
    public interface BalanceCallback {
        void onSuccess(Long balance);
        void onFailure(String errorMessage);
    }


    public void getAllTransactions(OnDataFetchedListener<List<Transaction>> listener) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("transactions");
            ref.orderByChild("CreateDate").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Transaction> list = new ArrayList<>();
                    for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                        Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                        if (transaction != null) {
                            list.add(transaction);
                        }
                    }
                    listener.onDataFetched(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "MyDbContext - getAllTransactions - onCancelled: " + databaseError.getMessage());
                    listener.onDataFetchFailed(databaseError.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MyDbContext - getAllTransactions - " + ex.getMessage());
            listener.onDataFetchFailed(ex.getMessage());
        }
    }

    public void getTransactionSearch(String title, String categoryId, String fromDate, String toDate, FirebaseTransactionCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("transactions");
        Query query = ref.orderByChild("createDate");

        // Thêm logic lọc cho khoảng thời gian nếu có
        if (!fromDate.isEmpty()) {
            query = query.startAt(convertDateFormat(fromDate));
        }
        if (!toDate.isEmpty()) {
            query = query.endAt(convertDateFormat(toDate));
        }

        // Lắng nghe dữ liệu từ Firebase
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Transaction> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Transaction transaction = data.getValue(Transaction.class);

                    // Lọc theo title nếu cần, sử dụng contains
                    boolean titleMatches = title.isEmpty() || transaction.getTitle().toLowerCase().contains(title.toLowerCase());
                    boolean categoryMatches = categoryId.equals("ALL") || transaction.getCategory().equals(categoryId);

                    // Thêm vào danh sách nếu thỏa mãn điều kiện
                    if (titleMatches && categoryMatches) {
                        list.add(transaction);
                    }
                }
                // Gọi callback khi hoàn tất
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "getTransactionSearch - Firebase error: " + error.getMessage());
                callback.onFailure(error.toException());
            }
        });
    }


    public interface FirebaseTransactionCallback {
        void onSuccess(List<Transaction> transactions);
        void onFailure(Exception e);
    }

    private String convertDateFormat(String inputDate) {
        // Định dạng ngày gốc (người dùng nhập)
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Định dạng ngày cần chuyển sang (Firebase sử dụng)
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Chuyển đổi từ chuỗi thành đối tượng Date
            Date date = inputFormat.parse(inputDate);
            // Định dạng lại ngày thành chuỗi theo yêu cầu
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "convertDateFormat - Lỗi chuyển đổi ngày: " + e.getMessage());
            // Trả về chuỗi rỗng nếu có lỗi
            return "";
        }
    }

    public void getAllTransactionsByString(OnTransactionFetchListener1 listener) {
        List<Transaction> transactions = new ArrayList<>();
        // Lấy tất cả giao dịch được sắp xếp theo ngày tạo
        transactionRef.orderByChild("createDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);
                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                }

                // Thực hiện thống kê và gửi báo cáo
                String report = generateReport(transactions);
                listener.onSuccess(report); // Gọi phương thức onSuccess với báo cáo
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch transactions", error.toException());
                listener.onFailure(error.toException());
            }
        });
    }

    // Giao diện callback
    public interface OnTransactionFetchListener1 {
        void onSuccess(String report); // Phương thức chỉ trả về báo cáo
        void onFailure(Exception e);
    }


    private String generateReport(List<Transaction> transactions) {
        double totalAmount = 0.0;
        int transactionCount = transactions.size();

        for (Transaction transaction : transactions) {
            try {
                // Nếu getPrice() trả về một String, chuyển đổi sang double
                double price = Double.parseDouble(transaction.getPrice());
                totalAmount += price;
            } catch (NumberFormatException e) {
                // Xử lý trường hợp không thể chuyển đổi được giá trị
                Log.e(TAG, "Failed to parse price: " + transaction.getPrice(), e);
            }
        }

        // Xây dựng báo cáo thống kê
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Tổng số giao dịch: ").append(transactionCount).append("\n");
        reportBuilder.append("Tổng số tiền: ").append(totalAmount).append("\n");

        return reportBuilder.toString();
    }


    public void getTransactionByMonth(int month, FirebaseTransactionCallback callback) {
        List<Transaction> list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("transactions");

        // Lắng nghe dữ liệu từ Firebase
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Transaction transaction = data.getValue(Transaction.class);
                    if (transaction != null) {
                        // Chuyển đổi CreateDate sang định dạng tháng để lọc
                        String createDateStr = transaction.getCreateDate(); // Giả sử CreateDate là thuộc tính trong Transaction
                        String[] parts = createDateStr.split(" ");
                        String datePart = parts[0]; // Lấy ngày từ chuỗi
                        String[] dateParts = datePart.split("-"); // Giả sử định dạng là YYYY-MM-DD

                        int transactionMonth = Integer.parseInt(dateParts[1]); // Lấy tháng từ ngày

                        // Kiểm tra tháng
                        if (transactionMonth == month) {
                            list.add(transaction);
                        }
                    }
                }
                // Gọi callback khi hoàn tất
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "getTransactionByMonth - Firebase error: " + error.getMessage());
                callback.onFailure(error.toException());
            }
        });
    }


}