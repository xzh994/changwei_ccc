package com.example.lwxg.changweistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

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
