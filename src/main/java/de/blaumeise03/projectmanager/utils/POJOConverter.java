package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.accounting.Corp;
import de.blaumeise03.projectmanager.accounting.Player;
import de.blaumeise03.projectmanager.accounting.PlayerService;
import de.blaumeise03.projectmanager.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

public abstract class POJOConverter<F, T> {
    static PlayerService service;

    public static void setService(PlayerService service) {
        POJOConverter.service = service;
    }

    public static PlayerService getService() {
        return service;
    }

    public static final POJOConverter<Object, Object> DEFAULT_CONVERTER = new POJOConverter<>() {
        @Override
        public Object convert(Object value) {
            return value;
        }
    };

    public static final POJOConverter<Integer, Integer> INTEGER_INTEGER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Integer convert(Integer value) {
            return value;
        }
    };

    public static final POJOConverter<Long, Long> LONG_LONG_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Long convert(Long value) {
            return value;
        }
    };

    public static final POJOConverter<Boolean, Boolean> BOOLEAN_BOOLEAN_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Boolean convert(Boolean value) {
            return value;
        }
    };

    public static final POJOConverter<Player, Integer> PLAYER_INTEGER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Integer convert(Player value) {
            if(value == null) return -1;
            if(!value.isNew())
                return value.getUid();
            return null;
        }
    };

    public static final POJOConverter<Integer, Player> INTEGER_PLAYER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Player convert(Integer value) {
            if(value == null) return null;
            return service.findByID(value);
        }
    };

    public static final POJOConverter<Player, String> PLAYER_STRING_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public String convert(Player value) {
            if(value == null) return null;
            if(!value.isNew())
                return value.getName();
            return null;
        }
    };

    public static final POJOConverter<Corp, Integer> CORP_INTEGER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Integer convert(Corp value) {
            if(value == null) return null;
            if(!value.isNew())
                return value.getCid();
            return null;
        }
    };

    public static final POJOConverter<User, Long> USER_LONG_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Long convert(User value) {
            if(value == null) return (long) -1;
            if(!value.isNew())
                return value.getId();
            return null;
        }
    };

    public abstract T convert(F f);

    public enum DefaultConverter {
        INTEGER(Integer.class, Integer.class, INTEGER_INTEGER_POJO_CONVERTER),
        LONG(Long.class, Long.class, LONG_LONG_POJO_CONVERTER),
        BOOLEAN(Boolean.class, Boolean.class, BOOLEAN_BOOLEAN_POJO_CONVERTER),
        PLAYER_INTEGER(Player.class, Integer.class, PLAYER_INTEGER_POJO_CONVERTER),
        INTEGER_PLAYER(Integer.class, Player.class, INTEGER_PLAYER_POJO_CONVERTER),
        PLAYER_STRING(Player.class, String.class, PLAYER_STRING_POJO_CONVERTER),
        CORP_INTEGER(Corp.class, Integer.class, CORP_INTEGER_POJO_CONVERTER),
        USER_LONG(User.class, Long.class, USER_LONG_POJO_CONVERTER);

        POJOConverter<?, ?> converter;
        Class<?> from;
        Class<?> to;

        DefaultConverter(Class<?> from, Class<?> to, POJOConverter<?, ?> converter) {
            this.converter = converter;
            this.from = from;
            this.to = to;
        }

        @SuppressWarnings("unchecked")
        public static POJOConverter<Object, Object> getDefaultConverter(Class<?> from, Class<?> to) {
            for(DefaultConverter defaultConverter : values()) {
                if(defaultConverter.from.equals(from) && defaultConverter.to.equals(to)) return (POJOConverter<Object, Object>) defaultConverter.converter;
            }
            return DEFAULT_CONVERTER;
        }
    }
}
