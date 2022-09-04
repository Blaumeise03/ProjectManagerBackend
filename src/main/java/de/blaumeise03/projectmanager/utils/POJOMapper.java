package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class POJOMapper {
    static Logger logger = LoggerFactory.getLogger(POJOMapper.class);

    private POJOMapper() {
    }

    /**
     * Maps the fields annotated with {@link POJOData} of the {@code source} object onto the target object.
     * It automatically converts the field values by using a {@link POJOConverter}.
     *
     * @see POJO
     * @see POJOData
     * @see POJOExtraMapping
     * @see POJOConverter
     * @param source the object which contains the data, has to be annotated with {@link POJO}.
     * @param target the target object which data will be overwritten by the data of {@code source}.
     * @return the mapped object {@code target}.
     * @throws POJOMappingException if the mapping failed.
     */
    public static Object map(Object source, Object target) throws POJOMappingException {
        logger.debug("Mapping: " + source.getClass().getName() + " to: " + target.getClass().getName());
        if(source.getClass().isAnnotationPresent(POJO.class)) {
            if (!(
                    source.getClass().getName().endsWith("POJO") &&
                            source.getClass().getName().equalsIgnoreCase(target.getClass().getName() + "POJO")
                    || target.getClass().getName().endsWith("POJO") &&
                            target.getClass().getName().equalsIgnoreCase(source.getClass().getName() + "POJO")
            )) {
                //Class names do not match
                logger.warn("Suspicious POJO found: " + source.getClass() + " to: " + target.getClass());
            }
            //Debug Data1
            Class<?> dFieldFromType = null;
            Class<?> dFieldToType = null;
            String dField = null;
            String dFieldTo = null;
            POJOConverter<?, ?> dTargetConverter = null;
            try {
                //Get class of result object
                POJO a = source.getClass().getAnnotation(POJO.class);
                Class<?> resClass = a.mappingClass();
                if(!target.getClass().isAssignableFrom(target.getClass())) throw new IllegalArgumentException("Target object '" + target.getClass() + "' is incompatible with expected class '" + resClass + "'");
                Field[] fields = source.getClass().getDeclaredFields();
                //iterate over all fields
                for (Field field : fields) {
                    dFieldFromType = null;
                    dFieldToType = null;
                    dField = null;
                    dFieldTo = null;
                    dTargetConverter = null;
                    if (field.isAnnotationPresent(POJOData.class)) {
                        logger.debug("Mapping field: {}", field);
                        POJOData pojoData = field.getAnnotation(POJOData.class);
                        dFieldFromType = field.getType();
                        dField = field.getName();
                        //Current field should be mapped onto the target object
                        //Generating names for getters/setters
                        String fieldName = convertName(field.getName());
                        String getterName = (field.getType().equals(Boolean.TYPE) ? "is" : "get") + fieldName;
                        String setterName = "set" + (pojoData.to().isBlank() ? fieldName : convertName(pojoData.to()));
                        //Getting current value of original object
                        Object value = source.getClass().getMethod(getterName).invoke(source);
                        Class<?> orType = field.getType();
                        logger.debug("Field type is: " + orType.getName());
                        logger.debug("Target field will be: " + (pojoData.to().isEmpty() ? field.getName() : pojoData.to()));
                        dFieldTo = pojoData.to().isEmpty() ? field.getName() : pojoData.to();
                        Class<?> targetType =
                                pojoData.to().isEmpty() ?
                                        target.getClass().getDeclaredField(field.getName()).getType() :
                                        target.getClass().getDeclaredField(pojoData.to()).getType();
                        logger.debug("Target type is: " + targetType.getName());
                        if(!pojoData.blocked()) {
                            dFieldToType = targetType;
                            if(value instanceof Collection) {
                                logger.debug("Value is a Collection!");
                                Collection<Object> resultValue;
                                if(Set.class.isAssignableFrom(targetType)) {
                                    resultValue = new HashSet<>();
                                } else {
                                    resultValue = new ArrayList<>();
                                }
                                for (Object o : ((Collection<?>) value)) {
                                    Object r = map(o);
                                    resultValue.add(r);
                                }
                                resClass.getMethod(setterName, targetType).invoke(target, resultValue);
                            } else if (pojoData.recursive()) {
                                logger.debug("Value is a POJO object, calling map function recursively");
                                Object resultValue = null;
                                if (value != null) {
                                    resultValue = map(value);
                                }

                                logger.debug("Value was converted recursively");
                                resClass.getMethod(setterName, targetType).invoke(target, resultValue);
                            } else {
                                POJOConverter<Object, Object> converter = POJOConverter.DefaultConverter.getDefaultConverter(orType, targetType);
                                logger.debug("Using converter " + POJOConverter.DefaultConverter.getName(converter) + ".");
                                dTargetConverter = converter;
                                Object resultValue = converter.convert(value);
                                resClass.getMethod(setterName, targetType).invoke(target, resultValue);
                            }
                        }
                        if(field.isAnnotationPresent(POJOExtraMapping.class) && value != null) {
                            logger.debug("Extra mapping found");
                            POJOExtraMapping extraMapping = field.getAnnotation(POJOExtraMapping.class);
                            Field extraTarget = target.getClass().getDeclaredField(extraMapping.to());
                            dFieldToType = extraTarget.getType();
                            if(extraMapping.recursive()) {
                                logger.debug("Extra mapping is using recursive mapping");
                                Object resultValue = map(value);
                                logger.debug("Value was converted recursively for extra mapping");
                                resClass.getMethod("set" + convertName(extraTarget.getName()), extraTarget.getType()).invoke(target, resultValue);
                            } else {
                                POJOConverter<Object, Object> converter = POJOConverter.DefaultConverter.getDefaultConverter(orType, extraTarget.getType());
                                dTargetConverter = converter;
                                Object resultValue = converter.convert(value);
                                logger.debug("Extra mapping to field " + extraTarget + " using converter " + POJOConverter.DefaultConverter.getName(converter));
                                resClass.getMethod("set" + convertName(extraTarget.getName()), extraTarget.getType()).invoke(target, resultValue);
                            }
                        }
                        logger.debug("Field " + field.getName() + " mapped!");
                    }
                }
                return target;
            } catch (InvocationTargetException | NullPointerException | IllegalAccessException | NoSuchMethodException e) {
                throw new POJOMappingException("Error while mapping POJO '" + source.getClass().getName() + "'", e);
            } catch (IllegalArgumentException | NoSuchFieldException e) {
                throw new POJOMappingException(String.format(
                        "Error while mapping %s.%s: %s to %s.%s:%s using converter '%s'",
                        source.getClass().getSimpleName(),
                        dField,
                        dFieldFromType == null ? "N/A" : dFieldFromType.getSimpleName(),
                        target.getClass().getSimpleName(),
                        dFieldTo,
                        dFieldToType == null ? "N/A" : dFieldToType.getSimpleName(),
                        POJOConverter.DefaultConverter.getName(dTargetConverter)
                ), e);
            }
        }
        throw new POJOMappingException("Object " + source + " is not an POJO Object!");
    }

    /**
     * Maps an entity to a POJO object {@code source} vice versa, the source object has to be annotated with {@link POJO}.
     * This method creates a new object with type {@link POJO#mappingClass()} and calls {@link POJOMapper#map(Object source, Object target)}.
     *
     * @see POJOMapper#map(Object source, Object target) for more details on the implementation.
     * @param source the object which should be converted.
     * @return a new object with the data of {@code source}.
     * @throws POJOMappingException if the mapping failed.
     */
    public static Object map(Object source) throws POJOMappingException {
        if(source.getClass().isAnnotationPresent(POJO.class)) {
            POJO a = source.getClass().getAnnotation(POJO.class);
            Class<?> resClass = a.mappingClass();
            Object res = null;
            try {
                res = resClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new POJOMappingException("Error while mapping POJO '" + source.getClass().getName() + "'", e);
            }
            return map(source, res);
        }
        throw new POJOMappingException("Object " + source + " is not an POJO Object!");
    }

    /**
     * Converts a list of objects.
     *
     * @see #map(Object)
     * @param objects The objects which should be mapped.
     * @return an {@link ArrayList} containing all mapped objects.
     * @throws POJOMappingException if the mapping failed.
     */
    public static List<?> mapAll(Collection<?> objects) throws POJOMappingException {
        ArrayList<Object> res = new ArrayList<>(objects.size());
        for(Object o : objects) {
            res.add(map(o));
        }
        return res;
    }

    /**
     * Sets the first letter to uppercase and appends the remaining ones. Used as a helper method to find the
     * getters and setters in {@link #map(Object, Object)}.
     *
     * @param name the name of the field that should be converted.
     * @return the converted name.
     */
    private static String convertName(String name) {
        return name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1) : "");
    }
}
