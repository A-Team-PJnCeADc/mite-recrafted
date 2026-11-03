/**
 * 子模组接入层，仅依赖 main 源集（服务端与通用代码）。
 *
 * <ul>
 *   <li>{@link com.mite.recraft.api.MiteIds} - Identifier / ResourceKey / TagKey 工厂</li>
 *   <li>{@link com.mite.recraft.api.ItemCategory} - 创造模式物品栏分类，枚举顺序即展示顺序</li>
 *   <li>{@link com.mite.recraft.api.MiteRegistration} - 物品、方块、食物、工具、燃料注册</li>
 *   <li>{@link com.mite.recraft.api.MiteContent} - 已注册内容的只读查询</li>
 *   <li>{@link com.mite.recraft.api.MiteToolMaterials} - 工具材料查询</li>
 *   <li>{@link com.mite.recraft.api.MiteNutrition} - 营养数值读写与同步</li>
 *   <li>{@link com.mite.recraft.api.MiteEvents} - 进食完成、工作台合成完成事件</li>
 * </ul>
 *
 * <p>子模组使用自己的命名空间注册时，传入 {@link net.minecraft.resources.Identifier} 重载；
 * 使用 {@code mite_recrafted} 命名空间时可用路径重载。</p>
 *
 * <p>依赖客户端类（渲染、界面、数据生成）的代码不能放入本包，必须位于 client 源集。</p>
 */
package com.mite.recraft.api;
