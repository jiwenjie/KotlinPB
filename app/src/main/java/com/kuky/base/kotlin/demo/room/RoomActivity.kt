package com.kuky.base.kotlin.demo.room

import android.annotation.SuppressLint
import android.arch.persistence.room.EmptyResultSetException
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import com.kuky.base.android.kotlin.baseutils.LogUtils
import com.kuky.base.android.kotlin.baseutils.ToastUtils
import com.kuky.base.android.kotlin.baseviews.BaseActivity
import com.kuky.base.kotlin.demo.R
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.dialog_create_user.view.*
import kotlinx.android.synthetic.main.dialog_delete_user.view.*
import kotlinx.android.synthetic.main.dialog_query_user.view.*
import kotlinx.android.synthetic.main.dialog_update_user.view.*

class RoomActivity : BaseActivity() {
    private lateinit var mCreateDialogView: View
    private lateinit var mUpdateDialogView: View
    private lateinit var mDeleteDialogView: View
    private lateinit var mQueryDialogView: View
    private lateinit var mInflater: LayoutInflater

    private val mCreateDialog: AlertDialog by lazy {
        initAlertDialog(mCreateDialogView)
    }
    private val mUpdateDialog: AlertDialog by lazy {
        initAlertDialog(mUpdateDialogView)
    }
    private val mDeleteDialog: AlertDialog by lazy {
        initAlertDialog(mDeleteDialogView)
    }
    private val mQueryDialog: AlertDialog by lazy {
        initAlertDialog(mQueryDialogView)
    }

    private fun initAlertDialog(view: View): AlertDialog {
        return AlertDialog.Builder(this@RoomActivity)
            .setView(view)
            .setCancelable(false).create()
    }

    override fun getLayoutId(): Int = R.layout.activity_room

    @SuppressLint("CheckResult")
    override fun initActivity(savedInstanceState: Bundle?) {
        room_tool_bar.title = this::class.java.simpleName
        setSupportActionBar(room_tool_bar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mInflater = LayoutInflater.from(this@RoomActivity)
        mCreateDialogView = mInflater.inflate(R.layout.dialog_create_user, null)
        mCreateDialogView.create_user_phone.filters = arrayOf(InputFilter.LengthFilter(11))

        mUpdateDialogView = mInflater.inflate(R.layout.dialog_update_user, null)
        mUpdateDialogView.update_user_phone.filters = arrayOf(InputFilter.LengthFilter(11))

        mDeleteDialogView = mInflater.inflate(R.layout.dialog_delete_user, null)
        mQueryDialogView = mInflater.inflate(R.layout.dialog_query_user, null)

        /**
         * Action!!
         * it is not allowed to insert/query/delete/update data on main thread, so need switch thread by rx,
         * then when insert an object of UserRepoJoin must make sure the user of userId and the repo of repoId exists
         */
        val repo = Repo(repoName = "KotlinPB", repoUrl = "https://github.com/kukyxs/KotlinPB")
        val repo2 = Repo(repoName = "blog_project", repoUrl = "https://github.com/kukyxs/blog_project")

        val userRepoJoin = UserRepoJoin(userId = 1, repoId = 1)
        val userRepoJoin2 = UserRepoJoin(userId = 2, repoId = 1)
        val userRepoJoin3 = UserRepoJoin(userId = 1, repoId = 2)
        val userRepoJoin4 = UserRepoJoin(userId = 2, repoId = 2)

        Completable.create {
            //            DatabaseUtils.mRepoDao.createRepo(repo)
//            DatabaseUtils.mRepoDao.createRepo(repo2)

//            DatabaseUtils.mUserRepoDao.createUserRepoJoin(userRepoJoin)
//            DatabaseUtils.mUserRepoDao.createUserRepoJoin(userRepoJoin2)
//            DatabaseUtils.mUserRepoDao.createUserRepoJoin(userRepoJoin3)
//            DatabaseUtils.mUserRepoDao.createUserRepoJoin(userRepoJoin4)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { }

        DatabaseUtils.mRepoDao
            .getAllRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                val sb = StringBuffer().append("\n")
                t.forEach { sb.append(it.toString()).append("\n") }
                LogUtils.e(sb)
            }

        DatabaseUtils.mUserRepoDao
            .getAllUserRepoJoin()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                val sb = StringBuffer().append("\n")
                t.forEach { sb.append(it.toString()).append("\n") }
                LogUtils.e(sb)
            }

        DatabaseUtils.mUserRepoDao
            .getReposForUser(2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                val sb = StringBuffer().append("\n").append("repos for user 2:\n")
                t.forEach { sb.append(it.toString()).append("\n") }
                LogUtils.e(sb)
            }

        DatabaseUtils.mUserRepoDao
            .getUsersForRepo(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                val sb = StringBuffer().append("\n").append("users for repo 1:\n")
                t.forEach { sb.append(it.toString()).append("\n") }
                LogUtils.e(sb)
            }
    }

