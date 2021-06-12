package com.peresapy.fundamentos_eh_ho

import android.app.Application
import com.peresapy.fundamentos_eh_ho.di.DIProvider



class App: Application() {
    override fun onCreate() {
        super.onCreate()
        DIProvider.init(this)
    }
}