package dev.jsoh.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.jsoh.myapplication.models.Todo
import dev.jsoh.myapplication.ui.TodoAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var touchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // Lottie 애니메이션 클릭시 사라지게
        animation_view.setOnClickListener {
            it.visibility = View.GONE
        }

        // 클릭 이벤트
        val adapter = TodoAdapter(
            clickListener = { item -> viewModel.update(item) },
            dragListener = { holder -> touchHelper.startDrag(holder) }
        )
        todo_list.layoutManager = LinearLayoutManager(this)
        todo_list.adapter = adapter

        // 스와이프 삭제
        touchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
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
            if (it.none { todo -> !todo.isDone }) {
                animation_view.visibility = View.VISIBLE
                animation_view.playAnimation()
                viewModel.deleteAll()
            }
        })

        // 추가
        add_button.setOnClickListener {
            viewModel.insert(Todo(text = new_todo_edit.text.toString()))
            new_todo_edit.setText("")
        }
    }
}