    override fun setListener() {
        room_tool_bar.setNavigationOnClickListener { finish() }

        /** insert user into database */
        insert_user.setOnClickListener {
            // clear content before show dialog
            mCreateDialogView.create_user_name.setText("")
            mCreateDialogView.create_user_phone.setText("")
            mCreateDialogView.create_user_province.setText("")
            mCreateDialogView.create_user_street.setText("")
            mCreateDialog.show()
        }
        mCreateDialogView.create_cancel.setOnClickListener { mCreateDialog.dismiss() }
        mCreateDialogView.create_ok.setOnClickListener {
            val name = mCreateDialogView.create_user_name.text.toString()
            val phone = mCreateDialogView.create_user_phone.text.toString()
            val province = mCreateDialogView.create_user_province.text.toString()
            val street = mCreateDialogView.create_user_street.text.toString()

            if (name.isEmpty() || phone.isEmpty() || province.isEmpty() || street.isEmpty()) {
                ToastUtils.showToast(this@RoomActivity, "You need input all info above")
            } else {
                Completable.create { t ->
                    val user = User(userName = name, phoneNo = phone, address = Address(province, street))
                    val value = DatabaseUtils.mUserDao.createUser(user)
                    if (value > 0) t.onComplete()
                    else t.onError(IllegalStateException("Insert failed"))
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mCreateDialog.dismiss()
                        ToastUtils.showToast(this@RoomActivity, "Insert succeed")
                    }, { t -> ToastUtils.showToast(this@RoomActivity, t.message!!) })
            }
        }

