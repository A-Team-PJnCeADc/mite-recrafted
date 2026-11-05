# 架构与子模组接入

## 项目结构

```text
common/src/main/java/com/mite/recraft/
├── MiteRecrafted.java        通用入口：客户端登记 + 运行期初始化
├── api/                      子模组接入层
├── internal/data/            附加数据平台入口
├── registry/
│   ├── MiteRegistrationPhases 注册阶段声明与分发
│   ├── ModItems              物品登记表（按 ItemCategory 分类）
│   ├── ModBlockRegister      方块与 BlockItem（BlockItem 延迟到物品阶段）
│   ├── ModChests             箱子方块、BlockEntityType、菜单、物品与升级
│   ├── ModFurnaces           熔炉方块与共用 BlockEntityType
│   ├── ModStrongboxes        保险箱方块、BlockEntityType、菜单
│   ├── ModCreativeTabs       创造模式物品栏
│   └── ModDataComponents / ModMenus / ModNetwork / ModSounds / ModFuels / 标签类
├── content/                  方块、附魔、实体、物品行为实现
├── chest/                    箱子等级、方块、方块实体、菜单、升级物品
├── client/                   共享客户端代码，均标注 @Environment(CLIENT)
│   ├── ClientRegistrations   客户端注册声明（@ExpectPlatform）
│   ├── ClientNetworkHandlers 客户端载荷处理
│   └── chest/ strongbox/ renderer/ screen/ command/ state/ mixin/
├── mixin/                    通用 Mixin
└── network/                  自定义载荷

fabric/src/main/java/com/mite/recraft/
├── fabric/MiteRecraftedFabric             加载器入口
├── client/fabric/ClientRegistrationsImpl  客户端注册实现
├── client/datagen/                        数据生成入口与全部生成器
└── client/compat/appleskin/               AppleSkin 集成

neoforge/src/main/java/com/mite/recraft/
├── neoforge/MiteRecraftedNeoForge         加载器入口，按 RegisterEvent 分发注册阶段
├── client/neoforge/ClientRegistrationsImpl 客户端注册实现，挂在客户端事件上
└── internal/data/neoforge/                附件注册实现
```

源集约束：

- common 不引用 Fabric Loader 或 NeoForge API，只依赖 Architectury API 与 Minecraft；平台差异用 `@ExpectPlatform` 声明，实现在平台模块的同包 `<platform>` 子包。
- 注册只写“声明与阶段”，不持有加载器注册表句柄：方块阶段只写方块表，BlockItem 用 `MiteRegistration.deferItem` 留到物品阶段。
- 客户端注册（界面、实体/方块实体渲染器、模型属性、客户端命令）在 `ClientRegistrations` 声明，实现落在各平台 `client` 包；共享客户端工具类留在 common 并标注 `@Environment(EnvType.CLIENT)`，由 `EnvExecutor` 在客户端环境触发，专用服务端不加载。
- 数据生成器只在 fabric 模块，产物写入 `common/src/main/generated` 且不入库，构建前先跑 `:fabric:runDatagen`。
- 箱子渲染与界面集中在 `client/chest`；箱子实体与升级物品贴图位于 `assets/ironchests`，界面条带与保险箱贴图位于 `assets/mite_recrafted`，图集由 `ModChestAtlasProvider` 生成。
- `Mixin` 目标方法必须存在于 26.3；注入方法名与参数表按目标签名书写，`mite-recrafted.mixins.json` 中 `defaultRequire: 1`。

## 子模组接入

`com.mite.recraft.api` 是唯一对外接口，位于 main 源集：

| 类 | 用途 |
| --- | --- |
| `MiteIds` | `Identifier` / `ResourceKey` / `TagKey` 工厂，含命名空间重载 |
| `ItemCategory` | 创造模式物品栏分类，枚举顺序即展示顺序 |
| `MiteRegistration` | 物品、方块 + BlockItem、食物、工具、燃料注册，并自动登记物品栏；方块与 BlockItem 分属两个注册阶段 |
| `MiteContent` | 查询已注册物品/方块/熔炉/保险箱 |
| `MiteToolMaterials` | 工具材料反查（名称、耐久系数、最高品质） |
| `MiteNutrition` | 营养数值读写、进食、客户端同步 |
| `MiteEvents` | `NUTRITION_CHANGED`、`CRAFT_COMPLETED` |

示例。内容注册写成一组调用，再由各加载器在对应窗口里执行：

