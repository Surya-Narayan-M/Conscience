package com.conscience.app.questions.models

data class AnswerResult(
    val isAvoidant: Boolean,
    val hasPunishment: Boolean,
    val punishmentMessage: String = ""
)
