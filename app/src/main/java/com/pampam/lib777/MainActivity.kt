package com.pampam.lib777

import com.pampam.lib.StartActivity


class MainActivity : StartActivity() {

    override fun getPlaceholderStartActivity(): Class<*> = ExampleActivity::class.java

    override fun getAlartReceiver() = AlartReceiver::class.java
}