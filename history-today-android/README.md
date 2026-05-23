# History Today - Android APP

一款集历史上的今天与万年历于一体的综合性日历应用。

## 功能特点

### 📅 万年历功能
- 公历/农历显示
- 二十四节气
- 黄历宜忌查询
- 干支纪年
- 生肖显示

### 📜 历史事件
- 历史上的今天事件展示
- 分类筛选（政治、科技、文化、体育、战争、人物）
- 事件详情页
- 时间线浏览

## 技术架构

### 架构模式
- **MVVM + Clean Architecture**

### 核心技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Kotlin | 1.9.x | 主要开发语言 |
| Jetpack Compose | BOM 2024.02 | 声明式UI框架 |
| ViewModel + StateFlow | - | 状态管理 |
| Hilt | 2.50 | 依赖注入 |
| Room | 2.6.x | 本地数据库 |
| Coil | 2.5.x | 图片加载 |
| Navigation | 2.7.x | 页面导航 |

## 项目结构

```
history-today-android/
├── app/                          # 应用主模块
│   ├── src/main/java/com/historytoday/
│   │   ├── MainActivity.kt
│   │   ├── HistoryTodayApp.kt
│   │   ├── navigation/           # 导航配置
│   │   ├── ui/                    # UI层
│   │   │   ├── calendar/          # 日历页面
│   │   │   ├── history/           # 历史事件页面
│   │   │   ├── detail/            # 详情页面
│   │   │   ├── components/        # 公共组件
│   │   │   └── theme/             # 主题样式
│   │   ├── lunar/                 # 农历计算
│   │   ├── viewmodel/             # ViewModel层
│   │   └── di/                    # 依赖注入
│   └── src/main/assets/
│       └── events.json            # 历史事件数据
├── domain/                        # 领域层
│   ├── model/                     # 数据模型
│   ├── repository/                # 仓库接口
│   └── usecase/                   # 用例
└── data/                          # 数据层
    ├── local/                     # 本地数据源
    ├── repository/                # 仓库实现
    └── mapper/                    # 数据转换
```

## 开发环境

- Android Studio Hedgehog
- Gradle 8.2
- minSdk 24
- targetSdk 34
- compileSdk 34

## 构建命令

```bash
# 构建APK
./gradlew assembleDebug

# 构建发布APK
./gradlew assembleRelease

# 运行测试
./gradlew test

# 清理构建
./gradlew clean
```

## 数据来源

- **农历数据**: 本地算法计算（1900-2100年）
- **历史事件**: 本地JSON数据（约50条历史事件）

## 许可证

MIT License
