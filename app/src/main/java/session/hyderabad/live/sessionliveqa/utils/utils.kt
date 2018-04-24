package session.hyderabad.live.sessionliveqa.utils

sealed class QuestionAdapterSeal
data class Delete(val docId: String) : QuestionAdapterSeal()
data class Answer(val docId: String) : QuestionAdapterSeal()
data class Edit(val docId: String, val answer: String?) : QuestionAdapterSeal()