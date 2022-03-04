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
import com.google.gson.Gson
import com.malliga.interviewtask.R
import com.malliga.interviewtask.adapters.CardsAdapter
import com.malliga.interviewtask.databinding.FragmentCardsBinding
import com.malliga.interviewtask.model.CardData
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
        /******
         *  ! Important - If your want to perform UI test please uncomment the below three lines
         */
        //val outputstring="{\"data\":[{\"id\":67048711,\"name\":\"7\",\"type\":\"SpellCard\",\"desc\":\"Whenthereare3face-up\\\"7\\\"cardsonyoursideofthefield,draw3cardsfromyourDeck.Thendestroyall\\\"7\\\"cards.WhenthiscardissentdirectlyfromthefieldtoyourGraveyard,increaseyourLifePointsby700points.\",\"race\":\"Continuous\",\"card_sets\":[{\"set_name\":\"AncientSanctuary\",\"set_code\":\"AST-091\",\"set_rarity\":\"ShortPrint\",\"set_rarity_code\":\"(SP)\",\"set_price\":\"3.22\"},{\"set_name\":\"DarkRevelationVolume2\",\"set_code\":\"DR2-EN204\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"4.44\"}],\"card_images\":[{\"id\":67048711,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/67048711.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/67048711.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.17\",\"tcgplayer_price\":\"1.14\",\"ebay_price\":\"4.97\",\"amazon_price\":\"24.97\",\"coolstuffinc_price\":\"0.49\"}]},{\"id\":23771716,\"name\":\"7ColoredFish\",\"type\":\"NormalMonster\",\"desc\":\"Ararerainbowfishthathasneverbeencaughtbymortalman.\",\"atk\":1800,\"def\":800,\"level\":4,\"race\":\"Fish\",\"attribute\":\"WATER\",\"card_sets\":[{\"set_name\":\"GoldSeries\",\"set_code\":\"GLD1-EN001\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.62\"},{\"set_name\":\"MetalRaiders\",\"set_code\":\"MRD-098\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.62\"},{\"set_name\":\"MetalRaiders\",\"set_code\":\"MRD-E098\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"7.66\"},{\"set_name\":\"MetalRaiders\",\"set_code\":\"MRD-EN098\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"4.9\"},{\"set_name\":\"StarterDeck:Joey\",\"set_code\":\"SDJ-008\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.46\"},{\"set_name\":\"StructureDeck:FuryfromtheDeep\",\"set_code\":\"SD4-EN002\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.44\"}],\"card_images\":[{\"id\":23771716,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/23771716.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/23771716.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.10\",\"tcgplayer_price\":\"0.36\",\"ebay_price\":\"2.99\",\"amazon_price\":\"1.49\",\"coolstuffinc_price\":\"0.49\"}]},{\"id\":86198326,\"name\":\"7Completed\",\"type\":\"SpellCard\",\"desc\":\"ActivatethiscardbychoosingATKorDEF;equiponlytoaMachinemonster.Itgains700ATKorDEF,dependingonthechoice.\",\"race\":\"Equip\",\"card_sets\":[{\"set_name\":\"BattlePack3:MonsterLeague\",\"set_code\":\"BP03-EN135\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.41\"},{\"set_name\":\"BattlePack3:MonsterLeague\",\"set_code\":\"BP03-EN135\",\"set_rarity\":\"ShatterfoilRare\",\"set_rarity_code\":\"(SHR)\",\"set_price\":\"1.41\"},{\"set_name\":\"DuelTerminal2\",\"set_code\":\"DT02-EN038\",\"set_rarity\":\"DuelTerminalNormalParallelRare\",\"set_rarity_code\":\"(DNPR)\",\"set_price\":\"3.99\"},{\"set_name\":\"Pharaoh'sServant\",\"set_code\":\"PSV-004\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"3.69\"},{\"set_name\":\"Pharaoh'sServant\",\"set_code\":\"PSV-E004\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"6.11\"},{\"set_name\":\"Pharaoh'sServant\",\"set_code\":\"PSV-EN004\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.92\"},{\"set_name\":\"SpeedDuel:ScarsofBattle\",\"set_code\":\"SBSC-EN029\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.21\"}],\"card_images\":[{\"id\":86198326,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/86198326.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/86198326.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.03\",\"tcgplayer_price\":\"0.16\",\"ebay_price\":\"2.50\",\"amazon_price\":\"1.07\",\"coolstuffinc_price\":\"1.49\"}]},{\"id\":14261867,\"name\":\"8-ClawsScorpion\",\"type\":\"EffectMonster\",\"desc\":\"Onceperturn,youcanflipthiscardintoface-downDefensePosition.Whenthiscardattacksanopponent'sface-downDefensePositionmonster,thiscard'sATKbecomes2400duringdamagecalculationonly.\",\"atk\":300,\"def\":200,\"level\":2,\"race\":\"Insect\",\"attribute\":\"DARK\",\"card_sets\":[{\"set_name\":\"GoldSeries\",\"set_code\":\"GLD1-EN007\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.95\"},{\"set_name\":\"PharaonicGuardian\",\"set_code\":\"PGD-024\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.4\"}],\"card_images\":[{\"id\":14261867,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/14261867.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/14261867.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.10\",\"tcgplayer_price\":\"0.14\",\"ebay_price\":\"0.99\",\"amazon_price\":\"0.20\",\"coolstuffinc_price\":\"0.25\"}]},{\"id\":24140059,\"name\":\"ACatofIllOmen\",\"type\":\"FlipEffectMonster\",\"desc\":\"FLIP:Choose1TrapfromyourDeckandplaceitontopofyourDeck,or,if\\\"Necrovalley\\\"isonthefield,youcanaddthatTraptoyourhandinstead.\",\"atk\":500,\"def\":300,\"level\":2,\"race\":\"Beast\",\"attribute\":\"DARK\",\"card_sets\":[{\"set_name\":\"DarkRevelationVolume1\",\"set_code\":\"DR1-EN018\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"3.25\"},{\"set_name\":\"PharaonicGuardian\",\"set_code\":\"PGD-070\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"3.45\"},{\"set_name\":\"SpeedDuelStarterDecks:DestinyMasters\",\"set_code\":\"SS01-ENB11\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"3.15\"}],\"card_images\":[{\"id\":24140059,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/24140059.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/24140059.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.17\",\"tcgplayer_price\":\"1.32\",\"ebay_price\":\"3.95\",\"amazon_price\":\"3.25\",\"coolstuffinc_price\":\"1.99\"}]},{\"id\":6850209,\"name\":\"ADealwithDarkRuler\",\"type\":\"SpellCard\",\"desc\":\"(Thiscardisalwaystreatedasan\\\"Archfiend\\\"card.)\\nIfaLevel8orhighermonsterunderyourcontrolwassenttotheGraveyardthisturn:SpecialSummon1\\\"BerserkDragon\\\"fromyourhandorDeck.\",\"race\":\"Quick-Play\",\"archetype\":\"Archfiend\",\"card_sets\":[{\"set_name\":\"DarkCrisis\",\"set_code\":\"DCR-030\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.92\"},{\"set_name\":\"DarkCrisis\",\"set_code\":\"DCR-EN030\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"0.00\"},{\"set_name\":\"DarkRevelationVolume1\",\"set_code\":\"DR1-EN192\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.22\"},{\"set_name\":\"LegendaryCollection4:Joey'sWorldMegaPack\",\"set_code\":\"LCJW-EN241\",\"set_rarity\":\"Rare\",\"set_rarity_code\":\"(R)\",\"set_price\":\"1.16\"}],\"card_images\":[{\"id\":6850209,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/6850209.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/6850209.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.21\",\"tcgplayer_price\":\"0.26\",\"ebay_price\":\"2.48\",\"amazon_price\":\"0.99\",\"coolstuffinc_price\":\"0.29\"}]},{\"id\":49140998,\"name\":\"AFeatherofthePhoenix\",\"type\":\"SpellCard\",\"desc\":\"Discard1card,thentarget1cardinyourGY;returnthattargettothetopofyourDeck.\",\"race\":\"Normal\",\"card_sets\":[{\"set_name\":\"ChampionPack:GameThree\",\"set_code\":\"CP03-EN018\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.04\"},{\"set_name\":\"DarkRevelationVolume3\",\"set_code\":\"DR3-EN157\",\"set_rarity\":\"SuperRare\",\"set_rarity_code\":\"(SR)\",\"set_price\":\"18.65\"},{\"set_name\":\"FlamingEternity\",\"set_code\":\"FET-EN037\",\"set_rarity\":\"SuperRare\",\"set_rarity_code\":\"(SR)\",\"set_price\":\"3.65\"},{\"set_name\":\"FlamingEternity\",\"set_code\":\"FET-EN037\",\"set_rarity\":\"UltimateRare\",\"set_rarity_code\":\"(UtR)\",\"set_price\":\"2.28\"},{\"set_name\":\"LegendaryCollection3:Yugi'sWorldMegaPack\",\"set_code\":\"LCYW-EN280\",\"set_rarity\":\"SecretRare\",\"set_rarity_code\":\"(ScR)\",\"set_price\":\"4.2\"},{\"set_name\":\"LegendaryHeroDecks\",\"set_code\":\"LEHD-ENA26\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.78\"},{\"set_name\":\"StarterDeck:SyrusTruesdale\",\"set_code\":\"YSDS-EN029\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.94\"}],\"card_images\":[{\"id\":49140998,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/49140998.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/49140998.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.18\",\"tcgplayer_price\":\"0.11\",\"ebay_price\":\"1.25\",\"amazon_price\":\"0.25\",\"coolstuffinc_price\":\"0.49\"}]},{\"id\":68170903,\"name\":\"AFeintPlan\",\"type\":\"TrapCard\",\"desc\":\"Aplayercannotattackface-downmonstersduringthisturn.\",\"race\":\"Normal\",\"card_sets\":[{\"set_name\":\"LegacyofDarkness\",\"set_code\":\"LOD-032\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.41\"},{\"set_name\":\"LegacyofDarkness\",\"set_code\":\"LOD-EN032\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.9\"}],\"card_images\":[{\"id\":68170903,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/68170903.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/68170903.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.06\",\"tcgplayer_price\":\"0.26\",\"ebay_price\":\"3.00\",\"amazon_price\":\"0.29\",\"coolstuffinc_price\":\"0.25\"}]},{\"id\":21597117,\"name\":\"AHeroEmerges\",\"type\":\"TrapCard\",\"desc\":\"Whenanopponent'smonsterdeclaresanattack:Youropponentchooses1randomcardfromyourhand,thenifitisamonsterthatcanbeSpecialSummoned,SpecialSummonit.Otherwise,sendittotheGraveyard.\",\"race\":\"Normal\",\"card_sets\":[{\"set_name\":\"BattlePack2:WaroftheGiants\",\"set_code\":\"BP02-EN179\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.31\"},{\"set_name\":\"BattlePack2:WaroftheGiants\",\"set_code\":\"BP02-EN179\",\"set_rarity\":\"MosaicRare\",\"set_rarity_code\":\"(MSR)\",\"set_price\":\"1.48\"},{\"set_name\":\"DarkRevelationVolume2\",\"set_code\":\"DR2-EN105\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.39\"},{\"set_name\":\"DuelTerminal2\",\"set_code\":\"DT02-EN048\",\"set_rarity\":\"DuelTerminalNormalParallelRare\",\"set_rarity_code\":\"(DNPR)\",\"set_price\":\"5.23\"},{\"set_name\":\"DuelistPack:JadenYuki\",\"set_code\":\"DP1-EN025\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.38\"},{\"set_name\":\"InvasionofChaos\",\"set_code\":\"IOC-104\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"2.02\"},{\"set_name\":\"InvasionofChaos\",\"set_code\":\"IOC-EN104\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"0.00\"},{\"set_name\":\"SuperStarter:Space-TimeShowdown\",\"set_code\":\"YS14-EN036\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.78\"}],\"card_images\":[{\"id\":21597117,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/21597117.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/21597117.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.12\",\"tcgplayer_price\":\"0.20\",\"ebay_price\":\"0.99\",\"amazon_price\":\"1.28\",\"coolstuffinc_price\":\"0.25\"}]},{\"id\":8949584,\"name\":\"AHeroLives\",\"type\":\"SpellCard\",\"desc\":\"Ifyoucontrolnoface-upmonsters:PayhalfyourLP;SpecialSummon1Level4orlower\\\"ElementalHERO\\\"monsterfromyourDeck.\",\"race\":\"Normal\",\"archetype\":\"ElementalHERO\",\"card_sets\":[{\"set_name\":\"DuelistSaga\",\"set_code\":\"DUSA-EN087\",\"set_rarity\":\"UltraRare\",\"set_rarity_code\":\"(UR)\",\"set_price\":\"5.07\"},{\"set_name\":\"GenerationForce\",\"set_code\":\"GENF-EN098\",\"set_rarity\":\"UltimateRare\",\"set_rarity_code\":\"(UtR)\",\"set_price\":\"67.58\"},{\"set_name\":\"GenerationForce\",\"set_code\":\"GENF-EN098\",\"set_rarity\":\"UltraRare\",\"set_rarity_code\":\"(UR)\",\"set_price\":\"4.96\"},{\"set_name\":\"HEROStrikeStructureDeck\",\"set_code\":\"SDHS-EN026\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1.86\"},{\"set_name\":\"LegendaryDuelists:MagicalHero\",\"set_code\":\"LED6-EN022\",\"set_rarity\":\"Common\",\"set_rarity_code\":\"(C)\",\"set_price\":\"1\"}],\"banlist_info\":{\"ban_tcg\":\"Semi-Limited\",\"ban_ocg\":\"Semi-Limited\"},\"card_images\":[{\"id\":8949584,\"image_url\":\"https://storage.googleapis.com/ygoprodeck.com/pics/8949584.jpg\",\"image_url_small\":\"https://storage.googleapis.com/ygoprodeck.com/pics_small/8949584.jpg\"}],\"card_prices\":[{\"cardmarket_price\":\"0.10\",\"tcgplayer_price\":\"0.16\",\"ebay_price\":\"88.88\",\"amazon_price\":\"29.99\",\"coolstuffinc_price\":\"0.49\"}]}],\"meta\":{\"current_rows\":10,\"total_rows\":11784,\"rows_remaining\":11763,\"total_pages\":1178,\"pages_remaining\":1177,\"previous_page\":\"https://db.ygoprodeck.com/api/v7/cardinfo.php?num=10&offset=1\",\"previous_page_offset\":1,\"next_page\":\"https://db.ygoprodeck.com/api/v7/cardinfo.php?num=10&offset=21\",\"next_page_offset\":21}}"
        //val outputdata: CardData = Gson().fromJson(outputstring, CardData::class.java)
        //handleCards(UIState.SUCCESS(outputdata))



        /******
         *  ! Important - If your want to perform UI test please comment the below two lines
         */
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