package dev.jsoh.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jsoh.myapplication.databinding.ItemTodoBinding
import dev.jsoh.myapplication.models.Todo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val adapter = TodoAdapter()
        todo_list.layoutManager = LinearLayoutManager(this)
        todo_list.adapter = adapter

        viewModel.todos().observe(this, Observer {
            adapter.submitList(it)
        })

        add_button.setOnClickListener {
            viewModel.insert(Todo(text = new_todo_edit.text.toString()))
            new_todo_edit.setText("")
        }
    }
}

class TodoAdapter : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        val viewHolder = TodoViewHolder(ItemTodoBinding.bind(view))
        return viewHolder
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.binding.todo = getItem(position)
    }

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
        }
    }
}