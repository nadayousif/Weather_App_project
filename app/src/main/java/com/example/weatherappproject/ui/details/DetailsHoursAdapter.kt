package com.example.weatherappproject.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherappproject.databinding.ItemHoursBinding
import com.example.weatherappproject.model.Current
import java.text.SimpleDateFormat
import java.util.*



class DetailsHoursAdapter( var current: List<Current>) : RecyclerView.Adapter<DetailsHoursAdapter.ViewHolder>() {
    lateinit var _current: Context
    lateinit var binding: ItemHoursBinding
    class ViewHolder (var binding : ItemHoursBinding): RecyclerView.ViewHolder(binding.root) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _current=parent.context
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= ItemHoursBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  current.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = current.get(position)
        var timeHour = getCurrentTime(currentObj.dt.toInt())
        Glide.with(_current).load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png").into(holder.binding.image)
        holder.binding.tempItem.text = currentObj.temp.toString()
        holder.binding.hourText.text= timeHour
    }

    fun getCurrentTime(dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone= TimeZone.getDefault()
        return sdf.format(date)
    }

}