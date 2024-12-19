package com.example.projemanag.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projemanag.databinding.ItemBoardBinding
import com.example.projemanag.models.Board

open class BoardItemsAdapter(
    private var list: ArrayList<Board>
) : RecyclerView.Adapter<BoardItemsAdapter.BoardViewHolder>() {

    // Propiedad para el listener de clics
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        // Infla el layout y crea el ViewHolder
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        // Obtiene el modelo actual y lo pasa al ViewHolder
        val model = list[position]
        holder.bind(model, position)
    }

    override fun getItemCount(): Int {
        // Retorna el tamaño de la lista
        return list.size
    }

    // Método para configurar el listener desde la actividad o fragmento
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // ViewHolder interno
    inner class BoardViewHolder(private val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board, position: Int) {
            // Asigna los valores a las vistas
            binding.tvName.text = board.name
            binding.tvCreatedBy.text = "Created by: ${board.createdBy}"

            // Carga la imagen usando Glide
            Glide.with(binding.root.context)
                .load(board.image)
                .centerCrop()
                .placeholder(com.example.projemanag.R.drawable.ic_board_place_holder)
                .into(binding.ivBoardImage)

            // Configura el clic en el elemento
            binding.root.setOnClickListener {
                onClickListener?.onClick(position, board)
            }
        }
    }

    // Interfaz para manejar clics
    interface OnClickListener {
        fun onClick(position: Int, model: Board)
    }
}
