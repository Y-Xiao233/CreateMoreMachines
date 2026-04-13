package net.yxiao233.createmoremachines.api.content.mechanical.deployer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.Field;

public class PackagedEnumHelper {
    public static <C> boolean equals(String memberName, Class<C> targetClass, C targetObject, String otherEnumName){
        try {
            if(!targetClass.isAssignableFrom(targetObject.getClass())){
                return false;
            }
            Field field = targetClass.getDeclaredField(memberName);
            field.setAccessible(true);
            Object object = field.get(targetObject);
            if(object instanceof Enum<?> e){
                return otherEnumName.equalsIgnoreCase(e.name());
            }else{
                return false;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readEnumFromNbt(CompoundTag nbt, String key, String... enumValues) {
        if (nbt.contains(key, Tag.TAG_STRING)) {
            String name = nbt.getString(key);
            for (String s : enumValues) {
                if (s.equalsIgnoreCase(name)){
                    return s;
                }
            }
        }
        return enumValues[0];
    }
}
