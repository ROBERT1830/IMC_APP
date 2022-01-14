package com.robertconstantindinescu.myimc.util

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.content.Context

import com.robertconstantindinescu.myimc.MainActivity

import androidx.core.content.ContextCompat
import com.robertconstantindinescu.myimc.R

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


/**
 * clase empleada para hacer el gesto de deslizar y eliminar.
 *
 * se ha utilizado una libreria "Swipedecorator" para hacer el efecto rojo al arrastrar el hijo del recycler.
 * Se ha definido el color y el icono de eliminar.
 *
 * En onChildDraw es donde se utilizará ese icono y color para dibujarlos a medida que se va arrastrando.
 */
abstract class SwipeGesture(context: Context):ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    val deleteColor = ContextCompat.getColor(context, R.color.deletecolor)
    val deleteIcon = R.drawable.ic_delete_forever

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}