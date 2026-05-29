package net.yxiao233.createmoremachines.utils;

public record ReflectionValue<T, C>(String memberName, Class<T> typeClass, Object targetObject, Class<C> targetClass) {

    public T getValue() {
        return ReflectionUtil.getPrivateField(memberName, typeClass, targetObject, targetClass);
    }

    public void setValue(T newValue) {
        ReflectionUtil.setPrivateField(memberName, typeClass, targetObject, targetClass, newValue);
    }
}
