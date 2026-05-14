package com.historytoday.lunar

import com.historytoday.domain.model.LunarInfo
import java.time.LocalDate

object LunarCalendar {

    private val LUNAR_INFO = intArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,
        0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0,
        0x0a2e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4,
        0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0,
        0x0b273, 0x06930, 0x073a8, 0x06aa0, 0x0ad50, 0x054d4, 0x04ba0, 0x0a5b0, 0x14576, 0x052b0,
        0x0a930, 0x0b235, 0x09930, 0x09e90, 0x0d558, 0x0d4a0, 0x0da50, 0x05d50, 0x05da0, 0x1a6d6,
        0x0a4e0, 0x0d260, 0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a4d0,
        0x0d150, 0x0f252, 0x0d520
    )

    private val TIAN_GAN = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    private val DI_ZHI = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    private val ZODIAC = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
    private val LUNAR_MONTHS = arrayOf("正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月")
    private val LUNAR_DAYS = arrayOf("初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十")

    private val SOLAR_TERMS = arrayOf(
        "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
        "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
        "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
        "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
    )

    private val SOLAR_TERM_TABLE = mapOf(
        2025 to mapOf(
            1 to listOf(6 to "小寒", 20 to "大寒"),
            2 to listOf(4 to "立春", 19 to "雨水"),
            3 to listOf(6 to "惊蛰", 20 to "春分"),
            4 to listOf(4 to "清明", 20 to "谷雨"),
            5 to listOf(6 to "立夏", 21 to "小满"),
            6 to listOf(6 to "芒种", 21 to "夏至"),
            7 to listOf(7 to "小暑", 23 to "大暑"),
            8 to listOf(8 to "立秋", 23 to "处暑"),
            9 to listOf(8 to "白露", 23 to "秋分"),
            10 to listOf(8 to "寒露", 24 to "霜降"),
            11 to listOf(8 to "立冬", 22 to "小雪"),
            12 to listOf(7 to "大雪", 22 to "冬至")
        ),
        2026 to mapOf(
            1 to listOf(5 to "小寒", 20 to "大寒"),
            2 to listOf(4 to "立春", 18 to "雨水"),
            3 to listOf(5 to "惊蛰", 20 to "春分"),
            4 to listOf(4 to "清明", 20 to "谷雨"),
            5 to listOf(5 to "立夏", 21 to "小满"),
            6 to listOf(6 to "芒种", 21 to "夏至"),
            7 to listOf(7 to "小暑", 23 to "大暑"),
            8 to listOf(7 to "立秋", 23 to "处暑"),
            9 to listOf(8 to "白露", 23 to "秋分"),
            10 to listOf(8 to "寒露", 23 to "霜降"),
            11 to listOf(7 to "立冬", 22 to "小雪"),
            12 to listOf(7 to "大雪", 22 to "冬至")
        ),
        2027 to mapOf(
            1 to listOf(5 to "小寒", 20 to "大寒"),
            2 to listOf(4 to "立春", 19 to "雨水"),
            3 to listOf(6 to "惊蛰", 21 to "春分"),
            4 to listOf(5 to "清明", 20 to "谷雨"),
            5 to listOf(6 to "立夏", 21 to "小满"),
            6 to listOf(6 to "芒种", 22 to "夏至"),
            7 to listOf(8 to "小暑", 23 to "大暑"),
            8 to listOf(8 to "立秋", 23 to "处暑"),
            9 to listOf(8 to "白露", 23 to "秋分"),
            10 to listOf(9 to "寒露", 24 to "霜降"),
            11 to listOf(8 to "立冬", 22 to "小雪"),
            12 to listOf(7 to "大雪", 22 to "冬至")
        ),
        2028 to mapOf(
            1 to listOf(6 to "小寒", 20 to "大寒"),
            2 to listOf(4 to "立春", 19 to "雨水"),
            3 to listOf(5 to "惊蛰", 20 to "春分"),
            4 to listOf(4 to "清明", 20 to "谷雨"),
            5 to listOf(5 to "立夏", 21 to "小满"),
            6 to listOf(5 to "芒种", 21 to "夏至"),
            7 to listOf(7 to "小暑", 23 to "大暑"),
            8 to listOf(7 to "立秋", 23 to "处暑"),
            9 to listOf(7 to "白露", 23 to "秋分"),
            10 to listOf(8 to "寒露", 23 to "霜降"),
            11 to listOf(7 to "立冬", 22 to "小雪"),
            12 to listOf(6 to "大雪", 21 to "冬至")
        ),
        2029 to mapOf(
            1 to listOf(5 to "小寒", 20 to "大寒"),
            2 to listOf(3 to "立春", 18 to "雨水"),
            3 to listOf(6 to "惊蛰", 20 to "春分"),
            4 to listOf(4 to "清明", 20 to "谷雨"),
            5 to listOf(6 to "立夏", 21 to "小满"),
            6 to listOf(6 to "芒种", 21 to "夏至"),
            7 to listOf(7 to "小暑", 23 to "大暑"),
            8 to listOf(8 to "立秋", 23 to "处暑"),
            9 to listOf(8 to "白露", 23 to "秋分"),
            10 to listOf(8 to "寒露", 24 to "霜降"),
            11 to listOf(8 to "立冬", 22 to "小雪"),
            12 to listOf(7 to "大雪", 22 to "冬至")
        ),
        2030 to mapOf(
            1 to listOf(5 to "小寒", 19 to "大寒"),
            2 to listOf(4 to "立春", 18 to "雨水"),
            3 to listOf(5 to "惊蛰", 20 to "春分"),
            4 to listOf(4 to "清明", 19 to "谷雨"),
            5 to listOf(5 to "立夏", 21 to "小满"),
            6 to listOf(5 to "芒种", 21 to "夏至"),
            7 to listOf(7 to "小暑", 23 to "大暑"),
            8 to listOf(7 to "立秋", 23 to "处暑"),
            9 to listOf(8 to "白露", 23 to "秋分"),
            10 to listOf(8 to "寒露", 23 to "霜降"),
            11 to listOf(7 to "立冬", 22 to "小雪"),
            12 to listOf(7 to "大雪", 22 to "冬至")
        )
    )

    fun solarToLunar(solarDate: LocalDate): LunarInfo {
        val year = solarDate.year
        val month = solarDate.monthValue
        val day = solarDate.dayOfMonth

        val offset = calculateOffset(year, month, day)
        val (lunarYear, lunarMonth, lunarDay, isLeap) = calculateLunarDate(offset)

        val lunarMonthStr = if (isLeap) "闰${LUNAR_MONTHS[lunarMonth - 1]}" else LUNAR_MONTHS[lunarMonth - 1]

        return LunarInfo(
            lunarDate = "$lunarMonthStr${LUNAR_DAYS[lunarDay - 1]}",
            lunarYear = "${calculateGanZhi(lunarYear)}年",
            zodiac = ZODIAC[(lunarYear - 4) % 12],
            ganZhi = calculateGanZhi(lunarYear),
            solarTerm = getSolarTerm(year, month, day),
            festival = getFestival(month, day) ?: getLunarFestival(lunarMonth, lunarDay),
            yi = getYi(lunarYear, lunarMonth, lunarDay),
            ji = getJi(lunarYear, lunarMonth, lunarDay)
        )
    }

    private fun calculateOffset(year: Int, month: Int, day: Int): Int {
        val baseYear = 1900
        var offset = 0

        for (y in baseYear until year) {
            offset += if (isLeapYear(y)) 366 else 365
        }

        val daysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        for (m in 1 until month) {
            offset += daysInMonth[m]
        }

        if (month > 2 && isLeapYear(year)) {
            offset += 1
        }

        offset += day - 1

        return offset
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

    private fun calculateLunarDate(offset: Int): Quadruple<Int, Int, Int, Boolean> {
        var days = offset
        var year = 1900
        var isLeap = false
        var leapMonth = 0

        while (year < 2101) {
            val yearInfo = LUNAR_INFO[year - 1900]
            val yearDays = if ((yearInfo and 0xF0000) != 0) 385 else 354

            if (days < yearDays) {
                break
            }
            days -= yearDays
            year++
        }

        val yearInfo = LUNAR_INFO[year - 1900]
        leapMonth = (yearInfo and 0xF0000) shr 16
        isLeap = false

        var month = 1
        while (month <= 12) {
            var monthDays = if ((yearInfo and (0x10000 shr (month - 1))) != 0) 30 else 29

            if (leapMonth > 0 && month == leapMonth && !isLeap) {
                isLeap = true
                monthDays = if ((yearInfo and (0x10000 shr (month - 1))) != 0) 30 else 29
            } else if (leapMonth > 0 && month == leapMonth + 1 && isLeap) {
                isLeap = false
            }

            if (days < monthDays) {
                break
            }
            days -= monthDays
            month++
        }

        return Quadruple(year, month, days + 1, isLeap)
    }

    private fun calculateGanZhi(year: Int): String {
        return TIAN_GAN[(year - 4) % 10] + DI_ZHI[(year - 4) % 12]
    }

    fun getSolarTerm(year: Int, month: Int, day: Int): String? {
        return SOLAR_TERM_TABLE[year]?.get(month)?.firstOrNull { it.first == day }?.second
    }

    private fun getFestival(month: Int, day: Int): String? = when {
        month == 1 && day == 1 -> "元旦"
        month == 2 && day == 14 -> "情人节"
        month == 3 && day == 8 -> "妇女节"
        month == 3 && day == 12 -> "植树节"
        month == 5 && day == 1 -> "劳动节"
        month == 5 && day == 4 -> "青年节"
        month == 6 && day == 1 -> "儿童节"
        month == 7 && day == 1 -> "建党节"
        month == 8 && day == 1 -> "建军节"
        month == 9 && day == 10 -> "教师节"
        month == 10 && day == 1 -> "国庆节"
        month == 10 && day == 15 -> "重阳节"
        month == 12 && day == 25 -> "圣诞节"
        else -> null
    }

    private fun getLunarFestival(month: Int, day: Int): String? = when {
        month == 1 && day == 1 -> "春节"
        month == 1 && day == 15 -> "元宵节"
        month == 5 && day == 5 -> "端午节"
        month == 7 && day == 7 -> "七夕节"
        month == 8 && day == 15 -> "中秋节"
        month == 9 && day == 9 -> "重阳节"
        month == 12 && day == 8 -> "腊八节"
        month == 12 && day == 30 -> "除夕"
        else -> null
    }

    private fun getYi(year: Int, month: Int, day: Int): List<String> {
        val allYi = listOf("出行", "搬家", "签订合同", "交易", "纳财", "开业", "安床", "订婚", "结婚", "祭祀", "祈福", "动土")
        val seed = (year * 10000 + month * 100 + day).toLong()
        return allYi.shuffled(java.util.Random(seed)).take(4 + (day % 3))
    }

    private fun getJi(year: Int, month: Int, day: Int): List<String> {
        val allJi = listOf("动土", "安葬", "破土", "伐木", "入宅", "移徙", "嫁娶", "开市", "开光", "修造")
        val seed = (year * 10000 + month * 100 + day + 10000).toLong()
        return allJi.shuffled(java.util.Random(seed)).take(2 + (day % 2))
    }

    data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
