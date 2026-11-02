# MITE-Recrafted

MITE-Recrafted 是 **[MITE (Minecraft Is Too Easy)](https://www.mcmod.cn/class/226.html)** 的高版本移植项目，基于 Fabric，目标 Minecraft **26.3**（Java 25）。

原版 MITE 基于 1.6.4 并覆盖类文件实现，不兼容其他模组。本项目用 Mixin 非侵入地重新实现 MITE 机制，并保持与其他 Fabric 模组的兼容性。

## Progress

Developing

## 未实现的原版 MITE 功能

- [ ] 玩家初始状态限制：初始 3 格血与饥饿值
- [ ] 银质工具对亡灵伤害加成
- [ ] 数值配平：工具/伤害/速度尚未最终确定
- [ ] 新的维度：地下世界 (Underworld) 与修改后的地狱进入条件
- [ ] 新生物：惧狼/地狱犬/食尸鬼/古尸/影子潜伏者等 26 种
- [ ] 耕种机制修改：洋葱种植/蓝莓丛/疫病/粪肥
- [ ] 天气影响：血月/蓝月/丰收之月
- [ ] 创造模式移除
- [ ] F3 屏幕限制
- [ ] 方块重力系统修改
- [ ] 动物生病系统

## 依赖

- **Java 25**
- **[Fabric API](https://github.com/FabricMC/fabric)** `0.165.0+26.3`
- **[AppleSkin](https://github.com/squeek502/AppleSkin)**（开发环境与客户端可选，编译期仅用 API）

## 构建

```bash
./gradlew runDatagen    # 生成数据，输出到 src/main/generated
./gradlew build         # 构建模组
./gradlew genSources    # 生成 Minecraft 源码
./gradlew runClient     # 运行客户端
./gradlew runServer     # 运行服务端
```

## 项目结构

```text
src/main/java/com/mite/recraft/
├── MiteRecrafted.java        通用入口，固定注册顺序
├── api/                      子模组接入层
├── registry/                 全部注册入口与登记表
│   ├── ModItems              物品登记表（按 ItemCategory 分类）
│   ├── ModBlocks             方块登记表
│   ├── ModBlockRegister      方块 + BlockItem 注册
│   ├── ModChests             箱子方块、BlockEntityType、菜单、物品与升级
│   ├── ModFurnaces           熔炉方块与共用 BlockEntityType
│   ├── ModStrongboxes        保险箱方块、BlockEntityType、菜单
│   ├── ModToolRegister       工具注册
│   ├── ModCreativeTabs       创造模式物品栏
│   ├── ModEnchantments       附魔键与定义
│   ├── ModEnchantmentEffects 附魔效果类型编解码器
│   ├── ModDataComponents     数据组件
│   ├── ModMenus / ModNetwork / ModSounds / ModFuels
│   └── ModBlockTags / ModItemTags / ModJukeboxSongs
├── content/
│   ├── block/{anvil,furnace,metal,strongbox,workbench}
│   ├── enchantment/{,effects}
│   ├── entity/
│   └── item/{armor,bucket,food,material,quality,record,tool}
├── chest/                    箱子等级、方块、方块实体、菜单、升级物品
├── mixin/                    通用与仅服务端 Mixin
├── network/                  自定义载荷
└── util/

src/client/java/com/mite/recraft/client/
├── MiteRecraftedClient.java  客户端入口
├── MiteRecraftedDataGenerator 数据生成入口
├── datagen/                  全部数据生成器
├── chest/                    箱子渲染器与界面
├── renderer/ screen/ command/ strongbox/
├── compat/appleskin/         AppleSkin 集成
├── state/                    客户端同步状态
└── mixin/                    客户端 Mixin
```

源集约束：

- 引用 `net.minecraft.client.*` 的代码只能放在 `src/client`；`src/main` 只放服务端与通用类。
- 注册表的写入集中在 `registry/`，`content/` 只放行为实现，不直接注册。
- 数据生成器只放在 `client.datagen`，避免 main 反向依赖 client。
- 箱子渲染与界面集中在 `client/chest`；箱子方块、箱子实体与升级物品贴图位于 `assets/ironchests`，界面条带与保险箱贴图位于 `assets/mite-recrafted`，图集由 `ModChestAtlasProvider` 生成。
- `Mixin` 目标方法必须存在于 26.3；注入方法名与参数表按目标签名书写，`mite-recrafted.mixins.json` 中 `defaultRequire: 1`。

## 子模组接入

`com.mite.recraft.api` 是唯一对外接口，位于 main 源集：

| 类 | 用途 |
| --- | --- |
| `MiteIds` | `Identifier` / `ResourceKey` / `TagKey` 工厂，含命名空间重载 |
| `ItemCategory` | 创造模式物品栏分类，枚举顺序即展示顺序 |
| `MiteRegistration` | 物品、方块 + BlockItem、食物、工具、燃料注册，并自动登记物品栏 |
| `MiteContent` | 查询已注册物品/方块/熔炉/保险箱 |
| `MiteToolMaterials` | 工具材料反查（名称、耐久系数、最高品质） |
| `MiteNutrition` | 营养数值读写、进食、客户端同步 |
| `MiteEvents` | `NUTRITION_CHANGED`、`CRAFT_COMPLETED` |

示例：

```java
public class ExampleMod implements ModInitializer {

    private static final Identifier DUST = Identifier.fromNamespaceAndPath("examplemod", "example_dust");

    @Override
    public void onInitialize() {
        // 使用本模组命名空间
        MiteRegistration.registerStackedItem(ItemCategory.MATERIAL, "example_dust", 16);

        // 使用子模组命名空间
        Item dust = MiteRegistration.registerItem(ItemCategory.MATERIAL, DUST,
                props -> new Item(props.stacksTo(16)));

        MiteRegistration.registerBlock(MiteIds.id("example_block"),
                props -> new Block(props.strength(2.0F)),
                props -> props.stacksTo(64));

        MiteRegistration.registerFuel(dust, 200, 1);

        MiteEvents.CRAFT_COMPLETED.register((player, result, quality, xp) ->
                MiteRecrafted.LOGGER.info("{} 制作 {} 品质 {} 消耗 XP {}", player.getName().getString(), result, quality, xp));
    }
}
```

注册时机不早于 `MiteRecrafted.onInitialize()`；物品栏内容在客户端打开时读取，注册后无需刷新。


## 客户端命令

- `/mite-recrafted:cnutrition` - 显示当前玩家的营养状态

## 贡献

1. `./gradlew build` 无错误
2. 客户端代码放 `src/client`，通用代码放 `src/main`
3. 注册表写入放 `registry/`，配方/标签/模型优先使用 datagen
4. 注释只写实现事实：公式、单位、调用时机、约束

## 致谢

- 所有参与开发和建议的人员

## License

<a href="https://github.com/A-Team-PJnCeADc/mite-recrafted">mite-recrafted</a> © 2026 by <a href="https://github.com/A-Team-PJnCeADc">A-Team-PJnCeADc</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>

例外：`assets/ironchests/` 下的 38 张贴图（箱子实体贴图与升级物品图标）取自 <a href="https://github.com/cpw/ironchest">IronChests</a> 项目，按 <a href="[https://www.gnu.org/licenses/gpl-3.0.txt](https://github.com/4nner/IronChests?tab=GPL-3.0-1-ov-file)">GPL-3.0</a> 提供。该目录与项目其余部分为聚合关系：贴图独立存放并按普通 PNG 资源读取，主项目不含 IronChests 的 GPL 代码，其余代码仍适用前述 CC BY-NC-SA 4.0。逐文件清单与隔离方式见 [THIRD-PARTY-NOTICES.md](docs/THIRD-PARTY-NOTICES.md)，许可全文见 `LICENSES/IronChests-GPL-3.0-license`。
