package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class POJOMapper {
    private POJOMapper() {
    }

    public static Object map(Object or) throws POJOMappingException {
        if(or.getClass().isAnnotationPresent(POJO.class)) {
            //Debug Data
            Class<?> dFieldFromType = null;
            Class<?> dFieldToType = null;
            String dField = null;
            POJOConverter<?, ?> dTargetConverter = null;
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
                        dFieldFromType = field.getType();
                        dField = field.getName();
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
                            dFieldToType = targetType;
                            if(value instanceof Collection) {
                                Collection<Object> resultValue = null;
                                if(Set.class.isAssignableFrom(targetType)) {
                                    resultValue = new HashSet<>();
                                } else {
                                    resultValue = new ArrayList<>();
                                }
                                for (Object o : ((Collection<?>) value)) {
                                    Object r = map(o);
                                    resultValue.add(r);
                                }

                                resClass.getMethod(setterName, targetType).invoke(res, resultValue);
                            } else {
                                POJOConverter<Object, Object> converter = POJOConverter.DefaultConverter.getDefaultConverter(orType, targetType);
                                dTargetConverter = converter;
                                Object resultValue = converter.convert(value);
                                resClass.getMethod(setterName, targetType).invoke(res, resultValue);
                            }
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
            } catch (InvocationTargetException | NullPointerException | NoSuchFieldException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new POJOMappingException("Error while mapping POJO '" + or.getClass().getName() + "'", e);
            } catch (IllegalArgumentException e) {
                throw new POJOMappingException(String.format(
                        "Error while mapping field '%s' of POJO '%s': '%s' -> '%s' using converter '%s'",
                        dField, or.getClass().getName(),
                        dFieldFromType,
                        dFieldToType,
                        POJOConverter.DefaultConverter.getName(dTargetConverter)
                ), e);
            }
        }
        throw new POJOMappingException("Object " + or + " is not an POJO Object!");
    }

    public static List<? extends Object> mapAll(Collection<? extends Object> objects) throws POJOMappingException {
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
