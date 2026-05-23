# History Today - Android APP 研发计划 V2.0（万年历+历史）

## 项目概述

| 项目 | 内容 |
|------|------|
| 项目名称 | History Today（历史上的今天 + 万年历） |
| 平台 | Android |
| 开发语言 | Kotlin |
| 架构 | MVVM + Clean Architecture |
| UI框架 | Jetpack Compose |
| 预计工期 | 6周 |
| 版本 | v2.0.0 |

---

## 1. 技术栈选型

### 1.1 核心技术

| 层级 | 技术/框架 | 版本 | 用途 |
|------|-----------|------|------|
| 编程语言 | Kotlin | 1.9.x | 主要开发语言 |
| UI框架 | Jetpack Compose | BOM 2024.02 | 声明式UI |
| 架构组件 | ViewModel + StateFlow | - | 状态管理 |
| 依赖注入 | Hilt | 2.50 | DI框架 |
| 异步处理 | Kotlin Coroutines + Flow | - | 异步编程 |
| 本地存储 | Room | 2.6.x | 数据库 |
| 图片加载 | Coil | 2.5.x | 图片加载 |
| 序列化 | Kotlinx Serialization | 1.6.x | JSON处理 |
| 导航 | Jetpack Navigation | 2.7.x | 页面导航 |

### 1.2 农历计算

| 方案 | 说明 | 优先级 |
|------|------|--------|
| 本地算法库 | 引入已验证的农历计算库 | P0 |
| 自定义算法 | 自行实现农历转换算法 | P1 |

### 1.3 开发工具

| 工具 | 版本 | 用途 |
|------|------|------|
| Android Studio | Hedgehog | IDE |
| Gradle | 8.2 | 构建工具 |
| minSdk | 24 | 最低支持版本 |
| targetSdk | 34 | 目标版本 |
| compileSdk | 34 | 编译版本 |

---

## 2. 项目架构

### 2.1 模块划分

```
history-today-android/
├── app/                          # 应用主模块
│   ├── src/main/
│   │   ├── java/com/historytoday/
│   │   │   ├── MainActivity.kt
│   │   │   ├── HistoryTodayApp.kt
│   │   │   ├── navigation/
│   │   │   ├── ui/
│   │   │   │   ├── calendar/     # 万年历页面
│   │   │   │   ├── history/      # 历史事件页面
│   │   │   │   ├── detail/       # 详情页面
│   │   │   │   └── components/   # 公共组件
│   │   │   ├── lunar/            # 农历计算
│   │   │   └── di/
│   │   └── res/
│   └── build.gradle.kts
├── domain/                       # 领域层
│   ├── model/                    # 数据模型
│   ├── repository/               # 仓库接口
│   └── usecase/                  # 用例
├── data/                         # 数据层
│   ├── local/                    # 本地数据源
│   ├── repository/               # 仓库实现
│   └── mapper/
└── build.gradle.kts
```

### 2.2 架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │CalendarScreen│  │HistoryScreen│  │   DetailScreen      │  │
│  └──────┬──────┘  └──────┬──────┘  └─────────────────────┘  │
│         │                │                                   │
│         └────────────────┘                                   │
│              ViewModel                                       │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────┼────────────────────────────────────┐
│                   Domain Layer                             │
│  ┌─────────────────────┼───────────────────────────────┐   │
│  │                     ▼                               │   │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────────────┐     │   │
│  │  │  Model  │  │ UseCase │  │ Repository接口  │     │   │
│  │  └─────────┘  └─────────┘  └─────────────────┘     │   │
│  └─────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────┼────────────────────────────────────┐
│                   Data Layer                               │
│  ┌─────────────────────┼───────────────────────────────┐   │
│  │                     ▼                               │   │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────────────┐     │   │
│  │  │  Local  │  │  Lunar  │  │ Repository实现  │     │   │
│  │  │ (Room)  │  │ (算法)  │  │                 │     │   │
│  │  └─────────┘  └─────────┘  └─────────────────┘     │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. 开发计划

### 第一阶段：项目搭建与农历算法（第1周）

#### Week 1 Day 1-2：环境搭建

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 创建Android项目 | Android开发 | 基础项目结构 |
| 配置Gradle和依赖 | Android开发 | build.gradle.kts |
| 配置Hilt依赖注入 | Android开发 | Application类、Module配置 |
| 配置Compose环境 | Android开发 | Theme、Color、Typography |

