package com.conscience.app.questions.models

data class Question(
    val id: String,
    val category: String,
    val text: String,
    val options: List<String>,
    val avoidantOptions: List<String> = emptyList(),  // These trigger punishment
    val punishmentMap: Map<String, String> = emptyMap()  // option -> punishment message
)
