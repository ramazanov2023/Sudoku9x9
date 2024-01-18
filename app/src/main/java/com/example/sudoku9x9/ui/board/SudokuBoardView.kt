package com.example.sudoku9x9.ui.board

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.sudoku9x9.R


class SudokuBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var indexSelectedCell: Int = 0
    private var cellVertical: Int = -1
    private var cellHorizontal: Int = -1
    private val boardWidth = 9
    private val cellGroupWidth = 3
    private var cellSize = 0F
    private var sudokuNumbers: List<Cell>? = null


    private val boardField = Paint().apply {
        color = resources.getColor(R.color.white_2)
        style = Paint.Style.FILL
    }
    private val boardBoldLine = Paint().apply {
        color = resources.getColor(R.color.gray_1)
        style = Paint.Style.STROKE
        strokeWidth = 4F
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


    fun insertSudokuNumbers(numbers: List<Cell>) {
        sudokuNumbers = numbers
    }

    fun checkInputNumber(number: Int?) {
        number?.let {
            val cell = sudokuNumbers!![indexSelectedCell]
            if (cell.wrong) {
                insertNumber(cell, number)
            } else {
                if (cell.hide) {
                    insertNumber(cell, number)
                } else {

                }
            }
        }
    }

    private fun insertNumber(cell: Cell, number: Int) {
        if (cell.value == number) {
            sudokuNumbers!![indexSelectedCell].wrong = false
        } else {
            sudokuNumbers!![indexSelectedCell].wrong = true
            sudokuNumbers!![indexSelectedCell].wrong_number = number
        }
        sudokuNumbers!![indexSelectedCell].hide = false
        invalidate()
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

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return if (event?.action == MotionEvent.ACTION_DOWN) {
            cellVertical = (event.y / cellSize).toInt()
            cellHorizontal = (event.x / cellSize).toInt()
            indexSelectedCell = ((cellVertical + 1) * 9) - (9 - cellHorizontal)
            invalidate()
            true
        } else false
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

        sudokuNumbers?.let { numbers ->
            val cell = numbers[indexSelectedCell]
            numbers.forEach {
                if (it.id == indexSelectedCell) {
                    drawCell(canvas, it, boardSelectCell)
                } else if (it.value == cell.value && !it.hide && !cell.hide) {
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


}