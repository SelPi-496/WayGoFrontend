package com.example.waygofrontend.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.waygofrontend.R

object WaygoData {

    data class Destination(
        val id: String,
        val name: String,
        val place: String,
        val rating: String,
        val price: String,
        val match: String,
        val reason: String,
        val topLabel: String,
        @DrawableRes val imageRes: Int
    )

    data class Category(
        val label: String,
        @DrawableRes val iconRes: Int,
        @ColorRes val tintRes: Int,
        @ColorRes val backgroundRes: Int
    )

    data class Trip(
        val destination: Destination,
        val range: String,
        val days: Int,
        val status: String
    )

    data class Activity(
        val id: Int,
        val time: String,
        val title: String,
        val place: String,
        val cost: String,
        @ColorRes val dotColor: Int
    )

    data class Hotel(
        val id: Int,
        val name: String,
        val area: String,
        val rating: String,
        val reviews: String,
        val price: Long,
        val oldPrice: Long,
        val badge: String,
        val amenity: String,
        @DrawableRes val imageRes: Int
    )

    data class Service(
        val title: String,
        val note: String,
        val price: String,
        @DrawableRes val imageRes: Int
    )

    data class ServiceContent(
        val subtitle: String,
        val items: List<Service>
    )

    data class Notice(
        val title: String,
        val note: String,
        @DrawableRes val iconRes: Int,
        @ColorRes val tintRes: Int,
        @ColorRes val backgroundRes: Int,
        val hoursAgo: Int
    )

    val destinations = listOf(
        Destination(
            id = "danang",
            name = "Đà Nẵng",
            place = "Thành phố biển đáng sống",
            rating = "4.9",
            price = "1.290.000đ",
            match = "96%",
            reason = "Biển đẹp · Ẩm thực phong phú · Đúng ngân sách",
            topLabel = "Đang thịnh hành",
            imageRes = R.drawable.bg_photo_beach
        ),
        Destination(
            id = "hoian",
            name = "Hội An",
            place = "Phố cổ rực rỡ sắc màu",
            rating = "4.8",
            price = "890.000đ",
            match = "91%",
            reason = "Văn hóa đặc sắc · Không gian lãng mạn",
            topLabel = "Phù hợp cặp đôi",
            imageRes = R.drawable.bg_photo_sunset
        ),
        Destination(
            id = "halong",
            name = "Vịnh Hạ Long",
            place = "Kỳ quan giữa biển trời",
            rating = "4.9",
            price = "1.650.000đ",
            match = "87%",
            reason = "Thiên nhiên kỳ vĩ · Trải nghiệm thư giãn",
            topLabel = "Kỳ quan thiên nhiên",
            imageRes = R.drawable.bg_photo_teal
        )
    )

    val categories = listOf(
        Category("Khách sạn", R.drawable.ic_hotel, R.color.cat_blue, R.color.cat_blue_bg),
        Category("Vé máy bay", R.drawable.ic_flight, R.color.cat_orange, R.color.cat_orange_bg),
        Category("Tour", R.drawable.ic_tour, R.color.cat_cyan, R.color.cat_cyan_bg),
        Category("Ẩm thực", R.drawable.ic_food, R.color.cat_amber, R.color.cat_amber_bg),
        Category("Trải nghiệm ẩn", R.drawable.ic_sparkle, R.color.cat_violet, R.color.cat_violet_bg)
    )

    val trips = listOf(
        Trip(destinations[0], "13–15 tháng 8", 3, "Sắp tới"),
        Trip(destinations[1], "23–27 tháng 8", 5, "Đang lên kế hoạch"),
        Trip(destinations[2], "05–08 tháng 9", 4, "Bản nháp")
    )

    fun destination(id: String?): Destination =
        destinations.firstOrNull { it.id == id } ?: destinations[0]

    fun tripFor(id: String?): Trip =
        trips.firstOrNull { it.destination.id == id } ?: trips[0]

