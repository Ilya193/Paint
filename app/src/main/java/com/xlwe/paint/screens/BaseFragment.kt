package com.xlwe.paint.screens

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected open fun setListeners() = Unit
    protected open fun isSettingsScreen(screen: String) = false
    protected open fun parseParams() = Unit
}