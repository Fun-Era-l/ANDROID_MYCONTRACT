package com.courseproject.mycontractitegration

interface BaseView<T> {
    fun setPresenter(presenter: T)
}