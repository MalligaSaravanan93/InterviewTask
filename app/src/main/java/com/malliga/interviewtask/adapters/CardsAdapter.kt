package com.malliga.interviewtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.malliga.interviewtask.model.CardItem
import com.malliga.interviewtask.R
import com.malliga.interviewtask.databinding.CardsItemBinding
import com.malliga.interviewtask.databinding.LoadingItemBinding

class CardsAdapter(
    private val listOfCards: MutableList<CardItem> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((CardItem) -> Unit)? = null

    var onLoadMore: ((Int) -> Unit)? = null

    private val item: Int = 0
    private val loading: Int = 1
    private var errorMsg: String? = ""
    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
          if(viewType == item){
              return CardViewHolder(CardsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }else{
              return LoadingViewHolder(LoadingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == listOfCards.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = listOfCards[position]
        if(getItemViewType(position) == item){
            val cardViewHolder: CardViewHolder = holder as CardViewHolder
            cardViewHolder.setInformationToTheViewHolder(model)
            cardViewHolder.itemView.setOnClickListener{
                onItemClick?.invoke(listOfCards[position])
            }
        }else{
            val loadingVH: LoadingViewHolder = holder as LoadingViewHolder
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.VISIBLE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE

                if(errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.text = errorMsg
                else loadingVH.itemRowBinding.loadmoreErrortxt.text = loadingVH.itemRowBinding.root.context.getString(R.string.error_msg_unknown)

            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.GONE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }

            loadingVH.itemRowBinding.loadmoreRetry.setOnClickListener{
                showRetry(false, "")
                onLoadMore?.invoke(position)
            }
            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener{
                showRetry(false, "")
                onLoadMore?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = listOfCards.size

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(listOfCards.size - 1)
        this.errorMsg = errorMsg
    }

    fun addAll(movies: MutableList<CardItem>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(cardItem: CardItem) {
        listOfCards.add(cardItem)
        notifyItemInserted(listOfCards.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(CardItem("", listOf(), listOf(), listOf(),"",0,"","",""))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position: Int =listOfCards.size -1
        if(position>-1) {
            val movie: CardItem = listOfCards[position]

            if (movie != null) {
                listOfCards.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
    class LoadingViewHolder(binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: LoadingItemBinding = binding
    }
    class CardViewHolder(itemView: CardsItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val cardName: TextView = itemView.cardName
        val cardType: TextView = itemView.cardType
        val cardDescription: TextView = itemView.descriptionCard
        val cardImage: ImageView = itemView.cardImage

        fun setInformationToTheViewHolder(cardItem: CardItem) {
            cardName.text = cardItem.name
            cardDescription.text = cardItem.desc
            cardType.text = cardItem.type

            Glide.with(itemView)
                .load(cardItem.cardImages[0].imageUrl)
                .placeholder(R.drawable.load_image)
                .error(R.drawable.image_error)
                .fallback(R.drawable.no_image_24)
                .into(cardImage)
        }
    }
}