#### Week 1 Day 3-5：农历算法与数据层

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 引入/实现农历算法 | Android开发 | LunarCalendar.kt |
| 定义领域模型 | Android开发 | CalendarDay、LunarInfo等 |
| 配置Room数据库 | Android开发 | Database、Entity、DAO |
| 准备历史事件数据 | Android开发 | events.json |

### 第二阶段：万年历模块（第2-3周）

#### Week 2 Day 1-3：日历视图

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 日历首页ViewModel | Android开发 | CalendarViewModel.kt |
| 月视图组件 | Android开发 | MonthView.kt |
| 日历格子组件 | Android开发 | CalendarDayCell.kt |
| 星期栏组件 | Android开发 | WeekdayHeader.kt |

#### Week 2 Day 4-5：农历与宜忌

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 农历信息展示 | Android开发 | LunarInfoCard.kt |
| 宜忌展示组件 | Android开发 | YiJiCard.kt |
| 节气节日标记 | Android开发 | SolarTermLabel.kt |
| 日期切换逻辑 | Android开发 | 月份切换、选中逻辑 |

#### Week 3 Day 1-3：日历交互

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 日期选择器 | Android开发 | DatePickerDialog.kt |
| 左右滑动手势 | Android开发 | 月份切换手势 |
| 快速返回今天 | Android开发 | BackToTodayButton.kt |
| 历史事件入口 | Android开发 | HistoryEntryCard.kt |

#### Week 3 Day 4-5：日历优化

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 日历性能优化 | Android开发 | Lazy加载优化 |
| 动画效果 | Android开发 | 切换动画 |
| 状态管理优化 | Android开发 | StateFlow优化 |

### 第三阶段：历史事件模块（第4-5周）

#### Week 4 Day 1-3：历史事件页

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 历史事件ViewModel | Android开发 | HistoryViewModel.kt |
| 历史事件页UI | Android开发 | HistoryScreen.kt |
| 分类筛选栏 | Android开发 | CategoryFilter.kt |
| 时间线列表 | Android开发 | TimelineList.kt |

#### Week 4 Day 4-5：事件卡片与详情

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 事件卡片组件 | Android开发 | EventCard.kt |
| 事件详情页 | Android开发 | DetailScreen.kt |
| 图片展示 | Android开发 | EventImage.kt |
| 相关推荐 | Android开发 | RelatedEvents.kt |

#### Week 5 Day 1-3：导航与交互

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 页面导航配置 | Android开发 | NavGraph.kt |
| 页面转场动画 | Android开发 | 动画配置 |
| 手势返回处理 | Android开发 | BackHandler |
| 深度链接 | Android开发 | DeepLink配置 |

#### Week 5 Day 4-5：数据关联

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 日历与事件关联 | Android开发 | 日期-事件查询 |
| 事件计数显示 | Android开发 | EventCountBadge.kt |
| 数据预加载 | Android开发 | 缓存策略 |

### 第四阶段：优化与测试（第6周）

#### Week 6 Day 1-2：性能优化

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 启动优化 | Android开发 | Startup优化 |
| 列表性能优化 | Android开发 | LazyColumn优化 |
| 内存优化 | Android开发 | 内存泄漏检查 |
| 包体积优化 | Android开发 | 资源压缩 |

#### Week 6 Day 3-4：测试

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 单元测试 | Android开发 | UseCase测试、算法测试 |
| UI测试 | Android开发 | Compose测试 |
| 集成测试 | Android开发 | 端到端测试 |
| 农历算法测试 | Android开发 | 边界值测试 |

#### Week 6 Day 5：发布准备

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 应用签名配置 | Android开发 | keystore |
| 应用图标 | UI设计 | 各尺寸图标 |
| 应用截图 | Android开发 | 商店截图 |
| 应用打包 | Android开发 | APK/AAB |

---

## 4. 详细任务分解

### 4.1 农历算法模块

