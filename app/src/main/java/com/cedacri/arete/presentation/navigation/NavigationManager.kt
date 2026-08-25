package com.cedacri.arete.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class NavigationManager : ViewModel(){
    val backStack: NavBackStack<NavKey> = NavBackStack(LoginScreen)

    fun navigateTo(key: NavKey) {
        backStack.add(key)
    }

    fun currentScreen() : NavKey {
        return backStack[backStack.lastIndex]
    }

    fun safeNavigateHome(){
        if(backStack.size == 1){
            replaceScreen(LoginScreen)
        }
    }

    fun goBack(): Boolean {
        if (backStack.size <= 1 || backStack.last() is LoginScreen) return false
        backStack.removeAt(backStack.lastIndex)
        return true
    }

    fun replaceScreen(key: NavKey) {
        if (backStack.isEmpty()) backStack.add(key)
        else backStack[backStack.lastIndex] = key
    }

    fun goBackHome() {
        while (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }

        if (backStack.firstOrNull() !is LoginScreen) {
            backStack.clear()
            backStack.add(LoginScreen)
        }
    }
}