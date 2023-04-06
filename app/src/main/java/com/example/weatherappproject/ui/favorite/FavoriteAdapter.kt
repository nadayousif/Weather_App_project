package com.example.weatherappproject.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappproject.databinding.ItemFavoriteBinding
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.util.ConnectionUtils.checkConnection


class FavoriteAdapter(
    private var list: List<FavoriteAddress> ,val onClick: OnClick) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(){
    lateinit var context: Context
    lateinit var binding: ItemFavoriteBinding

    class ViewHolder(var binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemFavoriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = list.get(position)



        holder.binding.des.text = currentObj.currentTemp.toString()
        holder.binding.countryFav.text = currentObj.address

        holder.binding.delete.setOnClickListener {
            onClick.onDeleteClickFavorites(currentObj)
            notifyDataSetChanged()
        }

        var lat =currentObj.latitude
        var lon =currentObj.longitude


        holder.binding.favoriteCard.setOnClickListener {
            if (checkConnection()){
            onClick.sendData(lat,lon)
            }
            else{
                Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT)
            }
        }


    }
    fun setList(favorite:List<FavoriteAddress>){
        list=favorite
        notifyDataSetChanged()
    }

}