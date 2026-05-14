# History Today - Android APP 研发计划

## 项目概述

| 项目 | 内容 |
|------|------|
| 项目名称 | History Today（历史上的今天） |
| 平台 | Android |
| 开发语言 | Kotlin |
| 架构 | MVVM + Clean Architecture |
| UI框架 | Jetpack Compose |
| 预计工期 | 4周 |
| 版本 | v1.0.0 |

---

## 1. 技术栈选型

### 1.1 核心技术

| 层级 | 技术/框架 | 版本 | 用途 |
|------|-----------|------|------|
| 编程语言 | Kotlin | 1.9.x | 主要开发语言 |
| UI框架 | Jetpack Compose | BOM 2024.02 | 声明式UI |
| 架构组件 | ViewModel + LiveData/Flow | - | 状态管理 |
| 依赖注入 | Hilt | 2.50 | DI框架 |
| 异步处理 | Kotlin Coroutines + Flow | - | 异步编程 |
| 本地存储 | Room | 2.6.x | 数据库 |
| 图片加载 | Coil | 2.5.x | 图片加载 |
| 序列化 | Kotlinx Serialization | 1.6.x | JSON处理 |
| 导航 | Jetpack Navigation | 2.7.x | 页面导航 |

### 1.2 开发工具

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
│   │   │   │   ├── home/
│   │   │   │   ├── detail/
│   │   │   │   └── components/
│   │   │   └── di/
│   │   └── res/
│   └── build.gradle.kts
├── domain/                       # 领域层（纯Kotlin模块）
│   ├── src/main/java/
│   │   ├── model/                # 数据模型
│   │   ├── repository/           # 仓库接口
│   │   └── usecase/              # 用例
│   └── build.gradle.kts
├── data/                         # 数据层
│   ├── src/main/java/
│   │   ├── local/                # 本地数据源
│   │   │   ├── database/
│   │   │   └── entity/
│   │   ├── remote/               # 远程数据源（预留）
│   │   ├── repository/           # 仓库实现
│   │   └── mapper/               # 数据映射
│   └── build.gradle.kts
└── build.gradle.kts
```

### 2.2 架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer (Compose)                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │  HomeScreen │  │DetailScreen │  │   Components        │  │
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
│  │  │  Local  │  │ Remote  │  │ Repository实现  │     │   │
│  │  │ (Room)  │  │ (API)   │  │                 │     │   │
│  │  └─────────┘  └─────────┘  └─────────────────┘     │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. 开发计划

### 第一阶段：项目搭建（第1周）

#### Week 1 Day 1-2：环境搭建与项目初始化

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 创建Android项目 | Android开发 | 基础项目结构 |
| 配置Gradle和依赖 | Android开发 | build.gradle.kts |
| 配置Hilt依赖注入 | Android开发 | Application类、Module配置 |
| 配置Compose环境 | Android开发 | Theme、Color、Typography |

#### Week 1 Day 3-4：基础架构搭建

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 创建多模块结构 | Android开发 | domain/data/app模块 |
| 定义领域模型 | Android开发 | HistoryEvent.kt等 |
| 定义Repository接口 | Android开发 | EventRepository.kt |
| 配置Room数据库 | Android开发 | Database、Entity、DAO |

#### Week 1 Day 5：数据层实现

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 准备本地JSON数据 | Android开发 | events.json |
| 实现本地数据源 | Android开发 | LocalDataSource.kt |
| 实现Repository | Android开发 | EventRepositoryImpl.kt |
| 数据映射器 | Android开发 | DataMapper.kt |

### 第二阶段：核心功能开发（第2-3周）

#### Week 2 Day 1-3：首页开发

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 首页ViewModel | Android开发 | HomeViewModel.kt |
| 日期展示组件 | Android开发 | DateHeader.kt |
| 分类筛选栏 | Android开发 | CategoryFilter.kt |
| 时间线列表 | Android开发 | TimelineList.kt |
| 事件卡片 | Android开发 | EventCard.kt |

#### Week 2 Day 4-5：交互功能

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 日期选择器 | Android开发 | DatePickerDialog.kt |
| 左右滑动切换日期 | Android开发 | 手势处理 |
| 分类筛选逻辑 | Android开发 | 筛选状态管理 |
| 列表下拉刷新 | Android开发 | PullToRefresh |

#### Week 3 Day 1-3：详情页开发

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 详情页ViewModel | Android开发 | DetailViewModel.kt |
| 详情页UI | Android开发 | DetailScreen.kt |
| 图片展示 | Android开发 | EventImage.kt |
| 返回导航 | Android开发 | 导航处理 |

#### Week 3 Day 4-5：导航与状态管理

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 配置Navigation | Android开发 | NavGraph.kt |
| 页面转场动画 | Android开发 | 动画配置 |
| 状态持久化 | Android开发 | SavedStateHandle |
| 错误处理 | Android开发 | ErrorHandler.kt |

### 第三阶段：优化与测试（第4周）

#### Week 4 Day 1-2：性能优化

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 列表性能优化 | Android开发 | LazyColumn优化 |
| 图片缓存优化 | Android开发 | Coil配置优化 |
| 启动速度优化 | Android开发 | Startup优化 |
| 内存优化 | Android开发 | 内存泄漏检查 |

#### Week 4 Day 3-4：测试

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 单元测试 | Android开发 | UseCase测试 |
| UI测试 | Android开发 | Compose测试 |
| 集成测试 | Android开发 | 端到端测试 |
| 手动测试 | 测试人员 | 测试报告 |

#### Week 4 Day 5：发布准备

| 任务 | 负责人 | 产出物 |
|------|--------|--------|
| 应用签名配置 | Android开发 | keystore |
| 应用图标 | UI设计 | 各尺寸图标 |
| 应用截图 | Android开发 | 商店截图 |
| 隐私政策 | 产品经理 | 隐私政策文档 |
| 应用打包 | Android开发 | APK/AAB |

---

## 4. 详细任务分解

### 4.1 数据模型定义

```kotlin
// domain/model/HistoryEvent.kt
data class HistoryEvent(
    val id: String,
    val title: String,
    val date: String, // MM-DD
    val year: Int,
    val category: EventCategory,
    val description: String,
    val shortDesc: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)

