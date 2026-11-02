# 第三方资源

## IronChests 贴图

`src/main/resources/assets/ironchests/` 下的 38 张贴图取自 IronChests 项目，单独按 GPL-3.0 提供。

- 来源仓库：https://github.com/cpw/ironchest
- 本项目目录：`src/main/resources/assets/ironchests/textures/`
- 许可全文：`../LICENSES/IronChests-GPL-3.0-license`

| 本项目目录 | 数量 | 
| --- | --- |
| `textures/entity/chest/` | 9 |
| `textures/item/chest/` | 29 |

## 与主项目的隔离方式

- 贴图放在独立命名空间 `ironchests`，与 `mite-recrafted` 命名空间下的自有资源分目录存放，可单独替换或删除。
- 主项目不依赖这些文件：删除 `assets/ironchests/` 后仍可编译、启动与运行，箱子功能（容量、界面、配方、掉落）不受影响；这些贴图相当于可选的资源覆盖。
- 主项目不含 IronChests 的 GPL 代码：箱子与保险箱的逻辑为独立实现，未复制或改写其类、字段与常量。
- 贴图只作为普通 PNG 资源，由资源标识符（如 `ironchests:entity/chest/iron_chest`）按路径读取，代码与贴图之间没有调用、链接或共享数据结构。
- 按 GPLv3 第 5 条，`assets/ironchests/` 与项目其余部分属于聚合（mere aggregation）：GPL-3.0 只适用于该目录下的贴图。

## 其余资源

- 保险箱与单箱贴图（`*_single`、`*_strongbox`）以及 `silver_chest` 方块贴图不是取自 IronChests，位于 `assets/mite-recrafted/textures/`
- 界面条带贴图 `assets/mite-recrafted/textures/gui/container/wide/` 由原版 `assets/minecraft/textures/gui/container/generic_54.png` 的边框与槽格像素横向延长生成，不取自 IronChests 文件。
