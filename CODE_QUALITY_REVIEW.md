# MITE-Recrafted 代码质量审查报告

> 审查日期：2026-07-20  
> 审查范围：`src/main/java/` 和 `src/client/java/` 下所有 Java 源文件  
> 审查方式：逐一阅读源码，按优先级分类记录问题

---

## 高优先级问题（影响正确性或可能导致运行时崩溃）

### 1. 砧永远不销毁 — 逻辑缺陷

**文件**: `ModAnvilBlockEntity.java` 第 61 行  
**问题**: `addDamage()` 方法中 `if (newStage >= 3)` 条件永远不成立。`getDamageStage()` 方法最大返回值为 2（当 `factor >= 0.75` 时），因此 `newStage` 的最大值也是 2，`newStage >= 3` 永远为 false。  
**后果**: 砧耐久耗尽时不会被销毁，与 MITE 原版行为不一致。  
**建议**: 应在 `getDamageStage()` 中添加 stage 3（对应 `factor >= 1.0`），或修改销毁条件为检查 damage 是否超过总耐久。

### 2. 空桶落水装水可能 NPE

**文件**: `ItemEntityMixin.java`  
**问题**: Mixin 中访问 `empty.waterPeer` 时缺少空检查。如果空桶的 `waterPeer` 字段未被正确初始化（为 null），将导致 NullPointerException 崩溃。  
**建议**: 添加 `if (empty.waterPeer != null)` 守卫条件。

### 3. 弓无法在砧上修理

**文件**: `BowItems.java`  
**问题**: 弓直接使用 `new BowItem()` 创建，没有设置 `TOOL_COMPONENTS`、`TOOL_MATERIAL_TIER`、`TOOL_REPAIR_TAG` 等 DataComponent。砧修理系统依赖这些组件识别工具材质。  
**后果**: 弓无法在任何金属砧上修理。  
**建议**: 为弓添加相应的 DataComponent，与其他工具保持一致。

### 4. `ToolType.create(ResourceKey)` 重载未设置 DataComponent

**文件**: `ToolType.java` 第 68-72 行  
**问题**: `create(ModToolMaterial, ResourceKey, float, float)` 重载方法只设置了 `.setId(key)`，没有设置 `TOOL_COMPONENTS`、`TOOL_MATERIAL_TIER`、`TOOL_REPAIR_TAG`，与主 `create()` 方法不一致。  
**后果**: 通过此路径创建的工具无法在砧上修理。  
**建议**: 在此重载中也添加 DataComponent 设置，或统一通过主 `create()` 方法创建。

---

## 中优先级问题（影响可维护性或存在潜在风险）

### 5. 材质识别逻辑重复且脆弱

**涉及文件**:
- `ModWorkbenchMenu.TieredResultSlot.getItemDurability()` (第 273-281 行)
- `RecipeCollectionMixin.getItemDurability()`
- `ModWorkbenchMenu.TieredResultSlot.getItemMaterialDurabilityStatic()`

**问题**: 
1. 同一逻辑在三处重复实现，违反 DRY 原则
2. 使用 `path.contains(mat.getName())` 字符串子串匹配，非常脆弱——例如 `"mithril_scythe"` 包含 `"mithril"`，但也包含 `"iril"` 这种无意义子串；如果未来有名字包含其他材质名的物品，会误匹配

**建议**: 抽取为统一工具方法，改用 DataComponent 中的 `TOOL_MATERIAL_TIER` 精确识别材质。

### 6. `ModWorkbenchMenu.removed()` 循环索引越界

**文件**: `ModWorkbenchMenu.java` 第 106 行  
**问题**: `for (int i = 0; i <= 9; i++)` 从槽 0（结果槽）到槽 9（第 9 个合成格）共清除 10 个槽位。工作台菜单的合成格索引应为 1-9（9 个合成格），槽 0 是结果槽。清除额外的槽位可能导致意外行为。  
**建议**: 确认槽位映射，调整循环范围。

### 7. 客户端 volatile 字段竞态条件

**文件**: `MiteRecraftedClient.java` 第 19-21 行  
**问题**: `syncedCraftingPeriod`、`syncedCraftingTicks`、`syncedBenchCoefficient` 是三个独立的 `volatile` 字段，从网络线程更新、渲染线程读取。虽然每个字段的读写是原子的，但三个字段可能在不同时刻更新，导致读取到不一致的组合（例如 period 是新值但 ticks 是旧值）。  
**建议**: 使用原子引用包装为不可变对象，或使用同步块保护读写。

