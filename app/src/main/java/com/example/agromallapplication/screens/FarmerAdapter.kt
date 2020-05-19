package com.example.agromallapplication.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.SingleFarmBinding
import com.example.agromallapplication.models.Farmer

class FarmerAdapter(var farmerArray:ArrayList<Farmer>, private val clickListener: (result:Farmer) -> Unit):
    RecyclerView.Adapter<FarmerAdapter.ViewHolder>() {


    class ViewHolder(var binding: SingleFarmBinding) :RecyclerView.ViewHolder(binding.root) {

        fun bind(item:Farmer, clickListener: (result: Farmer) -> Unit){
            binding.root.setOnClickListener {
                clickListener(item)
            }

            binding.farmer = item
            Glide.with(binding.root.context).asBitmap().error(R.drawable.human).load(item.farmerImage).into(binding.farmerImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = SingleFarmBinding.inflate(view)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return farmerArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(farmerArray[position],clickListener)
}