    fun activities(day: Int): List<Activity> = when (day) {
        2 -> listOf(
            Activity(21, "08:30", "Dạo phố cổ buổi sáng", "Chùa Cầu, Hội An", "80.000đ", R.color.cat_amber),
            Activity(22, "11:30", "Thưởng thức cao lầu", "Chợ Hội An", "180.000đ", R.color.cat_orange),
            Activity(23, "16:30", "Thả hoa đăng", "Sông Hoài", "150.000đ", R.color.cat_violet)
        )
        3 -> listOf(
            Activity(31, "07:30", "Khởi hành lên Bà Nà", "Cáp treo Bà Nà Hills", "900.000đ", R.color.cat_blue),
            Activity(32, "10:00", "Check-in Cầu Vàng", "Vườn Thiên Thai", "Đã gồm vé", R.color.cat_amber),
            Activity(33, "14:00", "Khám phá làng Pháp", "French Village", "350.000đ", R.color.cat_cyan)
        )
        4 -> listOf(
            Activity(41, "08:00", "Ăn sáng & mua đặc sản", "Chợ Hàn", "450.000đ", R.color.cat_orange),
            Activity(42, "11:00", "Trả phòng khách sạn", "Khu vực Mỹ Khê", "Đã thanh toán", R.color.waygo_success),
            Activity(43, "13:30", "Di chuyển ra sân bay", "Sân bay Quốc tế Đà Nẵng", "120.000đ", R.color.waygo_text_secondary)
        )
        else -> listOf(
            Activity(1, "08:00", "Cà phê buổi sáng", "43 Factory Coffee Roaster", "120.000đ", R.color.cat_orange),
            Activity(2, "10:30", "Khám phá bán đảo Sơn Trà", "Chùa Linh Ứng · 8,4 km", "Miễn phí", R.color.cat_blue),
            Activity(3, "15:00", "Tắm biển Mỹ Khê", "Bãi biển Mỹ Khê", "200.000đ", R.color.cat_cyan),
            Activity(4, "19:00", "Ăn tối bên sông Hàn", "Nhà hàng Madame Lân", "550.000đ", R.color.cat_violet)
        )
    }

    fun hotels(destinationId: String?): List<Hotel> {
        val names: Map<String, List<String>> = mapOf(
            "danang" to listOf("Sala Beach Hotel", "Naman Retreat", "The Ocean Suites"),
            "hoian" to listOf("Anantara Hội An", "La Siesta Resort", "Silk Sense River Resort"),
            "halong" to listOf("Oakwood Hạ Long", "Vinpearl Resort & Spa", "Citadines Marina")
        )
        val areas: Map<String, List<String>> = mapOf(
            "danang" to listOf("Mỹ Khê · Cách biển 120 m", "Ngũ Hành Sơn · Cách 4,2 km", "Sơn Trà · Cách biển 350 m"),
            "hoian" to listOf("Ven sông Thu Bồn · Cách 800 m", "Phố cổ · Cách 1,1 km", "Cẩm An · Cách biển 600 m"),
            "halong" to listOf("Bãi Cháy · Hướng vịnh", "Đảo Rều · Bãi biển riêng", "Hùng Thắng · Gần bến du thuyền")
        )
        val hotelNames = names[destinationId] ?: names.getValue("danang")
        val hotelAreas = areas[destinationId] ?: areas.getValue("danang")
        val images = listOf(R.drawable.bg_photo_beach, R.drawable.bg_photo_sunset, R.drawable.bg_photo_indigo)
        return hotelNames.mapIndexed { index, name ->
            Hotel(
                id = index,
                name = name,
                area = hotelAreas[index],
                rating = listOf("4.9", "4.8", "4.7")[index],
                reviews = listOf("1.284", "896", "642")[index],
                price = listOf(1_890_000L, 2_450_000L, 1_320_000L)[index],
                oldPrice = listOf(2_300_000L, 2_900_000L, 1_650_000L)[index],
                badge = listOf("Được yêu thích", "Nghỉ dưỡng cao cấp", "Giá tốt hôm nay")[index],
                amenity = listOf("Hồ bơi vô cực · Ăn sáng", "Bãi biển riêng · Spa", "View biển · Xe đưa đón")[index],
                imageRes = images[index]
            )
        }
    }