```kotlin
// lunar/LunarCalendar.kt
object LunarCalendar {
    
    /**
     * 公历转农历
     */
    fun solarToLunar(solarDate: LocalDate): LunarInfo {
        // 农历计算逻辑
    }
    
    /**
     * 获取二十四节气
     */
    fun getSolarTerm(year: Int, month: Int, day: Int): String? {
        // 节气计算逻辑
    }
    
    /**
     * 获取干支纪年
     */
    fun getGanZhiYear(year: Int): String {
        // 干支计算逻辑
    }
    
    /**
     * 获取生肖
     */
    fun getZodiac(year: Int): String {
        // 生肖计算逻辑
    }
    
    /**
     * 获取宜忌
     */
    fun getYiJi(lunarDate: String): Pair<List<String>, List<String>> {
        // 宜忌计算逻辑
    }
}
```

### 4.2 数据模型

```kotlin
// domain/model/CalendarDay.kt
data class CalendarDay(
    val date: LocalDate,
    val lunarInfo: LunarInfo,
    val isToday: Boolean,
    val isSelected: Boolean,
    val eventCount: Int,
    val hasSolarTerm: Boolean,
    val hasFestival: Boolean
)

// domain/model/LunarInfo.kt
data class LunarInfo(
    val lunarDate: String,      // 农历日期（如：四月初六）
    val lunarYear: String,      // 农历年份（如：乙巳年）
    val zodiac: String,         // 生肖
    val ganZhi: String,         // 干支
    val solarTerm: String?,     // 节气
    val festival: String?,      // 节日
    val yi: List<String>,       // 宜
    val ji: List<String>        // 忌
)

// domain/model/HistoryEvent.kt
data class HistoryEvent(
    val id: String,
    val title: String,
    val date: String,           // MM-DD
    val year: Int,
    val category: EventCategory,
    val description: String,
    val shortDesc: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)
```

### 4.3 Repository接口

```kotlin
// domain/repository/CalendarRepository.kt
interface CalendarRepository {
    suspend fun getCalendarMonth(year: Int, month: Int): List<CalendarDay>
    suspend fun getLunarInfo(date: LocalDate): LunarInfo
    suspend fun getEventsByDate(date: String): List<HistoryEvent>
}

// domain/repository/EventRepository.kt
interface EventRepository {
    suspend fun getEventsByDate(date: String, category: EventCategory): List<HistoryEvent>
    suspend fun getEventById(id: String): HistoryEvent?
    suspend fun searchEvents(query: String): List<HistoryEvent>
}
```

### 4.4 UseCase定义

```kotlin
// domain/usecase/GetCalendarMonthUseCase.kt
class GetCalendarMonthUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(year: Int, month: Int): List<CalendarDay> {
        return repository.getCalendarMonth(year, month)
    }
}

// domain/usecase/GetLunarInfoUseCase.kt
class GetLunarInfoUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(date: LocalDate): LunarInfo {
        return repository.getLunarInfo(date)
    }
}

// domain/usecase/GetEventsByDateUseCase.kt
class GetEventsByDateUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(date: String, category: EventCategory): List<HistoryEvent> {
        return repository.getEventsByDate(date, category)
    }
}
```

### 4.5 ViewModel实现

