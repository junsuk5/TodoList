package dev.jsoh.myapplication

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import dev.jsoh.myapplication.databinding.ItemTodoBinding
import dev.jsoh.myapplication.models.Todo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // Lottie 애니메이션 콜백
        animation_view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                animation_view.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })

        // 클릭 이벤트
        val adapter = TodoAdapter { item ->
            viewModel.update(item)
            if (item.isDone) {
                animation_view.visibility = View.VISIBLE
                animation_view.playAnimation()
            }
        }
        todo_list.layoutManager = LinearLayoutManager(this)
        todo_list.adapter = adapter

        // 스와이프 삭제
        val touchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.delete(adapter.getTodo(viewHolder.adapterPosition))
                Snackbar.make(viewHolder.itemView, "삭제됨", 3000)
                    .setAction("취소") {
                        viewModel.undo()
                    }.show()
            }
        })
        touchHelper.attachToRecyclerView(todo_list)

        // Query
        viewModel.todos().observe(this, Observer {
            adapter.submitList(it)
        })

        // 추가
        add_button.setOnClickListener {
            viewModel.insert(Todo(text = new_todo_edit.text.toString()))
            new_todo_edit.setText("")
        }
    }
}

class TodoAdapter(private val clickListener: (item: Todo) -> Unit) :
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

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.binding.todo = getItem(position)
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