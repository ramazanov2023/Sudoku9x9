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

const val INACTIVE_NUMBER = 1
const val USER_MISTAKES = 2
const val REMAIN_NUMBERS_DECREASE = 4
const val REMAIN_NUMBERS_INCREASE = 5
const val GAME_END = 3
const val SELECT_NUMBER_BUTTON = 6
const val NO_NUMBER_BUTTON = 10

class SudokuBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var selectCellsByButton: Int? = null
    private var indexSelectedCell: Int = 0
    private var lastSavedNumber: Int? = null
    private var cellVertical: Int = -1
    private var cellHorizontal: Int = -1
    private val boardWidth = 9
    private val cellGroupWidth = 3
    private var cellSize = 0F
    private var sudokuNumbers: List<Cell>? = null
    private var remainNumbers: List<Int>? = null
    private var listener: SudokuListener? = null
    private var level = 1
    private var userMistakes = 0
    private var userOpenedNumbers = 0
    private val lastFilledCells = Stack<Int>()
    private var speedGameMode = false

    private var animSelectCells:ValueAnimator = ValueAnimator()
    private var newAnim:ValueAnimator = ValueAnimator()


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
        textSize = 82F
    }
    private val boardWrongNumber = Paint().apply {
        color = resources.getColor(R.color.red_1)
        style = Paint.Style.FILL
        textSize = 82F
    }


    fun setListener(listener: SudokuListener) {
        this.listener = listener
    }

    fun setLevel(gameLevelId: Int) {
        level = when (gameLevelId) {
            1 -> FAST_LEVEL
            2 -> LIGHT_LEVEL
            3 -> HARD_LEVEL
            4 -> MASTER_LEVEL
            else -> FAST_LEVEL
        }
    }

    fun setSpeedMode(mode: Boolean) {
        speedGameMode = mode
//        selectLinkCells()
    }

    init {
        setAnimation()
    }

    private fun setAnimation(){
        val startColor = resources.getColor(R.color.white_2)
        val middleColor = resources.getColor(R.color.blue_2_1)
        val endColor = resources.getColor(R.color.blue_2)
        newAnim = ValueAnimator.ofArgb(middleColor,endColor)
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

    fun insertSudokuNumbers(numbers: List<Cell>) {
        sudokuNumbers = numbers
    }
    fun insertRemainNumbers(remainNumbers: List<Int>?) {
        this.remainNumbers = remainNumbers
    }

    fun checkInputNumber(number: Int?) {
        number?.let {
            val selectedCell = sudokuNumbers!![indexSelectedCell]
            if (selectedCell.wrong) {
                insertNumber(selectedCell, it)
            } else {
                if (selectedCell.hide) {
                    insertNumber(selectedCell, it)
                } else {
                    selectCellsByButton = it
                    lastSavedNumber = it
                    listener?.action(SELECT_NUMBER_BUTTON, it)
                    Log.e("nnnn","1.4  selectCellsByButton-$selectCellsByButton")
                    newAnim.start()
                }
            }
        }
    }

    private fun insertNumber(selectedCell: Cell, number: Int) {
        sudokuNumbers!![indexSelectedCell].hide = false
        if (selectedCell.value == number) {
            sudokuNumbers!![indexSelectedCell].wrong = false
            makeNumberInactive(REMAIN_NUMBERS_DECREASE, number)
            listener?.action(SELECT_NUMBER_BUTTON, number)
            userOpenedNumbers++
        } else {
            addUserMistake()
            sudokuNumbers!![indexSelectedCell].wrong = true
            sudokuNumbers!![indexSelectedCell].wrong_number = number
            listener?.action(SELECT_NUMBER_BUTTON, NO_NUMBER_BUTTON)
        }
//        sudokuNumbers!![indexSelectedCell].hide = false
        lastFilledCells.push(indexSelectedCell)
        invalidate()
//        selectLinkCells()
    }

    fun undo() {
        if (lastFilledCells.empty()) return
        sudokuNumbers?.let {
            val cellIndex = lastFilledCells.pop()
            if (sudokuNumbers!![cellIndex].wrong) {

            } else {
                userOpenedNumbers--
                makeNumberInactive(REMAIN_NUMBERS_INCREASE, sudokuNumbers!![cellIndex].value)
            }
            sudokuNumbers!![cellIndex].wrong = false
            sudokuNumbers!![cellIndex].wrong_number = 0
            sudokuNumbers!![cellIndex].hide = true
            if(it[indexSelectedCell].hide) lastSavedNumber = null
        }
        invalidate()
    }

    private fun addUserMistake() {
        userMistakes++
        listener?.action(USER_MISTAKES, userMistakes)
    }

    private fun makeNumberInactive(action: Int, number: Int) {
        var count = 0
        sudokuNumbers?.forEach {
            if (it.value == number && !it.hide) {
                Log.e("yyyy", "2 count-$count")
                count++
            }
        }
        Log.e("yyyy", "1 number-$number")

        if(count==9) lastSavedNumber = null

        // 9 - count и передаем значение слушателю
        listener?.action(action, number - 1)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas?) {
        cellSize = (width / boardWidth).toFloat()

        drawField(canvas)
        drawSelectCell(canvas)
        drawGrid(canvas)
        drawNumbers(canvas)

        if (userMistakes > 2) {
            listener?.action(GAME_END, userMistakes)
        }
        if (userOpenedNumbers == level) {
            listener?.action(GAME_END, userMistakes)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return if (event?.action == MotionEvent.ACTION_DOWN) {
            cellVertical = (event.y / cellSize).toInt()
            cellHorizontal = (event.x / cellSize).toInt()
            indexSelectedCell = ((cellVertical + 1) * 9) - (9 - cellHorizontal)
//            checkGameMode()
            selectCellsByButton = null
            selectLinkCells()
//            animSelectCells.start()
//            newAnim.start()
//            invalidate()
            true
        } else false
    }

    private fun selectLinkCells(){
        sudokuNumbers?.let {
            val cell = it[indexSelectedCell]
            if (!cell.wrong && !cell.hide && remainNumbers!![cell.value-1]!=0) {
                lastSavedNumber = cell.value
                listener?.action(SELECT_NUMBER_BUTTON, lastSavedNumber ?:1)
                Log.e("nnnn","1.1  lastSavedNumber-$lastSavedNumber")
                newAnim.start()
            } else if(remainNumbers!![cell.value-1]==0){
                lastSavedNumber = null
                listener?.action(SELECT_NUMBER_BUTTON, NO_NUMBER_BUTTON)
                Log.e("nnnn","1.2  lastSavedNumber-$lastSavedNumber")
                newAnim.start()
            } else {
                Log.e("nnnn","1.3  lastSavedNumber-$lastSavedNumber")
                listener?.action(SELECT_NUMBER_BUTTON, NO_NUMBER_BUTTON)
                if (speedGameMode) {
                    lastSavedNumber?.let { checkInputNumber(lastSavedNumber) }
                }
                invalidate()
            }
        }
    }


    private fun drawNumbers(canvas: Canvas?) {
        sudokuNumbers?.forEach {
            if (!it.hide) {
                val textBounds = Rect()
                var valueCell: String?
                var num: Paint?

                if (it.wrong) {
                    valueCell = it.wrong_number.toString()
                    num = boardWrongNumber
                } else {
                    valueCell = it.value.toString()
                    num = boardNumbers
                }
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

    }

    private fun drawSelectCell(canvas: Canvas?) {
        if (cellHorizontal == -1) return

        selectCellsByButton?.let{number ->
            sudokuNumbers?.forEach {if (it.value == number && !it.hide && !it.wrong) {
                    drawCell(canvas, it, boardSelectCell)
                }
            }
            return
        }

        sudokuNumbers?.let { numbers ->
            val cell = numbers[indexSelectedCell]
            numbers.forEach {
                if (it.id == indexSelectedCell) {
                    drawCell(canvas, it, boardSelectCell)
                } else if (it.value == cell.value && !it.hide && !cell.hide && !cell.wrong && !it.wrong) {
                    drawCell(canvas, it, boardSelectCell)
                } else if (it.ver == cellVertical || it.hor == cellHorizontal) {
                    drawCell(canvas, it, boardLinkedCells)
                } else if (cellVertical / 3 == it.ver / 3 && cellHorizontal / 3 == it.hor / 3) {
                    drawCell(canvas, it, boardLinkedCells)
                }
            }
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
        fun action(id: Int, value: Int)
    }

}