### 8. `Quality.getTranslationKey()` 命名不一致

**文件**: `Quality.java` 第 113 行  
**问题**: 返回 `"quality.mite_recraft." + name`，使用下划线 `mite_recraft`，但模组 ID 是 `"mite-recrafted"`（连字符）。翻译键命名空间与 MOD_ID 不一致，可能导致翻译查找失败。  
**建议**: 统一为 `"quality.mite-recrafted." + name`。

### 9. `ModModelProvider` 严重过大

**文件**: `ModModelProvider.java` (700+ 行)  
**问题**: 
1. 严重违反单一职责原则，一个类负责所有方块和物品的模型生成
2. `generateAnvilModels()` 通过覆写 `run()` 注入 `cachedOutput`，hack 了 DataProvider 的执行流程，脆弱且不规范
3. 大量重复的模型生成代码（每种工具类型手写循环），应抽取为通用方法

**建议**: 按类型拆分为多个 Provider（BlockModelProvider/ItemModelProvider/BowModelProvider/AnvilModelProvider），抽取通用方法消除重复。

### 10. `FishingHookMixin` 使用 `@Overwrite`

**文件**: `FishingHookMixin.java`  
**问题**: `@Overwrite` 是 Mixin 的最后手段，会完全替换目标方法，与其他修改同一方法的模组高度不兼容。  
**建议**: 改用 `@Inject` 或 `@Redirect` 实现相同逻辑。

### 11. `ModEntitys` 类名不符合英语语法

**文件**: `ModEntitys.java`  
**问题**: `Entitys` 不是正确的英语复数形式，应为 `Entities`。  
**建议**: 重命名为 `ModEntities`。

### 12. `VanillaFoodOverrideMixin` 仅覆盖两种原版食物

**文件**: `VanillaFoodOverrideMixin.java`  
**问题**: 目前只覆盖了土豆和胡萝卜的 MITE 数值，原版 MITE 中所有食物（面包/生牛肉/熟牛排/猪肉/鱼肉/苹果/西瓜等）都有不同的数值。  
**建议**: 扩展覆盖范围，或提供配置文件定义原版食物的 MITE 覆盖值。

---

## 低优先级问题（代码风格或可维护性改进建议）

### 13. 大量代码重复

**涉及文件**: `ModToolRegister.java`、各工具 Items 类、`ModBlockRegister.java`、`ModModelProvider.java`

**问题**: 
- 工具注册中每种工具的每个材质都手写一行，极易遗漏或写错
- 所有工具 Items 类（WoodenItems/AexItems/HatchetItems/...）结构完全相同
- 方块注册中门/栅栏/金属块/砧的注册逻辑各写一遍

**建议**: 使用循环遍历 `ModToolMaterial.ALL/METALS` 替代手写每行；工具 Items 类可考虑使用注解处理器或代码生成。

### 14. 硬编码魔法数字

**涉及文件**: 多处

| 位置 | 硬编码值 | 含义 |
|------|---------|------|
| `ModAnvilBlock.NUM_COMPONENTS` | 31 | 砧组件数，缺少注释 |
| `NutritionSystem.SUGAR_CONTENT_TO_INSULIN_RESPONSE` | 4.8F | 糖→胰岛素倍率 |
| `NutritionSystem.onEat()` | 160000, 1000, 192000 | 营养素上限 |
| `FoodDataMixin.HEAL_BASE` | 0.0004F | 回血基础值 |
| `FoodDataMixin.HEAL_NUTRITION_FACTOR` | 0.00002F | 回血营养因子 |
| `ItemEntityMixin` | `age < 20` | 空桶装水冷却时间 |
| `ModToolMaterial.GLASS/NETHERRACK` | 0.0001f | "不可挖掘"速度 |

**建议**: 提取为命名常量并添加注释说明含义。

### 15. `ModToolMaterial.fromCoefficient()` 浮点数精确比较

**文件**: `ModToolMaterial.java` 第 147 行  
**问题**: `mat.getDurabilityCoefficient() == coefficient` 使用浮点 `==` 比较，可能因精度问题匹配失败，返回 null 而非抛异常。  
**建议**: 使用 epsilon 比较（如 `Math.abs(a - b) < 1e-6`），或返回 `Optional` 并在找不到时抛出明确异常。

