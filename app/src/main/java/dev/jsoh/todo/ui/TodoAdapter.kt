package dev.jsoh.todo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jsoh.todo.R
import dev.jsoh.todo.databinding.ItemTodoBinding
import dev.jsoh.todo.models.Todo

class TodoAdapter(
    private val clickListener: (item: Todo) -> Unit,
    private val dragListener: (viewHolder: RecyclerView.ViewHolder) -> Unit
) :
    ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        val viewHolder = TodoViewHolder(ItemTodoBinding.bind(view))
        viewHolder.binding.checkbox.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition)
            item.isDone = !item.isDone
            clickListener.invoke(item)
        }
        return viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.binding.todo = getItem(position)
        holder.binding.handle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                dragListener.invoke(holder)
            }
            false
        }
    }

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    fun getTodo(position: Int): Todo = getItem(position)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
        }
    }
}