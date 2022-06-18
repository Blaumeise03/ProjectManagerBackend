package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class POJOMapper {
    private POJOMapper() {
    }

    public static Object map(Object o) throws POJOMappingException {
        if(o.getClass().isAnnotationPresent(POJO.class)) {
            try {
                //Get class of result object
                POJO a = o.getClass().getAnnotation(POJO.class);
                Class<?> resClass = a.mappingClass();
                Object res = resClass.getConstructor().newInstance();
                Field[] fields = o.getClass().getDeclaredFields();
                //iterate over all fields
                for (Field field : fields) {
                    if (field.isAnnotationPresent(POJOData.class)) {
                        //Current field should be mapped onto the target object
                        String invokeMethod = field.getAnnotation(POJOData.class).invokeMethod();
                        String fieldName =
                                field.getName().substring(0, 1).toUpperCase() +
                                        (field.getName().length() > 1 ? field.getName().substring(1) : "");
                        String getterName = (field.getType().equals(Boolean.TYPE) ? "is" : "get") + fieldName;
                        Object value = o.getClass().getMethod(getterName).invoke(o);
                        if (!field.getAnnotation(POJOData.class).blocked()) {
                            if(invokeMethod.equals("NONE"))
                                //Default mode for direct mapping
                                resClass.getMethod("set" + fieldName, field.getType()).invoke(res, value);
                            else {
                                //Special method has to be invoked to convert an object e.g. into its ID
                                if(value != null) {
                                    //Object is not null, method can be invoked
                                    resClass.getMethod(
                                            "set" + fieldName,
                                            value.getClass().getMethod(invokeMethod).getReturnType()
                                    ).invoke(res, value.getClass().getMethod(invokeMethod).invoke(value));
                                } else {
                                    //Object is null, if the target field is a number, -1 will be used
                                    Class<?> r = field.getType().getMethod(invokeMethod).getReturnType();
                                    if(r.equals(Integer.TYPE) || r.equals(Long.TYPE) || r.equals(Integer.class) || r.equals(Long.class)) {
                                        resClass.getMethod(
                                                "set" + fieldName,
                                               Long.TYPE
                                        ).invoke(res, -1);
                                    } else throw new POJOMappingException(String.format("Can't map field %s of class %s because it's null", field.getName(), o.getClass()));
                                }
                            }
                        }
                        if(field.isAnnotationPresent(POJOExtraMapping.class) && value != null) {
                            String nullCheck = field.getAnnotation(POJOExtraMapping.class).nullCheckMethod();
                            boolean isNull = !nullCheck.equals("NONE") && (boolean) value.getClass().getMethod(nullCheck).invoke(value);
                            if(!isNull) {
                                POJOExtraMapping m = field.getAnnotation(POJOExtraMapping.class);
                                resClass.getMethod(m.invokeMethodTo(), m.type())
                                        .invoke(res, value.getClass().getMethod(m.invokeMethodFrom()).invoke(value));
                            }
                        }
                    }
                }
                return res;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
                throw new POJOMappingException("Error while mapping POJO", e);
            }
        }
        throw new POJOMappingException("Object " + o + " is not an POJO Object!");
    }

    public static List<? extends Object> mapAll(List<? extends Object> objects) throws POJOMappingException {
        ArrayList<Object> res = new ArrayList<>(objects.size());
        for(Object o : objects) {
            res.add(map(o));
        }
        return res;
    }
}
