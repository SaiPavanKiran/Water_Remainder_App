package com.rspk.water_remainder_app.di

import android.content.Context
import com.rspk.water_remainder_app.dataStore
import com.rspk.water_remainder_app.datastore.TimingsStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideTimingsStore(
        @ApplicationContext context: Context
    ): TimingsStore {
        return TimingsStore(context.dataStore)
    }
}
