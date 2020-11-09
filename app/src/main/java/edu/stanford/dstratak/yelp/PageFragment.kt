package edu.stanford.dstratak.yelp
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//
//// In this case, the fragment displays simple text based on the page
//class PageFragment : Fragment() {
//    private var mPage = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mPage = arguments?.getInt(ARG_PAGE) ?: 0
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View {
//        return inflater.inflate(R.layout.activity_business_detail_overview, container, false)
//    }
//
//    companion object {
//        const val ARG_PAGE = "ARG_PAGE"
//        fun newInstance(page: Int): PageFragment {
//            val args = Bundle()
//            args.putInt(ARG_PAGE, page)
//            val fragment = PageFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//}