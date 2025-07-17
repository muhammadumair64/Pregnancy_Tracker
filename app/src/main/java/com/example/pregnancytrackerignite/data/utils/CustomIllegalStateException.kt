//package com.iobits.rubik_cube_solver.data.utils
//
//import android.content.Intent
//import com.iobits.rubik_cube_solver.data.utils.AnalyticsManager.logEvent
//import com.iobits.rubik_cube_solver.di.myApplication.MyApplication
//import com.iobits.rubik_cube_solver.presentation.activity.MainActivity
//import kotlin.system.exitProcess
//
//class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
//    override fun uncaughtException(t: Thread, e: Throwable) {
//
//            // Handle memory-related exception
//            try{
//                logEvent("ERROR", "Exception:${e.localizedMessage }")
//                val intent = Intent(MyApplication.mInstance, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                MyApplication.mInstance.startActivity(intent)
//            }catch (e:Exception){
//                logEvent("ERROR", "Exception:${e.localizedMessage }")
//                e.localizedMessage
//            }
//
//        android.os.Process.killProcess(android.os.Process.myPid())
//        exitProcess(1)
//    }
//    private fun isOutOfMemoryException(throwable: Throwable): Boolean {
//        // Check if the exception or its cause is an OutOfMemoryError
//        var t: Throwable? = throwable
//        while (t != null) {
//            if (t is OutOfMemoryError) {
//                return true
//            }
//            t = t.cause
//        }
//        return false
//    }
//}