package com.example.consolcalculator

class SymbolsStack {

    //SymbolsStack -- класс, для работы со стеком символов/операторов
    private var array = arrayOfNulls<Char>(255)
    private var position = 0

    fun push(item: Char?) {
        /*
        не забываем, что при такой расстановке инкремента position++, значение position увеличится
        на единицу уже после того, как в массив поместится значение
        */
        array[position++] = item
    }

    fun pop(): Char? {
        //при такой расстановке декремента будет использовано уже уменьшенное значение position
        return array[--position]
    }

    //тут сравниваем приоритеты у двух операторов между собой
    fun comparePriority(op: Char): Boolean {
        return (getPriority(op) > getPriority(array[position - 1]))
    }

    //тут можно получить приоритет оператора
    private fun getPriority(op: Char?): Int {
        return when (op) {
            '(' -> 0
            ')' -> 1
            '+' -> 2
            '-' -> 2
            '*' -> 3
            '/' -> 3
            '%' -> 3
            else -> -1
        }
    }

    fun isEmpty() = (position == 0)

}

class Stack {
    //это класс для работы со стеком чисел

    private var array = arrayOfNulls<String>(255)
    private var position = 0

    //так засовываем число в стек
    fun push(item: String) {
        /*
        не забываем, что при такой расстановке инкремента position++, значение position увеличится
        на единицу уже после того, как в массив поместится значение
        */
        array[position++] = item
    }

    //а так достаём число из стека
    fun pop(): String {
        //проверяемся на нулевой элемент
        return if (!isEmpty()) {
            //при такой расстановке декремента будет использовано уже уменьшенное значение position
            array[--position].toString()
        } else {
            ""
        }
    }

    fun isEmpty() = (position == 0)

}