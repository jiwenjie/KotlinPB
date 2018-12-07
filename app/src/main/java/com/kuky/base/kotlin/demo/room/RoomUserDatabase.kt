package com.kuky.base.kotlin.demo.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.arch.persistence.room.migration.Migration
import com.kuky.base.android.kotlin.baseutils.LogUtils
import com.kuky.base.kotlin.demo.DemoApplication
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * @author kuky.
 * @description
 */

/** Entity Part*/
@Entity(tableName = "users")
data class User(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "user_id") var userId: Long = 0,
        @ColumnInfo(name = "user_name") var userName: String = "",
        @ColumnInfo(name = "phone_number") var phoneNo: String = "",
        @ColumnInfo(name = "age") var age: Int = 0,
        @ColumnInfo(name = "birth") var birth: Date?,
        @Embedded var address: Address
)

data class Address(
        @ColumnInfo(name = "province") var province: String = "",
        @ColumnInfo(name = "street") var street: String = ""
)

/** one-to-many relationship */
@Entity(
        tableName = "user_orders",
        foreignKeys = [
            ForeignKey(
                    entity = User::class,
                    parentColumns = ["user_id"],
                    childColumns = ["order_user_id"],
                    onDelete = ForeignKey.CASCADE, // when user was deleted this order will be deleted also
                    onUpdate = ForeignKey.CASCADE,
                    deferred = true
            )
        ]
)
data class Order(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "order_id") var orderId: Long = 0,
        @ColumnInfo(name = "order_user_id") var orderUserId: Long?,
        @ColumnInfo(name = "order_name") var orderName: String = "",
        @ColumnInfo(name = "order_price") var orderPrice: Float = 0.0F
)

@Entity(tableName = "user_repos")
data class Repo(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "repo_id") var repoId: Long = 0,
        @ColumnInfo(name = "repo_name") var repoName: String = "",
        @ColumnInfo(name = "repo_url") var repoUrl: String = ""
)

@Entity(
        tableName = "user_repo_join",
        primaryKeys = ["user_id", "repo_id"],
        foreignKeys = [
            ForeignKey(
                    entity = User::class,
                    parentColumns = ["user_id"],
                    childColumns = ["user_id"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE,
                    deferred = true
            ),
            ForeignKey(
                    entity = Repo::class,
                    parentColumns = ["repo_id"],
                    childColumns = ["repo_id"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE,
                    deferred = true
            )
        ]
)
data class UserRepoJoin(
        @ColumnInfo(name = "user_id") var userId: Long = 0,
        @ColumnInfo(name = "repo_id") var repoId: Long = 0
)


/** Convert Part */
class DateConvert {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toDate(dateLong: Long): Date = Date(dateLong)

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date): Long = date.time
    }
}


/** Dao Part */
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flowable<List<User>>

    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getSingleUser(id: Long): Single<User>

    @Query("DELETE FROM users")
    fun deleteAllUser(): Int

    @Query("DELETE FROM users WHERE user_id = :id")
    fun deleteSingleUser(id: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(vararg user: User): Int
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM user_orders")
    fun getAllOrders(): Flowable<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createOrder(order: Order): Long

    @Query("SELECT * FROM user_orders WHERE order_user_id = :userId")
    fun getUserOrders(userId: Long): Flowable<List<Order>>
}

@Dao
interface RepoDao {
    @Query("SELECT * FROM user_repos")
    fun getAllRepos(): Flowable<List<Repo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createRepo(repo: Repo): Long
}

@Dao
interface UserRepoJoinDao {
    @Query("SELECT * FROM user_repo_join")
    fun getAllUserRepoJoin(): Flowable<List<UserRepoJoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUserRepoJoin(userRepoJoin: UserRepoJoin): Long

    @Query(
            "SELECT * FROM " +
                    "users INNER JOIN user_repo_join " +
                    "ON users.user_id = user_repo_join.user_id " +
                    "WHERE user_repo_join.repo_id = :repoId"
    )
    fun getUsersForRepo(repoId: Long): Flowable<List<User>>

    @Query(
            "SELECT * FROM " +
                    "user_repos INNER JOIN user_repo_join " +
                    "ON user_repos.repo_id = user_repo_join.repo_id " +
                    "WHERE user_repo_join.user_id = :userId"
    )
    fun getReposForUser(userId: Long): Flowable<List<Repo>>
}


/** Database */
@Database(entities = [User::class, Order::class, Repo::class, UserRepoJoin::class], version = 5)
@TypeConverters(DateConvert::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun orderDao(): OrderDao

    abstract fun repoDao(): RepoDao

    abstract fun userRepoDao(): UserRepoJoinDao
}

object DatabaseUtils {
    private const val TABLE_NAME = "user-db"

    /** database migrate */
    private val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                    "ALTER TABLE users ADD COLUMN age INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    private val migration_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                    "CREATE TABLE user_orders(" +
                            "order_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "order_user_id INTEGER, " +
                            "order_name TEXT NOT NULL, " +
                            "order_price REAL NOT NULL DEFAULT 0.0, " +
                            "FOREIGN KEY (order_user_id) REFERENCES users(user_id) " +
                            "ON DELETE CASCADE ON UPDATE CASCADE" +
                            ")"
            )
        }
    }

    private val migration_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                    "CREATE TABLE user_repos(" +
                            "repo_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "repo_name TEXT NOT NULL, " +
                            "repo_url TEXT NOT NULL" +
                            ")"
            )

            database.execSQL(
                    "CREATE TABLE user_repo_join(" +
                            "user_id INTEGER NOT NULL, " +
                            "repo_id INTEGER NOT NULL, " +
                            "PRIMARY KEY (user_id, repo_id), " +
                            "FOREIGN KEY (user_id) REFERENCES users(user_id) " +
                            "ON DELETE CASCADE ON UPDATE CASCADE, " +
                            "FOREIGN KEY (repo_id) REFERENCES user_repos(repo_id) " +
                            "ON DELETE CASCADE ON UPDATE CASCADE" +
                            ")"
            )
        }
    }

    private val migration_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                    "ALTER TABLE users ADD COLUMN birth INTEGER"
            )
        }
    }

    /** database instance */
    private val instance: UserDatabase by lazy {
        Room.databaseBuilder(
                DemoApplication.contextInstance,
                UserDatabase::class.java,
                TABLE_NAME)
                // this will allowed to do sql exec on main thread
                // .allowMainThreadQueries()
                .addMigrations(
                        migration_1_2,
                        migration_2_3,
                        migration_3_4,
                        migration_4_5)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        LogUtils.e("create database $TABLE_NAME")
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        LogUtils.e("open database $TABLE_NAME")
                    }
                }).build()
    }

    val mUserDao = instance.userDao()

    val mOrderDao = instance.orderDao()

    val mRepoDao = instance.repoDao()

    val mUserRepoDao = instance.userRepoDao()

    fun closeDatabase() {
        if (instance.isOpen)
            instance.close()
    }
}