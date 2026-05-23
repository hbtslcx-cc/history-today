# History Today - 数据源方案设计

## 概述

本文档详细介绍 History Today APP 中万年历和历史事件两个核心模块的数据源方案。

---

## 1. 万年历数据源

### 1.1 农历计算方案

#### 方案对比

| 方案 | 优点 | 缺点 | 推荐度 |
|------|------|------|--------|
| **本地算法库** | 完全离线、响应快、无网络依赖 | 需要维护算法准确性 | ⭐⭐⭐⭐⭐ |
| 在线API | 数据准确、无需维护 | 需要网络、有延迟、可能收费 | ⭐⭐⭐ |
| 预计算数据表 | 查询快 | 数据量大、占用空间 | ⭐⭐⭐⭐ |

#### 推荐方案：本地算法库

使用已验证的开源农历算法库，支持 1900-2100 年的农历计算。

**Android/Kotlin 实现方案：**

```kotlin
// lunar/LunarCalendar.kt
object LunarCalendar {
    
    // 农历数据表（1900-2100年）
    private val LUNAR_INFO = intArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, // 1900-1904
        0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, // 1905-1909
        // ... 更多年份数据
    )
    
    // 天干
    private val TIAN_GAN = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    
    // 地支
    private val DI_ZHI = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    
    // 生肖
    private val ZODIAC = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
    
    // 农历月份名称
    private val LUNAR_MONTHS = arrayOf(
        "正月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "冬月", "腊月"
    )
    
    // 农历日期名称
    private val LUNAR_DAYS = arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )
    
    /**
     * 公历转农历
     */
    fun solarToLunar(solarDate: LocalDate): LunarInfo {
        val year = solarDate.year
        val month = solarDate.monthValue
        val day = solarDate.dayOfMonth
        
        // 计算农历年月日
        val lunarDate = calculateLunarDate(year, month, day)
        
        // 计算干支
        val ganZhi = calculateGanZhi(year)
        
        // 计算生肖
        val zodiac = ZODIAC[(year - 4) % 12]
        
        // 获取节气
        val solarTerm = getSolarTerm(year, month, day)
        
        // 获取节日
        val festival = getFestival(month, day)
        
        // 获取宜忌
        val (yi, ji) = getYiJi(lunarDate)
        
        return LunarInfo(
            lunarDate = lunarDate,
            lunarYear = "${ganZhi}年",
            zodiac = zodiac,
            ganZhi = ganZhi,
            solarTerm = solarTerm,
            festival = festival,
            yi = yi,
            ji = ji
        )
    }
    
    /**
     * 计算农历日期
     */
    private fun calculateLunarDate(year: Int, month: Int, day: Int): String {
        // 基于LUNAR_INFO表的计算逻辑
        // 返回格式：四月初六
        val lunarYear = year - 1900
        val lunarInfo = LUNAR_INFO[lunarYear]
        
        // 计算逻辑...
        val lunarMonth = 4 // 示例
        val lunarDay = 6   // 示例
        
        return LUNAR_MONTHS[lunarMonth - 1] + LUNAR_DAYS[lunarDay - 1]
    }
    
    /**
     * 计算干支纪年
     */
    private fun calculateGanZhi(year: Int): String {
        val gan = TIAN_GAN[(year - 4) % 10]
        val zhi = DI_ZHI[(year - 4) % 12]
        return gan + zhi
    }
    
    /**
     * 获取二十四节气
     */
    fun getSolarTerm(year: Int, month: Int, day: Int): String? {
        val solarTerms = arrayOf(
            "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
            "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
            "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
            "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
        )
        
        // 节气计算算法（基于太阳黄经）
        // 返回对应节气或null
        return calculateSolarTerm(year, month, day)
    }
    
    /**
     * 获取公历节日
     */
    private fun getFestival(month: Int, day: Int): String? {
        return when {
            month == 1 && day == 1 -> "元旦"
            month == 2 && day == 14 -> "情人节"
            month == 3 && day == 8 -> "妇女节"
            month == 5 && day == 1 -> "劳动节"
            month == 5 && day == 4 -> "青年节"
            month == 6 && day == 1 -> "儿童节"
            month == 7 && day == 1 -> "建党节"
            month == 8 && day == 1 -> "建军节"
            month == 9 && day == 10 -> "教师节"
            month == 10 && day == 1 -> "国庆节"
            month == 12 && day == 25 -> "圣诞节"
            else -> null
        }
    }
    
    /**
     * 获取农历传统节日
     */
    fun getLunarFestival(lunarMonth: Int, lunarDay: Int): String? {
        return when {
            lunarMonth == 1 && lunarDay == 1 -> "春节"
            lunarMonth == 1 && lunarDay == 15 -> "元宵节"
            lunarMonth == 5 && lunarDay == 5 -> "端午节"
            lunarMonth == 7 && lunarDay == 7 -> "七夕节"
            lunarMonth == 8 && lunarDay == 15 -> "中秋节"
            lunarMonth == 9 && lunarDay == 9 -> "重阳节"
            lunarMonth == 12 && lunarDay == 8 -> "腊八节"
            lunarMonth == 12 && lunarDay == 30 -> "除夕"
            else -> null
        }
    }
    
    /**
     * 获取宜忌（简化版）
     */
    private fun getYiJi(lunarDate: String): Pair<List<String>, List<String>> {
        // 基于农历日期的宜忌算法
        // 可以使用简单的哈希算法或查表法
        val yi = listOf("出行", "搬家", "签订合同", "交易", "纳财")
        val ji = listOf("动土", "安葬", "破土", "伐木")
        return Pair(yi, ji)
    }
}
```

