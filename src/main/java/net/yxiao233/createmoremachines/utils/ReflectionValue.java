package net.yxiao233.createmoremachines.utils;

public class ReflectionValue<T, C> {
    private final T value;
    private final String memberName;
    private final Class<T> typeClass;
    private final Object targetObject;
    private final Class<C> targetClass;
    public ReflectionValue(String memberName, Class<T> typeClass, Object targetObject, Class<C> targetClass){
        this.value = ReflectionUtil.getPrivateField(memberName,typeClass,targetObject,targetClass);
        this.memberName = memberName;
        this.typeClass = typeClass;
        this.targetObject = targetObject;
        this.targetClass = targetClass;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue){
        ReflectionUtil.setPrivateField(memberName,typeClass,targetObject,targetClass,newValue);
    }

    public Class<C> getTargetClass() {
        return targetClass;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public String getMemberName() {
        return memberName;
    }
}
