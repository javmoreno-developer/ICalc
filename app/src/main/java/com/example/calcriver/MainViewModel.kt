package com.example.calcriver

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calcriver.databinding.ActivityMainBinding
import java.io.IOException

class MainViewModel : ViewModel() {

    var contadorPulsacion: Int = 0
    private var lecturaCopia: String = ""
    private var contadorBackground: Int = 0 // Sirve para las operaciones de mas de dos operadores
    private var operators = listOf<Char>('+', '-', 'x', '/')
    private var longitud = 0 //longitud de la operacion base
    lateinit var binding: ActivityMainBinding
    private var firstDouble: Boolean = false  // Operaciones con decimales
    private var secondDouble: Boolean = false // Operaciones con decimales


    //propiedades para observer
    private val _rewriteObserver = MutableLiveData<String>()
    val rewriteObserver: LiveData<String> get() = _rewriteObserver

    private val _calculateObserver = MutableLiveData<String>()
    val calculateObserver: LiveData<String> get() = _calculateObserver

    private val _deleteError = MutableLiveData<Boolean>()
    val deleteError: LiveData<Boolean> get() = _deleteError

    private val _calcError = MutableLiveData<Boolean>()
    val calcError: LiveData<Boolean> get() = _calcError

    //funciones
    fun rewrite(param: String) {

        var previous = ""
        if (contadorPulsacion > 1) {
            previous = binding.actualOperation.getText().toString()
        }
        for (m in 0..operators.size - 1) {
            if (param == operators[m].toString()) {
                contadorBackground++
                if (contadorBackground > 1) {
                    calculateHere(false)
                }
            }
        }

        //binding.actualOperation.text = previous +  param
        _rewriteObserver.value = previous + param

    }
    fun posibleNegativo(param : Char) : Boolean{
        for (n in 0..operators.size - 1) {
            if(param == operators[n]) {
                return true
            }
        }
        return false
    }

    fun calculateHere(param: Boolean) {
        // viewModel.calculate(binding.actualOperation.text.toString())
        _calcError.value = false

        var texto = ""
        var suboperacion = ""

        if (contadorBackground <= 1) {
            texto = binding.actualOperation.text.toString()
            longitud = texto.length
        } else {
            texto = binding.actualOperation.text.toString()
            suboperacion = texto.substring(longitud, texto.length)
            texto = lecturaCopia + suboperacion
            longitud += suboperacion.length

        }



        var first = ""
        var second = ""
        var operatorPosition = 0

        // Aislamos la posicion del operador

            for (m in 0..texto.length - 1) {
                for (n in 0..operators.size - 1) {
                    if (texto.get(m) == operators[n]) {
                        if(m != 0) {
                            var p = posibleNegativo(texto.get(m - 1))
                            if (p == true) {
                                operatorPosition = m - 1
                            } else {
                                operatorPosition = m
                            }
                        }
                    }
                }
            }
            // Sacamos los numeros a operar

            // first
            for (m in 0..operatorPosition - 1) {
                if (texto.get(m) == '.') {
                    firstDouble = true
                }
                first += texto.get(m)
            }

            //second
            for (m in operatorPosition + 1..texto.length - 1) {
                if (texto.get(m) == '.') {
                    secondDouble = true
                }
                second += texto.get(m)
            }


            //con los datos ya obtenidos procedemos a operar
            var resultado = 0.0

        if(first == "" || second == "") {
            _calcError.value = true
        }

        if(_calcError.value == false) {

            var opOne = first.toDouble()
            var opSecond = second.toDouble()
            when (texto.get(operatorPosition)) {
                '+' -> resultado = opOne + opSecond
                '-' -> resultado = opOne - opSecond
                'x' -> resultado = opOne * opSecond
                '/' -> resultado = opOne / opSecond
            }

            var resultado2 = 0
            if (secondDouble == false && firstDouble == false) {
                resultado2 = resultado.toInt()
            }

            lecturaCopia = resultado.toString()
            if (param == true) {
                contadorBackground = 0
                longitud = 0
                lecturaCopia = ""
                // binding.actualOperation.text = resultado.toString()
                if (secondDouble == false && firstDouble == false) {
                    _rewriteObserver.value = resultado2.toString()
                } else {
                    _rewriteObserver.value = resultado.toString()
                }
            }
            //binding.actualResult.text = resultado.toString()
            if (secondDouble == false && firstDouble == false) {
                _calculateObserver.value = resultado2.toString()
            } else {
                _calculateObserver.value = resultado.toString()
            }
        }
    }

    fun parcialDelete() {
        var beforeDelete = binding.actualOperation.getText().toString()
        if (beforeDelete.length == 0) {
            _deleteError.value = true
        } else {
            var newValue = beforeDelete.substring(0, beforeDelete.length - 1)
            //binding.actualOperation.text = newValue
            _rewriteObserver.value = newValue
        }
    }

    fun totalDelete() {
        contadorBackground = 0
        //binding.actualOperation.text = ""
        _rewriteObserver.value = ""
    }

    fun changeSymbol() {
        //localizar el ultimo operador
        var texto = binding.actualOperation.text.toString()
        var pos = 0
        for (m in 0..texto.length - 1) {
            for (n in 0..operators.size - 1) {
                if (texto.get(m) == operators[n]) {
                    pos = m
                }
            }
        }
        //extraer desde el principio hasta el ultimo op
        var beg = ""
        if(pos != 0) {
            for (n in 0..pos) {
                beg += texto.get(n)
            }
        }
        //extraer desde el op hasta el final
        var fin = ""
        if(pos == 0) {
            for (n in pos..texto.length - 1) {
                fin += texto.get(n)
            }
        } else {
            for (n in pos + 1..texto.length - 1) {
                fin += texto.get(n)
            }
        }
        //cambiar signo del final
        var change = fin.toDouble() * -1
        //fusionar los dos operandos
        var resultado = beg + change.toString()
       /* if(pos != 0) {
            if (texto.get(pos) == '-') {
                resultado = beg + "+" + (change*-1).toString()
            }
        }*/
        //resultado final
        _rewriteObserver.value = resultado
    }
}