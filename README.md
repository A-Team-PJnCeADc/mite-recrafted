# MITE-Recrafted

> "一个人不可能只是走进群山，用手中的石镐挖下铁矿。"

MITE-Recrafted 是经典 Minecraft 难度模组 **[MITE (Minecraft Is Too Easy)](https://www.mcmod.cn/class/226.html)** 的高版本移植项目，基于 Fabric 模组加载器，目标 Minecraft 版本为 **26.2**（Java 25）。

原版 MITE 仅基于 Minecraft 1.6.4 核心，通过直接覆盖类文件实现，不兼容任何其他模组。本项目使用 Mixin 技术以非侵入方式重新实现 MITE 的核心机制，使其能够在现代 Minecraft 版本上运行，并具备与其他 Fabric 模组的兼容性。

## Progress

Developing

## 原版 MITE 简介

Minecraft实在是太简单了（MITE）是一款提高游戏的生存方面的难度的综合类MOD，通过重新定义并调整游戏机制来增加生存的难度。  
它是个独立模组，改变了大约500多个游戏的类文件，并且增加了许多新的内容，反作弊机制的引进使得这个模组不兼容于任何其它的 MOD。

> 注意：原版 MITE 所有版本仅基于 Minecraft 原版 1.6.4 核心，安装时直接覆盖核心的类文件，因此尝试强行安装 Forge 或者同种安装方式的的 MOD 很可能会导致 MITE 客户端损坏！

原版 MITE 详细内容参见：[MCBBS 维基](https://www.mcmod.cn/class/226.html) | [MITE:重生（高版本资料页）](https://www.mcmod.cn/class/3288.html)

---

## 已实现的功能

以下功能均基于对实际代码的审查，标注了当前在 MITE-Recrafted 中的实现状态。

### 金属材料体系

| 金属 | 碎片/粒 | 锭 | 链 | 币 | 储存块 | 栅栏 | 门 | 砧 | 空桶 | 水桶 | 岩浆桶 | 石桶 | 奶桶 |
|------|---------|-----|-----|-----|--------|------|-----|-----|-------|--------|--------|------|------|
| 铜 (Copper) | chip + nugget | 8 | 8 | 64 | yes | yes | yes | yes | yes | yes | yes | yes | yes |
| 银 (Silver) | nugget | 8 | 8 | 64 | yes | yes | yes | yes | yes | yes | yes | yes | yes |
| 金 (Gold) | nugget | 8 | 8 | 64 | yes | yes | yes | yes | yes | yes | yes | yes | yes |
| 锈铁 (Rusted Iron) | - | - | 8 | - | yes | yes | yes | - | - | - | - | - | - |
| 铁 (Iron) | nugget | 8 | 8 | - | yes | yes | yes | - | - | - | - | - | - |
| 远古金属 (Ancient Metal) | nugget | 8 | 8 | 64 | yes | yes | yes | yes | yes | yes | yes | yes | yes |
| 秘银 (Mithril) | nugget | 8 | 8 | 64 | yes | yes | yes | yes | yes | yes | yes | yes | yes |
| 艾德曼 (Adamantium) | nugget | 8 | 8 | 64 | yes | yes | yes | yes | yes | yes | yes | yes | yes |

此外还有非金属基础材料：燧石碎片 (Flint Chip)、黑曜石碎片 (Obsidian Chip)、绿宝石碎片 (Emerald Chip)、钻石碎片 (Diamond Chip)、下界石英碎片 (Nether Quartz Shard)、玻璃碎片 (Glass Shard)。

### 工具体系

MITE 重新定义了工具类型和材料等级体系：

**工具类型**（每种类型 × 可用材质 = 具体工具实例）：

| 工具类型 | 说明 | 可用材质 |
|----------|------|----------|
| 木棒 (Club) | 棍棒，初级近战武器 | Wood |
| 短斧 (Hatchet) | 手斧，采集原木的基础工具 | ALL (燧石+黑曜石+金属) |
| 弓 (Bow) | 远程武器 | Wood, Mithril, Ancient Metal |
| 镐 (Pickaxe) | 采集石头与矿石 | METALS |
| 锹 (Shovel) | 挖掘土/沙等 | ALL |
| 斧 (Axe) | 砍伐与战斗 | ALL |
| 剑 (Sword) | 近战武器 | METALS |
| 短剑 (Dagger) | 轻型近战 | METALS |
| 小刀 (Knife) | 切割工具 | ALL |
| 战斧 (Battle Axe) | 重型砍伐武器 | METALS |
| 战锤 (War Hammer) | 破坏与攻击 | METALS |
| 鹤嘴锄 (Mattock) | 战锄 | METALS |
| 锄 (Hoe) | 耕种 | METALS |
| 镰 (Scythe) | 收割 | METALS |
| 剪刀 (Shears) | 剪切 | METALS |
| 钓鱼竿 (Fishing Rod) | 钓鱼 | ALL |
| 箭 (Arrow) | 弹药 | ALL |

**材料等级与耐久系数**：

| 材料 | 耐久系数 | 最高品质 | 基础耐久 | 挖掘速度 | 附魔值 |
|------|----------|----------|----------|----------|--------|
| Wood | 0.5 | Fine | 200 | 1.0 | 1 |
| Flint | 1.0 | Fine | 400 | 1.25 | 1 |
| Obsidian | 2.0 | Fine | 800 | 1.5 | 8 |
| Copper | 4.0 | Excellent | 1600 | 1.75 | 12 |
| Silver | 4.0 | Excellent | 1600 | 1.75 | 10 |
| Gold | 4.0 | Superb | 1600 | 1.75 | 22 |
| Rusted Iron | 4.0 | Poor | 1600 | 1.25 | 8 |
| Iron | 8.0 | Masterwork | 3200 | 2.0 | 14 |
| Diamond | 16.0 | Superb | 6400 | 2.5 | 10 |
| Ancient Metal | 16.0 | Masterwork | 6400 | 2.0 | 18 |
| Mithril | 64.0 | Legendary | 25600 | 2.5 | 24 |
| Adamantium | 256.0 | Legendary | 102400 | 3.0 | 30 |

### 品质系统

每件工具/武器在制作时可消耗经验获得品质，品质影响耐久修正系数：

| 品质 | 等级 | 耐久修正 |
|------|------|----------|
| Wretched (粗劣) | 0 | ×0.5 |
| Poor (差) | 1 | ×0.75 |
| Average (普通) | 2 | ×1.0 |
| Fine (良) | 3 | ×1.5 |
| Excellent (优) | 4 | ×2.0 |
| Superb (精) | 5 | ×2.5 |
| Masterwork (杰作) | 6 | ×3.0 |
| Legendary (传奇) | 7 | ×3.5 |

### 营养系统

MITE 的营养系统已在 Fabric Attachment API 上完整实现：

- **蛋白质 (Protein)**：0~160000，每 tick -1，耗尽时营养不良
- **植物营养素 (Phytonutrients)**：0~160000，每 tick -1，耗尽时营养不良
- **必需脂肪酸 (Essential Fats)**：0~160000，每 tick -1
- **糖分 (Sugar Content)**：0~1000
- **胰岛素抵抗 (Insulin Resistance)**：0~192000，每 tick -1，由糖分 × 4.8 累积
- **营养值 (Nutrition)**：0~20，次级储备，食物条耗尽后消耗
- **营养不良**：蛋白质或植物营养素为 0 时触发，回血速度降至 25%

营养数据通过 `NutritionSyncPayload` 从服务端同步到客户端，并支持 AppleSkin 模组兼容。

### 回血机制

- 回血公式：`heal_progress += (0.0004 + nutrition × 0.00002) × 修正`
- 营养不良修正：×0.25
- 睡觉修正：×4.0
- 食物上限随等级增长：`max(6, min(6 + level/5×2, 20))`，即每 5 级增加 2 点上限，35 级恢复原版水平

### 分级工作台

MITE 引入了金属工作台体系，不同材质的工作台决定可合成的物品等级和合成速度：

| 工作台材质 | 加速系数 |
|-----------|---------|
| Flint / Obsidian | +0.2 |
| Copper / Silver / Gold | +0.3 |
| Rusted Iron / Iron | +0.4 |
| Ancient Metal | +0.5 |
| Mithril | +0.6 |
| Adamantium | +0.7 |

合成时间计算：`basePeriod / (1.0 + playerLevel×0.02 + benchModifier)`，最小 25 tick。

工作台 GUI 显示合成进度条和百分比，配方书会自动过滤超过当前工作台等级的配方。

### 金属砧

6 种金属砧（铜/银/金/远古金属/秘银/艾德曼），具有：

- 耐久值系统（`perIngotDurability × 31 × durabilityCoefficient`）
- 3 级裂纹状态（0=完好, 1=75%耐久以下, 2=25%耐久以下）
- 上方有非流体方块时阻止使用
- 砧耐久通过 `DataComponents.DAMAGE` 持久化，支持战利品表保留

### 箭矢系统

- 10 种材质的自定义箭矢（燧石/铜/银/金/锈铁/铁/远古金属/秘银/艾德曼/黑曜石）+ 火把箭
- 箭矢命中实体时寄存到目标体内（`StuckArrowTracker`），生物死亡时统一掉落
- 支持回收概率（`RECOVERY_CHANCE` DataComponent）
- 箭矢材质通过物品注册名自动解析，渲染器根据材质动态选择纹理
- 弓模型支持 Select 属性，根据箭袋中的箭种切换纹理

### 食物体系

已实现的食物类型包括：

- **碗装食物**：蔬菜汤、水碗、奶碗（碗装牛奶）
- **桶装食物**：6 种金属奶桶
- **普通食物**：洋葱、土豆（Mixin 覆盖原版）、胡萝卜（Mixin 覆盖原版）
- **容器返回**：碗装食物返回碗，桶装食物返回对应空桶

### 桶系统

6 种金属 × 4 种内容（空/水/岩浆/石）= 24 种桶：

- 空桶落入水中自动装水（`ItemEntityMixin`）
- 满桶支持炼药锅交互、流体放置
- 岩浆桶可作为燃料（3200 tick = 烧制 16 个物品）
- 玩家浸入水中时：岩浆桶 → 石头桶，牛奶碗 → 水碗（`SubmergedConversionMixin`）

### 牛挤奶系统

- 支持金属桶和碗挤奶
- 每日冷却机制（`COW_LAST_MILKED` Attachment）
- 碗挤奶 → 4 碗牛奶，桶挤奶 → 对应奶桶

### 唱片

4 张唱片：Descent、Legends、Underworld、Wanderer

### Mixin 修改汇总

| Mixin 类 | 目标 | 功能 |
|----------|------|------|
| `FoodDataMixin` | `FoodData.tick` | MITE 食物系统：营养→食物条转移、回血公式 |
| `VanillaFoodOverrideMixin` | `FoodProperties.onConsume` | 覆盖原版食物为 MITE 数值 |
| `PlayerNutritionMixin` | `Player.tick` | 每tick递减营养素 |
| `ItemEntityMixin` | `ItemEntity.tick` | 金属空桶落水自动装水 |
| `ItemMaxStackMixin` | `Item.getDefaultMaxStackSize` | 土豆/胡萝卜堆叠数调整为 64 |
| `SubmergedConversionMixin` | `Player.tick` | 浸水时岩浆桶→石桶、奶碗→水碗 |
| `CowMilkingMixin` | `AbstractCow.mobInteract` | 金属桶/碗挤奶+冷却 |
| `ArrowRecoveryMixin` | `Entity.spawnAtLocation` | 箭矢回收概率控制 |
| `FishingHookMixin` | `FishingHook.shouldStopFishing` | 修复自定义鱼竿浮漂兼容性 |
| `LivingEntityStuckArrowsMixin` | `LivingEntity.dropAllDeathLoot/remove` | 生物死亡掉落卡箭 |
| `AbstractArrowInvoker` | `AbstractArrow` 构造函数 | 暴露构造函数（Accessor） |
| `AnvilMenuCostAccessor` | `AnvilMenu.cost` | 暴露砧修理费用字段 |
| `RecipeCollectionMixin` (Client) | `RecipeCollection.selectRecipes` | 配方书过滤等级不足配方 |

---

## 未实现的原版 MITE 功能

以下为原版 MITE 中已有但 MITE-Recrafted 尚未实现的功能（基于代码审查中的 TODO 注释和缺失逻辑）：

- [ ] **玩家初始状态限制**：初始只有 3 格血与饥饿值（代码中有 `//todo 初始饱食度3个` 注释）
- [ ] **更多原版食物的 MITE 化**：目前仅覆盖了土豆和胡萝卜，面包/牛肉/熟肉等未处理
- [ ] **银质工具对亡灵伤害加成**：代码中有 `//TODO 银质工具对亡灵怪物有伤害加成` 注释
- [ ] **工具数值配平**：代码中有 `//todo 配平数值` 注释，表明工具伤害/速度数值尚未最终确定
- [ ] **新的维度**：地下世界 (Underworld) 和修改后的地狱进入条件
- [ ] **新生物**：惧狼/地狱犬/食尸鬼/古尸/影子潜伏者等 26 种新生物
- [ ] **新的熔炉与热量等级**：粘土/沙石/黑曜石/地狱岩熔炉
- [ ] **附魔系统**：速度/再生/击晕/吸血/缴械/穿刺等新附魔
- [ ] **耕种机制修改**：洋葱种植/蓝莓丛/疫病/粪肥等
- [ ] **天气影响**：血月/蓝月/丰收之月
- [ ] **保险箱**（金属锁箱子）
- [ ] **创造模式移除**
- [ ] **F3 屏幕限制**
- [ ] **经验等级上限 200 级**
- [ ] **方块重力系统修改**
- [ ] **动物生病系统**
- [ ] **砧销毁逻辑修复**：砧耐久耗尽时应销毁但当前代码无法触发（`newStage >= 3` 永远为 false）
- [ ] **弓在砧上修理**：弓缺少 `TOOL_COMPONENTS`/`TOOL_MATERIAL_TIER` DataComponent
- [ ] **部分食物的完整营养数据**

---

## 技术信息

| 项目 | 值 |
|------|-----|
| Minecraft 版本 | 26.2 |
| Fabric Loader | >=0.19.3 |
| Fabric API | 0.154.2+26.2 |
| Java 版本 | >=25 |
| 模组 ID | `mite-recrafted` |
| 构建系统 | Gradle + Fabric Loom 1.17-SNAPSHOT |

### 依赖

- **Fabric Loader** & **Fabric API**（必需）
- **[AppleSkin](https://modrinth.com/mod/appleskin)**（可选，兼容支持：食物预览值匹配 MITE 数值）

### 数据生成

项目使用 Fabric 数据生成 API 自动生成：

- 方块模型/状态 JSON（工作台/门/栅栏/金属块/砧）
- 物品模型 JSON（所有工具/材料/桶/食物/弓的三层 dispatch 模型）
- 方块标签（`incorrect_for_xxx_tool` 系列）
- 物品标签（`repairs_xxx` 修理标签系列）
- 语言文件（中/英文）
- 合成配方
- 战利品表
- 音效定义
- 唱片歌曲

### 网络协议

| Payload | 方向 | 用途 |
|---------|------|------|
| `NutritionSyncPayload` | S→C | 同步玩家营养数据（7 个 VAR_INT 字段） |
| `CraftingProgressSyncPayload` | S→C | 同步工作台合成进度（period/ticks/benchCoefficient） |

### 客户端命令

- `/mite-recrafted:cnutrition` — 显示当前玩家的营养状态调试信息

---

## 构建与开发

```bash
# 克隆仓库
git clone https://github.com/A-Team-PJnCeADc/mite-recrafted.git
cd mite-recrafted

# 构建模组
./gradlew build

# 运行客户端
./gradlew runClient

# 运行服务端
./gradlew runServer

# 生成数据
./gradlew runDatagen
```

构建产物位于 `build/libs/` 目录下。

---

## 原版 MITE 完整内容参考

以下是原版 MITE 的完整功能列表，供对比参考：

<details>
<summary>原版 MITE 模组内容（来自 MCMOD）</summary>

### 全新的玩家状态、等级体系
- 玩家初始状态只有三格血与饥饿值
- 玩家现在具有蛋白质(+有益)、植物营养素(+)以及胰岛素反应(-有害)的状态数值
- 玩家变得极其脆弱，无装备情况下任何怪物都能轻松致其于死地
- 当玩家生命值低于半格，无法空手攻击怪物
- 饥饿值耗尽带来更多的惩罚
- 合成需要时间，不同的物品合成所需时间不同
- 玩家生命恢复变得极慢（64s/点）
- 升一级的所需经验被大大增加，每五级提升一格生命值和饥饿值上限

### 限制玩家的采集、破坏能力
- 玩家的破坏和攻击距离都被大幅削减
- 破坏方块变得极其费时
- 原木只能使用短斧，斧头或战斧来采集
- 石质方块和矿石必须使用对应的采集工具
- 金属块、非自己放置的保险箱只能被更高一级的金属镐或战锤破坏

### 全新的生活方式与生存节奏
- 游戏初期只能作为猎人并收集资源
- 食物消耗速度变快
- 资源在蓝月前不会再生
- 金属工具台的引入使游戏节奏变慢

### 提升生物AI
- 生物在更远的地方就能看见玩家
- 动物需要水和食物
- 怪物的进攻更具策略

### 五种新的金属矿石与六种新的金属材料
- 铜、银、秘银、远古金属与艾德曼
- 金属等级：金→铜/银（→锈铁）→铁→远古金属→秘银→艾德曼
- 锈铁只能从怪物身上获得
- 远古金属只能在地下世界获得

### 添加一个新的维度
- 地下世界，要进入地狱必须经过地下世界
- 地下世界的怪物远比主世界更危险

### 新的熔炉以及热量等级
- 添加粘土、沙石、硬化粘土、黑曜石以及地狱岩熔炉
- 冶炼金属需要对应热量等级的熔炉

### 添加26种新生物
- 惧狼、地狱犬、食尸鬼、古尸、影子潜伏者等

### 新的工具与耐久值系统
- 棍棒、小刀、短剑、战锤、战斧、鹤嘴锄和镰刀
- 品质系统
- 木质/石质/钻石工具移除
- 修理需要同等级或更高的金属砧

### 更改耕种机制
- 洋葱作物、蓝莓丛
- 作物生长条件更高
- 疫病系统

### 天气影响
- 血月、蓝月、丰收之月

</details>

---

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
