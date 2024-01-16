package com.example.sudoku9x9.ui.board

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.sudoku9x9.R

class SudokuBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val boardWidth = 9
    private val cellGroupWidth = 3
    private var cellSize = 0F

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








    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas?) {
        cellSize = (width / boardWidth).toFloat()

        drawField(canvas)
        drawGrid(canvas)


        /*cellSize = (width / amountCellsInRow).toFloat()

        fillCell2(canvas)
        drawLines(canvas)
        drawText(canvas)
        if(numErrors>2){
            Log.e("dfgg","1")
            finishListener?.onFinish(openedCells,numErrors,false)
            reset()
        }
        if(openedCells == HIDE_CELLS){
            Log.e("dfgg","2")
            finishListener?.onFinish(openedCells,numErrors,true)
            reset()
        }*/
    }











    private fun drawField(canvas: Canvas?) {
        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), boardField)
        width           // фунция класса родительского класа View, которая возвращает ширину нашего View
        height          // фунция класса родительского класа View, которая возвращает высоту нашего View
        boardField      // экземпляр класса Paint, в котором мы задаем цвет,стиль и другие свойства будущего рисунка
    }

    private fun drawGrid(canvas: Canvas?) {
        // Рисуем вертикальные линии
        for (i in 0..boardWidth) {
            // Если остаток после деления равен 0(в нашем случае это будет происходить каждый третий раз)
            if (i % cellGroupWidth == 0) {
                canvas?.drawLine(cellSize * i, 0F, cellSize * i, height.toFloat(), boardBoldLine)
            } else {
                canvas?.drawLine(cellSize * i, 0F, cellSize * i, height.toFloat(), boardThinLine)
            }
        }
        // Рисуем горизонтальные линии
        for (i in 0..boardWidth) {
            if (i % cellGroupWidth == 0) {
                canvas?.drawLine(0F, cellSize * i, width.toFloat(), cellSize * i, boardBoldLine)
            } else {
                canvas?.drawLine(0F, cellSize * i, width.toFloat(), cellSize * i, boardThinLine)
            }
        }
    }

}