enum class EventCategory {
    ALL, POLITICS, TECH, CULTURE, SPORTS, WAR, PEOPLE;
    
    fun getDisplayName(): String = when (this) {
        ALL -> "全部"
        POLITICS -> "政治"
        TECH -> "科技"
        CULTURE -> "文化"
        SPORTS -> "体育"
        WAR -> "战争"
        PEOPLE -> "人物"
    }
}
```

### 4.2 数据库设计

```kotlin
// data/local/entity/EventEntity.kt
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val date: String,
    val year: Int,
    val category: String,
    val description: String,
    val shortDesc: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)

// data/local/dao/EventDao.kt
@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE date = :date ORDER BY year DESC")
    suspend fun getEventsByDate(date: String): List<EventEntity>
    
    @Query("SELECT * FROM events WHERE date = :date AND category = :category ORDER BY year DESC")
    suspend fun getEventsByDateAndCategory(date: String, category: String): List<EventEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)
    
    @Query("SELECT COUNT(*) FROM events")
    suspend fun getCount(): Int
}
```

### 4.3 Repository接口

```kotlin
// domain/repository/EventRepository.kt
interface EventRepository {
    suspend fun getEventsByDate(date: String, category: EventCategory = EventCategory.ALL): List<HistoryEvent>
    suspend fun getEventById(id: String): HistoryEvent?
    suspend fun refreshData(): Result<Unit>
}
```

### 4.4 UseCase定义

```kotlin
// domain/usecase/GetEventsByDateUseCase.kt
class GetEventsByDateUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(date: String, category: EventCategory): List<HistoryEvent> {
        return repository.getEventsByDate(date, category)
    }
}

// domain/usecase/GetEventDetailUseCase.kt
class GetEventDetailUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: String): HistoryEvent? {
        return repository.getEventById(id)
    }
}
```

### 4.5 ViewModel实现

```kotlin
// ui/home/HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEventsByDate: GetEventsByDateUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadEvents()
    }
    
    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        loadEvents()
    }
    
    fun onCategorySelected(category: EventCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadEvents()
    }
    
    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val dateStr = _uiState.value.selectedDate.format(DateTimeFormatter.ofPattern("MM-dd"))
                val events = getEventsByDate(dateStr, _uiState.value.selectedCategory)
                _uiState.update { it.copy(events = events, isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

data class HomeUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedCategory: EventCategory = EventCategory.ALL,
    val events: List<HistoryEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### 4.6 UI组件清单

| 组件名称 | 文件路径 | 功能描述 |
|----------|----------|----------|
| HomeScreen | ui/home/HomeScreen.kt | 首页主屏幕 |
| DateHeader | ui/home/components/DateHeader.kt | 日期展示头部 |
| CategoryFilter | ui/home/components/CategoryFilter.kt | 分类筛选栏 |
| TimelineList | ui/home/components/TimelineList.kt | 时间线列表 |
| EventCard | ui/home/components/EventCard.kt | 事件卡片 |
| DetailScreen | ui/detail/DetailScreen.kt | 详情页 |
| EventImage | ui/components/EventImage.kt | 事件图片组件 |
| LoadingIndicator | ui/components/LoadingIndicator.kt | 加载指示器 |
| ErrorView | ui/components/ErrorView.kt | 错误视图 |
| EmptyView | ui/components/EmptyView.kt | 空状态视图 |

---

## 5. 项目时间表

```
Week 1: 项目搭建
├─ Day 1-2: 环境搭建与项目初始化
├─ Day 3-4: 基础架构搭建
└─ Day 5: 数据层实现

Week 2: 首页开发
├─ Day 1-3: 首页UI与ViewModel
└─ Day 4-5: 交互功能实现

Week 3: 详情页与导航
├─ Day 1-3: 详情页开发
└─ Day 4-5: 导航与状态管理

Week 4: 优化与测试
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
| 隐私政策 | 隐私政策文档 |

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
| 数据量过大导致包体积超标 | 高 | 采用远程API+本地缓存方案 |
| 列表滚动卡顿 | 中 | 使用LazyColumn、图片懒加载 |
| 日期切换动画不流畅 | 中 | 优化状态管理、减少重组 |
| 低版本Android兼容性问题 | 低 | 充分测试API 24-34 |

---

## 8. 验收标准

### 8.1 功能验收

- [ ] 首页正常展示当天历史事件
- [ ] 日期选择器可正常切换日期
- [ ] 左右滑动手势可切换日期
- [ ] 分类筛选功能正常
- [ ] 事件卡片点击进入详情页
- [ ] 详情页展示完整事件信息
- [ ] 返回按钮正常返回首页

### 8.2 性能验收

- [ ] 冷启动时间 ≤ 2秒
- [ ] 页面切换流畅，无卡顿
- [ ] 列表滚动60fps
- [ ] 内存占用 ≤ 150MB
- [ ] APK体积 ≤ 30MB

### 8.3 兼容性验收

- [ ] Android 7.0 - Android 14 正常运行
- [ ] 手机、平板适配正常
- [ ] 竖屏、横屏显示正常

---

**文档版本**: v1.0  
**创建日期**: 2026-05-03  
**最后更新**: 2026-05-03
