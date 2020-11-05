package edu.stanford.dstratak.yelp

import com.google.gson.annotations.SerializedName

// NOTE: use @SerializedName when the name of the key does not match the name of the local variable

data class YelpSearchResult (
    val total: Int,
    @SerializedName("businesses") val restaurants: List<YelpRestaurant>
)

data class YelpRestaurant (
    val id: String,
    val name: String,
    val rating: Double,
    val price: String,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpCategory>,
    val location: YelpLocation
) {
    fun displayDistance(): String {
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%.2f".format(distanceInMeters * milesPerMeter)
        return "$distanceInMiles mi"
    }
}

data class YelpBusinessDetail (
    val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("is_claimed") val isClaimed: String,
    @SerializedName("is_closed") val isClosed: String,
    val url: String,
    @SerializedName("display_phone") val phone: String,
    @SerializedName("review_count") val numReviews: Int,
    val categories: List<YelpCategory>,
    val rating: Double,
    val location: YelpLocation,
    val photos: List<String>,
    val price: String,
    // hours
    val transactions: List<String>
)

data class YelpCategory (
    val title: String
)

data class YelpLocation(
    @SerializedName("address1") val address: String
)