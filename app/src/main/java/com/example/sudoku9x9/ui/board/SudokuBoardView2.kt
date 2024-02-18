package com.example.sudoku9x9.ui.board

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.sudoku9x9.R
import java.util.*

class SudokuBoard(val level: Int = 1) {
    var selectCellsByButton: Int? = null
    var indexSelectedCell: Int = -1
    var lastSavedNumber: Int? = null
    var cellVertical: Int = -1
    var cellHorizontal: Int = -1
    val boardWidth = 9
    val cellGroupWidth = 3
    var cellSize = 0F
    var sudokuNumbers: List<Cell>? = null
    var remainNumbers: List<Int>? = null
    var userMistakes = 0
    var userOpenedNumbers = 0
    val lastFilledCells = Stack<Int>()
    var speedGameMode = false
}


class SudokuBoardView2(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var board: SudokuBoard = SudokuBoard(1)

    private val boardWidth = board.boardWidth
    private val cellGroupWidth = board.cellGroupWidth
    private var cellSize = board.cellSize
    private var listener: SudokuListener? = null
    private var animSelectCells: ValueAnimator = ValueAnimator()
    private var newAnim: ValueAnimator = ValueAnimator()


    private val boardField = Paint().apply {
        color = resources.getColor(R.color.white_2)
        style = Paint.Style.FILL
    }
    private val boardBoldLine = Paint().apply {
        color = resources.getColor(R.color.gray_1)
        style = Paint.Style.STROKE
        strokeWidth = 6F
    }
    private val boardThinLine = Paint().apply {
        color = resources.getColor(R.color.gray_5)
        style = Paint.Style.STROKE
        strokeWidth = 2F
    }
    private val boardSelectCell = Paint().apply {
        color = resources.getColor(R.color.blue_2)
        style = Paint.Style.FILL
    }
    private val boardLinkedCells = Paint().apply {
        color = resources.getColor(R.color.blue_3)
        style = Paint.Style.FILL
    }
    private val boardNumbers = Paint().apply {
        color = resources.getColor(R.color.gray_3)
        style = Paint.Style.FILL
        textSize = 90F
    }
    private val boardWrongNumber = Paint().apply {
        color = resources.getColor(R.color.red_1)
        style = Paint.Style.FILL
        textSize = 90F
    }


    fun setSudokuBoard(board: SudokuBoard, listener: SudokuListener) {
        this.board = board
        this.listener = listener
    }


    fun setSpeedMode(mode: Boolean) {
        board.speedGameMode = mode
//        selectLinkCells()
    }

    init {
        setAnimation()
    }

    private fun setAnimation() {
        val startColor = resources.getColor(R.color.white_2)
        val middleColor = resources.getColor(R.color.blue_2_1)
        val endColor = resources.getColor(R.color.blue_2)
        newAnim = ValueAnimator.ofArgb(middleColor, endColor)
        newAnim.duration = 200
        newAnim.addUpdateListener {
            boardSelectCell.color = it.animatedValue as Int
            postInvalidateOnAnimation()
        }
        /*animSelectCells.apply {
            duration = 1000
            setEvaluator(ArgbEvaluator())
            setIntValues(startColor,endColor)
            interpolator = LinearInterpolator()
            addUpdateListener {
                boardSelectCell.color = animatedValue as Int
                postInvalidateOnAnimation()
            }
        }*/

    }


//    fun insertSudokuNumbers(numbers: List<Cell>) {
//        sudokuNumbers = numbers
//    }

    fun checkInputNumber(number: Int?) {
        board.sudokuNumbers ?: return
        number?.let {
            // Если индекс меньше нуля, значит ниодна клетка не выделена нажатием на клетку
            // Если индекс равен или больше нуля, значит клетка была нажата на доске
            if (board.indexSelectedCell < 0) {
                board.indexSelectedCell = -2
                board.selectCellsByButton = it
                board.lastSavedNumber = it
                Log.e("nnnn", "1.5  selectCellsByButton-${board.selectCellsByButton}")
                newAnim.start()
                return
            }
            val selectedCell = board.sudokuNumbers!![board.indexSelectedCell]
            if (selectedCell.wrong) {
                insertNumber(selectedCell, it)
            } else {
                if (selectedCell.hide) {
                    insertNumber(selectedCell, it)
                } else {
                    board.selectCellsByButton = it
                    board.lastSavedNumber = it
                    listener?.onSelectNumberCell(it)
                    Log.e("nnnn", "1.4  selectCellsByButton-${board.selectCellsByButton}")
                    newAnim.start()
                }
            }
        }
    }

    private fun insertNumber(selectedCell: Cell, number: Int) {
        board.sudokuNumbers!![board.indexSelectedCell].hide = false
        if (selectedCell.value == number) {
            board.sudokuNumbers!![board.indexSelectedCell].wrong = false
            if (board.speedGameMode) board.lastSavedNumber = number
            changeRemainedNumbers(REMAIN_NUMBERS_DECREASE, number)
            listener?.onInputRightNumber(board.remainNumbers!!, number)
            listener?.onSelectNumberCell(number)
            board.userOpenedNumbers++
        } else {
            addUserMistake()
            board.sudokuNumbers!![board.indexSelectedCell].wrong = true
            board.sudokuNumbers!![board.indexSelectedCell].wrong_number = number
            listener?.onInputWrongNumber(board.userMistakes)
//            runVibrator()
        }
//        sudokuNumbers!![indexSelectedCell].hide = false
        board.lastFilledCells.push(board.indexSelectedCell)
        invalidate()
//        selectLinkCells()
    }

    fun undo() {
        board.sudokuNumbers ?: return
        if (board.lastFilledCells.empty()) return
        val cellIndex = board.lastFilledCells.pop()
        if (board.sudokuNumbers!![cellIndex].wrong) {
            board.sudokuNumbers!![cellIndex].wrong = false
            board.sudokuNumbers!![cellIndex].wrong_number = 0
            board.sudokuNumbers!![cellIndex].hide = true
        } else {
//            board.userOpenedNumbers--
//            changeRemainedNumbers(REMAIN_NUMBERS_INCREASE, board.sudokuNumbers!![cellIndex].value)
        }
        if (board.sudokuNumbers!![board.indexSelectedCell].hide) board.lastSavedNumber = null

        invalidate()
    }

    private fun addUserMistake() {
        board.userMistakes++
        listener?.onInputWrongNumber(board.userMistakes)
    }

    private fun changeRemainedNumbers(action: Int, number: Int) {
        val list = board.remainNumbers!!.toMutableList()
        val newValue = when (action) {
            REMAIN_NUMBERS_DECREASE -> list[number - 1] - 1
            REMAIN_NUMBERS_INCREASE -> list[number - 1] + 1
            else -> list[number - 1]
        }
        list[number - 1] = newValue
        board.remainNumbers = list.toList()

        if (newValue == 9) board.lastSavedNumber = null


        listener?.onInputRightNumber(board.remainNumbers!!, number)


        // 9 - count и передаем значение слушателю
//        listener?.action(action, number - 1)
//        listener?.onCancelUserStep(9 - newValue, number - 1)

    }

    private fun changeRemainedNumbers2(action: Int, number: Int) {
        var count = 0
        board.sudokuNumbers!!.forEach {
            if (it.value == number && !it.hide) {
                Log.e("yyyy", "2 count-$count")
                count++
            }
        }
        Log.e("yyyy", "1 number-$number")

        if (count == 9) board.lastSavedNumber = null

        // 9 - count и передаем значение слушателю
        listener?.onCancelUserStep(9 - count, number - 1)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas?) {
        cellSize = (width / boardWidth).toFloat()

        drawField(canvas)
        drawAllCells(canvas)
        drawGrid(canvas)

        Log.e("vvvv", "0  -  ")

        if (board.userMistakes > 2) {
            listener?.onGameEnd(board.userMistakes)
        }
        if (board.userOpenedNumbers == board.level) {
            listener?.onGameEnd(board.userMistakes)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return if (event?.action == MotionEvent.ACTION_DOWN && board.sudokuNumbers != null) {
            board.cellVertical = (event.y / cellSize).toInt()
            board.cellHorizontal = (event.x / cellSize).toInt()
            board.indexSelectedCell = ((board.cellVertical + 1) * 9) - (9 - board.cellHorizontal)

            // Сообщаем методу drawAllCells(canvas), что была нажата клетка на доске, а не кнопка снаружи
            board.selectCellsByButton = null

            selectLinkCells()
//            invalidate()
            true
        } else false
    }

    private fun selectLinkCells() {
        val cell = board.sudokuNumbers!![board.indexSelectedCell]
        if (!cell.wrong && !cell.hide && board.remainNumbers!![cell.value - 1] != 0) {
            board.lastSavedNumber = cell.value
            listener?.onSelectNumberCell(board.lastSavedNumber ?: 1)
            Log.e("nnnn", "1.1  lastSavedNumber-${board.lastSavedNumber}")
            newAnim.start()
        } else if (board.remainNumbers!![cell.value - 1] == 0) {
            board.lastSavedNumber = null
            listener?.onSelectCompleteValueCells(board.remainNumbers!!, cell.value)
            Log.e("nnnn", "1.2  lastSavedNumber-${board.lastSavedNumber}")
            newAnim.start()
        } else {
            Log.e("nnnn", "1.3  lastSavedNumber-${board.lastSavedNumber}")
            listener?.onSelectEmptyCell(cell.id)
            if (board.speedGameMode) {
                board.lastSavedNumber?.let { checkInputNumber(board.lastSavedNumber) }
            }
            invalidate()
        }

    }


    private fun drawAllCells(canvas: Canvas?) {
        board.sudokuNumbers ?: return

        // Пробегаемся по всему спику цифр
        // Но сперва проверяем была ли нажата клетка на доске или кнопка с цифрой снаружи
        // Какие клетки выделены, какие нет
        // Какие цифры видны, какие спрятаны

        // Если indexSelectedCell == -1, значит ни одна клетка не была нажата пользователем внутри доски
        // Поэтому мы рисуем тлько цифры
        if (board.indexSelectedCell == -1) {
            board.sudokuNumbers!!.forEach {
                drawNumber(it, canvas)
            }
            return
        }
        // Если selectCellsByButton не равен null значит цифра пришла снаружи через кнопку
        board.selectCellsByButton?.let { number ->
            board.sudokuNumbers!!.forEach {
                // Здесь мы не проверяем выделенную клетку, нам достаточно одного значения, которое мы получаем снаружи это view
                if (it.value == number && !it.hide && !it.wrong) {
                    drawCell(canvas, it, boardSelectCell)
                }
                drawNumber(it, canvas)
            }
            // И здесь мы выходим из метода drawAllCells, он нам больше не нужен
            return
        }

        // Если selectCellsByButton равен null, значит была нажата клетка на доске
        val cell = board.sudokuNumbers!![board.indexSelectedCell]
        board.sudokuNumbers!!.forEach {
            drawSelectedCell(cell, it, canvas)
            drawNumber(it, canvas)
        }

    }

    private fun drawSelectedCell(cell: Cell, it: Cell, canvas: Canvas?) {
        // Если id клетки равен индексу выделенной клетки
        if (it.id == board.indexSelectedCell) {
            drawCell(canvas, it, boardSelectCell)
        }
        // Если значение клетки равно значению выделенной клетки
        // Клетка и выделенная клетка видны
        // Клетка и выделенная клетка не имеют ошибок
        else if (it.value == cell.value && !it.hide && !cell.hide && !cell.wrong && !it.wrong) {
            drawCell(canvas, it, boardSelectCell)
        } else if (it.ver == board.cellVertical || it.hor == board.cellHorizontal) {
            drawCell(canvas, it, boardLinkedCells)
        } else if (board.cellVertical / 3 == it.ver / 3 && board.cellHorizontal / 3 == it.hor / 3) {
            drawCell(canvas, it, boardLinkedCells)
        }
    }

    private fun drawNumber(it: Cell, canvas: Canvas?) {
        // Если цифра видна(отображается)
        if (!it.hide) {
            // Создаем объект Rect(), чтобы потом вычислить высоту символа
            val textBounds = Rect()
            // Значение цифры, если правильная, то мы достаем её из value, если нет, то из wrong_number
            val valueCell: String?
            // Цвет цифры, если правильная цифра то синий, если нет то красный
            val num: Paint?

            // Проверяем правильная цифра или нет
            if (it.wrong) {
                valueCell = it.wrong_number.toString()
                num = boardWrongNumber
            } else {
                valueCell = it.value.toString()
                num = boardNumbers
            }

            // Вычисляем высоту и ширину символа, чтобы потом разместить цифру точно в центре клетки
            num.getTextBounds(valueCell, 0, valueCell.length, textBounds)
            val numberWidth = num.measureText(valueCell)
            val numberHeight = textBounds.height()

            canvas?.drawText(
                valueCell,
                it.hor * cellSize + cellSize / 2 - numberWidth / 2,
                it.ver * cellSize + cellSize / 2 + numberHeight / 2,
                num
            )
        }
    }

    private fun drawCell(canvas: Canvas?, it: Cell, paint: Paint) {
        canvas?.drawRect(
            it.hor * cellSize,
            it.ver * cellSize,
            (it.hor + 1) * cellSize,
            (it.ver + 1) * cellSize,
            paint
        )
    }

    private fun drawField(canvas: Canvas?) {
        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), boardField)
        width           // фунция класса родительского класа View, которая возвращает ширину нашего View
        height          // фунция класса родительского класа View, которая возвращает высоту нашего View
        boardField      // экземпляр класса Paint, в котором мы задаем цвет,стиль и другие свойства будущего рисунка
    }

    private fun drawGrid(canvas: Canvas?) {

        for (i in 0..boardWidth) {
            // Если остаток после деления не равен 0
            if (i % cellGroupWidth != 0) {
                // Рисуем вертикальные линии
                canvas?.drawLine(cellSize * i, 0F, cellSize * i, height.toFloat(), boardThinLine)
                // Рисуем горизонтальные линии
                canvas?.drawLine(0F, cellSize * i, width.toFloat(), cellSize * i, boardThinLine)
            }
        }

        for (i in 0..boardWidth) {
            // Если остаток после деления равен 0(в нашем случае это будет происходить каждый третий раз)
            if (i % cellGroupWidth == 0) {
                // Рисуем вертикальные линии
                canvas?.drawLine(cellSize * i, 0F, cellSize * i, height.toFloat(), boardBoldLine)
                // Рисуем горизонтальные линии
                canvas?.drawLine(0F, cellSize * i, width.toFloat(), cellSize * i, boardBoldLine)
            }
        }
    }


    interface SudokuListener {
        fun onInputWrongNumber(mistakes: Int)
        fun onGameEnd(mistakes: Int)
        fun onSelectNumberCell(number: Int)
        fun onInputRightNumber(remained: List<Int>, value: Int)
        fun onCancelUserStep(remained: Int, i: Int)
        fun onSelectEmptyCell(id: Int)
        fun onSelectCompleteValueCells(remained: List<Int>, id: Int)
    }


    // Нарисовать доску
    // Нарисовать цифры
    // Выделять(рисовать цветные) клетки
    // Анимировать выдиление цифр
    // Рисовать новые цифры
    // Анимировать заполнение одной из полос


}