```kotlin
// ui/calendar/CalendarViewModel.kt
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarMonth: GetCalendarMonthUseCase,
    private val getLunarInfo: GetLunarInfoUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    
    init {
        loadCalendarMonth()
    }
    
    fun onDateSelected(date: LocalDate) {
        _uiState.update { 
            it.copy(selectedDate = date)
        }
        loadLunarInfo(date)
    }
    
    fun onMonthChanged(year: Int, month: Int) {
        _uiState.update { 
            it.copy(currentYear = year, currentMonth = month)
        }
        loadCalendarMonth()
    }
    
    private fun loadCalendarMonth() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val days = getCalendarMonth(
                    _uiState.value.currentYear, 
                    _uiState.value.currentMonth
                )
                _uiState.update { 
                    it.copy(calendarDays = days, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
    
    private fun loadLunarInfo(date: LocalDate) {
        viewModelScope.launch {
            val lunarInfo = getLunarInfo(date)
            _uiState.update { 
                it.copy(currentLunarInfo = lunarInfo)
            }
        }
    }
}

data class CalendarUiState(
    val currentYear: Int = LocalDate.now().year,
    val currentMonth: Int = LocalDate.now().monthValue,
    val selectedDate: LocalDate = LocalDate.now(),
    val calendarDays: List<CalendarDay> = emptyList(),
    val currentLunarInfo: LunarInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### 4.6 UI组件清单

| 组件名称 | 文件路径 | 功能描述 |
|----------|----------|----------|
| CalendarScreen | ui/calendar/CalendarScreen.kt | 日历首页 |
| MonthView | ui/calendar/components/MonthView.kt | 月视图 |
| CalendarDayCell | ui/calendar/components/CalendarDayCell.kt | 日期格子 |
| WeekdayHeader | ui/calendar/components/WeekdayHeader.kt | 星期栏 |
| LunarInfoCard | ui/calendar/components/LunarInfoCard.kt | 农历信息卡 |
| YiJiCard | ui/calendar/components/YiJiCard.kt | 宜忌卡 |
| HistoryEntryCard | ui/calendar/components/HistoryEntryCard.kt | 历史事件入口 |
| HistoryScreen | ui/history/HistoryScreen.kt | 历史事件页 |
| CategoryFilter | ui/history/components/CategoryFilter.kt | 分类筛选 |
| TimelineList | ui/history/components/TimelineList.kt | 时间线列表 |
| EventCard | ui/history/components/EventCard.kt | 事件卡片 |
| DetailScreen | ui/detail/DetailScreen.kt | 详情页 |

---

## 5. 项目时间表

```
Week 1: 项目搭建与农历算法
├─ Day 1-2: 环境搭建
├─ Day 3-5: 农历算法与数据层

Week 2-3: 万年历模块
├─ Week 2 Day 1-3: 日历视图
├─ Week 2 Day 4-5: 农历与宜忌
├─ Week 3 Day 1-3: 日历交互
└─ Week 3 Day 4-5: 日历优化

Week 4-5: 历史事件模块
├─ Week 4 Day 1-3: 历史事件页
├─ Week 4 Day 4-5: 事件卡片与详情
├─ Week 5 Day 1-3: 导航与交互
└─ Week 5 Day 4-5: 数据关联

Week 6: 优化与测试
├─ Day 1-2: 性能优化
├─ Day 3-4: 测试
└─ Day 5: 发布准备
```

---

## 6. 交付物清单

### 6.1 代码交付物

| 交付物 | 说明 |
|--------|------|
| 源代码 | 完整Android项目代码 |
| 技术文档 | 架构说明、API文档 |
| 测试代码 | 单元测试、UI测试 |

### 6.2 产品交付物

| 交付物 | 说明 |
|--------|------|
| APK文件 | 安装包 |
| AAB文件 | Google Play上架包 |
| 应用图标 | 各尺寸图标资源 |
| 应用截图 | 商店展示截图 |

### 6.3 文档交付物

| 交付物 | 说明 |
|--------|------|
| 产品需求文档 | PRD |
| 技术设计文档 | 架构设计、数据库设计 |
| 测试报告 | 功能测试、性能测试报告 |
| 用户手册 | 使用说明 |

---

## 7. 风险与应对

| 风险 | 影响 | 应对措施 |
|------|------|----------|
| 农历算法准确性 | 高 | 使用已验证的开源算法，充分测试边界值 |
| 数据量过大 | 中 | 压缩历史事件数据，图片按需加载 |
| 日历渲染性能 | 中 | 使用Lazy加载，优化重组 |
| 低版本兼容性 | 低 | 充分测试API 24-34 |

---

## 8. 验收标准

### 8.1 功能验收

- [ ] 日历正确显示公历、农历
- [ ] 节气、节日正确显示
- [ ] 宜忌信息准确
- [ ] 月份切换流畅
- [ ] 日期选择器正常
- [ ] 历史事件按日期正确显示
- [ ] 分类筛选功能正常
- [ ] 事件详情页展示完整

### 8.2 性能验收

- [ ] 冷启动时间 ≤ 1.5秒
- [ ] 月份切换 ≤ 200ms
- [ ] 列表滚动60fps
- [ ] 内存占用 ≤ 120MB
- [ ] APK体积 ≤ 25MB

### 8.3 兼容性验收

- [ ] Android 7.0 - Android 14 正常运行
- [ ] 手机、平板适配正常
- [ ] 农历计算1900-2100年准确

---

**文档版本**: v2.0  
**创建日期**: 2026-05-03  
**最后更新**: 2026-05-03
