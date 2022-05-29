package com.sufian.locationtracker

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor,
) {
    fun diskIO():Executor{
        return diskIO
    }

    fun networkIO(): Executor{
        return  networkIO
    }

    fun mainThread():Executor{
        return mainThread
    }

    private class MainThreadExecutor : Executor{
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object{
        // For singleton instance
        private val LOCK = Any()
        private var sInstance:AppExecutors? = null
        val instance:AppExecutors?
        get() {
            if(sInstance == null){
                synchronized(LOCK){
                    sInstance = AppExecutors(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        MainThreadExecutor()
                    )
                }
            }
            return sInstance
        }
    }
}