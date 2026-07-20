# MITE-Recrafted

## Progress

Developing

## License

<a href="https://github.com/A-Team-PJnCeADc/mite-recrafted">mite-recrafted</a> © 2026 by <a href="https://github.com/A-Team-PJnCeADc">A-Team-PJnCeADc</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International</a><img src="https://mirrors.creativecommons.org/presskit/icons/cc.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/by.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/nc.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/sa.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;">

---

## 项目概述

MITE-Recrafted 是 [MITE (Many Interactions, Tiny Enhancements)](https://www.mcmod.cn/class/226.html) 模组的高版本移植项目，基于 **Fabric** 框架开发，目标 Minecraft 版本为 **26.2**。

原 MITE 模组以其硬核的生存体验和丰富的游戏机制而闻名，本项目致力于将这些经典特性带到更新的 Minecraft 版本中。

## 主要特性

### 🍎 营养系统
- **蛋白质** - 维持生命必需的营养素
- **植物营养素** - 来自植物食物的营养成分
- **必需脂肪酸** - 维持身体健康的脂肪来源
- **糖分** - 提供能量，但过量会导致胰岛素抵抗
- **胰岛素抵抗** - 衡量身体对糖分的耐受程度
- **营养值** - 次级储备，影响回血速度

### ⚔️ 工具系统
支持多种工具类型和材料组合：

**工具类型**：
- Axe（斧头）、Pickaxe（镐）、Shovel（铲）、Hoe（锄）、Scythe（镰刀）
- Sword（剑）、Hatchet（短柄斧）、Dagger（匕首）、Battle Axe（战斧）
- War Hammer（战锤）、Mattock（鹤嘴锄）、Shears（剪刀）、Knife（刀）、Fishing Rod（钓鱼竿）

**工具材料**：
- Flint（燧石）、Obsidian（黑曜石）
- Copper（铜）、Silver（银）、Gold（金）、Rusted Iron（生锈铁）、Iron（铁）
- Ancient Metal（古代金属）、Mithril（秘银）、Adamantium（精金）

### 🏆 品质系统
物品品质从低到高分为8个等级：
- Wretched → Poor → Average → Fine → Excellent → Superb → Masterwork → Legendary

品质影响物品的耐久度和属性表现。

### 🏗️ 工作台系统
不同材质的工作台，提供多样化的合成体验：
- Flint、Copper、Silver、Gold、Iron、Ancient Metal、Mithril、Adamantium、Obsidian

### 🍲 食物系统
- **碗装食物**：蔬菜汤、蘑菇汤、奶油蘑菇汤、南瓜汤等
- **桶装食物**：牛奶桶（多种金属材质）
- **普通食物**：洋葱、香蕉、奶酪、巧克力等

### 🎯 箭矢系统
- 多种材质的箭矢：燧石、铜、银、金、铁、古代金属、秘银、精金、黑曜石等
- 特殊箭矢：火把箭、布箭、刀片箭、冰箭

### 📦 其他特性
- 自定义方块：金属方块、门、栏杆等
- 音乐唱片：收录 MITE 经典音乐
- AppleSkin 集成：在 HUD 中显示营养信息
- 多人游戏支持：营养数据同步

## 技术栈

| 组件 | 版本 |
|------|------|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3 |
| Fabric API | 0.154.2+26.2 |
| Loom | 1.17-SNAPSHOT |
| Java | 25 |

## 依赖

- **Fabric API** - 必需
- **AppleSkin** - 可选，用于在 HUD 显示营养信息

## 构建

```bash
# 克隆仓库
git clone https://github.com/A-Team-PJnCeADc/mite-recrafted.git

# 进入项目目录
cd mite-recrafted

# 构建模组
./gradlew build

# 构建产物位于 build/libs/
```

## 开发

```bash
# 运行开发环境
./gradlew runClient

# 生成数据
./gradlew runData
```

## 项目结构

```
mite-recrafted/
├── src/main/java/com/mite/recraft/
│   ├── block/          # 方块相关
│   ├── component/      # 数据组件
│   ├── entity/         # 实体相关
│   ├── item/           # 物品相关
│   │   ├── tools/      # 工具系统
│   │   ├── quality/    # 品质系统
│   │   └── moditems/   # 自定义物品
│   ├── mixin/          # Mixin 注入
│   ├── network/        # 网络同步
│   └── MiteRecrafted.java
├── src/main/resources/
│   ├── assets/         # 资源文件
│   ├── fabric.mod.json # 模组配置
│   └── mixins/         # Mixin 配置
└── build.gradle
```

## 贡献

欢迎提交 Pull Request 和 Issue！

## 致谢

感谢原 MITE 模组的开发者为 Minecraft 社区带来如此精彩的模组体验。
