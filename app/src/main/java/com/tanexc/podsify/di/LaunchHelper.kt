package com.tanexc.podsify.di

import com.tanexc.bluetoothtool.domain.ApplicationLaunchHelper
import com.tanexc.podsify.util.PodsifyLaunchHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val launchHelper = module {
    singleOf(::PodsifyLaunchHelper) bind ApplicationLaunchHelper::class
}