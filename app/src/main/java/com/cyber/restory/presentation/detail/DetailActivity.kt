package com.cyber.restory.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.cyber.restory.R
import com.cyber.restory.data.model.Post
import com.cyber.restory.databinding.ActivityDetailBinding
import com.cyber.restory.presentation.detail.viewmodel.DetailViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private var mapView: MapView? = null
    private var kakaoMap: KakaoMap? = null

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.getPostDetail(args.postId)
        setupClickListeners()

        lifecycleScope.launch {
            viewModel.postDetail.collect { post ->
                if (post != null) {
                    updateUI(post)
                    initializeMapView(post)
                }
            }
        }
    }

    private fun initializeMapView(post: Post) {
        mapView = binding.mapView
        mapView?.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출됨
                Log.d("DetailActivity", "지도 API가 정상적으로 종료되었습니다.")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.e("DetailActivity", "지도 사용 중 에러 발생: ${error.message}", error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨
                Log.d("DetailActivity", "KakaoMap 준비 완료")
                kakaoMap = map
                showLocationOnMap(post) // 여기에 post 데이터를 전달
            }
        })
    }

    private fun updateUI(post: Post) {
        with(binding) {
            tvArticleDetailCategory.text = post.type
            articleDetailTitle.text = post.title
            tvArticleDetailDescription.text = post.summary
            tvArticleDetailTime.text = post.duration
            tvArticleDetailTimeHoliday.text = "${post.holiday} 휴무"
            tvArticleDetailTelephone.text = post.telephone
            tvArticleDetailHomepage.text = post.url

            if (post.postImages.isNotEmpty()) {
                Glide.with(this@DetailActivity)
                    .load(post.postImages[0].imageUrl)
                    .into(ivArticleDetailImage)

                Glide.with(this@DetailActivity)
                    .load(post.postImages[1].imageUrl)
                    .centerCrop()
                    .into(ivArticleDetailBehindImage)
                tvArticleDetailBehindText.text = post.content
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            ivArticleDetailTimeToggle.setOnClickListener {
                it.isSelected = !it.isSelected
                tvArticleDetailTimeHoliday.visibility =
                    if (it.isSelected) View.VISIBLE else View.INVISIBLE

                val params =
                    tvArticleDetailTelephoneLabel.layoutParams as ConstraintLayout.LayoutParams
                params.topMargin = when (it.isSelected) {
                    true -> resources.getDimensionPixelSize(R.dimen.expanded_margin)
                    false -> resources.getDimensionPixelSize(R.dimen.collapsed_margin)
                }
                tvArticleDetailTelephoneLabel.layoutParams = params
            }

            tvArticleDetailTelephone.setOnClickListener { openDialer(binding.tvArticleDetailTelephone.text.toString()) }
            tvArticleDetailHomepage.setOnClickListener { openWebPage(binding.tvArticleDetailHomepage.text.toString()) }

            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun openDialer(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun openWebPage(url: String) {
        if (url.isNotEmpty()) {
            val fullUrl =
                if (url.startsWith("http://") || url.startsWith("https://")) url else "http://$url"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
            startActivity(intent)
        }
    }

    private fun showLocationOnMap(post: Post) {
        val location = LatLng.from(post.latitude, post.longitude)

        kakaoMap?.let { map ->
            // 카메라 이동
            map.moveCamera(CameraUpdateFactory.newCenterPosition(location))
            map.moveCamera(CameraUpdateFactory.zoomTo(16))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.finish()
    }
}
