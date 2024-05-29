package com.dicoding.storyapp.view.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        // Temporary dan hanya untuk cek ui di hp kayak gimana (JGN LUPA DIHAPUS LAGI NANTI)
        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this@AddStoryActivity, DetailStoryActivity::class.java)
            startActivity(intent)
        }
    }
}