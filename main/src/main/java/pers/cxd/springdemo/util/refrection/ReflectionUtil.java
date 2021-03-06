package pers.cxd.springdemo.util.refrection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {

    private static final Map<ConstructorKey, Constructor<?>> sConstructors = new HashMap<>();
    private static final Map<FieldKey, Field> sFields = new HashMap<>();
    private static final Map<MethodKey, Method> sMethods = new HashMap<>();

    private static <T> Constructor<T> findOrCreateConstructor(Class<T> clazz, Class<?>[] parameterTypes) throws ReflectiveOperationException{
        ConstructorKey constructorKey = ConstructorKey.obtain(clazz, parameterTypes);
        Constructor<T> constructor = (Constructor<T>) sConstructors.get(constructorKey);
        if (constructor == null){
            synchronized (sConstructors){
                if ((constructor = (Constructor<T>) sConstructors.get(constructorKey)) == null){
                    try {
                        constructor = clazz.getDeclaredConstructor(parameterTypes);
                    }catch (NoSuchMethodException ex){
                        constructorKey.recycle();
                        throw ex;
                    }
                    constructor.setAccessible(true);
                    constructorKey.markInUse();
                    sConstructors.put(constructorKey, constructor);
                }else {
                    constructorKey.recycle();
                }
            }
        }else {
            constructorKey.recycle();
        }
        return constructor;
    }

    private static Field findOrCreateField(Class<?> clazz, String fieldName) throws ReflectiveOperationException{
        FieldKey fieldKey = FieldKey.obtain(clazz, fieldName);
        Field field = sFields.get(fieldKey);
        if (field == null){
            synchronized (sFields){
                if ((field = sFields.get(fieldKey)) == null){
                    try {
                        field = clazz.getDeclaredField(fieldName);
                    }catch (NoSuchFieldException ex){
                        fieldKey.recycle();
                        Class<?> superClazz = clazz.getSuperclass();
                        if (superClazz != null && superClazz != Object.class){
                            return findOrCreateField(superClazz, fieldName);
                        }else {
                            throw ex;
                        }
                    }
                    field.setAccessible(true);
                    fieldKey.markInUse();
                    sFields.put(fieldKey, field);
                }else {
                    fieldKey.recycle();
                }
            }
        }else {
            fieldKey.recycle();
        }
        return field;
    }

    private static Method findOrCreateMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws ReflectiveOperationException{
        MethodKey methodKey = MethodKey.obtain(clazz, methodName, parameterTypes);
        Method method = sMethods.get(methodKey);
        if (method == null){
            synchronized (sMethods){
                if ((method = sMethods.get(methodKey)) == null){
                    try {
                        method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    }catch (NoSuchMethodException ex){
                        methodKey.recycle();
                        Class<?> superClazz = clazz.getSuperclass();
                        if (superClazz != null && superClazz != Object.class){
                            return findOrCreateMethod(superClazz, methodName, parameterTypes);
                        }else {
                            throw ex;
                        }
                    }
                    method.setAccessible(true);
                    methodKey.markInUse();
                    sMethods.put(methodKey, method);
                }else {
                    methodKey.recycle();
                }
            }
        }else {
            methodKey.recycle();
        }
        return method;
    }

    public static <T> T newInstance(String className) throws ReflectiveOperationException{
        return (T) newInstance(Class.forName(className));
    }

    public static <T> T newInstance(Class<T> clazz) throws ReflectiveOperationException{
        return newInstance(clazz, null);
    }

    public static <T> T newInstance(String className, Class<?>[] parameterTypes, Object... args) throws ReflectiveOperationException{
        return (T) newInstance(Class.forName(className), parameterTypes, args);
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... args) throws ReflectiveOperationException {
        return findOrCreateConstructor(clazz, parameterTypes).newInstance(args);
    }

    public static <T> T getField(Object object, String fieldName) throws ReflectiveOperationException {
        return (T) getField(object.getClass(), object, fieldName);
    }

    public static <T> T getField(Class<?> clazz, Object object, String fieldName) throws ReflectiveOperationException {
        return (T) findOrCreateField(clazz, fieldName).get(object);
    }

    public static void setField(Object object, String fieldName, Object fieldValue) throws ReflectiveOperationException {
        findOrCreateField(object.getClass(), fieldName).set(object, fieldValue);
    }

    public static <T> T invokeMethod(Object object, String methodName) throws ReflectiveOperationException{
        return invokeMethod(object, methodName, null);
    }

    public static <T> T invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object... args) throws ReflectiveOperationException{
        return (T) findOrCreateMethod(object.getClass(), methodName, parameterTypes).invoke(object, args);
    }

    public static <T> T invokeMethod(Class<?> clazz, Object object, String methodName, Class<?>[] parameterTypes, Object... args) throws ReflectiveOperationException{
        return (T) findOrCreateMethod(clazz, methodName, parameterTypes).invoke(object, args);
    }

    public static <T> T getStaticField(String className, String fieldName) throws ReflectiveOperationException {
        return getStaticField(Class.forName(className), fieldName);
    }

    public static <T> T getStaticField(Class<?> clazz, String fieldName) throws ReflectiveOperationException {
        return (T) findOrCreateField(clazz, fieldName).get(null);
    }

    public static void setStaticFiled(String className, String fieldName, Object fieldValue) throws ReflectiveOperationException {
        setStaticFiled(Class.forName(className), fieldName, fieldValue);
    }

    public static void setStaticFiled(Class<?> clazz, String fieldName, Object fieldValue) throws ReflectiveOperationException {
        findOrCreateField(clazz, fieldName).set(null, fieldValue);
    }

    public static <T> T invokeStaticMethod(String className, String methodName) throws ReflectiveOperationException{
        return invokeStaticMethod(className, methodName, null);
    }

    public static <T> T invokeStaticMethod(Class<?> clazz, String methodName) throws ReflectiveOperationException{
        return invokeStaticMethod(clazz, methodName, null);
    }

    public static <T> T invokeStaticMethod(String className, String methodName, Class<?>[] parameterTypes, Object... args) throws ReflectiveOperationException{
        return invokeStaticMethod(Class.forName(className), methodName, parameterTypes, args);
    }

    public static <T> T invokeStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object... args) throws ReflectiveOperationException{
        return (T) findOrCreateMethod(clazz, methodName, parameterTypes).invoke(null, args);
    }

}
