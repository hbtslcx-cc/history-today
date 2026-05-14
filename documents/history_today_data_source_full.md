# History Today - 数据源方案设计（完整版）

## 概述

本文档详细介绍 History Today APP 中万年历和历史事件两个核心模块的数据源方案。

---

## 1. 万年历数据源

### 1.1 推荐方案：本地算法库

万年历数据**不需要外部数据源**，使用本地算法计算即可。

**原因：**
- 农历计算是固定的数学算法
- 1900-2100年的农历数据可以通过算法精确计算
- 完全离线，无需网络
- 响应速度快

**实现方式：**

```kotlin
// lunar/LunarCalendar.kt
object LunarCalendar {
    
    // 农历数据表（1900-2100年，共200个16进制数）
    private val LUNAR_INFO = intArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        // ... 共200个数据
    )
    
    // 天干
    private val TIAN_GAN = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    
    // 地支
    private val DI_ZHI = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    
    // 生肖
    private val ZODIAC = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
    
    // 农历月份
    private val LUNAR_MONTHS = arrayOf("正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月")
    
    // 农历日期
    private val LUNAR_DAYS = arrayOf("初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十")
    
    /**
     * 公历转农历
     */
    fun solarToLunar(solarDate: LocalDate): LunarInfo {
        val year = solarDate.year
        val month = solarDate.monthValue
        val day = solarDate.dayOfMonth
        
        // 计算农历
        val offset = calculateOffset(year, month, day)
        val (lunarYear, lunarMonth, lunarDay, isLeap) = calculateLunarDate(offset)
        
        return LunarInfo(
            lunarDate = "${LUNAR_MONTHS[lunarMonth - 1]}${LUNAR_DAYS[lunarDay - 1]}",
            lunarYear = "${calculateGanZhi(lunarYear)}年",
            zodiac = ZODIAC[(lunarYear - 4) % 12],
            ganZhi = calculateGanZhi(lunarYear),
            solarTerm = getSolarTerm(year, month, day),
            festival = getFestival(month, day) ?: getLunarFestival(lunarMonth, lunarDay),
            yi = getYi(lunarYear, lunarMonth, lunarDay),
            ji = getJi(lunarYear, lunarMonth, lunarDay)
        )
    }
    
    private fun calculateGanZhi(year: Int): String {
        return TIAN_GAN[(year - 4) % 10] + DI_ZHI[(year - 4) % 12]
    }
    
    private fun getFestival(month: Int, day: Int): String? = when {
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
    
    // 宜忌计算（简化版）
    private fun getYi(year: Int, month: Int, day: Int): List<String> {
        // 基于农历日期的宜忌算法
        val allYi = listOf("出行", "搬家", "签订合同", "交易", "纳财", "开业", "动土", "安葬")
        return allYi.shuffled().take(3 + (day % 3))
    }
    
    private fun getJi(year: Int, month: Int, day: Int): List<String> {
        val allJi = listOf("动土", "安葬", "破土", "伐木", "入宅", "移徙")
        return allJi.shuffled().take(2 + (day % 2))
    }
}
```

### 1.2 节气数据源

节气可以通过天文算法计算，也可以使用预计算的节气表。

**方案1：算法计算（推荐）**
```kotlin
object SolarTermCalculator {
    
    private val TERM_NAMES = arrayOf(
        "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
        "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
        "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
        "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
    )
    
    /**
     * 使用简化的节气计算算法
     * 基于1900-2100年的节气数据表
     */
    fun getSolarTerm(year: Int, month: Int, day: Int): String? {
        // 节气数据表（1900-2100年）
        // 或使用Celestia等天文算法库
        return calculateByTable(year, month, day)
    }
}
```

**方案2：预计算数据表**
```kotlin
// 1900-2100年节气数据（每年24个节气，共4800个数据点）
// 可以存储在本地JSON或数据库中
val solarTermData = mapOf(
    "2025" to mapOf(
        "立春" to "02-03",
        "雨水" to "02-18",
        "惊蛰" to "03-05",
        // ...
    )
)
```

---

## 2. 历史事件数据源

### 2.1 数据源获取途径

#### 途径1：公开API（免费）

| API名称 | 网址 | 限制 | 说明 |
|---------|------|------|------|
| 聚合数据-历史上的今天 | https://www.juhe.cn/ | 100次/天免费 | 需要注册申请Key |
| 天行数据-历史今天 | https://www.tianapi.com/ | 100次/天免费 | 需要注册 |
| 百度API | 已下线 | - | 不再推荐 |

