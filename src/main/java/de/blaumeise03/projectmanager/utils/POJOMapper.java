package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class POJOMapper {
    private POJOMapper() {
    }

    public static Object map(Object or) throws POJOMappingException {
        if(or.getClass().isAnnotationPresent(POJO.class)) {
            try {
                //Get class of result object
                POJO a = or.getClass().getAnnotation(POJO.class);
                Class<?> resClass = a.mappingClass();
                Object res = resClass.getConstructor().newInstance();
                Field[] fields = or.getClass().getDeclaredFields();
                //iterate over all fields
                for (Field field : fields) {
                    if (field.isAnnotationPresent(POJOData.class)) {
                        POJOData pojoData = field.getAnnotation(POJOData.class);
                        //Current field should be mapped onto the target object
                        String fieldName = convertName(field.getName());
                        String getterName = (field.getType().equals(Boolean.TYPE) ? "is" : "get") + fieldName;
                        String setterName = "set" + (pojoData.to().isBlank() ? fieldName : convertName(pojoData.to()));
                        Object value = or.getClass().getMethod(getterName).invoke(or);
                        Class<?> orType = field.getType();
                        Class<?> targetType =
                                pojoData.to().isEmpty() ?
                                        res.getClass().getDeclaredField(field.getName()).getType() :
                                        res.getClass().getDeclaredField(pojoData.to()).getType();
                        if(!pojoData.blocked()) {
                            POJOConverter<Object, Object> converter = POJOConverter.DefaultConverter.getDefaultConverter(orType, targetType);
                            Object resultValue = converter.convert(value);
                            resClass.getMethod(setterName, targetType).invoke(res, resultValue);
                        }
                        if(field.isAnnotationPresent(POJOExtraMapping.class) && value != null) {
                            POJOExtraMapping extraMapping = field.getAnnotation(POJOExtraMapping.class);
                            Field extraTarget = res.getClass().getDeclaredField(extraMapping.to());
                            POJOConverter<Object, Object> converter = POJOConverter.DefaultConverter.getDefaultConverter(orType, extraTarget.getType());
                            Object resultValue = converter.convert(value);
                            resClass.getMethod("set" + convertName(extraTarget.getName()),extraTarget.getType()).invoke(res, resultValue);
                        }
                    }
                }
                return res;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchFieldException | NullPointerException e) {
                throw new POJOMappingException("Error while mapping POJO \"" + or.getClass().getName() + "\"", e);
            }
        }
        throw new POJOMappingException("Object " + or + " is not an POJO Object!");
    }

    public static List<? extends Object> mapAll(List<? extends Object> objects) throws POJOMappingException {
        ArrayList<Object> res = new ArrayList<>(objects.size());
        for(Object o : objects) {
            res.add(map(o));
        }
        return res;
    }

    private static String convertName(String name) {
        return name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1) : "");
    }
}