### 16. `ArrowRecoveryMixin` 使用 `new Random()`

**文件**: `ArrowRecoveryMixin.java`  
**问题**: 使用 `new java.util.Random()` 创建新随机数生成器，每次调用都新建实例。  
**建议**: 使用 `ThreadLocalRandom.current()` 或实体自带的随机数。

### 17. `AbstractArrowInvoker` 可能是死代码

**文件**: `AbstractArrowInvoker.java`  
**问题**: 未找到对此 Invoker 的调用，可能是遗留死代码。  
**建议**: 确认是否仍有使用，如无则移除。

### 18. `NutritionSystem` 中 `COW_LAST_MILKED` 职责不匹配

**文件**: `NutritionSystem.java` 第 100-105 行  
**问题**: `COW_LAST_MILKED` 是牛的挤奶冷却时间，与营养系统无关，放在 `NutritionSystem` 中违反单一职责原则。  
**建议**: 移至 `CowMilkingMixin` 或独立的 `MilkingSystem` 类。

### 19. `ModBlockType.STACK_SIZES` 非线程安全

**文件**: `ModBlockType.java`  
**问题**: `STACK_SIZES` 是 `static` 共享可变 `LinkedHashMap`，非线程安全。  
**说明**: 模组初始化为单线程环境，实际影响不大，但设计上不够安全。

### 20. `ModFilledBucketItem.emptyContents()` 冗余类型检查

**文件**: `ModFilledBucketItem.java` 第 189 行  
**问题**: `this.fluid instanceof Fluid` — `Fluid` 是抽象类，所有流体都继承自它，此检查永远为 true。  
**建议**: 移除此冗余检查。

### 21. `ModEmptyBucketItem`/`ModFilledBucketItem` 封装问题

**文件**: `ModEmptyBucketItem.java`、`ModFilledBucketItem.java`  
**问题**: `waterPeer`/`lavaPeer`/`emptyPeer` 是 `public` 可变字段，直接赋值，违反封装原则。  
**建议**: 改为 `private` 字段 + setter 方法，或在构造时设置。

### 22. `BowlFood.nutrition()` 硬编码条件链

**文件**: `BowlFood.java`  
**问题**: `this == VEGETABLE_SOUP ? 6 : this == MILK_BOWL ? 1 : 0` 这种 if-else 链应通过构造函数参数传入。  
**建议**: 在枚举构造函数中添加 `nutrition` 参数。

### 23. `ModWorkbenchMenu.getBenchModifier()` 硬编码 switch

**文件**: `ModWorkbenchMenu.java` 第 173-182 行  
**问题**: 使用 switch 硬编码材质→加速系数映射，与 `WorkbenchMaterial` 枚举解耦不彻底。  
**建议**: 在 `WorkbenchMaterial` 枚举中定义 `benchModifier` 属性。

### 24. `ItemMaxStackMixin` 可能已无实际效果

**文件**: `ItemMaxStackMixin.java`  
**问题**: 修改土豆和胡萝卜的最大堆叠数为 64，但 MC 26.2 中原版堆叠数本身就是 64，此 mixin 可能已经无实际效果。  
**建议**: 确认当前 MC 版本的原版堆叠数，如已为 64 则移除此 mixin。

---

## 未完成的 TODO 清单

从源码中提取的 TODO 注释，供开发者参考：

| 文件 | TODO 内容 |
|------|----------|
| `NutritionSystem.java` | `//todo 初始饱食度3个` |
| `ToolType.java` | `//todo 配平数值` |
| `ToolType.java` | `//TODO 银质工具对亡灵怪物有伤害加成，且可攻击白色食尸鬼` |
| `FoodDataMixin.java` | `//todo` (MITE 回血逻辑后续完善) |
| `BucketType.java` | 液体转移逻辑未完成 |

---

## 总结

| 优先级 | 数量 | 说明 |
|--------|------|------|
| 高 | 4 | 影响正确性或可能崩溃 |
| 中 | 8 | 影响可维护性或存在潜在风险 |
| 低 | 12 | 代码风格或可维护性改进 |

最关键的问题集中在：
1. **砧销毁逻辑缺陷** — 影响游戏机制正确性
2. **空桶装水 NPE 风险** — 可能导致崩溃
3. **弓/部分工具缺少 DataComponent** — 影响修理系统完整性
4. **材质识别逻辑重复且脆弱** — 长期维护风险
