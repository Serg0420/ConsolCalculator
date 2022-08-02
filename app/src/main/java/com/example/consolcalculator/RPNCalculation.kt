package com.example.consolcalculator

class RPNCalculation {

    fun calculate(input: String): String {
        //формируем стек с числами и операторами
        val rpn = invertStack(toRPN(input))
        //создадим ещё один объект, уже для хранения вычислений
        val result = Stack()
        var msgError = ""

        try {
            while (!rpn.isEmpty()) {
                val item = rpn.pop()
                if (item.isNotEmpty()) {

                    //заносим в результирующий стек все числа
                    if (item[0].isDigit()) {
                        result.push(item)
                        //прерываем итерацию если пришло именно число
                        continue
                    }

                    /*
                    Как только вместо чисел начали приходить операторы, перестаём заносить их в
                    результирующий стек, и начинаем в зависимости от поступившего оператора
                    выполнять необходимые арифметические операции
                    */
                    //получаем из стека значения занесённых чисел в обратном порядке, по два.
                    //сюда, в num2, после первой итерации, будет попадать промежуточный ответ
                    val num2 = result.pop().toDouble()
                    val num1 = result.pop().toDouble()

                    //проверим деление на ноль
                    if (num2 == 0.0 && item[0] == '/') {
                        msgError = "Ошибка, деление на ноль!"
                        break
                    }

                    /*
                    Производим арифметические операции и заносим промежуточный ответ в стек.
                    Получается, выше мы извлекли из стека два значения, а тут поместим одно,
                    промежуточный ответ. Т.е. в следующей итерации в num2 будет храниться
                    промежуточный ответ
                    */
                    result.push(
                        when (item[0]) {
                            '+' -> num1 + num2
                            '-' -> num1 - num2
                            '*' -> num1 * num2
                            '/' -> num1 / num2
                            '%' -> num1 % num2
                            else -> 0
                        }.toString()
                    )
                }
            }
        } catch (e: Exception) {
            msgError =
                "Неизвестная ошибка (не совпадение скобок, ввод десятичного чиса или текста и т.д)"
        }

        //выводим результат если ошибок нет
        return msgError.ifEmpty {
            result.pop()
        }
    }

    private fun toRPN(expr: String): Stack {
        //toRPN -- метод, приводящий выражение в RPN
        val stOfNum = Stack()
        val stOfSymb = SymbolsStack()
        var bufferOfDigits = ""

        for (ch: Char in expr) {
            //ловим цифры из строки и запоминаем их
            if (ch.isDigit()) {
                bufferOfDigits += ch.toString()
                //если символ был цифрой, сразу прыгаем на следующую итерацию
                //так из последовательности цифр мы собираем число
                continue
            }

            //кидаем получившееся число в стек
            stOfNum.push(bufferOfDigits)
            bufferOfDigits = ""

            //Работаем с приоритетами операций
            //двигаем значения внутри скобок вперёд, увеличивая тем самым их приоритет
            if (stOfSymb.isEmpty() || ch == '(') {
                stOfSymb.push(ch)
            } else if (ch == ')') {
                while (!stOfSymb.isEmpty()) {
                    val last = stOfSymb.pop()
                    when (last) {
                        '(' -> break
                        else -> stOfNum.push(last.toString())
                    }
                }
            } else {
                /*
                далее расставляем в стеке символы операторов в порядке приоритета
                (умножение должно выполняться раньше суммы, т.е. 2+2*2=6)
                */
                if (stOfSymb.comparePriority(ch)) {
                    stOfSymb.push(ch)
                } else {
                    while (!stOfSymb.isEmpty()) {
                        if (!stOfSymb.comparePriority(ch)) {
                            stOfNum.push(stOfSymb.pop().toString())
                        } else {
                            break
                        }
                    }
                    stOfSymb.push(ch)
                }
            }
        }

        /*
        тут bufferOfDigits == "" всегда, т.е. .isEmpty()==true
        это позволит при работе с объединённым стеком позже найти место где кончаются числа и
        начинаются операторы
        */
        stOfNum.push(bufferOfDigits)

        //объединяем стек чисел со стеком символов, стек символов залетает в обратном порядке!!!
        while (!stOfSymb.isEmpty()) {
            stOfNum.push(stOfSymb.pop().toString())
        }
        return stOfNum
    }

    /*
    функция для разворота(т.е. последний элемент станет первым и т.д.) ЧАСТИ стека
    именно для этого мы и запушили посреди стека пустое значение-разделитель(bufferOfDigits)
    */
    private fun invertStack(stack: Stack): Stack {
        val result = Stack()
        while (!stack.isEmpty()) result.push(stack.pop())
        return result
    }
}