    val serviceContent: Map<String, ServiceContent> = mapOf(
        "Khách sạn" to ServiceContent(
            subtitle = "Nơi nghỉ được yêu thích",
            items = listOf(
                Service("Sala Beach Hotel", "Mỹ Khê · Hồ bơi vô cực", "1.890.000đ", R.drawable.bg_photo_beach),
                Service("Naman Retreat", "Bãi biển riêng · Spa", "2.450.000đ", R.drawable.bg_photo_sunset),
                Service("The Ocean Suites", "View biển · Ăn sáng", "1.320.000đ", R.drawable.bg_photo_indigo)
            )
        ),
        "Vé máy bay" to ServiceContent(
            subtitle = "Chuyến bay giá tốt hôm nay",
            items = listOf(
                Service("Hà Nội → Đà Nẵng", "08:15 – 09:40 · Bay thẳng", "1.290.000đ", R.drawable.bg_photo_beach),
                Service("TP.HCM → Đà Nẵng", "10:30 – 11:55 · Bay thẳng", "990.000đ", R.drawable.bg_photo_indigo),
                Service("Hà Nội → Hội An", "14:10 – 15:35 · Đến Đà Nẵng", "1.450.000đ", R.drawable.bg_photo_sunset)
            )
        ),
        "Tour" to ServiceContent(
            subtitle = "Hành trình được chọn nhiều",
            items = listOf(
                Service("Sơn Trà & Ngũ Hành Sơn", "8 giờ · Nhóm tối đa 10 khách", "790.000đ", R.drawable.bg_photo_beach),
                Service("Một ngày làm người Hội An", "6 giờ · Có hướng dẫn viên", "650.000đ", R.drawable.bg_photo_sunset),
                Service("Du thuyền kỳ quan Hạ Long", "2 ngày 1 đêm · Trọn gói", "2.890.000đ", R.drawable.bg_photo_teal)
            )
        ),
        "Ẩm thực" to ServiceContent(
            subtitle = "Hương vị không nên bỏ lỡ",
            items = listOf(
                Service("Food tour Đà Nẵng", "4 điểm ăn · 3 giờ", "490.000đ", R.drawable.bg_photo_beach),
                Service("Lớp nấu cao lầu", "Chợ Hội An · Bao gồm nguyên liệu", "620.000đ", R.drawable.bg_photo_sunset),
                Service("Bữa tối hải sản bên vịnh", "Set menu · 2 khách", "890.000đ", R.drawable.bg_photo_teal)
            )
        ),
        "Trải nghiệm ẩn" to ServiceContent(
            subtitle = "Những góc riêng chỉ Waygo biết",
            items = listOf(
                Service("Ngắm bình minh cùng ngư dân", "05:00 · Bãi biển Mân Thái", "350.000đ", R.drawable.bg_photo_beach),
                Service("Đạp xe xuyên làng rau", "07:30 · Trà Quế, Hội An", "280.000đ", R.drawable.bg_photo_sunset),
                Service("Chèo kayak qua hang Luồn", "15:00 · Vịnh Hạ Long", "450.000đ", R.drawable.bg_photo_teal)
            )
        )
    )

    fun service(label: String): ServiceContent =
        serviceContent[label] ?: serviceContent.getValue("Tour")

    val notices = listOf(
        Notice(
            "Giá khách sạn vừa giảm",
            "Sala Beach Hotel đang giảm 18% cho ngày bạn đã chọn.",
            R.drawable.ic_hotel,
            R.color.waygo_coral,
            R.color.waygo_coral_soft,
            1
        ),
        Notice(
            "Lịch trình đã sẵn sàng",
            "Waygo đã tối ưu lộ trình ngày 2 tại Hội An.",
            R.drawable.ic_sparkle,
            R.color.waygo_blue,
            R.color.waygo_blue_soft,
            2
        ),
        Notice(
            "Sắp đến giờ khởi hành",
            "Chuyến bay tới Đà Nẵng còn 2 ngày nữa.",
            R.drawable.ic_flight,
            R.color.waygo_blue,
            R.color.waygo_blue_soft,
            3
        )
    )

    fun formatPrice(value: Long): String {
        val digits = value.toString()
        val builder = StringBuilder()
        digits.forEachIndexed { index, c ->
            if (index > 0 && (digits.length - index) % 3 == 0) builder.append('.')
            builder.append(c)
        }
        return builder.append('đ').toString()
    }
}