```java
public final class ExampleContent {

    private static final Identifier DUST = Identifier.fromNamespaceAndPath("examplemod", "example_dust");

    public static void registerItems() {
        // 使用本模组命名空间
        MiteRegistration.registerStackedItem(ItemCategory.MATERIAL, "example_dust", 16);

        // 使用子模组命名空间
        Item dust = MiteRegistration.registerItem(ItemCategory.MATERIAL, DUST,
                props -> new Item(props.stacksTo(16)));

        MiteRegistration.registerFuel(dust, 200, 1);
    }

    public static void registerBlocks() {
        // BlockItem 由 registerBlock 内部延迟到物品阶段写入，不要在方块窗口里重复注册
        MiteRegistration.registerBlock(MiteIds.id("example_block"),
                props -> new Block(props.strength(2.0F)),
                props -> props.stacksTo(64));
    }
}
```

```java
// Fabric：注册表在模组初始化期可写
public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleContent.registerBlocks();
        ExampleContent.registerItems();
    }
}
```

```java
// NeoForge：注册表只在 RegisterEvent 期间解冻，按注册表分发
@Mod("examplemod")
public final class ExampleModNeoForge {
    public ExampleModNeoForge(IEventBus modBus) {
        modBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.BLOCK)) {
                ExampleContent.registerBlocks();
            } else if (event.getRegistryKey().equals(Registries.ITEM)) {
                ExampleContent.registerItems();
            }
        });
    }
}
```

事件订阅不受注册窗口限制，两端同一写法：

```java
MiteEvents.CRAFT_COMPLETED.register((player, result, quality, xp) ->
        MiteRecrafted.LOGGER.info("{} 制作 {} 品质 {} 消耗 XP {}",
                player.getName().getString(), result, quality, xp));
```

注册时机必须落在对应注册表的写入窗口内：Fabric 是模组初始化，NeoForge 是 `RegisterEvent`。物品栏内容在客户端打开时读取，注册完成后不需要额外刷新。

## 实体渲染器的登记写法

统一按「贴图能否由实体 id 推导」分成两种，不再混用：

- 能推导（贴图就是 `textures/entity/mob/<实体 id>.png`）：在渲染器类里维护 `REGISTERED` 列表并给出
  `provider(type)`，平台侧只写一个 for 循环 + 一个登记方法。目前是蜘蛛、胶质生物、蠹虫系。
- 不能推导（同一贴图被多个实体复用，或贴图名与 id 不同）：逐个登记并显式传贴图路径。目前是蝙蝠系
  （巨型吸血蝙蝠复用 `vampire_bat.png`）。其余人形生物各有专属渲染器类，逐个登记即可。

平台侧的登记入口固定为 `ClientRegistrationsImpl.registerEntityRenderers()`，Fabric 直接调用
`EntityRendererRegistry.register`，NeoForge 在 `EntityRenderersEvent.RegisterRenderers` 里登记。

## 可选模组兼容：以 AppleSkin 为例

AppleSkin 两端 API 不同，不能只编译一份：

| 项 | Fabric | NeoForge |
| --- | --- | --- |
| 依赖 | `squeek.appleskin:appleskin-fabric:mc26.3-3.0.10` | `squeek.appleskin:appleskin-neoforge:mc26.3-3.0.10` |
| 事件注册 | `FoodValuesEvent.EVENT`（Fabric API 事件） | `NeoForge.EVENT_BUS.addListener` |
| 事件字段 | `defaultFoodComponent` / `modifiedFoodComponent` | `defaultFoodProperties` / `modifiedFoodProperties` |
| `FoodHelper.isFood` | `isFood(ItemStack)` | `isFood(ItemStack, Player)` |

处理方式：

- `common` 只放与平台无关的预览值计算（`client/compat/appleskin/AppleSkinCompat#preview`）与一个
  `@ExpectPlatform init()`，业务侧只调用 `AppleSkinCompat.init()`。
- 实现落在 `<声明包>.fabric.AppleSkinCompatImpl` 与 `<声明包>.neoforge.AppleSkinCompatImpl`，
  各自用本平台的注册 API，先判断模组是否加载再注册，避免未安装时触发类加载。
- `FoodHelper.isFood` 的参数数量不同，无法抽成同一个 mixin：Fabric 版 mixin 在 `fabric` 模块，
  NeoForge 版在 `neoforge` 模块，各自带一份 `mite-recrafted-<platform>.client.mixins.json`，
  分别在 `fabric.mod.json` 的 `mixins` 与 `neoforge.mods.toml` 的 `[[mixins]]` 里登记。
- 两端 gradle 都用 `compileOnly` 引 API、`runtimeOnly` 引完整模组，并用 `exclusiveContent`
  锁定 `squeek.appleskin` 组，避免向其他仓库请求该产物。