package io.github.jbea.adminMode

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class ItemStackDataType : PersistentDataType<ByteArray, ItemStack> {
    companion object {
        val INSTANCE: ItemStackDataType = ItemStackDataType()
    }

    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<ItemStack> {
        return ItemStack::class.java
    }

    override fun toPrimitive(complex: ItemStack, context: PersistentDataAdapterContext): ByteArray {
        if(complex == ItemStack.empty()) return ByteArray(0)
        return complex.serializeAsBytes()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): ItemStack {
        if(primitive.isEmpty()) return ItemStack.empty()
        return ItemStack.deserializeBytes(primitive)
    }
}