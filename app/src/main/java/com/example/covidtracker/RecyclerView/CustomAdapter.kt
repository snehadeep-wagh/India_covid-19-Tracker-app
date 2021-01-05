package com.example.covidtracker.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.ApiData.Statewise
import com.example.covidtracker.DataForState.StateData
import com.example.covidtracker.R

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    var stateDataList = mutableListOf<Statewise>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val prevconf: TextView? = itemView.findViewById(R.id.PrevTotalCon)
        val prevrec: TextView? = itemView.findViewById(R.id.PrevTotalRec)
        val prevdeath: TextView? = itemView.findViewById(R.id.PrevTotalDec)
        val prevstatename: TextView? = itemView.findViewById(R.id.PrevStateName)
        val image: ImageView = itemView.findViewById(R.id.imageId)
        val previewId: LinearLayout = itemView.findViewById(R.id.previewLayoutId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_preview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPos = stateDataList[position]
        holder.prevstatename!!.text = currentPos.state
        holder.prevconf!!.text = currentPos.deltaconfirmed
        holder.prevrec!!.text = currentPos.deltarecovered
        holder.prevdeath!!.text = currentPos.deltadeaths
        holder.image.setImageResource(StateData().stateCode[currentPos.statecode] as Int)

        if(position%2 == 1)
        {
            holder.previewId.setBackgroundResource(R.drawable.box_design_listview1)
        }
    }

    override fun getItemCount(): Int {
        return stateDataList.size
    }


}