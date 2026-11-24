package com.example.waygofrontend.di

import android.content.Context
import com.example.waygofrontend.data.firebase.FirebaseAuthHelper
import com.example.waygofrontend.data.local.LocalDataSource
import com.example.waygofrontend.data.remote.RemoteDataSource
import com.example.waygofrontend.data.remote.RetrofitClient
import com.example.waygofrontend.data.repository.RepositoryImpl

object AppContainer {

    private lateinit var repository: RepositoryImpl

    fun init(context: Context) {
        val localDataSource = LocalDataSource.getInstance(context)
        val remoteDataSource = RemoteDataSource(RetrofitClient.api)
        val firebaseAuthHelper = FirebaseAuthHelper()
        repository = RepositoryImpl(localDataSource, remoteDataSource, firebaseAuthHelper)
    }

    fun getRepository(): RepositoryImpl = repository
}
