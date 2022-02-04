package com.traffappscorelib.wsc

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.pampam.lib.R
import com.pampam.lib.data.Analytics
import com.pampam.lib.data.EntityAppsflyerData
import com.pampam.lib.interfaces.IValueListener

abstract class App : Application() {

    companion object {
        lateinit var instance: App
    }

    private var listener: IValueListener<EntityAppsflyerData>? = null
    private var entity: EntityAppsflyerData? = null

    abstract fun getAppsflyerAppId() : String

    fun getAppsflyerData(listener: IValueListener<EntityAppsflyerData>) {
        when {
            entity != null -> listener.value(entity)
            else -> this.listener = listener
        }
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppThemeLib)
        instance = this

        AppsFlyerLib.getInstance().init(getAppsflyerAppId(), object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(map: Map<String, Any>) {

                val status = map["af_status"]
                val namingLong = map["campaign"]
                val namingShort = map["c"]

                val isOrganic = status.toString() == "Organic"
                val naming = namingShort ?: namingLong
                val namingString = naming?.toString()

                if(!isOrganic) Analytics.sendAppsflyerData(map.toString())
                val value = EntityAppsflyerData(isOrganic, namingString)

                if(listener == null) entity = value
                else listener?.value(value)
            }
            override fun onConversionDataFail(s: String) {
                val value = EntityAppsflyerData(true, null)

                if(listener == null) entity = value
                else listener?.value(value)
            }
            override fun onAppOpenAttribution(map: Map<String, String>) {}
            override fun onAttributionFailure(s: String) {}
        }, this)
        AppsFlyerLib.getInstance().start(this)
    }
}