**依赖库推荐：**

```kotlin
// build.gradle.kts
dependencies {
    // 可选：使用已有的农历库
    implementation("com.github.hehonghui:calendarlib:1.0.0")
    
    // 或自行实现（推荐）
    // 无需额外依赖
}
```

### 1.2 节气计算

```kotlin
/**
 * 节气计算基于太阳黄经
 * 二十四节气对应太阳黄经：0°, 15°, 30°... 345°
 */
object SolarTermCalculator {
    
    // 节气对应的角度
    private val TERM_ANGLES = listOf(
        285.0, 300.0, 315.0, 330.0, 345.0, 0.0,
        15.0, 30.0, 45.0, 60.0, 75.0, 90.0,
        105.0, 120.0, 135.0, 150.0, 165.0, 180.0,
        195.0, 210.0, 225.0, 240.0, 255.0, 270.0
    )
    
    /**
     * 计算指定年份的所有节气日期
     */
    fun calculateYearSolarTerms(year: Int): Map<String, LocalDate> {
        val terms = mutableMapOf<String, LocalDate>()
        val termNames = arrayOf(
            "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
            "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
            "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
            "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
        )
        
        // 使用天文算法计算节气日期
        // 简化版：使用已验证的数据表或算法
        
        return terms
    }
}
```

---

## 2. 历史事件数据源

### 2.1 数据源方案对比

| 方案 | 优点 | 缺点 | 推荐度 |
|------|------|------|--------|
| **本地JSON文件** | 完全离线、加载快、无需网络 | 数据更新需发版 | ⭐⭐⭐⭐⭐ |
| 远程API | 数据可实时更新 | 需要网络、服务器成本 | ⭐⭐⭐⭐ |
| 本地数据库 | 查询快、支持搜索 | 初始数据导入慢 | ⭐⭐⭐⭐ |
| 混合方案 | 离线可用+可更新 | 实现复杂 | ⭐⭐⭐⭐⭐ |

### 2.2 推荐方案：本地JSON + Room数据库

#### 数据结构设计

```json
{
  "version": "1.0.0",
  "lastUpdated": "2026-05-03",
  "events": [
    {
      "id": "evt_19450503_001",
      "title": "第二次世界大战欧洲战场结束",
      "date": "05-03",
      "year": 1945,
      "category": "war",
      "shortDesc": "德国签署无条件投降书，标志着二战欧洲战场结束",
      "description": "1945年5月3日，德国最高统帅部代表在柏林卡尔斯霍斯特的苏军司令部签署了无条件投降书，标志着第二次世界大战欧洲战场的正式结束。这一事件标志着纳粹德国的彻底失败，为战后欧洲的重建奠定了基础。",
      "imageUrl": "events/ww2_end.jpg",
      "importance": 5,
      "tags": ["二战", "德国", "投降"]
    },
    {
      "id": "evt_19190503_001",
      "title": "五四运动爆发",
      "date": "05-04",
      "year": 1919,
      "category": "politics",
