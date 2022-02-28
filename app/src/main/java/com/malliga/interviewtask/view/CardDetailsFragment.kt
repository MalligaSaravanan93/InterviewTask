package com.malliga.interviewtask.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import com.malliga.interviewtask.R
import com.malliga.interviewtask.databinding.FragmentCardDetailsBinding
import com.malliga.interviewtask.model.CardItem
import com.malliga.interviewtask.model.CardPrice

const val ARG_CARDITEM = "cardditem"

class CardDetails : Fragment() {

    private val binding by lazy {
        FragmentCardDetailsBinding.inflate(layoutInflater)
    }
    private var cardditem: CardItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardditem = it.getSerializable(ARG_CARDITEM) as CardItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        cardditem?.let {cardItem->
            binding.cardName.text = cardItem.name
            binding.descriptionCard.text = cardItem.desc
            binding.cardType.text = cardItem.type
            if(cardItem.cardPrices.size>0){
                val cardPrices=cardItem.cardPrices[0];
                binding.amazonPrice.text="\$"+cardPrices.amazonPrice
                binding.cardmarketPrice.text="\$"+cardPrices.cardmarketPrice
                binding.coolstuffincPrice.text="\$"+cardPrices.coolstuffincPrice
                binding.ebayPrice.text="\$"+cardPrices.ebayPrice
                binding.tcgplayerPrice.text="\$"+cardPrices.tcgplayerPrice
            }

            Glide.with(binding.root)
                .load(cardItem.cardImages[0].imageUrl)
                .placeholder(R.drawable.load_image)
                .error(R.drawable.image_error)
                .fallback(R.drawable.no_image_24)
                .into(binding.cardImage)
        }

        return binding.root
    }
}