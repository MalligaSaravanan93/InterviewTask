package com.malliga.interviewtask.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.malliga.interviewtask.R
import com.malliga.interviewtask.adapters.CardsAdapter
import com.malliga.interviewtask.databinding.FragmentCardsBinding
import com.malliga.interviewtask.utils.InternetAvailability
import com.malliga.interviewtask.utils.PaginationScrollListener

import com.malliga.interviewtask.utils.UIState
import com.malliga.interviewtask.viewmodel.CardsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeoutException

private const val TAG = "CardsFragment"
class CardsFragment : Fragment() {
    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart
    private val binding by lazy {
        FragmentCardsBinding.inflate(layoutInflater)
    }

    private val cardsViewModel: CardsViewModel by viewModel()

    private lateinit var cardsAdapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardsAdapter = CardsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.cardsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cardsAdapter
        }

        cardsAdapter.onItemClick={carditem->
            val action = CardsFragmentDirections.actionInterviewtaskListFragmentToInterviewtaskDetailFragment()
            val bundle=Bundle().apply{
                putSerializable(ARG_CARDITEM,carditem)
            }
            findNavController().navigate(R.id.action_interviewtaskListFragment_to_interviewtaskDetailFragment,bundle)
        }

        binding.cardsRecycler.addOnScrollListener(object : PaginationScrollListener(binding.cardsRecycler.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                Handler(Looper.myLooper()!!).postDelayed({
                    loadNextPage()
                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        cardsViewModel.cardsLivaData.observe(viewLifecycleOwner, ::handleCards)

        cardsViewModel.subscribeToCardsInfo(currentPage)

        return binding.root
    }

    private fun handleCards(uiState: UIState) {
        when(uiState) {
            is UIState.LOADING -> {
                binding.cardsRecycler.visibility = View.GONE
                binding.mainProgress.visibility = View.VISIBLE
                hideErrorView()
            }
            is UIState.SUCCESS -> {
                hideErrorView()
                binding.cardsRecycler.visibility = View.VISIBLE
                cardsAdapter.removeLoadingFooter()
                isLoading = false
                binding.mainProgress.visibility = View.GONE
                totalPages = uiState.success.meta.totalPages
                cardsAdapter.addAll(uiState.success.data)

                if (currentPage != totalPages) cardsAdapter.addLoadingFooter()
                else isLastPage = true
            }
            is UIState.ERROR -> {
                binding.mainProgress.visibility = View.GONE
                binding.cardsRecycler.visibility = View.GONE
                showErrorView(uiState.error)
                Toast.makeText(requireContext(), "Please retry again!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loadNextPage() {
        if (InternetAvailability.isConnected(requireContext())) {
            cardsViewModel.subscribeToCardsInfo(currentPage)
        }else{
            cardsAdapter.showRetry(true, fetchErrorMessage(null))
        }
    }

    private fun showErrorView(throwable: Throwable?) {
        if (binding.lyError.errorLayout.visibility == View.GONE) {
            binding.lyError.errorLayout.visibility = View.VISIBLE
            binding.mainProgress.visibility = View.GONE

            if (!InternetAvailability.isConnected(requireContext())) {
                binding.lyError.errorTxtCause.setText(R.string.error_msg_no_internet)
            } else {
                if (throwable is TimeoutException) {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_timeout)
                } else {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_unknown)
                }
            }
        }
    }

    private fun hideErrorView() {
        if (binding.lyError.errorLayout.visibility == View.VISIBLE) {
            binding.lyError.errorLayout.visibility = View.GONE
            binding.mainProgress.visibility = View.VISIBLE
        }
    }

    private fun fetchErrorMessage(throwable: Throwable?): String {
        var errorMsg: String = resources.getString(R.string.error_msg_unknown)

        if (!InternetAvailability.isConnected(requireContext())) {
            errorMsg = resources.getString(R.string.error_msg_no_internet)
        } else if (throwable is TimeoutException) {
            errorMsg = resources.getString(R.string.error_msg_timeout)
        }

        return errorMsg
    }

}