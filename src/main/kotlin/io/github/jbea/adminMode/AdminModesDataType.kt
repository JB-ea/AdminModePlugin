package io.github.jbea.adminMode

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class AdminModesDataType : PersistentDataType<String, AdminModes.Modes> {
    companion object {
        val INSTANCE: AdminModesDataType = AdminModesDataType()
    }

    override fun getPrimitiveType(): Class<String> {
        return String::class.java
    }

    override fun getComplexType(): Class<AdminModes.Modes> {
        return AdminModes.Modes::class.java
    }

    override fun toPrimitive(complex: AdminModes.Modes, context: PersistentDataAdapterContext): String {
        return complex.toString()
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): AdminModes.Modes {
        return AdminModes.Modes.valueOf(primitive.uppercase())
    }
}