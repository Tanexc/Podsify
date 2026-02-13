package com.tanexc.podsify.widgets.presentation.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.glance.LocalContext

@Composable
fun stringResource(@StringRes resId: Int): String =
    LocalContext.current.getString(resId)

@Composable
fun stringResource(@StringRes resId: Int, vararg formatArgs: Any): String =
    LocalContext.current.getString(resId, formatArgs)
