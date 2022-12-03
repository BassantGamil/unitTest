package com.udacity.project4

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.R

fun checkSnackBarTextMatches(@StringRes stringRes: Int) {
    Espresso.onView(ViewMatchers.withId(R.id.snackbar_text))
        .check(ViewAssertions.matches(ViewMatchers.withText(stringRes)))
}