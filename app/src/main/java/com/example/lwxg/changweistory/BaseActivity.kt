package com.example.lwxg.changweistory

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

open class BaseActivity : AppCompatActivity() {
    private lateinit var mFragmenManager: FragmentManager
    protected lateinit var mFragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFragmenManager = supportFragmentManager;
        mFragmentTransaction = mFragmenManager.beginTransaction()
    }

    fun getParentSupportFragmentManager(): FragmentManager {
        return mFragmenManager
    }

    fun getParentSupportFragmentTransaction(): FragmentTransaction {
        return mFragmentTransaction
    }

}
