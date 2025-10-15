package com.farma.parkinsoftapp.di

import com.farma.parkinsoftapp.data.repositories.AuthRepositoryImpl
import com.farma.parkinsoftapp.data.repositories.MainRepositoryImpl
import com.farma.parkinsoftapp.domain.repositories.AuthRepository
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun provideMainRepository(
        mainRepository: MainRepositoryImpl
    ): MainRepository
}