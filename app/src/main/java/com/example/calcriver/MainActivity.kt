package com.example.calcriver

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calcriver.databinding.ActivityMainBinding
import com.example.calcriver.recycler.itemAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var colores = "Colores"

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recycler
        initRecyclerView()  

        //viewModel


        binding.viewmodel = viewModel

        //observer

       /* viewModel._pulsado.observe(this, Observer {
            //Toast.makeText(this,"valor : ${it}",Toast.LENGTH_SHORT).show()
        })*/

        viewModel.binding = binding

        // Observers para el viewModel

        viewModel.rewriteObserver.observe(this, Observer {
            binding.actualOperation.text = it
        })

        viewModel.calculateObserver.observe(this, Observer {
            binding.actualResult.text = it
        })

        viewModel.deleteError.observe(this, Observer {
            Toast.makeText(this,"No puedes borrar",Toast.LENGTH_SHORT).show()
        })


        viewModel.calcError.observe(this, Observer {
            if(it == true) {
                Toast.makeText(this, "Error en la operacion", Toast.LENGTH_SHORT).show()
            }
        })

        //dark mode
        binding.light.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            button_provider.Colores = button_provider.ColoresDark



        }

        binding.dark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            button_provider.Colores = button_provider.ColoresLight

        }

    }



    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.Recycler)

        recyclerView.layoutManager= GridLayoutManager(this,4)

        recyclerView.adapter = itemAdapter(button_provider.Lista,
            button_provider.Colores,
            itemAdapter.OnClickListener { word ->
           //contador para ver la primera pulsacion
            viewModel.contadorPulsacion++

            //sobreescribimos actual_operation
            when(word) {
                "=" -> viewModel.calculateHere(true)
                "<-" -> viewModel.parcialDelete()
                "C" -> viewModel.totalDelete()
                "+/-" -> viewModel.changeSymbol()
                else -> viewModel.rewrite(word)
            }
        },this)
    }




}