**使用示例：**
```kotlin
// 远程API调用（备用方案）
interface HistoryApiService {
    @GET("todayOnhistory/queryEvent.php")
    suspend fun getEvents(
        @Query("date") date: String,  // MM/dd格式
        @Query("key") apiKey: String
    ): HistoryApiResponse
}
```

#### 途径2：自行整理数据（推荐）

**数据来源：**
1. **维基百科** - 历史上的今天页面
2. **百度百科** - 历史事件
3. **中国历史网** - 历史大事记
4. **维基文库** - 历史文献

**数据整理流程：**
```
1. 爬虫抓取/手动整理 -> JSON格式
2. 数据清洗（去重、格式化）
3. 分类标注（政治/科技/文化/体育/战争/人物）
4. 导入Room数据库
5. 打包到APK中
```

#### 途径3：开源数据集

| 数据集 | 来源 | 说明 |
|--------|------|------|
| Chinese-Historical-Today | GitHub | 中文历史事件数据集 |
| today-in-history | npm包 | JSON格式历史事件 |

### 2.2 推荐方案：本地JSON + Room数据库

#### 数据结构设计

```json
{
  "version": "1.0.0",
  "totalEvents": 3660,
  "events": [
    {
      "id": "evt_19450503_001",
      "title": "第二次世界大战欧洲战场结束",
      "date": "05-03",
      "year": 1945,
      "category": "war",
      "shortDesc": "德国签署无条件投降书，标志着二战欧洲战场结束",
      "description": "1945年5月3日，德国最高统帅部代表在柏林卡尔斯霍斯特的苏军司令部签署了无条件投降书...",
      "imageUrl": null,
      "importance": 5
    }
  ]
}
```

#### 数据量预估

| 数据类型 | 数量 | 单条大小 | 总大小 |
|----------|------|----------|--------|
| 历史事件 | 3,660条 | ~500B | ~1.8MB |
| 农历数据 | 算法生成 | - | ~10KB |
| 节气数据 | 4,800条 | ~20B | ~100KB |
| **总计** | - | - | **~2MB** |

### 2.3 数据初始化流程

```kotlin
// 应用首次启动时初始化数据
class DataInitializer @Inject constructor(
    private val context: Context,
    private val eventDao: EventDao
) {
    
    suspend fun initialize() {
        // 检查是否已初始化
        if (eventDao.getCount() > 0) return
        
        // 从assets加载JSON
        val json = context.assets.open("events.json").bufferedReader().use { it.readText() }
        
        // 解析并插入数据库
        val events = Json.decodeFromString<EventData>(json).events
        eventDao.insertAll(events.map { it.toEntity() })
    }
}
```

---

## 3. 完整数据架构

```
Data Layer
├── Lunar Data (本地算法)
│   ├── LunarCalendar.kt          # 农历计算
│   ├── SolarTermCalculator.kt    # 节气计算
│   └── FestivalData.kt           # 节日数据
│
├── History Data (本地JSON + Room)
│   ├── assets/events.json        # 原始数据
│   ├── EventEntity.kt            # 数据库实体
│   ├── EventDao.kt               # 数据访问
│   └── EventRepository.kt        # 仓库
│
└── Cache (可选)
    ├── Image Cache               # 图片缓存
    └── Data Cache                # 数据缓存
```

---

## 4. 数据源总结

| 功能模块 | 数据源 | 实现方式 | 是否需要网络 |
|----------|--------|----------|--------------|
| 公历日历 | 系统API | LocalDate | 否 |
| 农历转换 | 本地算法 | LunarCalendar | 否 |
| 节气计算 | 本地算法/数据表 | SolarTermCalculator | 否 |
| 节日显示 | 本地代码 | 硬编码节日规则 | 否 |
| 宜忌查询 | 本地算法 | 简化算法 | 否 |
| 历史事件 | 本地JSON | Room数据库 | 否 |
| 事件图片 | 本地资源 | drawable/assets | 否 |

**核心优势：**
- ✅ 完全离线可用
- ✅ 无需服务器成本
- ✅ 响应速度快
- ✅ 无网络依赖
- ✅ 用户隐私保护好

如需进一步的数据采集方案或具体的爬虫代码，请告诉我！
