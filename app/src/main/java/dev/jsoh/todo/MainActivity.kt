package dev.jsoh.todo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import dev.jsoh.todo.models.Todo
import dev.jsoh.todo.ui.TodoAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var touchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

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
                return true
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