        /** update user */
        update_user.setOnClickListener {
            mUpdateDialogView.update_user_id.setText("")
            mUpdateDialogView.update_user_name.setText("")
            mUpdateDialogView.update_user_phone.setText("")
            mUpdateDialogView.update_user_province.setText("")
            mUpdateDialogView.update_user_street.setText("")
            mUpdateDialog.show()
        }
        mUpdateDialogView.update_cancel.setOnClickListener { mUpdateDialog.dismiss() }
        mUpdateDialogView.update_ok.setOnClickListener {
            val id = mUpdateDialogView.update_user_id.text.toString()
            val name = mUpdateDialogView.update_user_name.text.toString()
            val phone = mUpdateDialogView.update_user_phone.text.toString()
            val province = mUpdateDialogView.update_user_province.text.toString()
            val street = mUpdateDialogView.update_user_street.text.toString()

            if (id.isEmpty()) {
                ToastUtils.showToast(this@RoomActivity, "user id can't be empty")
            } else if (name.isEmpty() && phone.isEmpty() && province.isEmpty() && street.isEmpty()) {
                ToastUtils.showToast(this@RoomActivity, "input the message what you want update")
            } else {
                DatabaseUtils.mUserDao
                    .getSingleUser(id.toLong())
                    .flatMap { t ->
                        if (name.isNotEmpty()) t.userName = name
                        if (phone.isNotEmpty()) t.phoneNo = phone
                        if (province.isNotEmpty()) t.address.province = province
                        if (street.isNotEmpty()) t.address.street = street
                        Single.just(DatabaseUtils.mUserDao.updateUser(t))
                    }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        if (t > 0) {
                            mUpdateDialog.dismiss()
                            ToastUtils.showToast(this@RoomActivity, "Update succeed")
                        } else
                            ToastUtils.showToast(this@RoomActivity, "Update failed")
                    }, { t ->
                        if (t is EmptyResultSetException) ToastUtils.showToast(
                            this@RoomActivity,
                            "this user not exists"
                        )
                    })
            }
        }

        /** delete user from database */
        delete_user.setOnClickListener {
            mDeleteDialogView.delete_user_id.setText("")
            mDeleteDialogView.delete_all.isChecked = false
            mDeleteDialog.show()
        }
        mDeleteDialogView.delete_cancel.setOnClickListener { mDeleteDialog.dismiss() }
        mDeleteDialogView.delete_all.setOnCheckedChangeListener { _, isChecked ->
            mDeleteDialogView.delete_user_id.isEnabled = !isChecked
        }
        mDeleteDialogView.delete_ok.setOnClickListener {
            val id = mDeleteDialogView.delete_user_id.text.toString()

            Completable.create { t ->
                if (id.isEmpty() && !mDeleteDialogView.delete_all.isChecked) {
                    t.onError(IllegalArgumentException("id can't be empty"))
                } else {
                    val value =
                        if (mDeleteDialogView.delete_all.isChecked)
                            DatabaseUtils.mUserDao.deleteAllUser()
                        else
                            DatabaseUtils.mUserDao.deleteSingleUser(id.toLong())
                    if (value <= 0) t.onError(IllegalStateException("this user not exists"))
                    else t.onComplete()
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mDeleteDialog.dismiss()
                    ToastUtils.showToast(this@RoomActivity, "delete succeed")
                }, { t -> ToastUtils.showToast(this@RoomActivity, t.message!!) })
        }

        /** query user from database */
        query_user.setOnClickListener {
            mQueryDialogView.query_user_id.setText("")
            mQueryDialogView.query_all.isChecked = false
            mQueryDialog.show()
        }
        mQueryDialogView.query_cancel.setOnClickListener { mQueryDialog.dismiss() }
        mQueryDialogView.query_all.setOnCheckedChangeListener { _, isChecked ->
            mQueryDialogView.query_user_id.isEnabled = !isChecked
        }
        mQueryDialogView.query_ok.setOnClickListener {
            val id = mQueryDialogView.query_user_id.text.toString()

            if (id.isEmpty() && !mQueryDialogView.query_all.isChecked) {
                ToastUtils.showToast(this@RoomActivity, "id can't be empty")
            } else {
                if (mQueryDialogView.query_all.isChecked)
                    DatabaseUtils.mUserDao
                        .getAllUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ t ->
                            mQueryDialog.dismiss()
                            val sb = StringBuffer()
                                .append("Totally find ${t.size} user${if (t.size > 1) "s" else ""}:\n\n")
                            t.forEach { sb.append(it.toString().trim()).append("\n\n") }
                            operate_result.text = sb.trim()
                        }, { t -> LogUtils.e(t.message!!) })
                else
                    DatabaseUtils.mUserDao
                        .getSingleUser(id.toLong())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { t ->
                                mQueryDialog.dismiss()
                                val sb = StringBuffer()
                                    .append("Find user whose id = $id:\n\n")
                                    .append(t.toString())
                                operate_result.text = sb.trim()
                            }, { t ->
                                if (t is EmptyResultSetException)
                                    ToastUtils.showToast(this@RoomActivity, "this user not exists")
                                else
                                    ToastUtils.showToast(this@RoomActivity, "query failed")
                            })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseUtils.closeDatabase()
    }
}
