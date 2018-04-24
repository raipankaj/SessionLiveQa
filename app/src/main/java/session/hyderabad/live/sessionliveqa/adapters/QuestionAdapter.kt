package session.hyderabad.live.sessionliveqa.adapters

import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.adapter_question_item.view.*
import session.hyderabad.live.sessionliveqa.R
import session.hyderabad.live.sessionliveqa.data.Questions
import session.hyderabad.live.sessionliveqa.utils.*

class QuestionAdapter(private var questionList: ArrayList<Questions>,
                      private val isAdmin: Boolean = false,
                      private val mQuestionModeClick: OnModeClick) :
        RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    fun updateList(questionList: ArrayList<Questions>) {
        this.questionList = questionList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            QuestionViewHolder(parent.loadAdapterLayout(R.layout.adapter_question_item))

    override fun getItemCount() = questionList.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {

        holder.apply {
            questionList[position].also {
                tvQuestion.text = it.question
                tvUsername.text = it.name

                tvAnswer.visibility = if (it.answer.isNullOrEmpty()) {
                    View.GONE
                } else {
                    tvAnswer.text = it.answer
                    View.VISIBLE
                }

                tvAnswerBy.visibility = if (it.answeredBy.isNullOrEmpty()) {
                    View.GONE
                } else {
                    tvAnswerBy.text = it.answeredBy
                    View.VISIBLE
                }
            }
            adminGroup.visibility = if (isAdmin) View.VISIBLE else View.GONE
        }
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.tvUserQuestion
        val tvUsername: TextView = itemView.tvUserName
        val tvAnswer: TextView = itemView.tvAnswer
        val tvAnswerBy: TextView = itemView.tvAnsweredBy
        val adminGroup: Group = itemView.admin_group

        init {
            itemView.btEdit.setOnClickListener {
                mQuestionModeClick.onClick(Edit(questionList.get(adapterPosition).id.toString(),
                        questionList.get(adapterPosition).answer))
            }

            itemView.btAnswer.setOnClickListener {
                mQuestionModeClick.onClick(Answer(questionList.get(adapterPosition).id.toString()))
            }

            itemView.btDelete.setOnClickListener {
                mQuestionModeClick.onClick(Delete(questionList.get(adapterPosition).id.toString()))
            }
        }
    }

    interface OnModeClick {
        fun onClick(questionAdapterSeal: QuestionAdapterSeal)
    }
}